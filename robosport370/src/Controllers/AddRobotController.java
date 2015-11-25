package Controllers;

import javax.swing.JOptionPane;

import Interpreters.JsonInterpreter;

public class AddRobotController {

    public AddRobotController() {
        
    }

    /**
     * Called when the user presses the confirm button
     * @param name the text in the name field at the time of the button press
     * @param team the text in the team field at the time of the button press
     * @param forth the text in the forth field at the time of the button press
     * @param movesLeft the number of moves for the checkbox values at the time of the button press
     * @param firePower the amount of power for the checkbox values at the time of the button press
     * @param health the amount of health for the checkbox values at the time of the button press
     */
    public void notifyAddRobot(String name, String team, String forth, int movesLeft, int firePower, int health){
        try{
            JsonInterpreter.registerRobot(name, team, firePower, health, movesLeft, forth);
            UIManager manager = UIManager.sharedInstance();
            manager.popScreen();
        } catch (RuntimeException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    
    /**
     * Called when the user presses the cancel button
     */
    public void notifyCancel(){
        UIManager manager = UIManager.sharedInstance();
        manager.popScreen();
    }
}
