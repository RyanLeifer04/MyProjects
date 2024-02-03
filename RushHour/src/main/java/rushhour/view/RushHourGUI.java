package rushhour.view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import rushhour.model.Direction;
import rushhour.model.Move;
import rushhour.model.Position;
import rushhour.model.RushHour;
import rushhour.model.RushHourException;
import rushhour.model.RushHourSolver;
import rushhour.model.Vehicle;

public class RushHourGUI extends Application {

    private RushHour hour;
    private int counter = 0;
    private Label bottomLabel;
    private Label counterLabel;

    public RushHourGUI() throws FileNotFoundException, IOException {
        hour = new RushHour("data//03_00.csv");

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        bottomLabel = new Label("Game started. Make your move!");
        counterLabel = new Label("Move count: 0");
        GridPane pane = makeBoard();

        primaryStage.setTitle("Rush Hour");
        primaryStage.setScene(new Scene(pane));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private GridPane makeBoard() {
        GridPane pane = new GridPane();
        
    
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i == 2 && j == 5) {
                    addImageToGrid(pane, "file:data/cars/EXIT.png", j, i);
                } else {
                    addImageToGrid(pane, "file:data/newCOlors/unnamed-4.png", j, i);
                }
            }
        }
        createVehicleButtons(pane);
        
    
        pane.add(bottomLabel, 0, 6, 6, 1);
        pane.add(counterLabel, 0, 7, 6, 1);
        Button resetButton = createResetButton();
        pane.add(resetButton, 0, 8, 6, 1);
    
        // Create the recommendation button and add it to the grid
        Button recommendationButton = createRecommendationButton();
        pane.add(recommendationButton, 0, 9, 6, 1);

        Button solveButton = createSolveButton();
    pane.add(solveButton, 0, 10, 6, 1);

    
        return pane;
    }
    private Button createSolveButton() {
        Button solveButton = new Button("Solve");
        solveButton.setOnAction(event -> {
            try {
                RushHourSolver solver = new RushHourSolver(hour);
                RushHourSolver solution = RushHourSolver.solve(hour);
                if (solution != null) {
                    executeSolution(solution.getMoveHistory(), (GridPane) solveButton.getParent());
                } else {
                    bottomLabel.setText("No solution found.");
                }
            } catch (Exception e) {
                bottomLabel.setText("Failed to solve the puzzle.");
            }
            createSolveButton();

        });
        return solveButton;
    }
    private void executeSolution(List<Move> moves, GridPane pane) {
        for (Move move : moves) {
            try {
                hour.moveVehicle(move);
                counter++; // Increment the counter
                counterLabel.setText("Move count: " + counter); // Update the counterLabel
                updateBoard(pane);
                checkGameStatus();
            } catch (RushHourException e) {
                bottomLabel.setText("Error executing solution move!");
            }
        }
    }
        
    
    private Button createRecommendationButton() {
        Button recommendationButton = new Button("Get Recommendation");
        recommendationButton.setOnAction(event -> {
            try {
                List<Move> possibleMoves = (List<Move>) hour.getPossibleMoves();
                recommendationButton.setText(possibleMoves.get(counter).toString());
                
            } catch (RushHourException e) {
                bottomLabel.setText("Failed to get recommendation.");
            }
        });
        return recommendationButton;
    }
    
    

    private void addImageToGrid(GridPane pane, String imageUrl, int col, int row) {
        Image image = new Image(imageUrl);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(120);
        imageView.setFitHeight(60);

        Button button = new Button("", imageView);
        button.setPadding(new Insets(0, 0, 0, 0));
        button.setPrefWidth(120);
        button.setPrefHeight(60);
        pane.add(button, col, row);
    }

    private void createVehicleButtons(GridPane pane) {
        List<Vehicle> vehiclesList = new ArrayList<>(hour.getVehicles());

        Vehicle firstVehicle = vehiclesList.get(2);
        Vehicle secondVehicle = vehiclesList.get(1);
        Vehicle thirdVehicle = vehiclesList.get(0);

        createButton(pane, firstVehicle, "file:data/newCOlors/unnamed-1.png", firstVehicle.getFront().getCol(),
                firstVehicle.getFront().getRow(), Direction.DOWN);
        createButton(pane, firstVehicle, "file:data/newCOlors/unnamed-1.png",
                firstVehicle.getMiddlePosition().getCol(), firstVehicle.getMiddlePosition().getRow(), null);

        createButton(pane, firstVehicle, "file:data/newCOlors/unnamed-1.png", firstVehicle.getBack().getCol(),
                firstVehicle.getBack().getRow(), Direction.UP);

        createButton(pane, secondVehicle, "file:data/newCOlors/unnamed-3.png", secondVehicle.getFront().getCol(),
                secondVehicle.getFront().getRow(), Direction.RIGHT);
        createButton(pane, secondVehicle, "file:data/newCOlors/unnamed-3.png", secondVehicle.getBack().getCol(),
                secondVehicle.getBack().getRow(), Direction.LEFT);
        createButton(pane, thirdVehicle, "file:data/newCOlors/unnamed-2.png", thirdVehicle.getFront().getCol(),
                thirdVehicle.getFront().getRow(), Direction.DOWN);
        createButton(pane, thirdVehicle, "file:data/newCOlors/unnamed-2.png", thirdVehicle.getBack().getCol(),
                thirdVehicle.getBack().getRow(), Direction.UP);

    }


    private void createButton(GridPane pane, Vehicle vehicle, String imageUrl, int col, int row, Direction direction) {
        Image image = new Image(imageUrl);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(120);
        imageView.setFitHeight(60);

        Button button = new Button("", imageView);
        button.setPadding(new Insets(0, 0, 0, 0));
        button.setPrefWidth(120);
        button.setPrefHeight(60);

        if (direction != null) {
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) { 
                    Move currentMove = new Move(vehicle.getSymbol(), direction);

                    Position newPosition = hour.findDir(direction, vehicle);

                    boolean canMove = !hour.isObstructed(vehicle, newPosition);

                    if (canMove ) {
                        try {
                            
                            hour.moveVehicle(currentMove);
                            counter++; // Increment the counter
                            counterLabel.setText("Move count: " + counter); // Update the counterLabel
                            updateBoard(pane);
                            checkGameStatus();
                            
                        } catch (RushHourException e) {
                            bottomLabel.setText("Invalid move!");
                        }
                    } else {
                        bottomLabel.setText("Invalid move! Another vehicle is in the way.");
                    }
                }
            });
        }

        pane.add(button, col, row);
    }


 
    /**
     * 
     * @param pane the pane being changed 
     */
    private void updateBoard(GridPane pane) {
        pane.getChildren().clear();
        createBackground(pane);
        createVehicleButtons(pane);
        createSolveButton();
        pane.add(bottomLabel, 0, 6, 6, 1);
        pane.add(counterLabel, 0, 7, 6, 1); // Add counterLabel to the GridPane
        Button resetButton = createResetButton();
        pane.add(resetButton, 0, 8, 6, 1); // Move reset button one row down
        
        // Recreate the recommendation button with its action handler
        Button recommendationButton = createRecommendationButton();
        pane.add(recommendationButton, 0, 9, 6, 1);

        Button solve = createSolveButton();
        pane.add(solve, 0, 10, 6, 1);
    }
    

    private void createBackground(GridPane pane) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i == 2 && j == 5) {
                    addImageToGrid(pane, "file:data/cars/EXIT.png", j, i);
                } else {
                    addImageToGrid(pane, "file:data/newCOlors/unnamed-4.png", j, i);
                }
            }
        }
    }

    private Button createResetButton() {
        Button resetButton = new Button("Reset");
        //looked shit up and isntead of a handler just did the event -> which really means if pressed then do this, fun thing wiht lambas
        resetButton.setOnAction(event -> {
            try {
                hour = new RushHour("data/03_00.csv");
                counter = 0;
                bottomLabel.setText("Game reset."); // Remove move count from this message
                counterLabel.setText("Move count: " + counter); // Update the counterLabel after resetting the game
                updateBoard((GridPane) resetButton.getParent()); // Pass the parent pane to updateBoard()
            } catch (IOException e) {
                bottomLabel.setText("Failed to reset the game.");
            }
        });
        return resetButton;
    }

    private void checkGameStatus() {
        if (hour.isGameOver()) {
            bottomLabel.setText("Congratulations! You've solved the puzzle.");
        }
    }
}