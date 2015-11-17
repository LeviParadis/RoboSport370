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
  
    public EndController(){
        /**
         * Called on initilization
         */
        // Getting the music initalized
        introMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/sound/Bit Quest.mp3"));
        introMusic.setLooping(true);
        introMusic.setVolume(0.6f);
        introMusic.play();
    }
    
    
    
}
