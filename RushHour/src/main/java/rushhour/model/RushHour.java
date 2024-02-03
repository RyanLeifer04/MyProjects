package rushhour.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RushHour {

    // fields the uml said to add
    private int BOARD_DIM = 6;
    private char RED_SYMBOL = 'R';
    private char EMPTY_SYMBOL = '-';
    private Position EXIT_POS = new Position(2, 5);

    private String[][] board = new String[BOARD_DIM][BOARD_DIM];

    public String[][] getBoard() {
        return board;
    }

    // just to keep track of the move count
    private int moveCount = 0;
    // should be hash map becuase doesn't need to be sorted and its a map becuase I
    // want to get the vechicle based on the sybmol for the moveVechicle method
    private Map<Character, Vehicle> ls = new HashMap<>();
    private RushHourObserver observer;
    private String filename;

    public RushHour(RushHour other) {
        this.BOARD_DIM = other.BOARD_DIM;
        this.RED_SYMBOL = other.RED_SYMBOL;
        this.EMPTY_SYMBOL = other.EMPTY_SYMBOL;
        this.EXIT_POS = other.EXIT_POS;
    
        for (int i = 0; i < BOARD_DIM; i++) {
            System.arraycopy(other.board[i], 0, this.board[i], 0, BOARD_DIM);
        }
    
        this.moveCount = other.moveCount;
    
        for (Map.Entry<Character, Vehicle> entry : other.ls.entrySet()) {
            this.ls.put(entry.getKey(), new Vehicle(entry.getValue()));
        }
    
        // Exclude the observer attribute
        this.observer = null;
    
        this.filename = other.filename;
    }
    
    
        

    /**
     * 
     * @param filename the file being opened
     * @throws FileNotFoundException need to throw the exception
     * @throws IOException           need to throw the exception
     */
    public RushHour(String filename) throws FileNotFoundException, IOException {
        this.filename = filename;

        // parses the file and adds the vechicles to the hashmap
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            while (line != null) {
                String[] cut = line.split(",");
                ls.put((cut[0].charAt(0)),
                        new Vehicle((cut[0].charAt(0)),
                                new Position(Integer.parseInt(cut[1]), Integer.parseInt(cut[2])),
                                new Position(Integer.parseInt(cut[3]), Integer.parseInt(cut[4]))));
                line = reader.readLine();
            }

        } catch (Exception E) {
            System.out.println("An Error Occured");
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                board[i][j] = "-";

            }
        }

        for (Vehicle car : ls.values()) {
            board[car.getFront().getRow()][car.getFront().getCol()] = String.valueOf(car.getSymbol());
            board[car.getBack().getRow()][car.getBack().getCol()] = String.valueOf(car.getSymbol());
            if (car.getMiddlePosition() != null) {
                Position par = car.getMiddlePosition();
                board[par.getRow()][par.getCol()] = String.valueOf(car.getSymbol());
            }

        }

    }

    // A method Im adding to find the direction becuase I have the same logic twice
    public Position findDir(Direction dir, Vehicle vehicle) {

        Position newPosition = null;
        if (dir == Direction.UP) {
            newPosition = new Position(vehicle.getFront().getRow() - 1, vehicle.getFront().getCol());
        } else if (dir == Direction.DOWN) {
            newPosition = new Position(vehicle.getBack().getRow() + 1, vehicle.getBack().getCol());
        } else if (dir == Direction.LEFT) {
            newPosition = new Position(vehicle.getBack().getRow(), vehicle.getBack().getCol() - 1);
        } else if (dir == Direction.RIGHT) {
            newPosition = new Position(vehicle.getFront().getRow(), vehicle.getFront().getCol() + 1);
        }

        return newPosition;

    }

    public boolean outOfBounds(Position newPosition, int BOARD_DIM) {
        if (newPosition.getRow() > 0 || newPosition.getRow() < BOARD_DIM || newPosition.getCol() > 0
                || newPosition.getCol() < BOARD_DIM) {
            return true;

        }
        return false;
    }
    

    // moving the vehicle
    public void moveVehicle(Move move) throws RushHourException {
        Vehicle vehicle = ls.get(move.getSymbol());
        if (vehicle == null) {
            throw new RushHourException("Invalid Move: Bad Direction for Car");
        }
    
        Position oldFront = vehicle.getFront();
        Position oldBack = vehicle.getBack();
    
        Position newFront = findDir(move.getDir(), vehicle);
        if (newFront == null) {
            throw new RushHourException("Invalid Move: Bad Direction");
        }
    
        // Calculate newBack based on the direction of the move
        Position newBack = null;
        if (move.getDir() == Direction.UP) {
            newBack = new Position(vehicle.getBack().getRow() - 1, vehicle.getBack().getCol());
        } else if (move.getDir() == Direction.DOWN) {
            newBack = new Position(vehicle.getBack().getRow() + 1, vehicle.getBack().getCol());
        } else if (move.getDir() == Direction.LEFT) {
            newBack = new Position(vehicle.getBack().getRow(), vehicle.getBack().getCol() - 1);
        } else if (move.getDir() == Direction.RIGHT) {
            newBack = new Position(vehicle.getBack().getRow(), vehicle.getBack().getCol() + 1);
        }
    
        if (!outOfBounds(newFront, BOARD_DIM) || !outOfBounds(newBack, BOARD_DIM)) {
            throw new RushHourException("Invalid move: Vehicle is at the edge of the board");
        }
    
        for (Vehicle otherVehicle : ls.values()) {
            if (otherVehicle != vehicle) {
                if ((otherVehicle.getBack().equals(newFront) && otherVehicle.getSymbol() != RED_SYMBOL)
                        || (otherVehicle.getFront().equals(newBack) && otherVehicle.getSymbol() != RED_SYMBOL)) {
                    throw new RushHourException("Invalid move: Another Car is in this Spot");
                }
            }
        }
    
        vehicle.move(move.getDir(), ls, BOARD_DIM);
    
        ls.remove(move.getSymbol());
        ls.put(move.getSymbol(), vehicle);
    
        // update positions on the board
        board[oldFront.getRow()][oldFront.getCol()] = String.valueOf(EMPTY_SYMBOL);
        board[oldBack.getRow()][oldBack.getCol()] = String.valueOf(EMPTY_SYMBOL);
        board[newFront.getRow()][newFront.getCol()] = String.valueOf(vehicle.getSymbol());
        board[newBack.getRow()][newBack.getCol()] = String.valueOf(vehicle.getSymbol());
    
        moveCount++;
    }

    // needs to be added
    public boolean isGameOver() {

        Vehicle car = ls.get(RED_SYMBOL);
        if (car.getFront().getRow() == (EXIT_POS.getRow()) && car.getFront().getCol() == (EXIT_POS.getCol())
                || car.getBack().getRow() == (EXIT_POS.getRow()) && car.getBack().getCol() == (EXIT_POS.getCol())) {
            return true;
        }
        return false;
    }

    public Collection<Move> getPossibleMoves() throws RushHourException {
        try {
            if (isGameOver()) {
                System.out.println("DOES IT FUCKING WORK");
                throw new RushHourException("Game is already over");
            }
            Collection<Move> pos = new ArrayList<>();
            // first make a new collection to add to
            for (Vehicle vehicle : ls.values()) {
                for (Direction dir : Direction.values()) {
    
                    Position newPosition = findDir(dir, vehicle);
    
                    if (outOfBounds(newPosition, BOARD_DIM)) {
    
                    boolean canMove = true;
                    for (Vehicle otherVehicle : ls.values()) {
                        if (otherVehicle != vehicle) {
                            if ((otherVehicle.getBack().getRow() == newPosition.getRow()
                                    && otherVehicle.getBack().getCol() == newPosition.getCol())
                                    || (otherVehicle.getFront().getRow() == newPosition.getRow()
                                    && otherVehicle.getFront().getCol() == newPosition.getCol())) {
                                canMove = false;
                                break;
                            }
                        }
                    }
    
                    if (canMove) {
                        pos.add(new Move(vehicle.getSymbol(), dir));
                    }
                }   
                }
    
            }
    
            return pos;
        } catch (Exception RushHourException) {
            System.out.println("ERROR");
            return null;
        }
    
    }
    
    

    // just return the field I added
    public int getMoveCount() {
        return moveCount;
    }

    // method to find the middle of two points
    private int midpoint(int a, int b) {
        return (a + b) / 2;
    }

    // I used string builder becuase its usufull and i dont want to not use it;
    @Override
    public String toString() {
        // Create a copy of the board to add the exit symbol to without modifying the
        // original board
        String[][] boardCopy = new String[BOARD_DIM][BOARD_DIM];
        for (int k = 0; k < BOARD_DIM; k++) {
            for (int j = 0; j < BOARD_DIM; j++) {
                boardCopy[k][j] = String.valueOf(EMPTY_SYMBOL);
            }
        }

        int exitRow = EXIT_POS.getRow();

        // create a new StringBuilder to hold the string representation of the board
        StringBuilder sb = new StringBuilder();

        // add the vehicles to the board string
        for (Vehicle car : ls.values()) {
            Position front = car.getFront();
            Position back = car.getBack();
            boardCopy[front.getRow()][front.getCol()] = String.valueOf(car.getSymbol());
            boardCopy[back.getRow()][back.getCol()] = String.valueOf(car.getSymbol());
            if (front.getRow() == back.getRow()) {
                int midCol = midpoint(front.getCol(), back.getCol());
                boardCopy[front.getRow()][midCol] = String.valueOf(car.getSymbol());
            } else if (front.getCol() == back.getCol()) {
                int midRow = midpoint(front.getRow(), back.getRow());
                boardCopy[midRow][front.getCol()] = String.valueOf(car.getSymbol());
            }
        }

        // update the board
        for (int i = 0; i < BOARD_DIM; i++) {
            for (int j = 0; j < BOARD_DIM; j++) {
                board[i][j] = boardCopy[i][j];
            }
        }

        // create the board string
        sb.append("\n");
        for (int i = 0; i < BOARD_DIM; i++) {
            sb.append(board[i][0]);
            for (int j = 1; j < BOARD_DIM; j++) {
                sb.append(board[i][j]);
            }
            if (i == exitRow) {
                sb.append("   < Exit");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // we have this main to test our methods as we go allong

    public static void main(String[] args) throws FileNotFoundException, IOException, RushHourException {
        RushHour rush = new RushHour("data/04_00.csv");
        Move move = new Move('R', Direction.RIGHT);

        System.out.println(rush.toString());
        rush.moveVehicle(move);
        System.out.println("________________________");
        System.out.println(rush.toString());
        System.out.println(rush.getMoveCount());
        System.out.println("________________________");

        System.out.println(rush.getPossibleMoves());

    }

    public Map<Character, Vehicle> getLs() {
        return ls;
    }










    

    public void registerObserver(RushHourObserver observer) {
        this.observer = observer;
    }

    private void notifyObserver(Vehicle vehicle) {
        if (vehicle != null) {
            observer.vehicleMoved(vehicle);
        }

    }






    public Collection<Vehicle> getVehicles() {
        List<Vehicle> work = new ArrayList<>();
        for (Vehicle car : ls.values()) {
            work.add((Vehicle) car);
        }
        return work;

    }

    public boolean isObstructed(Vehicle vehicle, Position newPosition) {
        for (Vehicle otherVehicle : getVehicles()) {
            if (otherVehicle != vehicle) {
                if ((otherVehicle.getBack().getRow() == newPosition.getRow()
                        && otherVehicle.getBack().getCol() == newPosition.getCol())
                        || (otherVehicle.getFront().getRow() == newPosition.getRow()
                                && otherVehicle.getFront().getCol() == newPosition.getCol())) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Vehicle> getBlockingVehiclesForTarget() {
        List<Vehicle> car = new ArrayList<>();
        int rowREd = EXIT_POS.getRow();
        int col = ls.get(RED_SYMBOL).getFront().getCol();
        for (Vehicle cat : ls.values()){
            if (cat.getFront().getRow() == rowREd && cat.getFront().getCol() > col || cat.getSymbol() == RED_SYMBOL)
            {
                car.add((Vehicle) cat);
            }
        }
        return car;
    }

}