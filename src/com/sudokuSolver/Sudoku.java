package com.sudokuSolver;

import java.util.*;
class Sudoku {
    private final int[][] sudoku = new int[9][9];
    private final DoublyLinkedList sudokuDLL;
    private int iterations;
    private String message;
    private long start;
    private long elapsedTime;

    public Sudoku(int[][] sudoku) {
        for (int i = 0; i < 9; i++) {
            System.arraycopy(sudoku[i], 0, this.sudoku[i], 0, 9);
        }

        this.sudokuDLL = new DoublyLinkedList();
        this.iterations = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!(sudoku[i][j] > 0 && sudoku[i][j] <= 9)) {
                    int[] ij = new int[]{i, j};
                    sudokuDLL.append(sudoku[i][j], ij);
                }
            }
        }
    }

    public ArrayList<Integer> rangeToCheck(int index) {
        ArrayList<Integer> range = new ArrayList<>();
        int lowerIndex = index/3 * 3;
        for (int k = 0; k < 3; k++) {
            range.add(lowerIndex + k);
        }
        return range;
    }//i range is i/3 * 3, i/3*3 + 1, i/3*3 + 2

    public int[][] getSudoku() {
        return sudoku;
    }

    public DoublyLinkedList getSudokuDLL() {
        return sudokuDLL;
    }


    public boolean isSafe(int n, int i, int j) {
        for (int x : rangeToCheck(i)) {
            for (int y: rangeToCheck(j)) {
                if (sudoku[x][y] == n) {
                    return false;
                }
            }
        }
        for (int x = 0; x < 9; x++) {
            if (sudoku[x][j] == n) {
                return false;
            }
        }
        for (int y = 0; y < 9; y++) {
            if (sudoku[i][y] == n) {
                return false;
            }
        }
        return true;
    }

    public void solver() {
        try {
            Node currentNode = getSudokuDLL().getHeadNode();
            while (currentNode != null) {
                iterations++;
                currentNode.incrementValue();
                int i = currentNode.getPosition_9x9_ij()[0];
                int j = currentNode.getPosition_9x9_ij()[1];
                if (currentNode.getValue() == 10) {
                    currentNode.setValue(0);
                    sudoku[i][j] = 0;
                    currentNode = currentNode.getPreviousNode();
                    if (currentNode == null) {
                        message = "no solution found\niterations: " + iterations;
                        return;
                    }
                } else {
                    if (isSafe(currentNode.getValue(), i, j)) {
                        sudoku[i][j] = currentNode.getValue();
                        currentNode = currentNode.getNextNode();
                    }
                }
            }
            message = "sudoku solved\niterations: " + iterations;
        } finally {
            long finish = System.currentTimeMillis();
            elapsedTime = finish - start;
        }
    }

    public void solve() {
        start = System.currentTimeMillis();
        solver();
    }

    public boolean isLegal() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int n = sudoku[i][j];
                try {
                    sudoku[i][j] = 0;
                    if (n != 0 && !isSafe(n, i, j)) {
                        return false;
                    }
                } finally {
                    sudoku[i][j] = n;
                }
            }
        }
        return true;
    }

    public String getMessage() {
        return message;
    }
    public long getElapsedTime() {
        return elapsedTime;
    }
}
	