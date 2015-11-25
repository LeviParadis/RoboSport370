package Controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import Views.mainMenuView;
import Views.setupView;

public class UIManager extends Game {
    
    public static UIManager manager;
    
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
        
        this.setScreen(initialView);
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
