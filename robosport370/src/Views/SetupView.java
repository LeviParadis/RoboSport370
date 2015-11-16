package Views;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Controllers.setupController;

public class SetupView extends ScreenAdapter {
	private SpriteBatch batch;
	
	private HashMap<Integer, Sprite> setupMenuMap;
	private Integer setupMenuIndex;
	
	// setupOption texture variables
	private Texture setupOptions;
	private static Integer menOpSrcX = 0;
	private static Integer menOpSrcY = 31;
	private static Integer menOpWidth = 190;
	private static Integer menOpHeight = 30;
	private Sprite addTeam;
	private Sprite deleteTeam;
	private Sprite mapToggle;
	private Sprite debugToggle;
	private Sprite continueSprite;
	private Sprite returnSprite;
	
	private Integer teamIndex;
	private Sprite team1;
	private Sprite team2;
	private Sprite team3;
	private Sprite team4;
	private Sprite team5;
	private Sprite team6;
	
	private Sprite configuration;
	private Sprite debugMode;
	private Sprite mapSize;
	
	// setupDecor texture variables
	private Texture setupdecor;
	private Sprite setuptitle;
	private TextureRegion robotFrame;
	private TextureRegion[] robots;
	private Animation robotAnimation;
	private Float stateTime;
	private Sprite bluePanel;
	private Sprite yellowPanel;
	private Sprite greenPanel;
	
    private Integer SCREEN_WIDTH;
    private Integer SCREEN_HEIGHT;
	
    private Sprite currentSprite;
    
	final setupController controller;
	
	/**
     * Constructor for SetupView
     * @param cont the controller creating this view
     */
	public SetupView(final setupController cont) {
		controller = cont;
		batch = new SpriteBatch();
		
        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();
		
		setupOptions = new Texture("assets/setupMenu/setupoptions.png");
		
		addTeam = new Sprite(setupOptions, menOpSrcX, menOpSrcY*1, menOpWidth, menOpHeight);
		currentSprite = addTeam;
		deleteTeam = new Sprite(setupOptions, menOpSrcX, menOpSrcY*2, menOpWidth, menOpHeight);
		mapToggle = new Sprite(setupOptions, menOpSrcX, menOpSrcY*4, menOpWidth, menOpHeight);
		debugToggle = new Sprite(setupOptions, menOpSrcX, menOpSrcY*6, menOpWidth, menOpHeight);
		continueSprite = new Sprite(setupOptions, menOpSrcX, menOpSrcY*8, menOpWidth, menOpHeight);
		returnSprite = new Sprite(setupOptions, menOpSrcX, menOpSrcY*10, menOpWidth, menOpHeight);
		
		addTeam.setPosition(SCREEN_WIDTH*0.2f, SCREEN_HEIGHT*0.75f);
		deleteTeam.setPosition(SCREEN_WIDTH*0.2f, SCREEN_HEIGHT*0.70f);
		mapToggle.setPosition(SCREEN_WIDTH*0.2f, SCREEN_HEIGHT*0.65f);
		debugToggle.setPosition(SCREEN_WIDTH*0.2f, SCREEN_HEIGHT*0.6f);
		continueSprite.setPosition(SCREEN_WIDTH*0.2f, SCREEN_HEIGHT*0.55f);
		returnSprite.setPosition(SCREEN_WIDTH*0.2f, SCREEN_HEIGHT*0.5f);
		
		setupMenuMap = new HashMap<Integer, Sprite>();
		setupMenuIndex = 1;
		
		setupMenuMap.put(1, addTeam);
		setupMenuMap.put(2, deleteTeam);
		setupMenuMap.put(3, mapToggle);
		setupMenuMap.put(4, debugToggle);
		setupMenuMap.put(5, continueSprite);
		setupMenuMap.put(6, returnSprite);
		
		teamIndex = 0;
		team1 = new Sprite(setupOptions, 215, 2, 50, 20);
		team2 = new Sprite(setupOptions, 215, 52, 50, 20);
		team3 = new Sprite(setupOptions, 215, 102, 50, 20);
		team4 = new Sprite(setupOptions, 215, 152, 50, 20);
		team5 = new Sprite(setupOptions, 215, 202, 50, 20);
		team6 = new Sprite(setupOptions, 215, 252, 50, 20);
		
		team1.setPosition(SCREEN_WIDTH*0.67f, SCREEN_HEIGHT*0.74f);
		team2.setPosition(SCREEN_WIDTH*0.67f, SCREEN_HEIGHT*0.70f);
		team3.setPosition(SCREEN_WIDTH*0.67f, SCREEN_HEIGHT*0.66f);
		team4.setPosition(SCREEN_WIDTH*0.67f, SCREEN_HEIGHT*0.62f);
		team5.setPosition(SCREEN_WIDTH*0.67f, SCREEN_HEIGHT*0.58f);
		team6.setPosition(SCREEN_WIDTH*0.67f, SCREEN_HEIGHT*0.54f);
		
		configuration = new Sprite(setupOptions, 290, 0, 150, 25);
		debugMode = new Sprite(setupOptions, 290, 26, 110, 25);
		mapSize = new Sprite(setupOptions, 290, 78, 100, 25);
		
		configuration.setPosition(SCREEN_WIDTH*0.64f, SCREEN_HEIGHT*0.33f);
		debugMode.setPosition(SCREEN_WIDTH*0.64f, SCREEN_HEIGHT*0.29f);
		mapSize.setPosition(SCREEN_WIDTH*0.64f, SCREEN_HEIGHT*0.25f);
		
		setupdecor = new Texture("assets/setupMenu/setupdecor.png");
		setuptitle = new Sprite(setupdecor, 0, 0, 155, 50);
		robots = new TextureRegion[2];
		robots[0] = new Sprite(setupdecor, 0, 55, 250, 50);
		robots[1] = new Sprite(setupdecor, 0, 110, 250, 50);
		robotAnimation = new Animation(0.6f, robots);
		stateTime = 0f;
		bluePanel = new Sprite(setupdecor, 0, 170, 260, 320);
		yellowPanel = new Sprite(setupdecor, 270, 15, 220, 305);
		greenPanel = new Sprite(setupdecor, 270, 340, 240, 170);
		
		setuptitle.setPosition(SCREEN_WIDTH*0.215f, SCREEN_HEIGHT*0.85f);
		bluePanel.setPosition(SCREEN_WIDTH*0.18f, SCREEN_HEIGHT*0.45f);
		yellowPanel.setPosition(SCREEN_WIDTH*0.6f, SCREEN_HEIGHT*0.48f);
		greenPanel.setPosition(SCREEN_WIDTH*0.6f, SCREEN_HEIGHT*0.2f);
    }
	
	/**
     * Called to do garbage collection
     */
    @Override
    public void dispose() {
        batch.dispose();
    }
    
    /**
     * Called every frame
     */
    @Override
    public void render(float delta) {   
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        
        checkKeyPresses();

        renderDecor();
        renderConfigOptions();
        renderSetupOptions();
        renderTeamOptions();
        
        batch.end();
    }   
    
    public void checkKeyPresses() {
    	
    	if(Gdx.input.isKeyJustPressed(Keys.ENTER)) {
        	switch(setupMenuIndex) {
        	case 1: // Add team
        		String filepath;
        		filepath = addTeam();
        		controller.addTeam(filepath);
        		break;
        	case 2: // Delete team
        		deleteTeam();
        		controller.deleteTeam();
        		break;
        	case 3: // Toggle map size
        		toggleMapSize();
        		controller.toggleMapSize();
        		break;
        	case 4: // Toggle debug mode
        		toggleDebugMode();
        		controller.toggleDebugMode();
        		break;
        	case 5: // Continue
        		continueSetup();
        		controller.continueSetup();
        		break;
        	case 6: // Return
        		returnSetup();
        		controller.returnSetup();
        		break;
        	}
        }
        if(Gdx.input.isKeyJustPressed(Keys.DOWN)) {
        	if(setupMenuIndex < 6) {
        		setupMenuIndex++;
        	}
        	controller.setupKeyDown();
        }
        if(Gdx.input.isKeyJustPressed(Keys.UP)) {
        	if(setupMenuIndex > 1) {
        		setupMenuIndex--;
        	}
        	controller.setupKeyUp();
        }
    }
    
    /**
     * Adds a team for the view
     * @return the filepath of the robot team file, null if not received
     */
    public String addTeam() {
    	if(teamIndex < 6) {
    		teamIndex++;
    		MyTextInputListener listener = new MyTextInputListener();
    		Gdx.input.getTextInput(listener, "Please enter a filepath::", "", "Robot team's filepath");
    		return listener.getInput();
    	}
    	return null;
    }
    
    public void deleteTeam() {
    	if(teamIndex > 0) {
    		teamIndex--;
    	}
    }
    
    public void toggleMapSize() {
    	
    }
    
    public void toggleDebugMode() {
    	
    }
    
    public void continueSetup() {
    	
    }
    
    public void returnSetup() {
    	
    }
    
    /**
     * 
     * Does all the rendering of the setup options menu
     */
    public void renderSetupOptions() {
    	// Remember, scroll uses percentages!
    	currentSprite.scroll(0, -menOpSrcY/(float)setupOptions.getHeight());
    	currentSprite = setupMenuMap.get(setupMenuIndex);
    	currentSprite.scroll(0, menOpSrcY/(float) setupOptions.getHeight());
    	
		addTeam.draw(batch);
		deleteTeam.draw(batch);
		mapToggle.draw(batch);
		debugToggle.draw(batch);
		continueSprite.draw(batch);
		returnSprite.draw(batch);
    }
    
    /**
     * 
     */
    public void renderTeamOptions() {
		team1.draw(batch);
		team2.draw(batch);
		team3.draw(batch);
		team4.draw(batch);
		team5.draw(batch);
		team6.draw(batch);
    }
    
    public void renderConfigOptions() {
		configuration.draw(batch);
		debugMode.draw(batch);
		mapSize.draw(batch);
    }
    
    public void renderDecor() {
        bluePanel.draw(batch);
        yellowPanel.draw(batch);
        greenPanel.draw(batch);    
        setuptitle.draw(batch);
        
        stateTime += Gdx.graphics.getDeltaTime();
        robotFrame = robotAnimation.getKeyFrame(stateTime, true);
        batch.draw(robotFrame, 250, 300);
    }
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "RobotSport370";
		config.height = 800;
		config.width = 1280;
		new LwjglApplication(new setupController(), config);
	}
}
