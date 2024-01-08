package minesweeper;

import java.util.Objects;

public class Cell {
    private int i;
    private int j;
    private char value;
    private int minesAround;
    private boolean isAMine;
    private boolean isVisit;

    public static int emptyCells;
    public static int mines;
    public static int guessedMines;
    public static int unExploredCells;
    public static boolean failed;

    static {
        emptyCells = 0;
        mines = 0;
        guessedMines = 0;
        unExploredCells = 0;
        failed = false;
    }

    public Cell(int i, int j) {
        this.i = i;
        this.j = j;
        this.value = '.';
        this.minesAround = 0;
        this.isAMine = false;
        this.isVisit = false;
    }

    public int getI() { return this.i; }
    public int getJ() { return this.j; }

    public char getValue() { return this.value; }
    public void setValue(char value) { this.value = value; }

    public int getMinesAround() {
        return minesAround;
    }

    public void setMinesAround(int minesAround) {
        if (!isAMine) {
            this.minesAround = minesAround;
        }
    }

    public boolean isAMine() {
        return isAMine;
    }

    public void setAMine(boolean AMine) {
        isAMine = AMine;
        this.minesAround = 0;
    }

    public boolean isVisit() {
        return isVisit;
    }

    public void setVisit(boolean visit) {
        isVisit = visit;
    }

    public static boolean isFinished() {
        return (mines == guessedMines && emptyCells == 0) || (unExploredCells == mines);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Cell other = (Cell) o;
        return Objects.equals(i, other.i) && Objects.equals(j, other.j) && Objects.equals(value, other.value) &&
                Objects.equals(minesAround, other.minesAround) && Objects.equals(isAMine, other.isAMine) &&
                Objects.equals(isVisit, other.isVisit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j, value, minesAround, isAMine, isVisit);
    }

    @Override
    public String toString() {
        String str;
        if (minesAround != 0 && !isAMine && isVisit) {
            str = (value != '*') ? String.valueOf(minesAround) : String.valueOf(value);
        } else if (failed && isAMine) {
            str = "X";
        } else {
            str = String.valueOf(value);
        }
        return str;
    }
}
