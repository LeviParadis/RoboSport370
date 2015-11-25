package Controllers;

import javax.swing.JOptionPane;
import java.util.Queue;
import Interpreters.JsonInterpreter;
import Models.Robot;

public class EditTeamController {
    
    private int minSelection;
    private int maxSelection;

    public EditTeamController(int minSelection, int maxSelection) {
        this.minSelection = minSelection;
        this.maxSelection = maxSelection;
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
        UIManager manager = UIManager.sharedInstance();
        manager.popScreen();
    }
    
    public void notifyConfirm(Queue<Robot> robotList){
        if (robotList.size() >= this.getMinimumSelectable()){
            System.out.println("Done");
        }
    }
    
    public Queue<Robot> notifySearch(){
        //TODO: Add parameters
        Queue<Robot> results = JsonInterpreter.listRobots(true, null, null, null, null, null, null, null, null);
       return results;
    }
}
