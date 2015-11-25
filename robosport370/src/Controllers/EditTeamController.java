package Controllers;

import javax.swing.JOptionPane;
import java.util.Queue;
import Interpreters.JsonInterpreter;
import Models.Robot;

public class EditTeamController {

    public EditTeamController() {
        
    }

    
    /**
     * Called when the user presses the cancel button
     */
    public void notifyCancel(){
        UIManager manager = UIManager.sharedInstance();
        manager.popScreen();
    }
    
    public Queue<Robot> notifySearch(){
        //TODO: Add parameters
        Queue<Robot> results = JsonInterpreter.listRobots(true, null, null, null, null, null, null, null, null);
       return results;
    }
}
