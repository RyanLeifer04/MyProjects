package rushhour.model;

public class Move {
    private char symbol;
    private Direction dir;




/**
 * 
 * @param symbol the char that represents the car 
 * @param dir the direction of the car
 */
    public Move(char symbol, Direction dir){
        this.symbol = symbol;
        this.dir = dir;
    }


    public char getSymbol() {
        return symbol;
    }
    public Direction getDir() {
        return dir;
    }

  @Override
  public String toString() {
      return "["+symbol+","+dir+"]";
      }


    
}
