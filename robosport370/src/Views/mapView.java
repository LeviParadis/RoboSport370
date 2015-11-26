package Views;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import Controllers.GameController;
import Controllers.gameVariables;
import Models.Robot;
import Models.Team;
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
	private final GameController controller;
	
	private float WINDOW_WIDTH;
    private float WINDOW_HEIGHT;
    
    // Size of the distance between tiles
    private static final int sizeX = 50;
	private static final int sizeY = 60;
	
	// For the various sprites
	private TextureAtlas atlas;
    private Array<Sprite> tiles;
    private Array<Sprite> robotSprites;
    
    private Texture projectileTexture;
    private Sprite projectile;
    
    // Camera settings
    private int cameraWidth;
    private OrthographicCamera cam;
	
	// Some map settings
	private int mapSize;
	private int mapDiameter;
	
	// Sprites for the various teams
	// It is a set of teams, which each holds a set of sprites
	private List<List<Sprite>> teamList;
	
	// For rendering tweens
	private TweenManager tweenManager;
	private Queue<AudibleTimeline> timelineTweenQueue;
	
	// For rendering sprites
    private SpriteBatch batch;
    
    // TODO For future fonts
    //private BitmapFont font = new BitmapFont(Gdx.files.internal("assets/MoonFlower.fnt"),Gdx.files.internal("assets/MoonFlower.png"),false);
    
    /**
     * Creates a mapView screen
     * @param controller the controller creating this screen
     */
    public mapView(final GameController controller, List<Team> teamsInMatch) {
    	this.controller = controller;
    	
    	WINDOW_WIDTH = Gdx.graphics.getWidth();
    	WINDOW_HEIGHT = Gdx.graphics.getHeight();
    	
    	// Getting our texture atlas of all the sprites
    	atlas = new TextureAtlas(Gdx.files.internal("assets/game_sprites/gamesprites.pack"));
    	
    	// Setting up the tiles
    	tiles = atlas.createSprites("tile");
    	
    	// Setting up the robots
    	robotSprites = atlas.createSprites("robot");
    	
    	projectileTexture = new Texture("assets/game_sprites/projectile.png");
    	projectile = new Sprite(projectileTexture);
    	projectile.setPosition(5000f, 5000f);
    	
    	// Setting up the camera based on map size
    	mapSize = gameVariables.mapSize; 
    	mapDiameter = mapSize * 2 - 1;
    	
    	cameraWidth = (int) (mapDiameter * sizeY * WINDOW_WIDTH/WINDOW_HEIGHT);
    	
    	cam = new OrthographicCamera(cameraWidth, cameraWidth * (WINDOW_HEIGHT/WINDOW_WIDTH));
    	
    	cam.position.set(3 * sizeX * mapSize / 4, 5, 0);
    	cam.update();

    	// Creates out robots
    this.teamList = new ArrayList<List<Sprite>>();
    
    Iterator<Team> it = teamsInMatch.iterator();
    while(it.hasNext()){
        createRobots(it.next());
    }

    	tweenManager = new TweenManager();
    	Tween.registerAccessor(Sprite.class, new SpriteAccessor());
    	timelineTweenQueue = new LinkedList<AudibleTimeline>();
    	batch = new SpriteBatch();
    }
    
    public void createRobots(Team teamToAdd){
    	    Queue<Robot> robotList = teamToAdd.getAllRobots();
    	    Iterator<Robot> it = robotList.iterator();
    	    ArrayList<Sprite> spriteList = new ArrayList<Sprite>();
    	    
    	    int counter = 1;
    	    while(it.hasNext()){
    	        it.next();
    	        Sprite s = atlas.createSprite("robot", teamToAdd.getTeamNumber());
            setRobotPosition(teamToAdd.getTeamNumber(), s);
            spriteList.add(s); 
            counter++;
    	    }
    	    this.teamList.add(spriteList);
        
    }
    
    /**
     * Sets a robot's initial position based on team size and team
     * @param i the team number
     * @param s the robot sprite
     */
    @SuppressWarnings("unused")
	public void setRobotPosition(Integer i, Sprite s) {
    	s.setPosition(-14, -23);
    	
    	if(i == 0) {
        s.translate(-(mapSize-1)*sizeX, 0);
    	}
    	else if(i == 1) {
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
    	else if(i == 2) {
    		// TODO controller.getNumTeams() == 3
    		if(false) {
    			s.translate((mapSize/2)*sizeX, (-0.75f*(float)mapSize+0.75f)*sizeY);
    		}
    		// TODO controller.getNumTeams() == 6
    		else if(true) {
    			s.translate((mapSize/2)*sizeX, (0.75f*(float)mapSize-0.75f)*sizeY);
    		}
    	}
    	else if(i == 3) {
    		s.translate((mapSize-1)*sizeX, 0);
    	}
    	else if(i == 4) {
    		s.translate((mapSize/2)*sizeX, (-0.75f*(float)mapSize+0.75f)*sizeY);
    	}
    	else if(i == 5) {
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
        if(timelineTweenQueue.peek() != null) {
        	if(timelineTweenQueue.peek().getTimeline().isFinished()) {
        		timelineTweenQueue.poll();
        	}
        	else if(!timelineTweenQueue.peek().getTimeline().isStarted()) {
        		timelineTweenQueue.peek().startTimeline(tweenManager);
        	}
        }
        tweenManager.update(delta);
        projectile.draw(batch);
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
    	if(Gdx.input.isKeyJustPressed(Keys.SPACE)) {
    		fireShot(1, 1, 3, 1);
    	}
    }
    
    public void renderTiles() {
    	int width = mapDiameter;
    	int height = mapSize;
    	
    	int xPos = -mapDiameter * sizeX / 2 ;
    	int yPos = (mapSize - 2) * sizeY / 2;
    	
    	// TODO remove the hard coded numbers
    	// Call the controller instead, getMapSize()
    	for(int i = 0; i < width; i++) {
    		for(int j = 0; j < height; j++) {
    			tiles.get(chooseIndex(i, j)).setPosition(xPos, yPos);
    			tiles.get(chooseIndex(i, j)).draw(batch);
    			yPos = yPos - sizeY;
    		}
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
    
    /**
     * This function finds a index (0 through 3) based on map position
     * @param tile the tile being chosen
     * @param i the current column
     * @param j the current height
     * @return the index based of the current map tile
     */
    public int chooseIndex(int i, int j) {
    	// This is essentially a bunch of magic number manipulation
    	// It generates consistent results
    	// But makes it so the map isnt either random or just all of one type of tile
    	if(i == 9 || i == 12 || ( i == 3 && j > 7)) {
    		return 1;
    	}
    	if((i == 0 || i == mapDiameter - 1) && j == mapSize - mapSize / 2 - 1) {
    		return 1;
    	}
    	if(j < i - mapSize && (i % 3 == 2 || i % 3 == 2)) {
    		return 2;
    	}
    	if(j > i + 3) {
    		return 2;
    	}

    	if(j < 3 && (i % 3 == 2 || i % 3 == 1)) {
    		return 2;
    	}
    	if(i < (mapDiameter / 2 + 1) && i < (mapDiameter / 2 - 1) && (j % 3 == 2 || j % 3 == 1) ) {
    		return 3;
    	}
    	if((i - 1 == mapSize/2) && j < 6 && j > 3) {
    		return 3;
    	}
    	if(i == mapSize / 2 + 3 && j == 6) {
    		return 3;
    	}
    	if(i < 4 && i > 1 && j < 4 && j > 2) {
    		return 0;
    	}
    	if(i > 5 && (j % 5 == 4)) {
    		return 0;
    	}
    	return 1;
    }
    
    public void renderRobots() {
    // Starts at team 1
        for(int i = 0; i < teamList.size(); i++) {
    		        for(int j = 0; j < teamList.get(i).size(); j++) {
    		            teamList.get(i).get(j).draw(batch);
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
    	AudibleTimeline aTimeline = new AudibleTimeline();
    	aTimeline.setTimeline(Timeline.createSequence()
    						   .push(Tween.to(teamList.get(1).get(1), SpriteAccessor.POSITION_XY, 0.5f).targetRelative(moveX, moveY)));
    	timelineTweenQueue.add(aTimeline);
    }
    
    public void fireShot(int team1, int robot1, int team2, int robot2) {
    	AudibleTimeline aTimeline = new AudibleTimeline();
    	aTimeline.setProjectile(projectile);
    	aTimeline.setSource(teamList.get(team1).get(robot1));
    	Timeline t = Timeline.createSequence()
    			.push(Tween.to(projectile, SpriteAccessor.POSITION_XY, 0.5f)
    					.target(teamList.get(team2).get(robot2).getX(), teamList.get(team2).get(robot2).getY()));
    	aTimeline.setTimeline(t);
    	timelineTweenQueue.add(aTimeline);
    }
    
    @Override
    public void dispose() {
        batch.dispose();
    }
}
