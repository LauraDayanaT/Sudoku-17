package com.sudoku;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * English: Controls the game logic and UI interaction.
 * Español: Controla la lógica del juego y la interacción con la interfaz.
 */
public class GameController {

    @FXML private GridPane board;   // English: Sudoku board / Español: Tablero de Sudoku
    @FXML private Button startBtn;  // English: Start button / Español: Botón iniciar
    @FXML private Button pauseBtn;  // English: Pause button / Español: Botón pausar
    @FXML private Button helpBtn;   // English: Help button / Español: Botón ayuda
    @FXML private Button endBtn;    // English: End button / Español: Botón finalizar
    @FXML private Label timerLabel; // English: Timer / Español: Temporizador

    private Cell[][] cells = new Cell[6][6];
    private SudokuModel model = new SudokuModel();
    private Timeline timeline;
    private int seconds = 0;

    @FXML
    public void initialize() {
        // English: Create 6x6 grid of cells / Español: Crear la cuadrícula 6x6 de celdas
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                Cell cell = new Cell(row, col);
                cells[row][col] = cell;
                board.add(cell, col, row);
            }
        }

        // English: Configure timer / Español: Configurar temporizador
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            seconds++;
            timerLabel.setText("⏱ " + seconds + "s");
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    @FXML
    private void startGame() {
        seconds = 0;
        timerLabel.setText("⏱ 0s");
        timeline.play();
        model.fillBoard(cells);
    }

    @FXML
    private void pauseGame() {
        timeline.pause();
    }

    @FXML
    private void endGame() {
        timeline.stop();
        timerLabel.setText("Game Over / Juego terminado");
    }

    @FXML
    private void helpGame() {
        timerLabel.setText("Help not implemented yet / Ayuda no implementada todavía");
    }
}
