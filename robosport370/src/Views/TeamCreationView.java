package Views;

import java.util.Iterator;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Orientation;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.Particle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import Controllers.SetupController;
import Interpreters.JsonInterpreter;
import Models.Robot;

public class TeamCreationView extends ScreenAdapter {

    // The controller which called the view
    final SetupController controller;

    // To store the screen dimensions
    private Integer SCREEN_WIDTH;
    private Integer SCREEN_HEIGHT;

    // search bar listener
    MyTextInputListener listener = new MyTextInputListener();

    // queue and iterator of all robots from server
    Queue<Robot> robotList = JsonInterpreter.listRobots(true, null, null, null, null, null, null, null, null);
    Iterator<Robot> robotIterator = JsonInterpreter.listRobots(true, null, null, null, null, null, null, null, null)
            .iterator();

    private SpriteBatch batch;

    private BitmapFont font = new BitmapFont(Gdx.files.internal("assets/MoonFlower.fnt"),
            Gdx.files.internal("assets/MoonFlower.png"), false);

    public TeamCreationView(final SetupController cont) {
        controller = cont;

        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();

        batch = new SpriteBatch();

    }

    public void nameClicked() {
        if (Gdx.input.isTouched(Buttons.LEFT)) {
            int clickedX = Gdx.input.getX();
            int clickedY = Gdx.input.getY();
            System.out.println(clickedX + "\n" + clickedY);
        }
    }

    public void drawRobots() {
        Iterator<Robot> robotIterator = JsonInterpreter.listRobots(true, null, null, null, null, null, null, null, null)
                .iterator();
        Robot current = robotIterator.next();

        font.draw(batch, robotList.element().toString(), SCREEN_WIDTH * 0.5f, SCREEN_HEIGHT * 0.85f);
        for (int i = 1; robotIterator.hasNext(); i++) {
            font.draw(batch, current.toString(), SCREEN_WIDTH * 0.5f, SCREEN_HEIGHT * 0.85f - (30 * i));
            current = robotIterator.next();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        font.draw(batch, "Name", SCREEN_WIDTH * 0.5f, SCREEN_HEIGHT * 0.9f);
        drawRobots();

        font.draw(batch, "Current Roster", SCREEN_WIDTH * 0.8f, SCREEN_HEIGHT * 0.9f);
        font.draw(batch, "Search Name", SCREEN_WIDTH * 0.05f, SCREEN_HEIGHT * 0.9f);
        font.draw(batch, "Search Wins", SCREEN_WIDTH * 0.05f, SCREEN_HEIGHT * 0.85f);
        font.draw(batch, "Search Games Played", SCREEN_WIDTH * 0.05f, SCREEN_HEIGHT * 0.8f);
        nameClicked();

        batch.end();
    }

}
