package Views;

import java.util.HashMap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

import Controllers.GameController;
import Controllers.setupController;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * A GUI view for the main menu
 * @author Corey
 *
 */
public class mapView extends ScreenAdapter {
	// The controller which called the view
	private final Game controller;
	
	// To store the screen dimensions
	private Integer SCREEN_WIDTH;
	private Integer SCREEN_HEIGHT;
	
	// For the various sprites
	private TextureAtlas atlas;
    private Array<Sprite> tiles;
    private Array<Sprite> robots;
    
	private OrthographicCamera cam;
    static final int WORLD_WIDTH = 1280;
    static final int WORLD_HEIGHT = 800;
    private float rotationSpeed;
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();
	
	// For rendering sprites
    private SpriteBatch batch;

    
    /**
     * Creates a mapView screen
     * @param controller the controller creating this screen
     */
    public mapView(final Game controller) {
    	this.controller = controller;
    	
    	SCREEN_WIDTH = Gdx.graphics.getWidth();
    	SCREEN_HEIGHT = Gdx.graphics.getHeight();
    	
    	// Getting our texture atlas of all the sprites
    	atlas = new TextureAtlas(Gdx.files.internal("assets/game_sprites/gamesprites.pack"));
    	
    	// Setting up the tiles
    	tiles = atlas.createSprites("tile");
    	
    	// Setting up the robots
    	robots = atlas.createSprites("robot");
    	
    	// Setting up the camera
    	rotationSpeed = 0.5f;
    	cam = new OrthographicCamera(500, 500 * (h/w));
    	cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight/2f, 0);
    	cam.update();

    	batch = new SpriteBatch();
    }
    
    public void render(float delta) {   

        Gdx.gl.glClearColor(1, 1, 1, 1);
        
        cam.translate(0.005f/(float) delta, 0.005f/(float) delta);
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        
       
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        renderTiles();
        batch.end();
    }
    
    public void renderTiles() {
    	int index = 0;
    	int width = 21;
    	int height = 11;
    	
    	int sizeY = 60;
    	int sizeX = 50;
    	
    	int xPos = 0;
    	int yPos = 800;
    	
    	// TODO remove the hard coded numbers
    	// Call the controller instead, getMapSize()
    	for(int i = 0; i < width; i++) {
    		for(int j = 0; j < height; j++) {
    			if(index >= 4) {
    				index = 0;
    			}
    			tiles.get(index).setPosition(xPos, yPos);
    			tiles.get(index).draw(batch);
    			index++;
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
    
    @Override
    public void dispose() {
        batch.dispose();
    }
}
