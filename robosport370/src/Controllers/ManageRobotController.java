package Controllers;

import java.util.Queue;

import Interfaces.PickRobotsDelegate;
import Models.Robot;
import Views.AddRobotView;
import Views.PickRobotsView;

public class ManageRobotController implements PickRobotsDelegate{

    public ManageRobotController() {
    }

    /**
     * The back button was pressed. Go back to the main menu
     */
    public void notifyBackButtonPressed() {
        UIManager manager = UIManager.sharedInstance();
        manager.popToRootScreen();
    }

    /**
     * The add button was pressed. Push the screen to create a robot
     */
    public void notifyAddButtonPressed() {
        AddRobotController nextController = new AddRobotController();
        AddRobotView nextView = new AddRobotView(nextController);
        
        UIManager manager = UIManager.sharedInstance();
        manager.pushScreen(nextView);
    }

    /**
     * The edit button was pressed. Push the screen to edit existing robots
     */
    public void notifyEditButtonPressed() {
        PickRobotsController nextController = new PickRobotsController(1, 1, this);
        PickRobotsView nextView = new PickRobotsView(nextController);
        
        UIManager manager = UIManager.sharedInstance();
        manager.pushScreen(nextView);
    }

    @Override
    public void robotsListCancelled() {
        UIManager manager = UIManager.sharedInstance();
        manager.popScreen();
    }

    @Override
    public void robotListFinished(Queue<Robot> listSelected) {
        UIManager manager = UIManager.sharedInstance();
        manager.popScreen();
        if(listSelected.size() == 1){
            //TODO: Add edit robot screen
           System.out.println("Done");
        }
    }

}
