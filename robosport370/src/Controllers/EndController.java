package Controllers;

import com.badlogic.gdx.Game;

import Views.mainMenuView;
import Views.endView;



public class EndController {
    //loggerController controller;
    endView view;
  
    
    /**
     * gets called when the exit button is pressed
     */
    public void notifyExitButtonPressed(){
        System.exit(0);
    }
    
    /**
     * gets called when the main menu button is pressed
     */
    public void notifyMainMenuPressed(){
        UIManager manager = UIManager.sharedInstance();
        manager.pushScreen(new mainMenuView(new setupController()));
    }
    

}
