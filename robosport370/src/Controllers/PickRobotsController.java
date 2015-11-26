package Controllers;

import javax.swing.JOptionPane;

import Interfaces.PickRobotsDelegate;

import java.util.Queue;
import Interpreters.JsonInterpreter;
import Models.Robot;

public class PickRobotsController {
    
    private int minSelection;
    private int maxSelection;
    
    private PickRobotsDelegate delegate;

    public PickRobotsController(int minSelection, int maxSelection, PickRobotsDelegate delegate) {
        this.minSelection = minSelection;
        this.maxSelection = maxSelection;
        this.delegate = delegate;
    }
    
    public int getMinimumSelectable(){
        return this.minSelection;
    }
    
    public int getMaxSelectable(){
        return this.maxSelection;
    }

    
    /**
     * Called when the user presses the cancel button
     */
    public void notifyCancel(){
        this.delegate.robotsListCancelled();
    }
    
    public void notifyConfirm(Queue<Robot> robotList){
        if (robotList.size() >= this.getMinimumSelectable()){
            this.delegate.robotListFinished(robotList);
        }
    }
    
    public Queue<Robot> notifySearch(String name, String team, String  minWins, String maxWins, String minLosses, String maxLosses, String minGamesPlayed, String maxGamesPlayed, boolean currentOnly) throws NumberFormatException {
        int minWinsInt = Integer.parseInt(minWins);
        int maxWinsInt = Integer.parseInt(maxWins);
        int minLossesInt = Integer.parseInt(minLosses);
        int maxLossesInt = Integer.parseInt(maxLosses);
        int minGamesInt = Integer.parseInt(minGamesPlayed);
        int maxGamesInt = Integer.parseInt(maxGamesPlayed);
            
        Queue<Robot> results = JsonInterpreter.listRobots(currentOnly, name, team, minWinsInt, maxWinsInt, minLossesInt, maxLossesInt, minGamesInt, maxGamesInt);
        return results;
    }
}
