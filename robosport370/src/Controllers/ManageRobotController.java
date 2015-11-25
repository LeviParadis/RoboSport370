package Controllers;

import java.util.Queue;

import Interfaces.ListRobotsDelegate;
import Models.Robot;
import Views.AddRobotView;
import Views.EditTeamView;

public class ManageRobotController implements ListRobotsDelegate{

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
        EditTeamController nextController = new EditTeamController(2, 6, this);
        EditTeamView nextView = new EditTeamView(nextController);
        
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
        System.out.println(listSelected);
    }

}
