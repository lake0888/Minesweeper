package minesweeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static final Scanner scanner = new Scanner(System.in);
    static Cell[][] mineField = new Cell[9][9];

    public static void main(String[] args) {
        // write your code here
        initMineField();
        int mines = mines();
        scanner.nextLine();
        Cell.mines = mines;
        Cell.unExploredCells = 81;
        setMinesInFiled(mines);
        showMineField();
        play();
    }

    public static int mines() {
        System.out.print("How many mines do you want on the field? ");
        return scanner.nextInt();
    }

    public static void initMineField() {
        for (int i = 0; i < mineField.length; i++) {
            for (int j = 0; j < mineField[i].length; j++) {
                mineField[i][j] = new Cell(i, j);
            }
        }
    }

    public static void setMinesInFiled(int mines) {
        while (mines > 0) {
            int cell = (int) (Math.random() * 80);
            int i = cell / 9;
            int j = cell % 9;
            if (!mineField[i][j].isAMine()) {
                mineField[i][j].setAMine(true);
                updateMinesAround(mineField[i][j]);
                mines--;
            }
        }
    }

    public static void updateMinesAround(Cell cell) {
        List<Cell> cellsAround = cellsAround(cell);
        for (Cell current : cellsAround) {
            if (!current.isAMine()) {
                current.setMinesAround(current.getMinesAround() + 1);
            }
        }
    }

    public static List<Cell> cellsAround(Cell cell) {
        List<Cell> aroundCells = new ArrayList<>();
        //TOP
        int i = cell.getI() - 1, j = cell.getJ();
        if (isCellValid(i, j)) {
            aroundCells.add(mineField[i][j]);
        }

        //TOP RIGHT
        j = j + 1;
        if (isCellValid(i, j)) {
            aroundCells.add(mineField[i][j]);
        }

        //RIGHT
        i = cell.getI();
        if (isCellValid(i, j)) {
            aroundCells.add(mineField[i][j]);
        }

        //BOTTOM RIGHT
        i = i + 1;
        if (isCellValid(i, j)) {
            aroundCells.add(mineField[i][j]);
        }

        //BOTTOM
        j = cell.getJ();
        if (isCellValid(i, j)) {
            aroundCells.add(mineField[i][j]);
        }

        //BOTTOM LEFT
        j = j - 1;
        if (isCellValid(i, j)) {
            aroundCells.add(mineField[i][j]);
        }

        //LEFT
        i = cell.getI();
        if (isCellValid(i, j)) {
            aroundCells.add(mineField[i][j]);
        }

        //TOP LEFT
        i = i - 1;
        if (isCellValid(i, j)) {
            aroundCells.add(mineField[i][j]);
        }
        return aroundCells;
    }

    public static boolean isCellValid(int i, int j) {
        return i >= 0 && i < 9 && j >= 0 && j < 9;
    }

    public static void showMineField() {
        printHeader();
        for (int i = 0; i < mineField.length; i++) {
            System.out.print(i + 1 + "|");
            for (int j = 0; j < mineField[i].length; j++) {
                System.out.print(mineField[i][j]);
            }
            System.out.println("|");
        }
        printBorder();
    }

    public static void printHeader() {
        System.out.print(" |");
        for (int i = 0; i < mineField.length; i++) {
            System.out.print(i + 1);
        }
        System.out.println("|");
        printBorder();
    }

    public static void printBorder() {
        System.out.print("-|");
        for (int i = 0; i < mineField.length; i++) {
            System.out.print("-");
        }
        System.out.println("|");
    }

    public static void play() {
        while (!Cell.isFinished() && !Cell.failed) {
            System.out.print("Set/unset mine marks or claim a cell as free: ");
            String line = scanner.nextLine();
            if (line.matches("^\\d\\s\\d\\s(free|mine)")) {
                String[] params = line.split(" ");
                int i = Integer.parseInt(params[0]) - 1;
                int j = Integer.parseInt(params[1]) - 1;
                String option = params[2];
                if (isCellValid(i, j)) {
                    Cell inMatrix = mineField[j][i];
                    if (inMatrix.getValue() == '/') {
                        System.out.println("This cell has already explored");
                    } else if (inMatrix.isAMine() && option.equals("free")) {
                        Cell.failed = true;
                        showMineField();
                        System.out.println("You stepped on a mine and failed!");
                    } else {
                        if (option.equals("mine")) {
                            if (!inMatrix.isVisit()) {
                                inMatrix.setVisit(true);
                                inMatrix.setValue('*');
                                if (inMatrix.isAMine()) Cell.guessedMines++;
                                else Cell.emptyCells++;
                                Cell.unExploredCells--;
                            } else {
                                inMatrix.setVisit(false);
                                inMatrix.setValue('.');
                                if (inMatrix.isAMine()) Cell.guessedMines--;
                                else Cell.emptyCells--;
                                Cell.unExploredCells++;
                            }

                        } else {
                            exploredCell(inMatrix);
                        }
                        showMineField();
                    }
                }
            }
        }
        if (Cell.isFinished()) System.out.println("Congratulations! You found all the mines!");
    }

    public static boolean canBeExplored(Cell cell) {
        List<Cell> aroundCells = cellsAround(cell);
        for (Cell current : aroundCells) {
            if (current.isAMine()) return false;
        }
        return true;
    }

    public static void exploredCell(Cell cell) {
        if (cell.getMinesAround() == 0 && canBeExplored(cell)) {
            cell.setVisit(true);
            cell.setValue('/');
            Cell.unExploredCells--;
            List<Cell> aroundCells = cellsAround(cell);
            for (Cell current : aroundCells) {
                if (!current.isVisit()) {
                    exploredCell(current);
                } else {
                    if (!current.isAMine() && current.getValue() == '*') {
                        current.setValue('/');
                        Cell.unExploredCells++;
                        exploredCell(current);
                    }
                }
            }
        } else if (cell.getMinesAround() != 0) {
            cell.setVisit(true);
            Cell.unExploredCells--;
        }
    }
}