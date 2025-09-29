package com.sudoku;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;

public class GameController {

    // 🔹 Vínculos con los elementos del FXML
    @FXML private Label timerLabel;
    @FXML private GridPane sudokuGrid;
    @FXML private Button startButton;
    @FXML private Button pauseButton;
    @FXML private Button helpButton;
    @FXML private Button finishButton;

    private boolean gameRunning = false;

    @FXML
    public void initialize() {
        System.out.println("✅ Controlador cargado.");
        createEmptyBoard();
    }

    // 🔹 Crear un tablero 6x6 vacío con TextField
    private void createEmptyBoard() {
        sudokuGrid.getChildren().clear();
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                TextField cell = new TextField();
                cell.setPrefSize(50, 50);
                cell.setStyle("-fx-font-size: 16px; -fx-alignment: center;");
                sudokuGrid.add(cell, col, row);
            }
        }
    }

    // 🔹 Botón "Iniciar"
    @FXML
    private void onStartGame() {
        gameRunning = true;
        timerLabel.setText("00:00");
        System.out.println("🎮 Juego iniciado.");
    }

    // 🔹 Botón "Pausar"
    @FXML
    private void onPauseGame() {
        if (gameRunning) {
            gameRunning = false;
            System.out.println("⏸ Juego pausado.");
        }
    }

    // 🔹 Botón "Ayuda"
    @FXML
    private void onHelp() {
        System.out.println("💡 Mostrar pista o validación (a implementar).");
    }

    // 🔹 Botón "Finalizar"
    @FXML
    private void onFinish() {
        gameRunning = false;
        System.out.println("🏁 Juego finalizado.");
    }
}
