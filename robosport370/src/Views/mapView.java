package Views;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Array;

import Controllers.GameController;
import Controllers.gameVariables;
import Enums.ConsoleMessageType;
import Enums.GameSpeed;
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
public class mapView extends ScreenAdapter implements EventListener {
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
    
    private Stage stage;
    private Table topTable;
    private LabelStyle blackLabelStyle;
    private LabelStyle redLabelStyle;
    private LabelStyle blueLabelStyle;
    private LabelStyle purpleLabelStyle;
    private ScrollPane scrollResults;
    private Label nameLabel;
    private Label teamLabel;
    private Label turnLabel;
    private Label numLabel;
    private TextButton pauseBtn;
    private TextButton speedBtn;
    
    // TODO For future fonts
    //private BitmapFont font = new BitmapFont(Gdx.files.internal("assets/MoonFlower.fnt"),Gdx.files.internal("assets/MoonFlower.png"),false);
    
    /**
     * Creates a mapView screen
     * @param controller the controller creating this screen
     */
    public mapView(final GameController controller, List<Team> teamsInMatch) {
    	this.controller = controller;
    
    this.stage = new Stage();
    	
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
    
    
    //add table to sides
  //set up scroll bar style
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("assets/ui_atlas/ui-blue.atlas"));
    Skin skin = new Skin();
    skin.addRegions(atlas);
    ScrollPaneStyle scrollStyle = new ScrollPaneStyle(); 
    scrollStyle.vScrollKnob = skin.getDrawable("slider_back_ver");

    TextButtonStyle buttonStyle = new TextButtonStyle();
    buttonStyle.font = new BitmapFont();
    buttonStyle.up = skin.getDrawable("button_02");
    buttonStyle.down = skin.getDrawable("button_01");
    
    //put lists in scroll panes, so we can scroll to see all entries
    
    pauseBtn = new TextButton("Pause", buttonStyle);
    pauseBtn.addListener(this);
    speedBtn = new TextButton("1X", buttonStyle);
    speedBtn.addListener(this);
    
    Table master = new Table();
    master.setSize(WINDOW_WIDTH/3, WINDOW_HEIGHT);
    master.setPosition(WINDOW_WIDTH-(WINDOW_WIDTH/3), 0);
    topTable = new Table();
    Table bottom = new Table();
    
    scrollResults = new ScrollPane(topTable, scrollStyle);
    scrollResults.setFadeScrollBars(false);
    
    master.add(scrollResults);
    master.row();
    master.add(speedBtn);
    master.add(pauseBtn);
    master.row();
    master.add(bottom).padBottom(300);
    stage.addActor(master);
    
    BitmapFont font = new BitmapFont();
    
    blackLabelStyle = new LabelStyle();
    blackLabelStyle.fontColor = Color.BLACK;
    blackLabelStyle.font = font;
    
    blueLabelStyle = new LabelStyle();
    blueLabelStyle.fontColor = Color.BLUE;
    blueLabelStyle.font = font;
    
    redLabelStyle = new LabelStyle();
    redLabelStyle.fontColor = Color.RED;
    redLabelStyle.font = font;
    
    purpleLabelStyle = new LabelStyle();
    purpleLabelStyle.fontColor = Color.PURPLE;
    purpleLabelStyle.font = font;

    
    Label titleLabel = new Label("Current Robot Information: ", blackLabelStyle);
    Label nameTitle = new Label("Name: ", blackLabelStyle);
    Label teamTitle = new Label("Team: ", blackLabelStyle);
    Label turnTitle = new Label("Turn: ", blackLabelStyle);
    Label numTitle = new Label("Number: ", blackLabelStyle);
    nameLabel = new Label("", blackLabelStyle);
    teamLabel = new Label("", blackLabelStyle);
    turnLabel = new Label("", blackLabelStyle);
    numLabel = new Label("", blackLabelStyle);
    bottom.add(titleLabel);
    bottom.row();
    bottom.add(nameTitle);
    bottom.add(nameLabel);
    bottom.row();
    bottom.add(teamTitle);
    bottom.add(teamLabel);
    bottom.row();
    bottom.add(numTitle);
    bottom.add(numLabel);
    bottom.row();
    bottom.add(turnTitle);
    bottom.add(turnLabel);
    }
    
    /**
     * used to update the console logger
     * newMessage the latest message to display
     */
    public void displayMessage(String newMessage, ConsoleMessageType type){  
        LabelStyle style;
        switch(type){
        case CONSOLE_ERROR:
            style = redLabelStyle;
            break;
        case CONSOLE_ROBOT_MESSAGE:
            style = purpleLabelStyle;
            break;
        case CONSOLE_SIMULATOR_MESSAGE:
            style = blueLabelStyle;
            break;
        default:
            style = blackLabelStyle;
            break;
        }
        Label messageLabel = new Label(newMessage, style);
        topTable.add(messageLabel);
        topTable.row();   
    }
    
    /**
     * used to update the current robot's info on the screen
     * @param robot the robot that is running it's turn
     */
    public void updateRobotInfo(Robot robot, int turnNum){
        nameLabel.setText(robot.getName());
        long teamNum = robot.getTeamNumber() + 1;
        teamLabel.setText("Team " + teamNum);
        numLabel.setText("" + robot.getMemberNumber());
        if(turnNum == 0){
            turnLabel.setText("init");
        } else {
            turnLabel.setText("" + turnNum);
        }
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
        
        //we want to keep the scroll bar at the bottom when we add new items
        if(!controller.isPaused() && (scrollResults.getScrollPercentY() > 0.8 || topTable.getHeight() < 500)){
            scrollResults.setScrollPercentY(1);
        }
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
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
    
    /**
     * set this screen to receive buttons whenever it becomes active
     */
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public boolean handle(Event arg0) {
        if(arg0.getTarget() instanceof TextButton &&  ((TextButton)arg0.getTarget()).isPressed()){
            //handle button presses
            TextButton sender = (TextButton)arg0.getTarget();
            if(sender == this.pauseBtn){
                if(this.controller.isPaused()){
                    this.controller.resume();
                    pauseBtn.setText("Pause");
                } else {
                    this.controller.pause();
                    pauseBtn.setText("Play");
                }
            } else if (sender == this.speedBtn){
                GameSpeed newSpeed = this.controller.switchGameSpeed();
                switch(newSpeed){
                    case GAME_SPEED_1X:
                        this.speedBtn.setText("1X");
                        break;
                    case GAME_SPEED_2X:
                        this.speedBtn.setText("2X");
                        break;
                    case GAME_SPEED_4X:
                        this.speedBtn.setText("4X");
                        break;
                    case GAME_SPEED_16X:
                        this.speedBtn.setText("16X");
                        break;
                }
            }
        }
        return false;
    }
}
