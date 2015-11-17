package Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import Controllers.setupController;

/**
 * A view for end screen
 * @author Matt, Based off of Corey's Main Menu View
 *
 */
public class endView extends ScreenAdapter {
    //controller that called end view
    final setupController controller;
    
    // Constant variables for determining menu option coordinates and dimensions
    private static final Integer menOpSrcX = 45;
    private static final Integer menOpSrcY = 30;
    private static final Integer menOpWidth = 170;
    private static final Integer menOpHeight = 30;
    
    // To store the screen dimensions
    private Integer SCREEN_WIDTH;
    private Integer SCREEN_HEIGHT;
    
    // For ease of use when passing to the controller
    private static final int REMATCH = 1;
    private static final int MAIN_MENU = 2;
    private static final int DISPLAY_RESULTS = 3;
    private static final int EXIT = 4;
    
    // For rendering sprites
    private SpriteBatch batch;
    
    // Art for the main menu
    private Texture menuArtTexture;
    private Sprite menuArtSprite;
    
    // Art for the menu options
    private Texture menuOptionsTexture;
    private Sprite menuOptionsRematch;
    private Sprite menuOptionsMainMenu;
    private Sprite menuOptionsDisplayResults;
    private Sprite menuOptionsExit;
    
    // For tracking the active option
    private spriteMenuHandler menu;
    
    /**
     * Constructor for EndView
     * @param cont the controller creating this view
     */
    public endView(final setupController cont) {
        controller = cont;

        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();
        
        batch = new SpriteBatch();
        
        menuArtTexture = new Texture("assets/mainMenu/menu_title.png");
        menuArtSprite = new Sprite(menuArtTexture, 280, 126);
        
        //menuCreditTexture = new Texture("assets/mainMenu/menu_credits.png");
        //menuCreditSprite = new Sprite(menuCreditTexture, 295, 75);
        
        menuOptionsTexture = new Texture("assets/mainMenu/menu_options.png");
        menuOptionsRematch = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY*0, menOpWidth, menOpHeight);
        menuOptionsMainMenu = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY*2, menOpWidth, menOpHeight);
        menuOptionsDisplayResults = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY*4, menOpWidth, menOpHeight);
        menuOptionsExit = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY*6, menOpWidth, menOpHeight);
        
        menuOptionsRematch.setPosition(SCREEN_WIDTH*0.5f-menuOptionsRematch.getWidth()/2, SCREEN_HEIGHT*0.45f-30*1f);
        menuOptionsMainMenu.setPosition(SCREEN_WIDTH*0.5f-menuOptionsRematch.getWidth()/2, SCREEN_HEIGHT*0.45f-30*2f);
        menuOptionsDisplayResults.setPosition(SCREEN_WIDTH*0.5f-menuOptionsRematch.getWidth()/2, SCREEN_HEIGHT*0.45f-30*3f);
        menuOptionsExit.setPosition(SCREEN_WIDTH*0.5f-menuOptionsRematch.getWidth()/2, SCREEN_HEIGHT*0.45f-30*4f);
        
        // Add in the order for the associated constant variables
        menu = new spriteMenuHandler(menuOptionsRematch);
        menu.addSprite(menuOptionsMainMenu);
        menu.addSprite(menuOptionsDisplayResults);
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
        menuArtSprite.setPosition(SCREEN_WIDTH*0.5f-menuArtSprite.getWidth()/2, SCREEN_HEIGHT*0.5f);
        menuArtSprite.draw(batch);
        
        // Draw the credits at the bottom right of the screen
        //menuCreditSprite.setPosition(SCREEN_WIDTH-menuCreditSprite.getWidth(), 0f);
        //menuCreditSprite.draw(batch);
        
        // Draw the options
        menuOptionsRematch.draw(batch);
        menuOptionsMainMenu.draw(batch);
        menuOptionsDisplayResults.draw(batch);
        menuOptionsExit.draw(batch);
        
        batch.end();
    }
    
    /**
     * Checks the relevant key presses and acts appropriately
     */
    public void handleKeyPresses() {
        // Checks for which keys have been pressed
        if(Gdx.input.isKeyJustPressed(Keys.DOWN)) {
            menu.down();
        }
        
        if(Gdx.input.isKeyJustPressed(Keys.UP)) {
            menu.up();
        }
        if(Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            switch(menu.getIndex()) {
            case REMATCH:
                controller.notifyTournament();
                break;
            case MAIN_MENU:
                controller.notifySim();
                break;
            case DISPLAY_RESULTS:
                //controller.notifyRes();
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
        //menuCreditTexture.dispose();
        menuOptionsTexture.dispose();
        
        batch.dispose();
    }
    
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "RobotSport370";
        config.height = 800;
        config.width = 1280;
        new LwjglApplication(new setupController(), config);
        
        
    }
}
