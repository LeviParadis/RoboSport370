package Controllers;

import Views.AddRobotView;

public class ManageRobotController {

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
        // TODO When edit screen is created
        //push robot list view, then after we are given a robot from the list, push a new view to edit it
        
    }

}
