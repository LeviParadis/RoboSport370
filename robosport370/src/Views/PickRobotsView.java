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
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import Controllers.PickRobotsController;
import Models.Robot;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JOptionPane;

public class PickRobotsView extends ScreenAdapter implements EventListener {
    
    private final Stage stage;
    private static final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("assets/ui_atlas/ui-blue.atlas"));
    private static final TextureAtlas grayAtlas = new TextureAtlas(Gdx.files.internal("assets/ui_atlas/ui-gray.atlas"));
    private static final TextureAtlas commonAtlas = new TextureAtlas(Gdx.files.internal("assets/ui_atlas/ui-commons.atlas"));

    private TextButton confirmButton;
    private TextButton searchButton;
    private TextButton backButton;
    
    private Table searchTable;
    private Table resultsTable;
    private Table rosterTable;
    private Table robotInfoTable;

    private CheckBoxStyle checkboxStyle;
    
    
    private Queue<Robot> robotList;
    private Queue<Robot> rosterList;
    private Robot hoveredRobot;

    private PickRobotsController controller;
    
    
    private TextField nameSearchField;
    private TextField teamSearchField;
    private TextField minWinsSearchField;
    private TextField maxWinsSearchField;
    private TextField minLossesSearchField;
    private TextField maxLossesSearchField;
    private TextField minGamesPlayedSearchField;
    private TextField maxGamesPlayedSearchField;
    private CheckBox versionsSearchBox;
    
    
    /**
     * Set up the controller
     * @param controller the controller we are setting up
     */
    public PickRobotsView(PickRobotsController controller) {
        robotList = new LinkedList<Robot>();
        rosterList = new LinkedList<Robot>();
        
        this.controller = controller;
        
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        
        //set up the stage
        stage = new Stage();
        
        BitmapFont font = new BitmapFont();
        Skin skin = new Skin();
        skin.addRegions(atlas);
        
        Skin inactiveSkin = new Skin();
        inactiveSkin.addRegions(grayAtlas);
        
        Skin selectionSkin = new Skin();
        selectionSkin.addRegions(commonAtlas);
        
        
        //set up buttons
        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.up = skin.getDrawable("button_02");
        buttonStyle.down = skin.getDrawable("button_01");
        buttonStyle.disabled = inactiveSkin.getDrawable("button_01");
        
        //set up text fields
        TextFieldStyle textFieldStyle = new TextFieldStyle();
        textFieldStyle.background= skin.getDrawable("textbox_01");
        textFieldStyle.font=font;
        textFieldStyle.fontColor = Color.BLACK;
        textFieldStyle.cursor=skin.getDrawable("textbox_cursor_02");
        textFieldStyle.selection = selectionSkin.getDrawable("transparent-black-30");
        
        
        //set up labels
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.fontColor = Color.BLACK;
        labelStyle.font = font;   
        
        //set up checkboxes
        checkboxStyle = new CheckBoxStyle();
        checkboxStyle.checkboxOn = skin.getDrawable("checkbox_on");
        checkboxStyle.checkboxOff = skin.getDrawable("checkbox_off");
        checkboxStyle.fontColor = Color.BLACK;
        checkboxStyle.font = font;
             
        
        //set up the title
        String titleString;
        if(controller.getMinimumSelectable() == controller.getMinimumSelectable()){
            titleString = "Select " + controller.getMinimumSelectable() + " Robots";
        } else {
            titleString = "Select " + controller.getMinimumSelectable() + " - "  + controller.getMinimumSelectable() + " Robots";
        }
        Label titleLabel = new Label(titleString, labelStyle);
        titleLabel.setPosition(width/2 - 100, height-50);
        titleLabel.setFontScale(2);
        
        //set up buttons on the bottom
        backButton = new TextButton("Cancel", buttonStyle);
        backButton.setPosition(100, 50);
        backButton.setSize(500, 50);
        backButton.addListener(this);
        
        
        confirmButton = new TextButton("Confirm", buttonStyle);
        confirmButton.setPosition(width-600,  50);
        confirmButton.setSize(500, 50);
        confirmButton.addListener(this);
        
        searchButton = new TextButton("Search", buttonStyle);
        searchButton.setPosition(width-200,  50);
        searchButton.setSize(500, 50);
        searchButton.addListener(this);
        
        Table masterTable = new Table();
        masterTable.setFillParent(true);
        
        Table searchTable = new Table();
        searchTable.setColor(Color.BLUE);
        resultsTable = new Table();
        rosterTable = new Table();
        robotInfoTable = new Table();
        
        Label searchTitle = new Label("Search", labelStyle);
        Label resultsTitle = new Label("Robot List", labelStyle);
        Label rosterTitle = new Label("Current Roster", labelStyle);
        Label infoTitle = new Label("Robot Info", labelStyle);
        
        ScrollPaneStyle scrollStyle = new ScrollPaneStyle(); 
        scrollStyle.vScrollKnob = skin.getDrawable("slider_back_ver");
        scrollStyle.hScrollKnob = skin.getDrawable("slider_back_hor");
        
        
        
        ScrollPane scrollResults = new ScrollPane(resultsTable, scrollStyle);
        scrollResults.setFadeScrollBars(false);
        ScrollPane scrollRoster = new ScrollPane(rosterTable, scrollStyle);
        scrollRoster.setFadeScrollBars(false);
        
        masterTable.add(searchTitle);
        masterTable.add(resultsTitle);
        masterTable.add(rosterTitle);
        masterTable.add(infoTitle);
        masterTable.row();
        masterTable.add(searchTable).width(200).padRight(50);
        masterTable.add(scrollResults).width(200).height(400);
        masterTable.add(scrollRoster).width(200);
        masterTable.add(robotInfoTable).width(200);

        
        Label nameSearchLabel = new Label("Robot Name:", labelStyle);
        Label teamSearchLabel = new Label("Team Name:", labelStyle); 
        Label minWinsSearchLabel = new Label("Min Wins:", labelStyle); 
        Label maxWinsSearchLabel = new Label("Max Wins:", labelStyle); 
        Label minLossesSearchLabel = new Label("Min Losses:", labelStyle); 
        Label maxLossesSearchLabel = new Label("Max Losees:", labelStyle); 
        Label minGamesPlayedSearchLabel = new Label("Min Matches:", labelStyle); 
        Label maxGamesPlayedSearchLabel = new Label("Max Matches:", labelStyle); 
        Label allVersionsSearchLabel = new Label("All Versions:", labelStyle); 
        nameSearchField = new TextField("*", textFieldStyle);
        teamSearchField = new TextField("C3", textFieldStyle);
        minWinsSearchField = new TextField("0", textFieldStyle);
        maxWinsSearchField = new TextField("1000", textFieldStyle);
        minLossesSearchField = new TextField("0", textFieldStyle);
        maxLossesSearchField = new TextField("1000", textFieldStyle);
        minGamesPlayedSearchField = new TextField("0", textFieldStyle);
        maxGamesPlayedSearchField = new TextField("1000", textFieldStyle);
        versionsSearchBox = new CheckBox("", checkboxStyle);
        versionsSearchBox.setChecked(true);
        searchTable.row();
        searchTable.add(nameSearchLabel).padBottom(5);
        searchTable.add(nameSearchField).padBottom(5);
        searchTable.row();
        searchTable.add(teamSearchLabel).padBottom(5);
        searchTable.add(teamSearchField).padBottom(5);
        searchTable.row();
        searchTable.add(minWinsSearchLabel).padBottom(5);
        searchTable.add(minWinsSearchField).padBottom(5);
        searchTable.row();
        searchTable.add(maxWinsSearchLabel).padBottom(5);
        searchTable.add(maxWinsSearchField).padBottom(5);
        searchTable.row();
        searchTable.add(minLossesSearchLabel).padBottom(5);
        searchTable.add(minLossesSearchField).padBottom(5);
        searchTable.row();
        searchTable.add(maxLossesSearchLabel).padBottom(5);
        searchTable.add(maxLossesSearchField).padBottom(5);
        searchTable.row();
        searchTable.add(minGamesPlayedSearchLabel).padBottom(5);
        searchTable.add(minGamesPlayedSearchField).padBottom(5);
        searchTable.row();
        searchTable.add(maxGamesPlayedSearchLabel).padBottom(5);
        searchTable.add(maxGamesPlayedSearchField).padBottom(5);
        searchTable.row();
        searchTable.add(allVersionsSearchLabel).padBottom(5);
        searchTable.add(versionsSearchBox).padBottom(5);
        searchTable.row();
        searchTable.add(searchButton);
        
        //add actors to stage
        stage.addActor(titleLabel);
        stage.addActor(backButton);
        stage.addActor(confirmButton);
        stage.addActor(masterTable);
        
        this.refreshRosterList();
        
    }

    
    /**
     * render the scene
     */
    public void render(float delta) {   
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    /**
     * We use this to handle button presses
     */
    public boolean handle(Event arg0) {
        if(arg0.getTarget() instanceof CheckBox){
            CheckBox checked = (CheckBox)arg0.getTarget();
            boolean isChecked = checked.isChecked();
            
            Robot selectedRobot = (Robot)arg0.getTarget().getUserObject();
            if(checked.getParent() == this.resultsTable){
                if(isChecked){
                    if(rosterList.size() < controller.getMaxSelectable()){
                        rosterList.add(selectedRobot);
                    } else {
                        checked.setChecked(false);
                    }
                } else {
                    rosterList.remove(selectedRobot);
                }
            } else {
                rosterList.remove(selectedRobot);
            }
            refreshRosterList();
            refreshResultsList();

        } else  if(arg0.getTarget() instanceof TextButton &&  ((TextButton)arg0.getTarget()).isPressed()){
            TextButton sender = (TextButton)arg0.getTarget();
            if(sender == this.confirmButton){
                this.controller.notifyConfirm(this.rosterList);     
            } else if (sender == this.backButton){
                this.controller.notifyCancel();
            } else if (sender == this.searchButton){
                String name = nameSearchField.getText();
                String team = teamSearchField.getText();
                String minWins = minWinsSearchField.getText();
                String maxWins = maxWinsSearchField.getText();
                String minLosses = minLossesSearchField.getText();
                String maxLosses = maxLossesSearchField.getText();
                String minGamesPlayed = minGamesPlayedSearchField.getText();
                String maxGamesPlayed = maxGamesPlayedSearchField.getText();
                boolean allVersions = versionsSearchBox.isChecked();
                try{
                    this.robotList = this.controller.notifySearch(name, team, minWins, maxWins, minLosses, maxLosses, minGamesPlayed, maxGamesPlayed, !allVersions);
                } catch (NumberFormatException e){
                    JOptionPane.showMessageDialog(null, "Min and Max Values Must be Integers");
                }
                refreshResultsList();
            }
        } else if(arg0.getTarget() instanceof Label && arg0.getTarget().getUserObject() instanceof Robot){
            Robot currentRobot = (Robot)arg0.getTarget().getUserObject();
            hoveredRobot = currentRobot;
            refreshInfoList();
        }
        return false;
    }
    
    public void refreshResultsList(){
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.fontColor = Color.BLACK;
        labelStyle.font = new BitmapFont();   
        
        this.resultsTable.clear();
        Iterator<Robot> it = this.robotList.iterator();
        
        while(it.hasNext()){
            Robot next = it.next();
            Label nameLabel = new Label(next.getName(), labelStyle);
            CheckBox box = new CheckBox("", checkboxStyle);
            if(this.rosterList.contains(next)){
                box.setChecked(true);
            }
            box.setUserObject(next);
            box.addListener(this);
            nameLabel.setUserObject(next);
            nameLabel.addListener(this);
            this.resultsTable.add(nameLabel).padBottom(10).padRight(50);
            this.resultsTable.add(box).padBottom(10);
            this.resultsTable.row();
        }
    }
    
    public void refreshRosterList(){
        
        System.out.println(this.rosterList);
        
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.fontColor = Color.BLACK;
        labelStyle.font = new BitmapFont();   
        
        this.rosterTable.clear();
        Iterator<Robot> it = this.rosterList.iterator();
        
        while(it.hasNext()){
            Robot next = it.next();
            Label nameLabel = new Label(next.getName(), labelStyle);
            CheckBox box = new CheckBox("", checkboxStyle);
            box.setUserObject(next);
            box.setChecked(true);
            box.addListener(this);
            this.rosterTable.add(nameLabel).padBottom(10).padRight(50);
            this.rosterTable.add(box).padBottom(10);
            this.rosterTable.row();
        }  
        int size = this.rosterList.size();

        this.confirmButton.setDisabled(size < controller.getMinimumSelectable());
        
    }
    
    public void refreshInfoList(){
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.fontColor = Color.BLACK;
        labelStyle.font = new BitmapFont();
        
        this.robotInfoTable.clear();
        Label nameInfoLabel = new Label("Robot Name:", labelStyle);
        Label winsInfoLabel = new Label("Robot Total Wins:", labelStyle);
        Label lossesInfoLabel = new Label("Robot Total Losses:", labelStyle);
        Label healthInfoLabel = new Label("Robot Starting Health:", labelStyle);
        Label powerInfoLabel = new Label("Robot Damage Per Shot:", labelStyle);
        Label movementInfoLabel = new Label("Robot Moves Per Turn:", labelStyle);
        
        Label nameRobotLabel = new Label(hoveredRobot.getName(), labelStyle);
        Label winsRobotLabel = new Label(String.valueOf(hoveredRobot.getStats().getWins()), labelStyle);
        Label lossesRobotLabel = new Label(String.valueOf(hoveredRobot.getStats().getLosses()), labelStyle);
        Label healthRobotLabel = new Label(String.valueOf(hoveredRobot.getBaseHealth()), labelStyle);
        Label powerRobotLabel = new Label(String.valueOf(hoveredRobot.getStrength()), labelStyle);
        Label movementRobotLabel = new Label(String.valueOf(hoveredRobot.getMovesPerTurn()), labelStyle);
        
        this.robotInfoTable.row();
        this.robotInfoTable.add(nameInfoLabel).padBottom(5);
        this.robotInfoTable.add(nameRobotLabel).padBottom(5);
        this.robotInfoTable.row();
        this.robotInfoTable.add(winsInfoLabel).padBottom(5);
        this.robotInfoTable.add(winsRobotLabel).padBottom(5);
        this.robotInfoTable.row();
        this.robotInfoTable.add(lossesInfoLabel).padBottom(5);
        this.robotInfoTable.add(lossesRobotLabel).padBottom(5);
        this.robotInfoTable.row();
        this.robotInfoTable.add(healthInfoLabel).padBottom(5);
        this.robotInfoTable.add(healthRobotLabel).padBottom(5);
        this.robotInfoTable.row();
        this.robotInfoTable.add(powerInfoLabel).padBottom(5);
        this.robotInfoTable.add(powerRobotLabel).padBottom(5);
        this.robotInfoTable.row();
        this.robotInfoTable.add(movementInfoLabel).padBottom(5);
        this.robotInfoTable.add(movementRobotLabel).padBottom(5);
        this.robotInfoTable.padLeft(100);
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