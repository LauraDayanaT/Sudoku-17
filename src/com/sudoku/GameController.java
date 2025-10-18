package com.sudoku;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Controller of the Sudoku game (handles UI + logic).
 */
public class GameController {

    @FXML private Label timerLabel;
    @FXML private GridPane sudokuGrid;
    @FXML private Button startButton;
    @FXML private Button pauseButton;
    @FXML private Button resumeButton;
    @FXML private Button helpButton;
    @FXML private Button finishButton;
    @FXML private Label statusLabel;
    @FXML private Button newGameButton; // Asumiendo que has añadido este ID en el FXML
    @FXML private Label sixCountLabel;


    private boolean gameRunning = false;
    private int seconds = 0;
    private Timeline timeline;

    private final int SIZE = 6;
    private final int SUBROWS = 2; // Para bloque 2x3
    private final int SUBCOLS = 3; // Para bloque 2x3

    private Cell[][] cells = new Cell[SIZE][SIZE];
    private SudokuModel model;
    private int[][] solutionBoard;
    private int hintsUsed = 0;
    private int[][] initialPuzzle; // Almacena el estado inicial del puzzle

    @FXML
    public void initialize() {
        model = new SudokuModel();
        setupTimer();
        statusLabel.setText("Press Start to begin a new game.");
    }

    /** Crea un tablero vacío e inicializa las celdas. */
    private void createEmptyBoard() {
        sudokuGrid.getChildren().clear();
        sudokuGrid.getColumnConstraints().clear();
        sudokuGrid.getRowConstraints().clear();

        // Configura el GridPane
        for (int i = 0; i < SIZE; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            sudokuGrid.getColumnConstraints().add(cc);

            RowConstraints rc = new RowConstraints();
            rc.setVgrow(Priority.ALWAYS);
            sudokuGrid.getRowConstraints().add(rc);
        }

        // Crea y añade celdas
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Cell cell = new Cell(row, col, solutionBoard);

                cell.getStyleClass().add("grid-cell");
                cell.setId("cell-" + row + "-" + col);

                // === LÓGICA DE BORDES GRUESOS (2x3 Blocks) ===

                // Bordes horizontales gruesos (después de filas 1 y 3)
                if ((row + 1) % SUBROWS == 0 && row != SIZE - 1) {
                    cell.getStyleClass().add("border-bottom-thick");
                }
                // Bordes verticales gruesos (después de columna 2)
                if ((col + 1) % SUBCOLS == 0 && col != SIZE - 1) {
                    cell.getStyleClass().add("border-right-thick");
                }

                // --- Bordes exteriores ---
                if (row == 0) cell.getStyleClass().add("border-top-thick");
                if (row == SIZE - 1) cell.getStyleClass().add("border-bottom-thick");
                if (col == 0) cell.getStyleClass().add("border-left-thick");
                if (col == SIZE - 1) cell.getStyleClass().add("border-right-thick");

                sudokuGrid.add(cell, col, row);
                cells[row][col] = cell;
            }
        }
    }

    private void setupTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (gameRunning) {
                seconds++;
                int minutes = seconds / 60;
                int sec = seconds % 60;
                timerLabel.setText(String.format("%02d:%02d", minutes, sec));
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    /** Actualiza el conteo de números 6 en el tablero */
    private void updateSixCount() {
        int count = 0;
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                String val = cells[r][c].getText();
                if ("6".equals(val)) {
                    count++;
                }
            }
        }
        sixCountLabel.setText("Count of 6: " + count);
    }


    @FXML
    private void onStartGame() {
        // La lógica de onStartGame se utiliza también para New Game
        seconds = 0;
        timerLabel.setText("00:00");
        hintsUsed = 0;
        gameRunning = false; // Detiene el timer si estaba corriendo

        // 2. Generar puzzle y obtener solución
        initialPuzzle = model.generatePuzzle();
        solutionBoard = model.getSolution();

        // 3. Crear el tablero
        createEmptyBoard();

        // 4. Llenar celdas
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Cell cell = cells[r][c];
                if (initialPuzzle[r][c] != 0) {
                    String num = String.valueOf(initialPuzzle[r][c]);

                    cell.setText(num);

                    // Estilos para números iniciales
                    cell.setEditable(false);
                    cell.setStyle("-fx-background-color: #e9ecef; -fx-font-weight: bold; -fx-alignment: center;");
                } else {
                    // Celdas editables (limpias para empezar)
                    cell.setText("");
                    cell.setEditable(true);
                    cell.setStyle("-fx-background-color: white; -fx-alignment: center;");
                }
            }
        }

        gameRunning = true;
        timeline.play();
        statusLabel.setText("🎮 Game started.");

        updateSixCount();

    }

    @FXML
    private void onNewGame() {
        // Simplemente reinicia el juego llamando a onStartGame
        onStartGame();
    }

    @FXML
    private void onPauseGame() {
        if (gameRunning) {
            gameRunning = false;
            timeline.pause();
            statusLabel.setText("⏸ Game paused.");
        }
    }

    @FXML
    private void onResumeGame() {
        if (!gameRunning) {
            gameRunning = true;
            timeline.play();
            statusLabel.setText("▶ Game resumed.");
        }
    }

    @FXML
    private void onHelp(ActionEvent event) {
        if (!gameRunning) {
            statusLabel.setText("Game not running. Press Start.");
            return;
        }

        // 1. Encontrar todas las celdas vacías y editables
        List<Cell> emptyCells = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (cells[r][c].getText().isEmpty() && cells[r][c].isEditable()) {
                    emptyCells.add(cells[r][c]);
                }
            }
        }

        if (emptyCells.isEmpty()) {
            statusLabel.setText("Board complete! Presiona Finish.");
            return;
        }

        // 2. Seleccionar una celda vacía al azar
        Random rand = new Random();
        Cell hintCell = emptyCells.get(rand.nextInt(emptyCells.size()));

        int r = hintCell.getRow();
        int c = hintCell.getCol();

        // 3. Colocar la pista de la solución
        int correctNumber = solutionBoard[r][c];

        hintCell.setText(String.valueOf(correctNumber));

        // 4. Bloquear la celda y aplicar estilo visual
        hintCell.setEditable(false);
        hintCell.getStyleClass().add("hint-given");

        hintsUsed++;
        statusLabel.setText("Pista utilizada. Total: " + hintsUsed);

        updateSixCount();

    }

    @FXML
    private void onFinish() {
        gameRunning = false;
        timeline.stop();
        boolean solved = model.isSolved(cells);
        showAlert("Game finished", solved ? "🎉 Congratulations, you solved the Sudoku!" : "❌ The board is incorrect.");
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}