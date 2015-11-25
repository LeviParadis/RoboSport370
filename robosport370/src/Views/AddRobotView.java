package Views;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

import Controllers.AddRobotController;

public class AddRobotView extends ScreenAdapter {
    
    private final Stage stage;
    private static final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("assets/ui_atlas/ui-blue.atlas"));

    public AddRobotView(AddRobotController controller) {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        
        stage = new Stage();
        
        Gdx.input.setInputProcessor(stage);
        
        BitmapFont font = new BitmapFont();//(Gdx.files.internal("assets/MoonFlower.fnt"),Gdx.files.internal("assets/MoonFlower.png"),false);
        Skin skin = new Skin();
        skin.addRegions(atlas);
        
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("button_02");
        textButtonStyle.down = skin.getDrawable("button_01");
        
        TextButton backButton = new TextButton("Cancel", textButtonStyle);
        backButton.setPosition(100, 50);
        backButton.setSize(500, 50);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("backButton Pressed");
            }
        });
        
        TextButton confirmButton = new TextButton("Confirm", textButtonStyle);
        confirmButton.setPosition(width-600,  50);
        confirmButton.setSize(500, 50);
        confirmButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("confirmButton Pressed");
            }
        });
        
        
        TextFieldStyle textFieldStyle = new TextFieldStyle();
        textFieldStyle.background= skin.getDrawable("textbox_01");
        textFieldStyle.font=font;
        textFieldStyle.fontColor = Color.BLACK;
        textFieldStyle.cursor=skin.getDrawable("textbox_cursor_02");

    
        TextField nameField = new TextField("R2D2", textFieldStyle);
        TextField teamField = new TextField("C3", textFieldStyle);
        TextArea forthField = new TextArea("", textFieldStyle);
        forthField.setWidth(600);
        
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.fontColor = Color.BLACK;
        labelStyle.font = font;
        
        Label nameLabel = new Label("Name: ", labelStyle);
        Label teamLabel = new Label("Team: ", labelStyle);
        Label forthLabel = new Label("Forth Code: ", labelStyle);
        Label pointLabel1 = new Label("Point 1: ", labelStyle);
        Label pointLabel2 = new Label("Point 2: ", labelStyle);
        Label pointLabel3 = new Label("Point 3: ", labelStyle);
        
        Label powerLabel = new Label("Selected Power: ", labelStyle);
        Label healthLabel = new Label("Selected Health: ", labelStyle);
        Label movementLabel = new Label("Selected Movement: ", labelStyle);
        Label powerNumber = new Label("5", labelStyle);
        Label healthNumber = new Label("5", labelStyle);
        Label movementNumber = new Label("5", labelStyle);
        
        
        CheckBoxStyle checkboxStyle = new CheckBoxStyle();
        checkboxStyle.checkboxOn = skin.getDrawable("checkbox_on");
        checkboxStyle.checkboxOff = skin.getDrawable("checkbox_off");
        checkboxStyle.fontColor = Color.BLACK;
        checkboxStyle.font = font;
        
        CheckBox power1 = new CheckBox("Power", checkboxStyle);
        CheckBox power2 = new CheckBox("Power", checkboxStyle);
        CheckBox power3 = new CheckBox("Power", checkboxStyle);
        CheckBox health1 = new CheckBox("Health", checkboxStyle);
        CheckBox health2 = new CheckBox("Health", checkboxStyle);
        CheckBox health3 = new CheckBox("Health", checkboxStyle);
        
        Table table = new Table();
        table.setFillParent(true);
        table.add(nameLabel).padBottom(40f); 
        table.add(nameField).padBottom(40f).width(600); 
        table.row();
        table.add(teamLabel).padBottom(40f); 
        table.add(teamField).padBottom(40f).width(600); 
        table.row();
        table.add(pointLabel1).padBottom(40f); 
        table.add(health1).padBottom(40f);
        table.add(power1).padBottom(40f);
        table.row();
        table.add(pointLabel2).padBottom(40f); 
        table.add(health2).padBottom(40f); 
        table.add(power2).padBottom(40f); 
        table.row();
        table.add(pointLabel3).padBottom(40f);
        table.add(health3).padBottom(40f); 
        table.add(power3).padBottom(40f); 
        table.row();
        table.add(forthLabel).padBottom(40f); 
        table.add(forthField).padBottom(40f).width(600).height(200);
        table.row();
        
        
        

        
        Label titleLabel = new Label("Create a Robot", labelStyle);
        titleLabel.setPosition(width/2 - 100, height-50);
        titleLabel.setFontScale(2);
        
        
        stage.addActor(titleLabel);
        stage.addActor(table);
        stage.addActor(backButton);
        stage.addActor(confirmButton);
        
        
    }
    
    
    public void render(float delta) {   
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }



}
