package Controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import Views.mainMenuView;
import Views.setupView;
import Views.endView;


public class EndController extends Game {
    private Music introMusic;
    
    //loggerController controller;
    endView view;
  
    public void create() {
        // Getting the music initialized
        introMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/sound/Bit Quest.mp3"));
        introMusic.setLooping(true);
        introMusic.setVolume(0.6f);
        introMusic.play();
        
        //This is for testing my first screen
        this.setScreen(new endView(this));
    }
    
    /**
     * gets called when the Main Menu view selects exit
     */
    public void notifyExit(){
        System.exit(0);
    }
    /**
     * gets called when Setup view selects Main Menu
     */
    public void notifyMainMenu(){
        this.setScreen(new mainMenuView(new setupController()));
    }
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "RobotSport370";
        config.height = 800;
        config.width = 1280;
        new LwjglApplication(new EndController(), config);
    }
    
}
