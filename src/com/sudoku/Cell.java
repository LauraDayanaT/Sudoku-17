package com.sudoku;

import javafx.scene.control.TextField;

/** Represents a single cell in the Sudoku grid. */
public class Cell extends TextField {
    private final int row;
    private final int col;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        setPrefSize(50, 50);
        setStyle("-fx-alignment: center; -fx-font-size: 16px;");
        getStyleClass().add("grid-cell");

        textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("[1-6]?")) setText(oldValue);
        });
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
}
