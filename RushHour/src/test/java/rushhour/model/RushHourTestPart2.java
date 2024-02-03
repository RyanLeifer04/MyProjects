// package rushhour.model;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotEquals;

// import java.io.FileNotFoundException;
// import java.io.IOException;
// import java.util.Collection;
// import java.util.Map;

// import org.junit.jupiter.api.Test;

// public class RushHourTestPart2 {
//     Vehicle verticalVehicle = new Vehicle('a', new Position(4, 5), new Position(6, 5));
//     Vehicle horizontalVehicle = new Vehicle('b', new Position(5, 5), new Position(5, 4));
//     RushHour rush;

//     public RushHourTestPart2() throws FileNotFoundException, IOException {
//      super();
//      rush = new RushHour("data/03_00.csv");
//  }

//     @Test
//     public void getMiddlePositionTest(){
//         //setup
//         Position expectedMidPosition = new Position(5, 5);
        

//         //calculate
//         Position position = verticalVehicle.getMiddlePosition();
//         //assert
//         assertEquals(expectedMidPosition.getCol(), position.getCol());
//         assertEquals(expectedMidPosition.getRow(), position.getRow());


//     }
// }
