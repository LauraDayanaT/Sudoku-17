package com.sudoku;

import java.util.*;

/**
 * Sudoku logic (generate + validate + check solution).
 */
public class SudokuModel {
    private final int SIZE = 6;
    private final int SUBROWS = 2;
    private final int SUBCOLS = 3;

    private int[][] solution = new int[SIZE][SIZE];

    /** Generate a valid Sudoku puzzle (6x6). */
    public void generatePuzzle(Cell[][] cells) {
        // Step 1: generate full solved board
        solveBoard(new int[SIZE][SIZE], 0, 0);

        // Copy solution
        for (int r = 0; r < SIZE; r++) {
            System.arraycopy(solution[r], 0, solution[r], 0, SIZE);
        }

        // Step 2: clear some cells to create puzzle
        Random rand = new Random();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (rand.nextDouble() < 0.5) {
                    cells[r][c].setText(String.valueOf(solution[r][c]));
                    cells[r][c].setEditable(false);
                    cells[r][c].setStyle("-fx-background-color: #e9ecef; -fx-font-weight: bold; -fx-alignment: center;");
                } else {
                    cells[r][c].setText("");
                    cells[r][c].setEditable(true);
                    cells[r][c].setStyle("-fx-background-color: white; -fx-alignment: center;");


                }
            }
        }
    }

    /** Backtracking solver */
    private boolean solveBoard(int[][] board, int row, int col) {
        if (row == SIZE) {
            // copy solution
            for (int r = 0; r < SIZE; r++) {
                System.arraycopy(board[r], 0, solution[r], 0, SIZE);
            }
            return true;
        }
        int nextRow = (col == SIZE - 1) ? row + 1 : row;
        int nextCol = (col + 1) % SIZE;
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= SIZE; i++) numbers.add(i);
        Collections.shuffle(numbers);
        for (int num : numbers) {
            if (isValid(board, row, col, num)) {
                board[row][col] = num;
                if (solveBoard(board, nextRow, nextCol)) return true;
                board[row][col] = 0;
            }
        }
        return false;
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) return false;
        }
        int boxRow = (row / SUBROWS) * SUBROWS;
        int boxCol = (col / SUBCOLS) * SUBCOLS;
        for (int r = 0; r < SUBROWS; r++) {
            for (int c = 0; c < SUBCOLS; c++) {
                if (board[boxRow + r][boxCol + c] == num) return false;
            }
        }
        return true;
    }

    /** Validate current board (no duplicates). */
    public boolean validateBoard(Cell[][] cells) {
        try {
            for (int r = 0; r < SIZE; r++) {
                boolean[] seenRow = new boolean[SIZE + 1];
                boolean[] seenCol = new boolean[SIZE + 1];
                for (int c = 0; c < SIZE; c++) {
                    // Row check
                    String valRow = cells[r][c].getText();
                    if (!valRow.isEmpty()) {
                        int n = Integer.parseInt(valRow);
                        if (seenRow[n]) return false;
                        seenRow[n] = true;
                    }
                    // Column check
                    String valCol = cells[c][r].getText();
                    if (!valCol.isEmpty()) {
                        int n = Integer.parseInt(valCol);
                        if (seenCol[n]) return false;
                        seenCol[n] = true;
                    }
                }
            }
            // Blocks check
            for (int br = 0; br < SIZE; br += SUBROWS) {
                for (int bc = 0; bc < SIZE; bc += SUBCOLS) {
                    boolean[] seen = new boolean[SIZE + 1];
                    for (int r = 0; r < SUBROWS; r++) {
                        for (int c = 0; c < SUBCOLS; c++) {
                            String val = cells[br + r][bc + c].getText();
                            if (!val.isEmpty()) {
                                int n = Integer.parseInt(val);
                                if (seen[n]) return false;
                                seen[n] = true;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /** Check if board is completely solved. */
    public boolean isSolved(Cell[][] cells) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                String val = cells[r][c].getText();
                if (val.isEmpty() || Integer.parseInt(val) != solution[r][c]) return false;
            }
        }
        return true;
    }
}