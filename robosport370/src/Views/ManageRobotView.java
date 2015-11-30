package Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

import Controllers.ManageRobotController;

public class ManageRobotView extends ScreenAdapter implements EventListener {

    private ManageRobotController controller;
    private final Stage stage;

    private TextButton backButton;
    private TextButton addButton;
    private TextButton editButton;

    /**
     * Set up the buttons for the view
     * 
     * @param controller
     *            the manage robot controller to handle the button presses
     */
    public ManageRobotView(ManageRobotController controller) {
        this.controller = controller;

        // set up the stage
        stage = new Stage();

        TextureAtlas blueAtlas = new TextureAtlas(Gdx.files.internal("assets/ui_atlas/ui-blue.atlas"));
        BitmapFont font = new BitmapFont();
        Skin skin = new Skin();
        skin.addRegions(blueAtlas);

        // set up the buttons

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("button_02");
        textButtonStyle.down = skin.getDrawable("button_01");

        backButton = new TextButton("Cancel", textButtonStyle);
        backButton.addListener((EventListener) this);

        addButton = new TextButton("Add Robot", textButtonStyle);
        addButton.addListener((EventListener) this);

        editButton = new TextButton("Edit Robot", textButtonStyle);
        editButton.addListener((EventListener) this);

        // set up the table
        Table table = new Table();
        table.setFillParent(true);
        table.add(addButton).width(500).height(75).padBottom(25);
        table.row();
        table.add(editButton).width(500).height(75).padBottom(25);
        table.row();
        table.add(backButton).width(500).height(75).padBottom(25);
        stage.addActor(table);
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
        if (arg0.getTarget() instanceof TextButton && ((TextButton) arg0.getTarget()).isPressed()) {
            TextButton sender = (TextButton) arg0.getTarget();
            if (sender == backButton) {
                controller.notifyBackButtonPressed();
            } else if (sender == addButton) {
                controller.notifyAddButtonPressed();
            } else if (sender == editButton) {
                controller.notifyEditButtonPressed();
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
