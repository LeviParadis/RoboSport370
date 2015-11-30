package Controllers;

import java.util.Queue;

import Interfaces.PickRobotsDelegate;
import Models.Robot;
import Views.AddRobotView;
import Views.EditRobotView;
import Views.PickRobotsView;

public class ManageRobotController implements PickRobotsDelegate {

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
    /**
     * This method lets us know the user canceled out of the robot picker, so we
     * pop back to the previous view
     */
    public void robotsListCancelled() {
        UIManager manager = UIManager.sharedInstance();
        manager.popScreen();
    }

    @Override
    /**
     * This method lets us know the user has selected a robot to edit. We pop
     * back to the previous view, and then push a new edit view to edit it
     */
    public void robotListFinished(Queue<Robot> listSelected) {
        UIManager manager = UIManager.sharedInstance();
        manager.popScreen();
        Robot selected = listSelected.peek();
        EditRobotController cont = new EditRobotController(selected);
        EditRobotView view = new EditRobotView(cont, selected);

        manager.pushScreen(view);

    }

}
