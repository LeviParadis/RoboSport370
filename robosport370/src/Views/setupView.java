package Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import Controllers.setupController;

public class setupView extends ScreenAdapter {
	private SpriteBatch batch;
    
    private Texture menuArtTexture;
    private Sprite menuArtSprite;
    
    private Texture menuCreditTexture;
    private Sprite menuCreditSprite;
    
    private Texture menuOptionsTexture;
    private Sprite menuOptionsTournament;
    private Sprite menuOptionsSimulation;
    private Sprite menuOptionsExit;
    
    private setupMenu setupMenu;
    
    private Music introMusic;
    private Sound beep;
    
    private Integer SCREEN_WIDTH;
    private Integer SCREEN_HEIGHT;
    
    private static Integer menOpSrcX = 45;
    private static Integer menOpSrcY = 30;
    private static Integer menOpWidth = 170;
    private static Integer menOpHeight = 30;
    
    final setupController controller;
    
    public setupView(final setupController cont) {
    	controller = cont;
    	
        batch = new SpriteBatch();
        
        menuArtTexture = new Texture("assets/mainMenu/menu_title.png");
        menuArtSprite = new Sprite(menuArtTexture, 280, 126);
        
        menuCreditTexture = new Texture("assets/mainMenu/menu_credits.png");
        menuCreditSprite = new Sprite(menuCreditTexture, 295, 75);
        
        menuOptionsTexture = new Texture("assets/mainMenu/menu_options.png");
        menuOptionsTournament = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY*0, menOpWidth, menOpHeight);
        menuOptionsSimulation = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY*2, menOpWidth, menOpHeight);
        menuOptionsExit = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY*4, menOpWidth, menOpHeight);
        
        setupMenu = new setupMenu();
        
        introMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/sound/Bit Quest.mp3"));
        introMusic.setLooping(true);
        introMusic.setVolume(0.6f);
        introMusic.play();
        beep = Gdx.audio.newSound(Gdx.files.internal("assets/sound/Beep.mp3"));
        
        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();
    }

    @Override
    public void dispose() {
        batch.dispose();
        menuArtTexture.dispose();
        menuCreditTexture.dispose();
        menuOptionsTexture.dispose();
        introMusic.dispose();
        beep.dispose();
    }

    @Override
    public void render(float delta) {   
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        
        if(Gdx.input.isKeyJustPressed(Keys.DOWN)) {
        	setupMenu.down();
        	beep.setVolume(beep.play(),0.3f);
        }
        
        if(Gdx.input.isKeyJustPressed(Keys.UP)) {
        	setupMenu.up();
        	beep.setVolume(beep.play(),0.3f);
        }
        
        // Draw the title screen at the middle of the screen, centered
        menuArtSprite.setPosition(SCREEN_WIDTH*0.5f-menuArtSprite.getWidth()/2, SCREEN_HEIGHT*0.5f);
        menuArtSprite.draw(batch);
        
        menuCreditSprite.setPosition(SCREEN_WIDTH-menuCreditSprite.getWidth(), 0f);
        menuCreditSprite.draw(batch);
        
        renderMenu();
        
        batch.end();
    }
    
    private void renderMenu() {
        menuOptionsTournament = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY*0, menOpWidth, menOpHeight);
        menuOptionsSimulation = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY*2, menOpWidth, menOpHeight);
        menuOptionsExit = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY*4, menOpWidth, menOpHeight);
    	
    	menuOptionsTournament.setPosition(SCREEN_WIDTH*0.5f-menuOptionsTournament.getWidth()/2, SCREEN_HEIGHT*0.45f-30*1f);
    	menuOptionsSimulation.setPosition(SCREEN_WIDTH*0.5f-menuOptionsTournament.getWidth()/2, SCREEN_HEIGHT*0.45f-30*2f);
    	menuOptionsExit.setPosition(SCREEN_WIDTH*0.5f-menuOptionsTournament.getWidth()/2, SCREEN_HEIGHT*0.45f-30*3f);
    	
    	switch(setupMenu.getCurrentOption()) {
    		case "New Tournament":
    			menuOptionsTournament = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY*1, menOpWidth, menOpHeight);
    			menuOptionsTournament.setPosition(SCREEN_WIDTH*0.5f-menuOptionsTournament.getWidth()*0.5f, SCREEN_HEIGHT*0.45f-30*1f);
    			break;
    		case "New Simulation":
    			menuOptionsSimulation = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY*3, menOpWidth, menOpHeight);
    			menuOptionsSimulation.setPosition(SCREEN_WIDTH*0.5f-menuOptionsTournament.getWidth()*0.5f, SCREEN_HEIGHT*0.45f-30*2f);
    			break;
    		case "Exit":
    			menuOptionsExit = new Sprite(menuOptionsTexture, menOpSrcX, menOpSrcY*5, menOpWidth, menOpHeight);
    			menuOptionsExit.setPosition(SCREEN_WIDTH*0.5f-menuOptionsTournament.getWidth()*0.5f, SCREEN_HEIGHT*0.45f-30*3f);
    			break;
    	}
    	
    	menuOptionsTournament.draw(batch);
    	menuOptionsSimulation.draw(batch);
    	menuOptionsExit.draw(batch);
    }
	
	public static void main(String[] args) {

	}
}
