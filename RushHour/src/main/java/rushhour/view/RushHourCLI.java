package rushhour.view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import rushhour.model.Direction;
import rushhour.model.Move;
import rushhour.model.RushHour;
import rushhour.model.RushHourException;
import rushhour.model.RushHourSolver;
// 2225-124-project-great-lakes-avengers\data\03_00.csv
public class RushHourCLI {

    public static void main(String[] args) throws FileNotFoundException, IOException, RushHourException {
        try (Scanner scan = new Scanner(System.in)) {
            System.out.print("Enter a Rush Hour filename: ");
            // String filename = scan.nextLine();\
            String filename = "data/03_00.csv";
            RushHour rush = new RushHour(filename);

            System.out.println("\nType 'help' for help menu.\n");

            while (true) {
                System.out.print(rush.toString() + "\n\nMoves: " + rush.getMoveCount() + "\n> ");
                String input = scan.nextLine();
                String[] tokens = input.trim().split("\\s+");
                String command = tokens[0].toLowerCase();

                switch (command) {
                    case "help":
                        System.out.println("Help Menu:");
                        System.out.println("\thelp - this menu");
                        System.out.println("\tquit - quit");
                        System.out.println("\thint - display a valid move");
                        System.out.println("\treset - reset the game");
                        System.out.println("\tsolve - automatically solve the game");
                        System.out.println("\t<symbol> <UP|DOWN|LEFT|RIGHT> - move the vehicle one space in the given direction");
                        break;

                    case "quit":
                        System.out.println("Goodbye!");
                        return;

                    case "hint":
                        Collection<Move> list = rush.getPossibleMoves();
                        if (!list.isEmpty()) {
                            Move move = ((ArrayList<Move>) list).get(0);
                            System.out.println("One move is " + move);
                        }
                        break;

                    case "reset":
                        rush = new RushHour(filename);
                        break;

                    case "solve":
                        RushHourSolver solution = RushHourSolver.solve(rush);
                        if (solution != null) {
                            for (Move move : solution.getMoveHistory()) {
                                System.out.println("Move: " + move);
                                rush.moveVehicle(move);
                                System.out.println(rush.toString());
                            }
                            System.out.println("Moves: " + rush.getMoveCount() + "\nWell Done!!!\nType 'reset' to play again");
                        } else {
                            System.out.println("No solution found.");
                        }
                        break;

                    default:
                        if (tokens.length != 2) {
                            System.out.println("Not Good Input");
                            break;
                        }

                        char symbol = tokens[0].charAt(0);
                        String maybeGood = tokens[1];

                        if (maybeGood.toUpperCase().equals("LEFT") || maybeGood.toUpperCase().equals("RIGHT") || maybeGood.toUpperCase().equals("DOWN") || maybeGood.toUpperCase().equals("UP")) {
                            Direction direction = Direction.valueOf(tokens[1].toUpperCase());
                            Move move = new Move(symbol, direction);
                            rush.moveVehicle(move);
                        } else {
                            System.out.println("Invalid Move: Bad Direction");
                        }
                        if (rush.isGameOver()) {
                            System.out.println(rush.toString());
                            System.out.println("\nWell done!\nType 'reset' to play again");
                        }
                }
            }
        }
    }
}
