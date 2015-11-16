package Controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import Models.MainMenu;
import Views.MainMenuView;
import Views.SetupView;

/**
 * 
 * setupController handles the main menu and setup screens while interfacing with the models
 *
 */
public class setupController extends Game {
	private MainMenu mainMenu;
	
	private Music introMusic;
    private Sound beep;
	
	/**
	 * Called on initilization
	 */
	public void create() {
		mainMenu = new MainMenu();
		
		// Getting the music intialized
		introMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/sound/Bit Quest.mp3"));
        introMusic.setLooping(true);
        introMusic.setVolume(0.6f);
        introMusic.play();
        beep = Gdx.audio.newSound(Gdx.files.internal("assets/sound/Beep.mp3"));
		
		//This is for testing my first screen
//        this.setScreen(new MainMenuView(this));
		
		//This is for testing my second screen
		this.setScreen(new SetupView(this));
    }
	
	/**
	 * Called every frame
	 */
	public void render() {
        super.render();
    }
	
	/**
	 * Moves the menu option up
	 */
	public void menuUp() {
    	beep.setVolume(beep.play(),0.3f);
		mainMenu.menuUp();
	}
	
	/**
	 * Moves the menu option down
	 */
	public void menuDown() {
    	beep.setVolume(beep.play(),0.3f);
		mainMenu.menuDown();
	}
	
	/**
	 * 
	 * @return the current menu option
	 */
	public String currentMenuOption() {
		return mainMenu.currentMenuOption();
	}
	
	public String currentSetupOption() {
		// TODO This is just a place holder
		return "Delete Team";
	}
	
	/**
	 * The user has submitted their option and this class moves onto the new view
	 */
	public void submitMenuOption() {
		this.screen.dispose();
		/* TODO This is a placeholder for the controller
		 Realistically, it would need to call the next screen, not the same screen again
		 It may need to adjust for currentMenuOption */
		if(this.currentMenuOption().equals("Exit")) {
			this.dispose();
			Gdx.app.exit();
		}
		this.setScreen(new MainMenuView(this));
	}

	public void setupKeyDown() {
		// TODO Auto-generated method stub
		beep.setVolume(beep.play(),0.3f);
	}	
	
	public void setupKeyUp() {
		// TODO Auto-generated method stub
		beep.setVolume(beep.play(),0.3f);
	}	
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "RobotSport370";
		config.height = 800;
		config.width = 1280;
		new LwjglApplication(new setupController(), config);
	}

	public void addTeam(String filepath) {
		// TODO Auto-generated method stub
		
	}

	public void deleteTeam() {
		// TODO Auto-generated method stub
		
	}

	public void toggleMapSize() {
		// TODO Auto-generated method stub
		
	}

	public void toggleDebugMode() {
		// TODO Auto-generated method stub
		
	}

	public void continueSetup() {
		// TODO Auto-generated method stub
		
	}

	public void returnSetup() {
		// TODO Auto-generated method stub
		
	}
}
