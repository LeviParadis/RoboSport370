package Controllers;

import javax.swing.JOptionPane;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import Interpreters.JsonInterpreter;
import Models.Robot;

public class EditRobotController extends AddRobotController {

    private Robot editing;
    
    public EditRobotController(Robot input) {
        this.editing = input;
    }
    
    @Override
    /**
     * Called when the user presses the confirm button
     * @param name the text in the name field at the time of the button press
     * @param team the text in the team field at the time of the button press
     * @param forth the text in the forth field at the time of the button press
     * @param movesLeft the number of moves for the checkbox values at the time of the button press
     * @param firePower the amount of power for the checkbox values at the time of the button press
     * @param health the amount of health for the checkbox values at the time of the button press
     */
    public void notifyDone(String name, String team, String forth, int movesLeft, int firePower, int health){
        try{
            JsonInterpreter.registerRobot(name, team, firePower, health, movesLeft, forth);
            JsonInterpreter.reviseRobot(editing.getSerialNumber(), firePower, health, movesLeft, forth);
            UIManager manager = UIManager.sharedInstance();
            manager.popScreen();
        } catch (RuntimeException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    /**
     * Called when the user pressed the retire button
     * Tells the Json Interpreter to retire the selected robot
     */
    public void notifyRetire(){
        try{
            JsonInterpreter.retireRobot(editing.getSerialNumber());
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
