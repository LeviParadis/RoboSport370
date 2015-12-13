package Controllers;

import Views.MainMenuView;
import Views.EndView;

public class EndController {
    // loggerController controller;
    EndView view;

    /**
     * gets called when the exit button is pressed
     */
    public void notifyExitButtonPressed() {
        System.exit(0);
    }

    /**
     * gets called when the main menu button is pressed
     */
    public void notifyMainMenuPressed() {
        UIManager manager = UIManager.sharedInstance();
        manager.pushScreen(new MainMenuView(new SetupController()));
    }

}
