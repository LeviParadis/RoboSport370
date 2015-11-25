package Views;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

import Controllers.AddRobotController;

public class AddRobotView extends ScreenAdapter {
    
    private final Stage backButton;
    private static final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("assets/ui_atlas/ui-blue.atlas"));

    public AddRobotView(AddRobotController controller) {
        backButton = new Stage();
        
        Gdx.input.setInputProcessor(backButton);
        
        BitmapFont font = new BitmapFont();
        Skin skin = new Skin();
        skin.addRegions(atlas);
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("button_02");
        textButtonStyle.down = skin.getDrawable("button_01");
        TextButton button = new TextButton("Back", textButtonStyle);
        button.setPosition(50, 50);
        button.setSize(500, 50);
        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Button Pressed");
            }
        });
        
        backButton.addActor(button);
    }
    
    
    public void render(float delta) {   
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        backButton.draw();
    }



}
