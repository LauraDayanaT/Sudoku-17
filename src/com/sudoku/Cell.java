package com.sudoku;

import javafx.scene.control.TextField;

/**
 * English: Represents a single cell in the Sudoku grid.
 * Español: Representa una sola celda en la cuadrícula del Sudoku.
 */
public class Cell extends TextField {

    private final int row; // English: Row index / Español: Índice de fila
    private final int col; // English: Column index / Español: Índice de columna

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;

        // English: Cell setup / Español: Configuración de la celda
        setPrefSize(50, 50); // English: Size / Español: Tamaño
        setStyle("-fx-alignment: center; -fx-font-size: 16px;");

        // English: Only allow one digit (1–6) / Español: Solo permitir un dígito (1–6)
        textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("[1-6]?")) {
                setText(oldValue);
            }
        });
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}