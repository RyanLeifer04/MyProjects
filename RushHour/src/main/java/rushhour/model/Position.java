package rushhour.model;

public class Position {
    private int row;
    private int col;

    /**
     * 
     * @param row the row that the car is in
     * @param col the column that the care is in
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }


    @Override
    public String toString() {
        return row +" , "+col;
    }



}
