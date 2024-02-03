package rushhour.model;

import backtracker.Configuration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.*;

public class RushHourSolver implements Configuration<RushHourSolver> {
    private RushHour rushHour;
    private List<Move> moveHistory;

    public RushHourSolver(RushHour rushHour) {
        this.rushHour = new RushHour(rushHour);
        this.moveHistory = new ArrayList<>();
    }





    @Override
    public Collection<RushHourSolver> getSuccessors() {
        try {
            Collection<RushHourSolver> successors = new ArrayList<>();
    
            for (Move move : rushHour.getPossibleMoves()) {
                RushHour newRushHour = new RushHour(rushHour);
                try {
                    newRushHour.moveVehicle(move);
    
                    boolean isBlocking = false;
                    for (Vehicle blockingVehicle : newRushHour.getBlockingVehiclesForTarget()) {
                        int num = rushHour.getLs().get(move.getSymbol()).getFront().getRow();
                        int num2 = rushHour.getLs().get(move.getSymbol()).getFront().getCol();
                        int num3 = rushHour.getLs().get(move.getSymbol()).getBack().getRow();
                        int num4 = rushHour.getLs().get(move.getSymbol()).getBack().getCol();
    
                        if (num < 6 && num >= 0 && num2 >= 0 && num2 < 6 && num3 < 6 && num3 >= 0 && num4 >= 0
                                && num4 < 6) {
    
                            if (blockingVehicle.getSymbol() == move.getSymbol()) {
                                isBlocking = true;
                                break;
                            }
                        } else {
                            isBlocking = true;
                            break;
                        }
                    }
    
                    if (!isBlocking) {
                        RushHourSolver newConfig = new RushHourSolver(newRushHour);
                        newConfig.moveHistory.addAll(this.moveHistory);
                        newConfig.moveHistory.add(move);
                        successors.add(newConfig);
                    }
                } catch (RushHourException e) {
                    // If the move causes an error, skip it and continue with the next move.
                    continue;
                }
            }
    
            return successors;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
   

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isGoal() {
        return rushHour.isGameOver();
    }

    public List<Move> getMoveHistory() {
        return moveHistory;
    }

    @Override
    public String toString() {
        String s = "Move:\n";
        for (Move m : moveHistory) {
            s += m + "\n";
        }
        s += rushHour.toString();

        return s;
    }

    public static RushHourSolver solve(RushHour rushHour) {
        try {
            RushHourSolver initialConfig = new RushHourSolver(rushHour);
            backtracker.Backtracker<RushHourSolver> backtracker = new backtracker.Backtracker<>(false);
            return backtracker.solve(initialConfig);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
