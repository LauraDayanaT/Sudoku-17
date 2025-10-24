package com.sudoku;

import java.util.*;

/**
 * Sudoku logic (generate + validate + check solution).
 * Size is 6x6, with 2x3 sub-grids.
 */
public class SudokuModel {
    private final int SIZE = 6;
    private final int SUBROWS = 2;
    private final int SUBCOLS = 3;

    private int[][] solution = new int[SIZE][SIZE];

    /** * Generate a valid Sudoku puzzle (6x6) and return the initial puzzle state */
    public int[][] generatePuzzle() {
        // 1. Genera el tablero resuelto
        solveBoard(new int[SIZE][SIZE], 0, 0);

        int[][] initialPuzzle = new int[SIZE][SIZE];

        // 2. Borra algunas celdas (50% de probabilidad de borrar)
        Random rand = new Random();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (rand.nextDouble() < 0.5) {
                    initialPuzzle[r][c] = solution[r][c];
                } else {
                    initialPuzzle[r][c] = 0;
                }
            }
        }
        return initialPuzzle;
    }

    /** Backtracking solver (generates the complete solution) */
    private boolean solveBoard(int[][] board, int row, int col) {
        if (row == SIZE) {
            // Copia la solución final
            for (int r = 0; r < SIZE; r++) {
                System.arraycopy(board[r], 0, solution[r], 0, SIZE);
            }
            return true;
        }
        int nextRow = (col == SIZE - 1) ? row + 1 : row;
        int nextCol = (col + 1) % SIZE;

        // Prueba números al azar (1 a 6) para variación
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= SIZE; i++) numbers.add(i);
        Collections.shuffle(numbers);

        for (int num : numbers) {
            if (isValid(board, row, col, num)) {
                board[row][col] = num;
                if (solveBoard(board, nextRow, nextCol)) return true;
                board[row][col] = 0; // Backtrack
            }
        }
        return false;
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        // Validación de fila y columna
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) return false;
        }
        // Validación de sub-grilla (2x3)
        int boxRow = (row / SUBROWS) * SUBROWS;
        int boxCol = (col / SUBCOLS) * SUBCOLS;
        for (int r = 0; r < SUBROWS; r++) {
            for (int c = 0; c < SUBCOLS; c++) {
                if (board[boxRow + r][boxCol + c] == num) return false;
            }
        }
        return true;
    }

    /** Validate current board (no duplicates in filled cells). */
    public boolean validateBoard(Cell[][] cells) {
        try {
            for (int r = 0; r < SIZE; r++) {
                boolean[] seenRow = new boolean[SIZE + 1];
                boolean[] seenCol = new boolean[SIZE + 1];

                for (int c = 0; c < SIZE; c++) {
                    // Chequeo de Fila
                    String valRow = cells[r][c].getText();
                    if (!valRow.isEmpty()) {
                        int n = Integer.parseInt(valRow);
                        if (n < 1 || n > SIZE || seenRow[n]) return false;
                        seenRow[n] = true;
                    }
                    // Chequeo de Columna
                    String valCol = cells[c][r].getText();
                    if (!valCol.isEmpty()) {
                        int n = Integer.parseInt(valCol);
                        if (n < 1 || n > SIZE || seenCol[n]) return false;
                        seenCol[n] = true;
                    }
                }
            }
            // Chequeo de Bloques
            for (int br = 0; br < SIZE; br += SUBROWS) {
                for (int bc = 0; bc < SIZE; bc += SUBCOLS) {
                    boolean[] seen = new boolean[SIZE + 1];
                    for (int r = 0; r < SUBROWS; r++) {
                        for (int c = 0; c < SUBCOLS; c++) {
                            String val = cells[br + r][bc + c].getText();
                            if (!val.isEmpty()) {
                                int n = Integer.parseInt(val);
                                if (n < 1 || n > SIZE || seen[n]) return false;
                                seen[n] = true;
                            }
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /** Check if board is completely solved. */
    public boolean isSolved(Cell[][] cells) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                String val = cells[r][c].getText();
                // Verifica que no esté vacío Y que el número coincida con la solución
                if (val.isEmpty() || Integer.parseInt(val) != solution[r][c]) return false;
            }
        }
        return true;
    }

    /** Get the full solved board. */
    public int[][] getSolution() {
        return solution;
    }
}