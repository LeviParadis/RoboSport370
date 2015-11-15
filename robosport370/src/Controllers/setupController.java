package Controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import Models.MainMenu;
import Views.MainMenuView;

/**
 * 
 * setupController handles the main menu and setup screens while interfacing with the models
 *
 */
public class setupController extends Game {
	private MainMenu mainMenu;
	
	/**
	 * Called on initilization
	 */
	public void create() {
		mainMenu = new MainMenu();
        this.setScreen(new MainMenuView(this));
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
		mainMenu.menuUp();
	}
	
	/**
	 * Moves the menu option down
	 */
	public void menuDown() {
		mainMenu.menuDown();
	}
	
	/**
	 * 
	 * @return the current menu option
	 */
	public String currentMenuOption() {
		return mainMenu.currentMenuOption();
	}
	
	/**
	 * The user has submitted their option and this class moves onto the new view
	 */
	public void submitOption() {
		this.screen.dispose();
		/* TODO This is a placeholder for the controller
		 Realistically, it would need to call the next screen, not the same screen again
		 It may need to adjust for currentMenuOption */
		this.setScreen(new MainMenuView(this));
	}
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "RobotSport370";
		config.height = 800;
		config.width = 1280;
		new LwjglApplication(new setupController(), config);
	}
}
