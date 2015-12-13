package Views;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import Controllers.SetupController;
import Models.Team;

/**
 * A GUI view for the setup screen
 * 
 * @author Corey Reviewed by:
 *
 */
public class SetupView extends ScreenAdapter {
    // The controller which called the view
    final SetupController controller;

    // To store the screen dimensions
    private Integer SCREEN_WIDTH;
    private Integer SCREEN_HEIGHT;

    // Constants for the switch statement
    private static final int ADD_TEAM = 1;
    private static final int DELETE_TEAM = 2;
    private static final int TOGGLE_MAP_SIZE = 3;
    private static final int TOGGLE_DEBUG_MODE = 4;
    private static final int CONTINUE = 5;
    private static final int RETURN = 6;

    // For rendering sprites
    private SpriteBatch batch;

    // setupOption texture variables and sprites
    private Texture setupOptions;
    private static final Integer menOpSrcX = 0;
    private static final Integer menOpSrcY = 31;
    private static final Integer menOpWidth = 190;
    private static final Integer menOpHeight = 30;
    private Sprite addTeam;
    private Integer teamIndex;
    private HashMap<Integer, HideableSprite> teamMap;
    private MyTextInputListener listener;
    private Sprite deleteTeam;
    private Sprite mapToggle;
    private Sprite debugToggle;
    private Sprite continueSprite;
    private Sprite returnSprite;

    // For tracking the active option on the menu
    private SpriteMenuHandler setupMenu;

    // sprites to represent the teams
    private HideableSprite team1;
    private HideableSprite team2;
    private HideableSprite team3;
    private HideableSprite team4;
    private HideableSprite team5;
    private HideableSprite team6;

    // sprites to represent the configuration info
    private Sprite configuration;
    private Sprite debugMode;
    private boolean debug;
    private Sprite mapSize;
    private Integer mapSizeIndex;

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

    private Stage stage;
    private Table teamList;

    private BitmapFont font = new BitmapFont(Gdx.files.internal("assets/MoonFlower.fnt"),
            Gdx.files.internal("assets/MoonFlower.png"), false);

    /**
     * Constructor for SetupView
     * 
     * @param cont
     *            the controller creating this view
     */
    public SetupView(final SetupController cont) {
        // Initailizing the controller, constants, and batch
        controller = cont;

        stage = new Stage();

        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();

        batch = new SpriteBatch();

        // Loading the setup options into their sprites
        setupOptions = new Texture("assets/setupMenu/setupoptions.png");

        addTeam = new Sprite(setupOptions, menOpSrcX, menOpSrcY * 0, menOpWidth, menOpHeight);
        deleteTeam = new Sprite(setupOptions, menOpSrcX, menOpSrcY * 2, menOpWidth, menOpHeight);
        mapToggle = new Sprite(setupOptions, menOpSrcX, menOpSrcY * 4, menOpWidth, menOpHeight);
        debugToggle = new Sprite(setupOptions, menOpSrcX, menOpSrcY * 6, menOpWidth, menOpHeight);
        continueSprite = new Sprite(setupOptions, menOpSrcX, menOpSrcY * 8, menOpWidth, menOpHeight);
        returnSprite = new Sprite(setupOptions, menOpSrcX, menOpSrcY * 10, menOpWidth, menOpHeight);

        // Populating a menu with our sprites
        setupMenu = new SpriteMenuHandler(addTeam);
        setupMenu.addSprite(deleteTeam);
        setupMenu.addSprite(mapToggle);
        setupMenu.addSprite(debugToggle);
        setupMenu.addSprite(continueSprite);
        setupMenu.addSprite(returnSprite);

        // Giving the setup options positions on the screen
        addTeam.setPosition(SCREEN_WIDTH * 0.2f, SCREEN_HEIGHT * 0.75f);
        deleteTeam.setPosition(SCREEN_WIDTH * 0.2f, SCREEN_HEIGHT * 0.70f);
        mapToggle.setPosition(SCREEN_WIDTH * 0.2f, SCREEN_HEIGHT * 0.65f);
        debugToggle.setPosition(SCREEN_WIDTH * 0.2f, SCREEN_HEIGHT * 0.6f);
        continueSprite.setPosition(SCREEN_WIDTH * 0.2f, SCREEN_HEIGHT * 0.55f);
        returnSprite.setPosition(SCREEN_WIDTH * 0.2f, SCREEN_HEIGHT * 0.5f);

        // Giving the team sprites and positions
        teamIndex = 0;
        listener = new MyTextInputListener();
        team1 = new HideableSprite(setupOptions, 215, 2, 50, 20);
        team2 = new HideableSprite(setupOptions, 215, 52, 50, 20);
        team3 = new HideableSprite(setupOptions, 215, 102, 50, 20);
        team4 = new HideableSprite(setupOptions, 215, 152, 50, 20);
        team5 = new HideableSprite(setupOptions, 215, 202, 50, 20);
        team6 = new HideableSprite(setupOptions, 215, 252, 50, 20);
        teamMap = new HashMap<Integer, HideableSprite>();
        teamMap.put(1, team1);
        teamMap.put(2, team2);
        teamMap.put(3, team3);
        teamMap.put(4, team4);
        teamMap.put(5, team5);
        teamMap.put(6, team6);

        team1.setPosition(SCREEN_WIDTH * 0.67f, SCREEN_HEIGHT * 0.74f);
        team2.setPosition(SCREEN_WIDTH * 0.67f, SCREEN_HEIGHT * 0.70f);
        team3.setPosition(SCREEN_WIDTH * 0.67f, SCREEN_HEIGHT * 0.66f);
        team4.setPosition(SCREEN_WIDTH * 0.67f, SCREEN_HEIGHT * 0.62f);
        team5.setPosition(SCREEN_WIDTH * 0.67f, SCREEN_HEIGHT * 0.58f);
        team6.setPosition(SCREEN_WIDTH * 0.67f, SCREEN_HEIGHT * 0.54f);

        // Getting a positioning the configuration info
        configuration = new Sprite(setupOptions, 290, 0, 150, 25);
        debugMode = new Sprite(setupOptions, 290, 26, 110, 25);
        debug = false;
        mapSize = new Sprite(setupOptions, 290, 78, 100, 25);
        mapSizeIndex = 5;

        configuration.setPosition(SCREEN_WIDTH * 0.64f, SCREEN_HEIGHT * 0.33f);
        debugMode.setPosition(SCREEN_WIDTH * 0.64f, SCREEN_HEIGHT * 0.25f);
        mapSize.setPosition(SCREEN_WIDTH * 0.64f, SCREEN_HEIGHT * 0.29f);

        // Creating the decor texture
        setupdecor = new Texture("assets/setupMenu/setupdecor.png");
        // Making the title sprite and positioning it
        setuptitle = new Sprite(setupdecor, 0, 0, 155, 50);
        setuptitle.setPosition(SCREEN_WIDTH * 0.215f, SCREEN_HEIGHT * 0.85f);
        // Setting up the bouncing robots animation
        robots = new TextureRegion[2];
        robots[0] = new Sprite(setupdecor, 0, 55, 250, 50);
        robots[1] = new Sprite(setupdecor, 0, 110, 250, 50);
        robotAnimation = new Animation(0.6f, robots);
        stateTime = 0f;
        // Giving the panels to sprites and positioning them
        bluePanel = new Sprite(setupdecor, 0, 170, 260, 320);
        yellowPanel = new Sprite(setupdecor, 270, 15, 220, 305);
        greenPanel = new Sprite(setupdecor, 270, 340, 240, 170);

        bluePanel.setPosition(SCREEN_WIDTH * 0.18f, SCREEN_HEIGHT * 0.45f);
        yellowPanel.setPosition(SCREEN_WIDTH * 0.6f, SCREEN_HEIGHT * 0.48f);
        greenPanel.setPosition(SCREEN_WIDTH * 0.6f, SCREEN_HEIGHT * 0.2f);

        this.teamList = new Table();
        teamList.setPosition(880, SCREEN_HEIGHT - 285);
        teamList.setFillParent(false);
        this.stage.addActor(teamList);
        this.updateTeamTable(new LinkedList<Team>());
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

        renderDecor();
        renderConfigOptions();
        renderSetupOptions();
        renderTeamOptions();

        font.draw(batch, "Teams", SCREEN_WIDTH * 0.65f, SCREEN_HEIGHT * 0.9f);

        batch.end();

        stage.draw();
    }

    /**
     * Manages any key presses since last render
     */
    public void handleKeyPresses() {
        // Checks for which keys have been pressed
        if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            switch (setupMenu.getIndex()) {
            case ADD_TEAM:
                addTeam();
                break;
            case DELETE_TEAM:
                deleteTeam();
                break;
            case TOGGLE_MAP_SIZE:
                toggleMapSize();
                break;
            case TOGGLE_DEBUG_MODE:
                toggleDebugMode();
                break;
            case CONTINUE:
                continueSetup();
                break;
            case RETURN:
                returnSetup();
                break;
            }
        }
        if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
            setupMenu.down();
        }
        if (Gdx.input.isKeyJustPressed(Keys.UP)) {
            setupMenu.up();
        }
    }

    // TODO All of these funtions either do or may require functions call to the
    // controller
    public void addTeam() {

        controller.notifyNewTeam();
        this.updateTeamTable(controller.selectedTeams);

    }

    public void deleteTeam() {
        controller.notifyDeleteTeam();
        this.updateTeamTable(controller.selectedTeams);
    }

    public void updateTeamTable(List<Team> teams) {
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.fontColor = Color.BLACK;
        labelStyle.font = font;

        this.teamList.clear();

        Iterator<Team> it = teams.iterator();
        while (it.hasNext()) {
            Team next = it.next();
            Label newLabel = new Label(next.getTeamName(), labelStyle);
            this.teamList.add(newLabel).width(50).padBottom(3);
            this.teamList.row();
        }

        if (teams.isEmpty()) {
            Label newLabel = new Label("No Teams", labelStyle);
            this.teamList.add(newLabel).width(50).padBottom(3);
            this.teamList.row();
        }
    }

    public void toggleMapSize() {
        controller.notifyMapSize();
        if (mapSizeIndex < 11) {
            mapSize.scroll(0, mapSize.getHeight() / (float) mapSize.getTexture().getHeight());
            mapSizeIndex = mapSizeIndex + 2;
        } else if (mapSizeIndex >= 11) {
            mapSize.scroll(0, -3 * mapSize.getHeight() / (float) mapSize.getTexture().getHeight());
            mapSizeIndex = mapSizeIndex - 6;
        }
    }

    public void toggleDebugMode() {
        controller.notifyDebug();
        debug = !debug;

        // Switch to debug mode
        if (debug) {
            debugMode.scroll(0, debugMode.getHeight() / (float) debugMode.getTexture().getHeight());
        }
        // Switch to not debug mode
        else {
            debugMode.scroll(0, -debugMode.getHeight() / (float) debugMode.getTexture().getHeight());
        }
    }

    public void continueSetup() {
        controller.notifyContinue();
    }

    public void returnSetup() {
        controller.notifyReturn();
    }

    /**
     * Renders all of the decorative sprites onto the screen
     */
    public void renderDecor() {
        bluePanel.draw(batch);
        yellowPanel.draw(batch);
        greenPanel.draw(batch);
        setuptitle.draw(batch);

        // Adds a animation to the robots
        stateTime += Gdx.graphics.getDeltaTime();
        robotFrame = robotAnimation.getKeyFrame(stateTime, true);
        batch.draw(robotFrame, 250, 300);
    }

    /**
     * Renders all of the configuration sprites onto the screen
     */
    public void renderConfigOptions() {
        configuration.draw(batch);
        debugMode.draw(batch);
        mapSize.draw(batch);
    }

    /**
     * Renders the setup option sprites onto the screen
     */
    public void renderSetupOptions() {
        addTeam.draw(batch);
        deleteTeam.draw(batch);
        mapToggle.draw(batch);
        debugToggle.draw(batch);
        continueSprite.draw(batch);
        returnSprite.draw(batch);
    }

    /**
     * Renders the team number sprites to the screen
     */
    public void renderTeamOptions() {
        // This will check out listener for a filepath
        if (listener.getInput() != null) {
            // TODO Pass the filepath to the controller
            teamIndex++;
            teamMap.get(teamIndex).setVisible(true);
            listener = new MyTextInputListener();
        }

        team1.draw(batch);
        team2.draw(batch);
        team3.draw(batch);
        team4.draw(batch);
        team5.draw(batch);
        team6.draw(batch);
    }

    /**
     * Called to do garbage collection
     */
    @Override
    public void dispose() {
        setupOptions.dispose();
        setupdecor.dispose();
        batch.dispose();
        this.stage.dispose();
    }

    /**
     * set this screen to receive buttons whenever it becomes active. Also,
     * refresh the list of teams
     */
    public void show() {
        Gdx.input.setInputProcessor(stage);
        this.updateTeamTable(controller.selectedTeams);
    }

}
