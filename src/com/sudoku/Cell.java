package com.sudoku;

import javafx.scene.control.TextField;
import javafx.beans.value.ObservableValue;

/** Represents a single cell in the Sudoku grid. */
public class Cell extends TextField {
    private final int row;
    private final int col;

    /* Store the solution */
    private final int[][] solutionBoard;

    public Cell(int row, int col, int[][] solutionBoard) {
        this.row = row;
        this.col = col;
        this.solutionBoard = solutionBoard;

        setPrefSize(50, 50);
        setStyle("-fx-alignment: center; -fx-font-size: 16px;");
        getStyleClass().add("grid-cell");

        // SINTAXIS CORREGIDA: Estructura del listener
        textProperty().addListener((ObservableValue<? extends String> obs, String oldValue, String newValue) -> {

            // 1. Validación de formato (solo números 1 al 6 o vacío)
            if (!newValue.matches("[1-6]?")) {
                setText(oldValue);
            }
            // 2. Validación de lógica (comparar con la solución)
            else if (!newValue.isEmpty()) {
                validateEntry(newValue);
            }
            // 3. Caso vacío
            else {
                getStyleClass().removeAll("correct-entry", "incorrect-entry");
            }
        });
    }

    private void validateEntry(String entry) {
        getStyleClass().removeAll("correct-entry", "incorrect-entry");

        try {
            int number = Integer.parseInt(entry);
            // Compara con la solución
            if (number == solutionBoard[row][col]) {
                getStyleClass().add("correct-entry");
            } else {
                getStyleClass().add("incorrect-entry");
            }
        } catch (NumberFormatException e) {
            getStyleClass().removeAll("correct-entry", "incorrect-entry");
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}