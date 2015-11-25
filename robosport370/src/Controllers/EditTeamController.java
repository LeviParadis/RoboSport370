package Controllers;

import javax.swing.JOptionPane;

import Interfaces.ListRobotsDelegate;

import java.util.Queue;
import Interpreters.JsonInterpreter;
import Models.Robot;

public class EditTeamController {
    
    private int minSelection;
    private int maxSelection;
    
    private ListRobotsDelegate delegate;

    public EditTeamController(int minSelection, int maxSelection, ListRobotsDelegate delegate) {
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
    
    public Queue<Robot> notifySearch(){
        //TODO: Add parameters
        Queue<Robot> results = JsonInterpreter.listRobots(true, null, null, null, null, null, null, null, null);
       return results;
    }
}
