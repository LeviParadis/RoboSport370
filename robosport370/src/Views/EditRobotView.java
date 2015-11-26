package Views;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

import Controllers.AddRobotController;
import Controllers.EditRobotController;
import Interpreters.JsonInterpreter;
import Models.Robot;

public class EditRobotView extends AddRobotView{
    
    private static final TextureAtlas redAtlas = new TextureAtlas(Gdx.files.internal("assets/ui_atlas/ui-red.atlas"));
    private TextButton retireButton;

    public EditRobotView(EditRobotController controller, Robot robot) {
        super(controller);
        this.titleLabel.setText("Editing " + robot.getName());
        
        //hide the name and team fields
        this.table.removeActor(this.nameField);
        this.table.removeActor(this.teamField);
        this.table.removeActor(this.teamLabel);
        this.table.removeActor(this.nameLabel);
        
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        
        //add a retire button
        BitmapFont font = new BitmapFont();//(Gdx.files.internal("assets/MoonFlower.fnt"),Gdx.files.internal("assets/MoonFlower.png"),false);
        Skin skin = new Skin();
        skin.addRegions(redAtlas);
        
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("button_02");
        textButtonStyle.down = skin.getDrawable("button_01");
        
        retireButton = new TextButton("Retire Robot", textButtonStyle);
        retireButton.setPosition(width/2 - 250, height-150);
        retireButton.setSize(500, 50);
        retireButton.addListener(this);
        this.stage.addActor(retireButton);
        
        //set fields to start at the robot's stats
        
        //set the forth code field
        Set<String> vars = robot.getAllForthVariables();
        Set<String> wordNames = robot.getAllForthWords();
        HashMap<String, String> fullWords = new  HashMap<String, String>();
        Iterator<String> it = wordNames.iterator();
        while(it.hasNext()){
            String wordName = it.next();
            String wordValue = robot.getForthWord(wordName);
            fullWords.put(wordName, wordValue);
        }
        JSONArray json = JsonInterpreter.forthCodeToJson(vars, fullWords);
        this.forthField.setText(json.toJSONString());
       
        
        
        //for the health and power stats, we know that the base health is 1,
        //and points can be used for either health or power. We can mark boxes as health
        //points until health == 1, and then we can mark the power boxes
        int healthValue = (int) robot.getHealth();
        if(healthValue > 1){
            this.health1.setChecked(true);
            healthValue--;
        } else {
            this.power1.setChecked(true);
        }
        if(healthValue > 1){
            this.health2.setChecked(true);
            healthValue--;
        } else {
            this.power2.setChecked(true);
        }       
        if(healthValue > 1){
            this.health3.setChecked(true);
            healthValue--;
        } else {
            this.power3.setChecked(true);
        }

        

    }
    
    @Override
    /**
     * Handle the user pressing the retire button
     */
    public boolean handle(Event arg0) {
        super.handle(arg0);
        if(arg0.getTarget() instanceof TextButton &&  ((TextButton)arg0.getTarget()).isPressed()){
            TextButton sender = (TextButton)arg0.getTarget();
            if(sender == this.retireButton){
               ((EditRobotController)this.controller).notifyRetire();
            }
        }
        return false;
    }
    
}
