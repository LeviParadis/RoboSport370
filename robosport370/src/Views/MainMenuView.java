package Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Constants.UIConstants;
import Controllers.SetupController;

/**
 * A GUI view for the main menu
 * 
 * @author Corey
 * @author Levi Paradis
 *
 */
public class MainMenuView extends ScreenAdapter {
    // The controller which called the view
    final SetupController controller;

    // Constant variables for determining menu option coordinates and dimensions
    private static final Integer menOpSrcX = 45;
    private static final Integer menOpSrcY = 30;
    private static final Integer menOpWidth = 170;
    private static final Integer menOpHeight = 31;

    // To store the screen dimensions
    private Integer SCREEN_WIDTH;
    private Integer SCREEN_HEIGHT;

    // For ease of use when passing to the controller
    private static final int NEW_TOURNAMENT = 1;
    private static final int NEW_SIMULATION = 2;
    private static final int NEW_ROBOT = 3;
    private static final int EXIT = 4;

    // For rendering sprites
    private SpriteBatch batch;

    // Art for the main menu
    private Texture menuArtTexture;
    private Sprite menuArtSprite;

    // Art for the menu credits
    private Texture menuCreditTexture;
    private Sprite menuCreditSprite;

    // Art for the menu options
    private Texture menuOptionsTexture;
    private Sprite menuOptionsTournament;
    private Sprite menuOptionsSimulation;
    private Sprite menuOptionsNewRobot;
    private Sprite menuOptionsExit;

    // For tracking the active option
    private SpriteMenuHandler menu;

    /**
     * Constructor for MainMenuView
     * 
     * @param cont
     *            the controller creating this view
     */
    public MainMenuView(final SetupController cont) {
        controller = cont;

        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();

        batch = new SpriteBatch();

        menuArtTexture = new Texture(UIConstants.SPRITE_MENU);
        menuArtSprite = new Sprite(menuArtTexture, 280, 126);

        menuCreditTexture = new Texture(UIConstants.SPRITE_CREDITS);
        menuCreditSprite = new Sprite(menuCreditTexture, 295, 75);

        menuOptionsTexture = new Texture(UIConstants.SPRITE_OPTIONS);
        menuOptionsTournament = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY * 0, menOpWidth, menOpHeight);
        menuOptionsSimulation = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY * 2, menOpWidth, menOpHeight);
        menuOptionsNewRobot = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY * 4, menOpWidth, menOpHeight);
        menuOptionsExit = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY * 6, menOpWidth, menOpHeight);

        menuOptionsTournament.setPosition(SCREEN_WIDTH * 0.5f - menuOptionsTournament.getWidth() / 2,
                SCREEN_HEIGHT * 0.45f - 30 * 1f);
        menuOptionsSimulation.setPosition(SCREEN_WIDTH * 0.5f - menuOptionsTournament.getWidth() / 2,
                SCREEN_HEIGHT * 0.45f - 30 * 2f);
        menuOptionsNewRobot.setPosition(SCREEN_WIDTH * .5f - menuOptionsTournament.getWidth() / 2,
                SCREEN_HEIGHT * 0.45f - 30 * 3f);
        menuOptionsExit.setPosition(SCREEN_WIDTH * 0.5f - menuOptionsTournament.getWidth() / 2,
                SCREEN_HEIGHT * 0.45f - 30 * 4f);

        // Add in the order for the associated constant variables
        menu = new SpriteMenuHandler(menuOptionsTournament);
        menu.addSprite(menuOptionsSimulation);
        menu.addSprite(menuOptionsNewRobot);
        menu.addSprite(menuOptionsExit);
    }

    /**
     * Called every frame
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        handleKeyPresses();

        // Draw the title screen at the middle of the screen, centered
        menuArtSprite.setPosition(SCREEN_WIDTH * 0.5f - menuArtSprite.getWidth() / 2, SCREEN_HEIGHT * 0.5f);
        menuArtSprite.draw(batch);

        // Draw the credits at the bottom right of the screen
        menuCreditSprite.setPosition(SCREEN_WIDTH - menuCreditSprite.getWidth(), 0f);
        menuCreditSprite.draw(batch);

        // Draw the options
        menuOptionsTournament.draw(batch);
        menuOptionsSimulation.draw(batch);
        menuOptionsNewRobot.draw(batch);
        menuOptionsExit.draw(batch);

        batch.end();
    }

    /**
     * Checks the relevant key presses and acts appropriately
     */
    public void handleKeyPresses() {
        // Checks for which keys have been pressed
        if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
            menu.down();
        }

        if (Gdx.input.isKeyJustPressed(Keys.UP)) {
            menu.up();
        }
        if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            switch (menu.getIndex()) {
            case NEW_TOURNAMENT:
                controller.notifyTournament();
                break;
            case NEW_SIMULATION:
                controller.notifySim();
                break;
            case NEW_ROBOT:
                controller.notifyNewRobot();
                break;
            case EXIT:
                controller.notifyExit();
                break;
            }
        }
    }

    /**
     * Called to do garbage collection
     */
    @Override
    public void dispose() {
        menuArtTexture.dispose();
        menuCreditTexture.dispose();
        menuOptionsTexture.dispose();

        batch.dispose();
    }
}
