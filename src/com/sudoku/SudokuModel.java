package com.sudoku;

/**
 * English: Represents the Sudoku game logic (model).
 * Español: Representa la lógica del juego Sudoku (modelo).
 */
public class SudokuModel {

    /**
     * English: Fills the board with some example values.
     * Español: Llena el tablero con algunos valores de ejemplo.
     */
    public void fillBoard(Cell[][] cells) {
        // English: Example values (can be improved later).
        // Español: Valores de ejemplo (se puede mejorar después).
        for (int i = 0; i < 6; i++) {
            cells[i][i].setText(String.valueOf((i % 6) + 1));
        }
    }
}
