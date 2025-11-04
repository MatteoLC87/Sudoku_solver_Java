package com.sudokuSolver;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.text.*;
import java.io.*;

public class SudokuGUI extends JFrame {
    private final List<JTextField> linear9x9Grid;
    private final Container container;
    private final JTextPane result;
    private final JScrollPane resultPane;
    private final JButton solve;
    private final JButton save;
    private final JButton load;
    private final JButton reset;
    private final List<JButton> buttons;
    private final int[][] rawSudoku;
    private int[][] solvedSudoku;
    private Sudoku sudokuUI;

    public SudokuGUI() {
        this.linear9x9Grid = new ArrayList<>();
        container = getContentPane();
        result = new JTextPane();
        resultPane = new JScrollPane(result);

        solve = new JButton("solve");
        save = new JButton("save");
        load = new JButton("load");
        reset = new JButton("reset");
        buttons = List.of(solve, save, load, reset);

        rawSudoku = new int[9][9];
        solvedSudoku = new int[9][9];
    }

    public static void main(String[] args) {
        new SudokuGUI().go();
    }

    private void go() {
        setBounds(0, 0, 350, 500);
        FlowLayout fl = new FlowLayout();
        setLayout(fl);
        fl.setVgap(0);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        for (JButton button : buttons) {
            setUpButton(button);
        }

        solve.addActionListener(e -> {
            sudokuUI = new Sudoku(rawSudoku.clone());
            if (sudokuUI.isLegal()) {
                sudokuUI.solve();
                solvedSudoku = sudokuUI.getSudoku();
                result.setText(printSudoku(solvedSudoku));
            } else {
                result.setText("impossible to solve");
                System.out.println("impossible to solve");
            }
        });

        save.addActionListener(e -> saveSudoku());
        load.addActionListener(e -> loadSudoku());
        reset.addActionListener(e -> resetAll());

        addEmptyLabel();

        for (int position = 0; position < 81; position++) {
            JTextField singleDigit = new JTextField(1);
            linear9x9Grid.add(singleDigit);
            if (position % 9 == 0 && position != 0) {
                addEmptyLabel();
            }
            container.add(singleDigit);
            singleDigit.setText("0");
            singleDigit.addKeyListener(new KeyAdapter() { //anonymous class + overridden method
                public void keyPressed(KeyEvent ke) {
                    char keyChar = ke.getKeyChar();
                    int keyCode = ke.getKeyCode();
                    if (ke.getKeyCode() == 8 || ke.getKeyCode() == 127) { // enable backspace or delete
                        singleDigit.setText("0");
                    }
                    if (keyChar >= '0' && keyChar <= '9') { //allows only numbers (also keypad numbers)
                        String digit;
                        //System.out.println(getCurrentIndex());//debug only
                        if (ke.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD) {
                            String numPadKey = KeyEvent.getKeyText(keyCode);
                            digit = numPadKey.substring(numPadKey.length() - 1);
                        } else {
                            digit = KeyEvent.getKeyText(keyCode);
                        }
                        updateBox(getCurrentIndex(), digit);
                        singleDigit.setText("");
                        singleDigit.setEditable(true);
                    } else {
                        ke.consume();
                        singleDigit.setEditable(false);
                    }
                    //implements arrows to navigate through boxes
                    if (ke.getKeyCode() == KeyEvent.VK_UP) {
                        int nextIndex = getCurrentIndex() - 9;
                        if (nextIndex >= 0) {
                            linear9x9Grid.get(nextIndex).requestFocus();
                        }
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
                        int nextIndex = getCurrentIndex() + 9;
                        if (nextIndex < 81) {
                            linear9x9Grid.get(nextIndex).requestFocus();
                        }
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
                        int nextIndex = getCurrentIndex() - 1;
                        if (nextIndex >= 0) {
                            linear9x9Grid.get(nextIndex).requestFocus();
                        }
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                        int nextIndex = getCurrentIndex() + 1;
                        if (nextIndex < 81) {
                            linear9x9Grid.get(nextIndex).requestFocus();
                        }
                    }
                }
            });
        }
        addEmptyLabel();
        container.add(resultPane);
        resultPane.setPreferredSize(new Dimension(250, 400));

        StyledDocument doc = result.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        setVisible(true);
    }
    private int getCurrentIndex() {
        Component focusOwner = getFocusOwner();
        return linear9x9Grid.indexOf(focusOwner);
    }
    private void addEmptyLabel() {
        JLabel emptyLabel = new JLabel("");
        container.add(emptyLabel);
        emptyLabel.setPreferredSize(new Dimension(2000,0));
    }
    private void setUpButton(JButton button) {
        container.add(button);
        Dimension dimension = new Dimension(70, 20);
        button.setPreferredSize(dimension);
    }
    private void updateBox(int position, String digit) {
        int row = position / 9;
        int col = position % 9;
        rawSudoku[row][col] = Integer.parseInt(digit);
    }
    private String printSudoku(int[][] sudoku) {
        StringBuilder printableSudoku = new StringBuilder(
                "\n" + sudokuUI.getMessage() + "\n" + "time elapsed " + sudokuUI.getTimeElapsed() + " ms" + "\n\n"
        );
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                printableSudoku.append("  ").append(sudoku[i][j]).append("  ");
                if (j == 8) {
                    printableSudoku.append("\n");
                }
            }
        }
        return printableSudoku.toString();
    }
    private void resetGridUI() {
        for (JTextField textField : linear9x9Grid) {
            textField.setText("0");
        }
    }
    private void resetRawSudoku() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                rawSudoku[i][j] = 0;
            }
        }
    }
    private void resetTextArea() {
        result.setText("");
    }
    private void resetAll() {
        resetGridUI();
        resetRawSudoku();
        resetTextArea();
    }
    private void saveSudoku() {
        JFileChooser fileSave = new JFileChooser();
        fileSave.showSaveDialog(container);
        saveFile(fileSave.getSelectedFile());
    }
    private void saveFile(File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            StringBuilder printableSudoku = new StringBuilder();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    printableSudoku.append(rawSudoku[i][j]);
                }
            }
            writer.write(String.valueOf(printableSudoku));
            writer.close();
        } catch (IOException e) {
            System.out.println("Couldn't write the sudoku out:" + e.getMessage());
        }
    }
    private void loadSudoku() {
        resetTextArea();
        JFileChooser fileOpen = new JFileChooser();
        fileOpen.showOpenDialog(container);
        loadFile(fileOpen.getSelectedFile());
    }
    private void loadFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String digit = reader.readLine();
            for (int index = 0; index < 81; index++) {
                String string = digit.substring(index, index + 1);
                linear9x9Grid.get(index).setText(string);
                rawSudoku[index / 9][ index % 9] = Integer.parseInt(string);
            }
        } catch (IOException e) {
            System.out.println("Couldn't read the sudoku out:" + e.getMessage());
        }
    }
}

