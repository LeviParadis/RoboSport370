package Controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import Views.mainMenuView;
import Views.setupView;

/**
 * 
 * setupController handles the main menu and setup screens while interfacing with the models
 *
 */
public class setupController extends Game {
	private Music introMusic;
	
	/**
	 * Called on initilization
	 */
	public void create() {
		// Getting the music intialized
		introMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/sound/Bit Quest.mp3"));
        introMusic.setLooping(true);
        introMusic.setVolume(0.6f);
        introMusic.play();
		
		//This is for testing my first screen
//        this.setScreen(new mainMenuView(this));
		
		//This is for testing my second screen
		this.setScreen(new setupView(this));
    }
	
	/**
	 * Called every frame
	 */
	public void render() {
        super.render();
    }
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "RobotSport370";
		config.height = 800;
		config.width = 1280;
		new LwjglApplication(new setupController(), config);
	}
}
