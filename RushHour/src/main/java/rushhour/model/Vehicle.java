package rushhour.model;

import java.util.Map;

public class Vehicle {
    private char symbol;
    private Position back;
    private Position front;

    /**
     * 
     * @param symbol the char that represents the car 
     * @param back the positon of the back of the car
     * @param front the positon of the front of the car
     */
    public Vehicle(char symbol, Position back, Position front){
        this.symbol = symbol;
        this.back = back;
        this.front =  front;
    }

    public Vehicle(Vehicle car){
        this.symbol = car.getSymbol();
        this.back = car.getBack();
        this.front = car.getFront();
    }



    public char getSymbol() {
        return symbol;
    }
    public Position getBack() {
        return back;
    }
    public Position getFront() {
        return front;
    }


    public Position getMiddlePosition() {
        if (this.getFront().getRow() == this.getBack().getRow()) {
            if (Math.abs(this.getFront().getCol() - this.getBack().getCol()) == 2) {
                int midCol = (this.getFront().getCol() + this.getBack().getCol()) / 2;
                return new Position(this.getFront().getRow(), midCol);
            }
        } else if (this.getFront().getCol() == this.getBack().getCol()) {
            if (Math.abs(this.getFront().getRow() - this.getBack().getRow()) == 2) {
                int midRow = (this.getFront().getRow() + this.getBack().getRow()) / 2;
                return new Position(midRow, this.getFront().getCol());
            }
        }
        return null;
    }
    


   


    /**
     * 
     * @param dir the directoin of the car
     * @return return true if it can move and false if not 
     * @throws Exception needs to throw exception
     */
       
     public boolean move(Direction dir, Map<Character, Vehicle> ls, int BOARD_DIM) throws RushHourException {
        // Determine the amount of movement in rows and columns based on the direction
        try {
        int rowChange = 0;
        int colChange = 0;
        if (dir == Direction.UP) {
            rowChange = -1;
        } else if (dir == Direction.DOWN) {
            rowChange = 1;
        } else if (dir == Direction.LEFT) {
            colChange = -1;
        } else if (dir == Direction.RIGHT) {
            colChange = 1;
        } else {
            throw new RushHourException("Invalid direction: " + dir);
        }
        
        // Update the positions of the front and back of the vehicle based on the direction
        int newFrontRow = front.getRow() + rowChange;
        int newFrontCol = front.getCol() + colChange;
        int newBackRow = back.getRow() + rowChange;
        int newBackCol = back.getCol() + colChange;
        
        // Check if the new positions are within the bounds of the board
        if (newFrontRow < 0 || newFrontRow >= BOARD_DIM || newFrontCol < 0 || newFrontCol >= BOARD_DIM
                || newBackRow < 0 || newBackRow >= BOARD_DIM || newBackCol < 0 || newBackCol >= BOARD_DIM) {
            throw new RushHourException("Vehicle would go out of bounds");
        }
        
        // Check if the new positions overlap with any other vehicles
        for (Vehicle otherVehicle : ls.values()) {
            if (otherVehicle != this) {
                if (otherVehicle.getFront().equals(new Position(newFrontRow, newFrontCol))
                        || otherVehicle.getBack().equals(new Position(newBackRow, newBackCol))) {
                    throw new RushHourException("Vehicle would collide with another vehicle");
                }
            }
        }
        
        // Update the positions of the front and back of the vehicle
        front = new Position(newFrontRow, newFrontCol);
        back = new Position(newBackRow, newBackCol);
        
        return true;
    }
    //added this bc the method does not work when called without it
    catch(Exception RushHourException)
    {
        System.out.println("Error");
        return false;
    }
    }
    



    //I added this for myself to check if I parsed the file correctly 
    @Override
    public String toString() {
        return ","+this.back+","+this.front;
    }

    public Object[] toArray() {
        return null;
    }
}