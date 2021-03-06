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
import Controllers.AddRobotController;
import Constants.UIConstants;
public class AddRobotView extends ScreenAdapter implements EventListener {

    protected final Stage stage;
    private static final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(UIConstants.BLUE_ATLAS));
    private static final TextureAtlas commonAtlas = new TextureAtlas(Gdx.files.internal(UIConstants.COMMON_ATLAS));

    protected CheckBox power1;
    protected CheckBox power2;
    protected CheckBox power3;
    protected CheckBox health1;
    protected CheckBox health2;
    protected CheckBox health3;
    private TextButton confirmButton;
    private TextButton backButton;
    protected TextField nameField;
    protected TextField teamField;
    protected TextArea forthField;
    protected Label titleLabel;
    protected Table table;
    protected Label nameLabel;
    protected Label teamLabel;

    protected AddRobotController controller;

    /**
     * Set up the controller
     * 
     * @param controller
     *            the controller we are setting up
     */
    public AddRobotView(AddRobotController controller) {
        this.controller = controller;

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        // set up the stage
        stage = new Stage();

        BitmapFont font = new BitmapFont();// (Gdx.files.internal("assets/MoonFlower.fnt"),Gdx.files.internal("assets/MoonFlower.png"),false);
        Skin skin = new Skin();
        skin.addRegions(atlas);

        Skin selectionSkin = new Skin();
        selectionSkin.addRegions(commonAtlas);

        // set up buttons
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable(UIConstants.BUTTON_UP);
        textButtonStyle.down = skin.getDrawable(UIConstants.BUTTON_DOWN);

        backButton = new TextButton(UIConstants.BUTTON_TEXT_CANCEL, textButtonStyle);
        backButton.setPosition(100, 50);
        backButton.setSize(500, 50);
        backButton.addListener(this);

        confirmButton = new TextButton(UIConstants.BUTTON_TEXT_CONFIRM, textButtonStyle);
        confirmButton.setPosition(width - 600, 50);
        confirmButton.setSize(500, 50);
        confirmButton.addListener(this);

        // set up text fields
        TextFieldStyle textFieldStyle = new TextFieldStyle();
        textFieldStyle.background = skin.getDrawable(UIConstants.TEXTBOX);
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.BLACK;
        textFieldStyle.cursor = skin.getDrawable(UIConstants.TEXTBOX_CURSOR);
        textFieldStyle.selection = selectionSkin.getDrawable(UIConstants.TEXTBOX_SELECTED);

        nameField = new TextField("R2D2", textFieldStyle);
        teamField = new TextField("C3", textFieldStyle);
        forthField = new TextArea("", textFieldStyle);
        forthField.setWidth(600);

        // set up labels
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.fontColor = Color.BLACK;
        labelStyle.font = font;

        nameLabel = new Label("Name: ", labelStyle);
        teamLabel = new Label("Team: ", labelStyle);
        Label forthLabel = new Label("Forth Code: ", labelStyle);
        Label pointLabel1 = new Label("Point 1: ", labelStyle);
        Label pointLabel2 = new Label("Point 2: ", labelStyle);
        Label pointLabel3 = new Label("Point 3: ", labelStyle);

        // set up checkboxes
        CheckBoxStyle checkboxStyle = new CheckBoxStyle();
        checkboxStyle.checkboxOn = skin.getDrawable("checkbox_on");
        checkboxStyle.checkboxOff = skin.getDrawable("checkbox_off");
        checkboxStyle.fontColor = Color.BLACK;
        checkboxStyle.font = font;

        power1 = new CheckBox("Power", checkboxStyle);
        power2 = new CheckBox("Power", checkboxStyle);
        power3 = new CheckBox("Power", checkboxStyle);
        health1 = new CheckBox("Health", checkboxStyle);
        health2 = new CheckBox("Health", checkboxStyle);
        health3 = new CheckBox("Health", checkboxStyle);

        ChangeListener invertNeighbour = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                CheckBox thisBox = (CheckBox) actor;
                CheckBox otherBox = (CheckBox) actor.getUserObject();
                otherBox.setChecked(!thisBox.isChecked());
            }
        };

        power1.addListener(invertNeighbour);
        power1.setUserObject(health1);
        health1.addListener(invertNeighbour);
        health1.setUserObject(power1);
        power2.addListener(invertNeighbour);
        power2.setUserObject(health2);
        health2.addListener(invertNeighbour);
        health2.setUserObject(power2);
        power3.addListener(invertNeighbour);
        power3.setUserObject(health3);
        health3.addListener(invertNeighbour);
        health3.setUserObject(power3);

        // set up the table
        table = new Table();
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

        // set up the title
        titleLabel = new Label("Create a Robot", labelStyle);
        titleLabel.setPosition(width / 2 - 100, height - 50);
        titleLabel.setFontScale(2);

        // add actors to stage
        stage.addActor(titleLabel);
        stage.addActor(table);
        stage.addActor(backButton);
        stage.addActor(confirmButton);

    }

    /**
     * render the scene
     */
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
            if (sender == this.confirmButton) {
                String name = this.nameField.getText();
                String team = this.teamField.getText();
                String forth = this.forthField.getText();

                int movesLeft = 3;
                int firePower = 1;
                int health = 1;

                if (this.power1.isChecked()) {
                    firePower++;
                    movesLeft--;
                } else {
                    health++;
                }
                if (this.power3.isChecked()) {
                    firePower++;
                    movesLeft--;
                } else {
                    health++;
                }
                if (this.power3.isChecked()) {
                    firePower++;
                    movesLeft--;
                } else {
                    health++;
                }

                this.controller.notifyDone(name, team, forth, movesLeft, firePower, health);
                return true;
            } else if (sender == this.backButton) {
                this.controller.notifyCancel();
                return true;
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
