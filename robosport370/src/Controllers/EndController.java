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
        // Getting the music intialized
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
     * gets called when Setup view selects return
     */
    public void notifyMainMenu(){
        this.setScreen(new mainMenuView(new setupController()));
    }   
    
}
