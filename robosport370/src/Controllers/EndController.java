package Controllers;

import com.badlogic.gdx.Game;

import Views.mainMenuView;
import Views.endView;



public class EndController extends Game {
    //loggerController controller;
    endView view;
  
    public void create() {
        this.setScreen(new endView(this, teams));
    }
    
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
    
    /**
     * gets called when the display button is pressed
     */
    public void notifyDisplayResultsButtonPressed(){
        System.out.println("RESULTS");
    }    
}
