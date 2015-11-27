package Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

import Controllers.EndController;
import Models.Team;
import Models.Robot;

import java.util.Iterator;
import java.util.List;

public class endView  extends ScreenAdapter  implements EventListener{
    
    private EndController controller;
    private final Stage stage;
    
    private TextButton mainMenuButton;
    private TextButton displayResultsButton;
    private TextButton exitButton;
    
    private List<Team> teamsList;

    /**
     * Set up the buttons for the view
     * @param controller the manage robot controller to handle the button presses
     */
    public endView(EndController controller, List<Team> teams) {
        this.controller = controller;
        this.teamsList = teams;
        
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        
        //set up the stage
        stage = new Stage();        
      
        TextureAtlas blueAtlas = new TextureAtlas(Gdx.files.internal("assets/ui_atlas/ui-blue.atlas"));
        BitmapFont font = new BitmapFont();
        BitmapFont fontTitle = new BitmapFont();
        Skin skin = new Skin();
        skin.addRegions(blueAtlas);
        
        //set up the buttons
        
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("button_02");
        textButtonStyle.down = skin.getDrawable("button_01");
        
        mainMenuButton = new TextButton("Main Menu", textButtonStyle);
        mainMenuButton.addListener((EventListener) this);
        
        displayResultsButton = new TextButton("Display Results", textButtonStyle);
        displayResultsButton.addListener((EventListener) this);
        
        exitButton = new TextButton("Exit Game", textButtonStyle);
        exitButton.addListener((EventListener) this);
        
        //LabelsStyles for screen
        LabelStyle titleStyle = new LabelStyle();
        titleStyle.fontColor = Color.BLACK;
        titleStyle.font = fontTitle;
        
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.fontColor = Color.BLACK;
        labelStyle.font = font;
        
        LabelStyle aliveStyle = new LabelStyle();
        aliveStyle.fontColor = Color.GREEN;
        aliveStyle.font = font;
        
        LabelStyle deadStyle = new LabelStyle();
        deadStyle.fontColor = Color.RED;
        deadStyle.font = font;
        
        //title label
        Label title = new Label("GAME OVER", titleStyle);
        title.setFontScaleX(2);
        title.setFontScaleY(3);
        title.setPosition(width/2-87, height-50);
        
        //Add teams and there robots (color coded: green is alive, red is dead) to screen
        Table statsTable = new Table();
        Iterator<Team> tIt = teamsList.iterator();
        
        while (tIt.hasNext()){
            Team currentTeam = tIt.next();
            Label teamName = new Label(currentTeam.getTeamName(), labelStyle);
            Table teamTable = new Table();
            teamTable.add(teamName).padBottom(10);
            
            Iterator<Robot> rIt = currentTeam.getAllRobots().iterator();
            while (rIt.hasNext()) {
                Robot currentRobot = rIt.next();
                if (currentRobot.isAlive()){
                    Label robotName = new Label(currentRobot.getName(), aliveStyle);
                    teamTable.row();
                    teamTable.add(robotName).padBottom(5).width(40);
                }
                else {
                    Label robotName = new Label(currentRobot.getName(), deadStyle);
                    teamTable.row();
                    teamTable.add(robotName).padBottom(5);
                }
            }
            statsTable.add(teamTable).padLeft(25).padRight(25);    
        }
        
        
        //set up the master table
        Table masterTable = new Table();
        masterTable.setFillParent(true);
        masterTable.add(statsTable).padBottom(25);
        masterTable.row();
        masterTable.add(mainMenuButton).width(500).height(75).padBottom(25);
        masterTable.row();
        masterTable.add(exitButton).width(500).height(75).padBottom(25);
        stage.addActor(title);
        stage.addActor(masterTable);
    }

    public void render(float delta) {   
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }
    
    @Override
    /**
     * We use this to handle button presses
     */
    public boolean handle(Event arg0) {
        if(arg0.getTarget() instanceof TextButton &&  ((TextButton)arg0.getTarget()).isPressed()){
            TextButton sender = (TextButton)arg0.getTarget();
           if(sender == mainMenuButton){
               controller.notifyMainMenuPressed();
           } else if (sender == exitButton){
               controller.notifyExitButtonPressed();
           }
        }
        return false;
    }
    
    /**
     * set this screen to receive buttons whenever it becomes active
     */
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    
    /**
     * Called to do garbage collection
     */
    @Override
    public void dispose() {
        this.stage.dispose();
    }
 
}
