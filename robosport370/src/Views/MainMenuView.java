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

public class MainMenuView extends ScreenAdapter {
	private SpriteBatch batch;
    
    private Texture menuArtTexture;
    private Sprite menuArtSprite;
    
    private Texture menuCreditTexture;
    private Sprite menuCreditSprite;
    
    private Texture menuOptionsTexture;
    private Sprite menuOptionsTournament;
    private Sprite menuOptionsSimulation;
    private Sprite menuOptionsExit;
    
    private Integer SCREEN_WIDTH;
    private Integer SCREEN_HEIGHT;
    
    private static Integer menOpSrcX = 45;
    private static Integer menOpSrcY = 30;
    private static Integer menOpWidth = 170;
    private static Integer menOpHeight = 30;
    
    final setupController controller;
    
    /**
     * Constructor for MainMenuView
     * @param cont the controller creating this view
     */
    public MainMenuView(final setupController cont) {
    	controller = cont;
    	
        batch = new SpriteBatch();
        
        menuArtTexture = new Texture("assets/mainMenu/menu_title.png");
        menuArtSprite = new Sprite(menuArtTexture, 280, 126);
        
        menuCreditTexture = new Texture("assets/mainMenu/menu_credits.png");
        menuCreditSprite = new Sprite(menuCreditTexture, 295, 75);
        
        menuOptionsTexture = new Texture("assets/mainMenu/menu_options.png");
        menuOptionsTournament = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY*0, menOpWidth, menOpHeight);
        menuOptionsSimulation = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY*2, menOpWidth, menOpHeight);
        menuOptionsExit = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY*4, menOpWidth, menOpHeight);
        
        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();
    }

    /**
     * Called to do garbage collection
     */
    @Override
    public void dispose() {
        batch.dispose();
        menuArtTexture.dispose();
        menuCreditTexture.dispose();
        menuOptionsTexture.dispose();
    }

    /**
     * Called every frame
     */
    @Override
    public void render(float delta) {   
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        
        if(Gdx.input.isKeyJustPressed(Keys.ENTER)) {
        	controller.submitMenuOption();
        }
        
        if(Gdx.input.isKeyJustPressed(Keys.DOWN)) {
        	controller.menuDown();
        }
        
        if(Gdx.input.isKeyJustPressed(Keys.UP)) {
        	controller.menuUp();
        }
        
        // Draw the title screen at the middle of the screen, centered
        menuArtSprite.setPosition(SCREEN_WIDTH*0.5f-menuArtSprite.getWidth()/2, SCREEN_HEIGHT*0.5f);
        menuArtSprite.draw(batch);
        
        menuCreditSprite.setPosition(SCREEN_WIDTH-menuCreditSprite.getWidth(), 0f);
        menuCreditSprite.draw(batch);
        
        renderMenu();
        
        batch.end();
    }
    
    /**
     * renders the main menu with the appropriate sprites based on the current option
     */
    private void renderMenu() {
        menuOptionsTournament = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY*0, menOpWidth, menOpHeight);
        menuOptionsSimulation = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY*2, menOpWidth, menOpHeight);
        menuOptionsExit = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY*4, menOpWidth, menOpHeight);
    	
    	menuOptionsTournament.setPosition(SCREEN_WIDTH*0.5f-menuOptionsTournament.getWidth()/2, SCREEN_HEIGHT*0.45f-30*1f);
    	menuOptionsSimulation.setPosition(SCREEN_WIDTH*0.5f-menuOptionsTournament.getWidth()/2, SCREEN_HEIGHT*0.45f-30*2f);
    	menuOptionsExit.setPosition(SCREEN_WIDTH*0.5f-menuOptionsTournament.getWidth()/2, SCREEN_HEIGHT*0.45f-30*3f);
    	
    	switch(controller.currentMenuOption()) {
    		case "New Tournament":
    			menuOptionsTournament = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY*1, menOpWidth, menOpHeight);
    			menuOptionsTournament.setPosition(SCREEN_WIDTH*0.5f-menuOptionsTournament.getWidth()*0.5f, SCREEN_HEIGHT*0.45f-30*1f);
    			break;
    		case "New Simulation":
    			menuOptionsSimulation = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY*3, menOpWidth, menOpHeight);
    			menuOptionsSimulation.setPosition(SCREEN_WIDTH*0.5f-menuOptionsTournament.getWidth()*0.5f, SCREEN_HEIGHT*0.45f-30*2f);
    			break;
    		case "Exit":
    			menuOptionsExit = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY*5, menOpWidth, menOpHeight);
    			menuOptionsExit.setPosition(SCREEN_WIDTH*0.5f-menuOptionsTournament.getWidth()*0.5f, SCREEN_HEIGHT*0.45f-30*3f);
    			break;
    	}
    	
    	menuOptionsTournament.draw(batch);
    	menuOptionsSimulation.draw(batch);
    	menuOptionsExit.draw(batch);
    }
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "RobotSport370";
		config.height = 800;
		config.width = 1280;
		new LwjglApplication(new setupController(), config);
	}
}
