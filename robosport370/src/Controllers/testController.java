package Controllers;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import Views.mapView;

public class testController extends Game {

	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "RobotSport370";
		config.height = 800;
		config.width = 1280;
		new LwjglApplication(new testController(), config);
	}

	@Override
	public void create() {
		this.setScreen(new mapView(this));
	}

}
