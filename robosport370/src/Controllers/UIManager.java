package Controllers;

import java.util.Stack;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import Views.AddRobotView;
import Views.MainMenuView;
import Views.SetupView;
import Views.EndView;

public class UIManager extends Game {

    public static UIManager manager;

    private Stack<Screen> screenStack;

    public static UIManager sharedInstance() {
        if (manager == null) {
            manager = new UIManager();
        }
        return manager;
    }

    public UIManager() {
    }

    @Override
    /**
     * Set up initial values. It should initially load an instance of the main
     * menu and it's view
     */
    public void create() {
        SetupController initialController = new SetupController();
        MainMenuView initialView = new MainMenuView(initialController);

        this.screenStack = new Stack<Screen>();

        this.pushScreen(initialView);
    }

    /**
     * adds a new screen to the stack of view stack
     * 
     * @param newScreen
     *            the new screen to add on to the stack
     */
    public void pushScreen(Screen newScreen) {
        this.screenStack.push(newScreen);
        this.setScreen(newScreen);
    }

    /**
     * goes back to the previous view in the stack
     */
    public void popScreen() {
        if (screenStack.size() <= 1) {
            return;
        }
        screenStack.pop();

        Screen newScreen = screenStack.peek();
        this.setScreen(newScreen);
    }

    /**
     * Goes back to the first screen in the view stack (welcome screen)
     */
    public void popToRootScreen() {
        while (this.screenStack.size() > 1) {
            screenStack.pop();
        }
        this.setScreen(screenStack.peek());
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
