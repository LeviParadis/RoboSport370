package Views;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;


/**
 * A GUI view for the main menu
 * @author Corey
 *
 */
public class mapView extends ScreenAdapter {
	// The controller which called the view
	private final Game controller;
	
	private float WINDOW_WIDTH;
    private float WINDOW_HEIGHT;
    
    // Size of the distance between tiles
    private static final int sizeX = 50;
	private static final int sizeY = 60;
	
	// For the various sprites
	private TextureAtlas atlas;
    private Array<Sprite> tiles;
    private Array<Sprite> robotSprites;
    
    // Camera settings
    private int cameraWidth;
    private OrthographicCamera cam;
	
	// Some map settings
	private int mapSize;
	private int mapDiameter;
	
	// Sprites for the various teams
	// It is a set of teams, which each holds a set of sprites
	private HashMap<Integer, HashMap<Integer, Sprite>> teams;
	
	// For rendering tweens
	private TweenManager tweenManager;
	private Queue<Tween> tweenQueue;
	
	// For rendering sprites
    private SpriteBatch batch;
    
    // TODO For future fonts
    //private BitmapFont font = new BitmapFont(Gdx.files.internal("assets/MoonFlower.fnt"),Gdx.files.internal("assets/MoonFlower.png"),false);
    
    /**
     * Creates a mapView screen
     * @param controller the controller creating this screen
     */
    public mapView(final Game controller) {
    	this.controller = controller;
    	
    	WINDOW_WIDTH = Gdx.graphics.getWidth();
    	WINDOW_HEIGHT = Gdx.graphics.getHeight();
    	
    	// Getting our texture atlas of all the sprites
    	atlas = new TextureAtlas(Gdx.files.internal("assets/game_sprites/gamesprites.pack"));
    	
    	// Setting up the tiles
    	tiles = atlas.createSprites("tile");
    	
    	// Setting up the robots
    	robotSprites = atlas.createSprites("robot");
    	
    	// Setting up the camera based on map size
    	// TODO Add a function from the controller here 
    	mapSize = 7; // mapSize = controller.getMapSize();
    	mapDiameter = mapSize * 2 - 1;
    	
    	cameraWidth = (int) (mapDiameter * sizeY * WINDOW_WIDTH/WINDOW_HEIGHT);
    	
    	cam = new OrthographicCamera(cameraWidth, cameraWidth * (WINDOW_HEIGHT/WINDOW_WIDTH));
    	
    	cam.position.set(3 * sizeX * mapSize / 4, 5, 0);
    	cam.update();

    	// Creates out robots
    	teams = new HashMap<Integer, HashMap<Integer, Sprite>>();
    	createRobots();

    	tweenManager = new TweenManager();
    	Tween.registerAccessor(Sprite.class, new SpriteAccessor());
    	tweenQueue = new LinkedList<Tween>();
    	batch = new SpriteBatch();
    }
    
    public void createRobots() {
    	HashMap<Integer, Sprite> team;
    	Sprite s;
    	
    	// TODO include controller.getNumberOfTeams() as the for loop bound
    	// Fill the map with the appropriate amount of robots
    	// For every team
    	for(int i = 1; i <= 6; i++) {
    		team = new HashMap<Integer, Sprite>();
    		// For every robot
    		for(int j = 1; j <= 6; j++) {
    			// Creates a new sprite instance
    			s = atlas.createSprite("robot", i-1);
    			setRobotPosition(i, s);
    			team.put(j, s);
    		}
    		teams.put(i, team);
    	}
    }
    
    /**
     * Sets a robot's initial position based on team size and team
     * @param i the team number
     * @param s the robot sprite
     */
    @SuppressWarnings("unused")
	public void setRobotPosition(Integer i, Sprite s) {
    	s.setPosition(-14, -23);
    	
    	if(i == 1) {
    		s.translate(-(mapSize-1)*sizeX, 0);
    	}
    	else if(i == 2) {
    		// TODO controller.getNumTeams() == 2
    		if(false) {
    			s.translate((mapSize-1)*sizeX, 0);
    		}
    		// TODO controller.getNumTeams() == 3
    		else if(false) {
    			s.translate((mapSize/2)*sizeX, (0.75f*(float)mapSize-0.75f)*sizeY);
    		}
    		// TODO controller.getNumTeams() == 6
    		else if(true) {
    			s.translate(-(mapSize/2)*sizeX, (0.75f*(float)mapSize-0.75f)*sizeY);
    		}
    	}
    	else if(i == 3) {
    		// TODO controller.getNumTeams() == 3
    		if(false) {
    			s.translate((mapSize/2)*sizeX, (-0.75f*(float)mapSize+0.75f)*sizeY);
    		}
    		// TODO controller.getNumTeams() == 6
    		else if(true) {
    			s.translate((mapSize/2)*sizeX, (0.75f*(float)mapSize-0.75f)*sizeY);
    		}
    	}
    	else if(i == 4) {
    		s.translate((mapSize-1)*sizeX, 0);
    	}
    	else if(i == 5) {
    		s.translate((mapSize/2)*sizeX, (-0.75f*(float)mapSize+0.75f)*sizeY);
    	}
    	else if(i == 6) {
    		s.translate(-(mapSize/2)*sizeX, (-0.75f*(float)mapSize+0.75f)*sizeY);
    	}
    }
    
    public void render(float delta) {   
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        
        batch.begin();
        renderTiles();
        renderRobots();
        renderTesting();
        if(tweenQueue.peek() != null) {
        	if(tweenQueue.peek().isFinished()) {
        		tweenQueue.poll();
        	}
        	else if(!tweenQueue.peek().isStarted()) {
        		tweenQueue.peek().start(tweenManager);
        	}
        }
        tweenManager.update(delta);
        batch.end();
    }
    
    public void renderTesting() {
    	if(Gdx.input.isKeyJustPressed(Keys.NUM_1)) {
        	moveRobot(1, 1, 1);
        }
    	if(Gdx.input.isKeyJustPressed(Keys.NUM_2)) {
        	moveRobot(1, 1, 2);
        }
    	if(Gdx.input.isKeyJustPressed(Keys.NUM_3)) {
        	moveRobot(1, 1, 3);
        }
    	if(Gdx.input.isKeyJustPressed(Keys.NUM_4)) {
        	moveRobot(1, 1, 4);
        }
    	if(Gdx.input.isKeyJustPressed(Keys.NUM_5)) {
        	moveRobot(1, 1, 5);
        }
    	if(Gdx.input.isKeyJustPressed(Keys.NUM_6)) {
        	moveRobot(1, 1, 6);
        }
    }
    
    public void renderTiles() {
    	int tileIndex = 0;
    	int width = mapDiameter;
    	int height = mapSize;
    	
    	int xPos = -mapDiameter * sizeX / 2 ;
    	int yPos = (mapSize - 2) * sizeY / 2;
    	
    	// TODO remove the hard coded numbers
    	// Call the controller instead, getMapSize()
    	for(int i = 0; i < width; i++) {
    		for(int j = 0; j < height; j++) {
    			tiles.get(tileIndex % 4).setPosition(xPos, yPos);
    			tiles.get(tileIndex % 4).draw(batch);
    			tileIndex++;
    			yPos = yPos - sizeY;
    		}
    		tileIndex++;
    		tileIndex++;
    		if( (width) / (i+1) >= 2) {
    			yPos = yPos + sizeY * height + sizeY/2;
    			height++;
    		}
    		else {
    			yPos = yPos + sizeY * height - sizeY/2;
    			height--;
    		}
    		xPos = xPos + sizeX;
    	}
    }
    
    public void renderRobots() {
    	// Starts at team 1
    	for(int i = 1; i <= teams.size(); i++) {
    		for(int j = i; j <= teams.get(i).size(); j++) {
    			teams.get(i).get(j).draw(batch);
    		}
    	}
    }
    
    /**
     * Moves a robot in a certain direction
     * @param team the number of the team the robot is on
     * @param robot the number of the robot on the team
     * @param direction the direct (1 is north, 2 is north east, etc. to 6)
     */
    public void moveRobot(int team, int robot, int direction) {
    	int moveX = 0;
    	int moveY = 0;
    	
    	// Doing all of our x translations
    	if(direction == 2 || direction == 3) {
    		moveX = sizeX;
    	}
    	if(direction == 5 || direction == 6) {
    		moveX = -sizeX;
    	}
    	
    	// Doing all of our y translations
    	if(direction == 1) {
    		moveY = sizeY;
    	}
    	if(direction == 2 || direction == 6) {
    		moveY = sizeY/2;
    	}
    	if(direction == 3 || direction == 5) {
    		moveY = -sizeY/2;
    	}
    	if(direction == 4) {
    		moveY = -sizeY;
    	}
    	
    	tweenQueue.add(Tween.to(teams.get(1).get(1), SpriteAccessor.POSITION_XY, 0.5f).targetRelative(moveX, moveY));
    }
    
    @Override
    public void dispose() {
        batch.dispose();
    }
}
