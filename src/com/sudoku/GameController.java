package com.sudoku;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

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

    private boolean gameRunning = false;
    private int seconds = 0;
    private Timeline timeline;

    private final int SIZE = 6;
    private Cell[][] cells = new Cell[SIZE][SIZE];
    private SudokuModel model = new SudokuModel();

    @FXML
    public void initialize() {
        createEmptyBoard();
        setupTimer();
    }

    private void createEmptyBoard() {
        sudokuGrid.getChildren().clear();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Cell cell = new Cell(row, col);
                cell.getStyleClass().add("grid-cell");
                cell.setId("cell-" + row + "-" + col);
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

    @FXML
    private void onStartGame() {
        if (!gameRunning) {
            gameRunning = true;
            seconds = 0;
            timerLabel.setText("00:00");
            timeline.play();
            model.generatePuzzle(cells);
            statusLabel.setText("🎮 Game started.");
        }
    }

    @FXML
    private void onPauseGame() {
        if (gameRunning) {
            gameRunning = false;
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
    private void onHelp() {
        boolean valid = model.validateBoard(cells);
        showAlert("Help", valid ? "✅ So far everything looks good!" : "⚠️ There's an error in the board.");
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