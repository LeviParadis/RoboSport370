package Controllers;

import java.util.Stack;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import Views.AddRobotView;
import Views.mainMenuView;
import Views.setupView;

public class UIManager extends Game {
    
    public static UIManager manager;
    
    private Stack<Screen> screenStack;
    
    public static UIManager sharedInstance(){
        if(manager == null){
            manager = new UIManager();
        }
        return manager;
    }
    

    public UIManager() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void create() {
        // TODO Auto-generated method stub
        setupController initialController = new setupController();
        mainMenuView initialView = new mainMenuView(initialController);
         
        this.screenStack = new Stack<Screen>();
        
        this.pushScreen(initialView);
    }
    
    /**
     * adds a new screen to the stack of view stack
     * @param newScreen the new screen to add on to the stack
     */
    public void pushScreen(Screen newScreen){
        this.screenStack.push(newScreen);
        this.setScreen(newScreen);
    }
    
    /**
     * goes back to the previous view in the stack
     */
    public void popScreen(){
        if(screenStack.size()<=1){
            return;
        }
        screenStack.pop();
        Screen prevScreen = screenStack.peek();
        this.setScreen(prevScreen);        
    }

    /**
     * Goes back to the first screen in the view stack (welcome screen)
     */
    public void popToRootScreen(){
        Screen prevScreen = screenStack.peek();
        while(this.screenStack.size() > 1){
            prevScreen = screenStack.pop();
        }
        this.setScreen(prevScreen);
    }
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "RobotSport370";
        config.height = 800;
        config.width = 1280;
        new LwjglApplication(UIManager.sharedInstance(), config);

    }
    
    

}
