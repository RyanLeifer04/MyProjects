package rushhour.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class RushHourTest {

    Vehicle verticalVehicle = new Vehicle('a', new Position(4, 5), new Position(5, 5));
    Vehicle horizontalVehicle = new Vehicle('b', new Position(5, 5), new Position(5, 4));
    RushHour rush;

    public RushHourTest() throws FileNotFoundException, IOException {
     super();
     rush = new RushHour("data/03_00.csv");
 }
 

    @Test
    public void testFindDirUp() {
        // setup
        Direction direct = Direction.UP;
        // expected
        Position expected = new Position(3, 5);

        // calculate actual
        Position calculated = rush.findDir(direct, verticalVehicle);

        // assert
        assertEquals(expected.getCol(), calculated.getCol());
    }

    @Test
    public void testFindDirDown() {
        // setup
        Direction direct = Direction.DOWN;
        // expected
        Position expected = new Position(5, 5);

        // calculate actual
        Position calculated = rush.findDir(direct, verticalVehicle);

        // assert
        assertEquals(expected.getCol(), calculated.getCol());
    }

    @Test
    public void testFindDirLeft() {
        // setup
        Direction direct = Direction.LEFT;
        // expected
        Position expected = new Position(5, 4);

        // calculate actual
        Position calculated = rush.findDir(direct, horizontalVehicle);

        // assert
        assertEquals(expected.getRow(), calculated.getRow());
    }

    @Test
    public void testFindDirRight() {
        // setup
        Direction direct = Direction.RIGHT;
        // expected
        Position expected = new Position(5, 5);

        // calculate actual
        Position calculated = rush.findDir(direct, horizontalVehicle);

        // assert
        assertEquals(expected.getCol(), calculated.getCol());
    }

    @Test
    public void testOutOfBoundsValid() {
        // setup
        Position pos = new Position(4, 4);
        boolean expected = false;

        // calculate
        boolean calc = rush.outOfBounds(pos, 6);

        // assert
        assertEquals(expected, calc);
    }

    @Test
    public void testOutOfBoundsInvalid() {
        // setup
        Position pos = new Position(8, 8);
        boolean expected = true;

        // calculate
        boolean calc = rush.outOfBounds(pos, 6);

        // assert
        assertEquals(expected, calc);
    }

    @Test
    public void testGameOver() {
        assertEquals(false, rush.isGameOver());
    }

    @Test
    public void testPossibleMoves() throws RushHourException {

        Collection<Move> move = rush.getPossibleMoves();

        assertNotEquals("[[A,UP], [A,RIGHT], [A,LEFT], [A,DOWN], [R,UP], [R,DOWN], [O,UP], [O,LEFT], [O,DOWN]]", move);
    }

    @Test
    public void testToString() {
        // setup
        String expected = "\n--O---\n--OA--\nRROA--   < Exit\n------\n------\n------\n";

        // compute
        String computed = rush.toString();

        // assert
        assertEquals(expected, computed);
    }

     @Test
     public void testMoveToString()
     {
          Move move = new Move('A', Direction.UP);
          String expected = "[A,UP]";

          String computed = move.toString();

          assertEquals(expected, computed);
     }

     @Test
     public void testMoveGetters()
     {
          Move move = new Move('A', Direction.UP);

          assertEquals('A', move.getSymbol());
          assertEquals(Direction.UP, move.getDir());
     }

     @Test
     public void testPositionGetters()
     {
          Position pos = new Position(0, 0);

          assertEquals(0, pos.getRow());
          assertEquals(0, pos.getCol());
     }

     @Test
     public void testVehicleGetters()
     {
          Vehicle car = new Vehicle('A', new Position(0, 0), new Position(1, 0));

          assertEquals('A', car.getSymbol());
          assertEquals(0, car.getFront().getCol());
          assertEquals(0, car.getBack().getRow());
     }

     @Test
     public void testVehicleMoveValid() throws RushHourException
     {
          //setup
          Vehicle car = new Vehicle('A', new Position(0, 0), new Position(1, 0));
          boolean expected = true;
          Map<Character, Vehicle> coll = rush.getLs();

          //compute
          boolean computed = car.move(Direction.DOWN, coll, 6);

          //assert
          assertEquals(expected, computed);
     }

     public void testVehicleMoveInvalid() throws RushHourException
     {
          //setup
          Vehicle car = new Vehicle('A', new Position(0, 0), new Position(1, 0));
          boolean expected = false;
          Map<Character, Vehicle> coll = rush.getLs();

          //compute
          boolean computed = car.move(Direction.UP, coll, 6);

          //assert
          assertEquals(expected, computed);
     }
}
