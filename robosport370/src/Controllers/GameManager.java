package Controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import Views.mainMenuView;
import Views.setupView;

public class GameManager extends Game {
    
    public static GameManager manager;
    
    public static GameManager sharedInstance(){
        if(manager == null){
            manager = new GameManager();
        }
        return manager;
    }
    

    public GameManager() {
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
        new LwjglApplication(GameManager.sharedInstance(), config);

    }
    
    

}
