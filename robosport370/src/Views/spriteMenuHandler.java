package Views;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * A class which handles and keeps track of a sprite menu for a view
 * 
 * @author Corey
 *
 */
public class spriteMenuHandler {
    private Sound beep;

    private Integer currentSpriteIndex;
    private Integer maxSprites;
    private HashMap<Integer, Sprite> spriteMap;

    /**
     * 
     * @param s
     *            the first menu option
     */
    public spriteMenuHandler(Sprite s) {
        beep = Gdx.audio.newSound(Gdx.files.internal("assets/sound/Beep.mp3"));

        currentSpriteIndex = 1;
        maxSprites = 1;
        spriteMap = new HashMap<Integer, Sprite>();
        spriteMap.put(currentSpriteIndex, s);
        currentSprite().scroll(0, currentSprite().getHeight() / (float) currentSprite().getTexture().getHeight());
    }

    /**
     * Adds sprite which track the different options Your sprite texture must be
     * kept vertically
     * 
     * @param s
     *            the *NEXT* menu option sprite in the menu
     */
    public void addSprite(Sprite s) {
        maxSprites++;
        spriteMap.put(maxSprites, s);
    }

    /**
     * Move up in the menu
     */
    public void up() {
        if (getIndex() > 1) {
            currentSprite().scroll(0, -currentSprite().getHeight() / (float) currentSprite().getTexture().getHeight());
            currentSpriteIndex--;
            currentSprite().scroll(0, currentSprite().getHeight() / (float) currentSprite().getTexture().getHeight());
        }
        beep();
    }

    /**
     * Move down in the menu
     */
    public void down() {
        if (currentSpriteIndex < maxSprites) {
            currentSprite().scroll(0, -currentSprite().getHeight() / (float) currentSprite().getTexture().getHeight());
            currentSpriteIndex++;
            currentSprite().scroll(0, currentSprite().getHeight() / (float) currentSprite().getTexture().getHeight());
        }
        beep();
    }

    /**
     * Helper function for the menu which makes a beep sound
     */
    public void beep() {
        beep.setVolume(beep.play(), 0.3f);
    }

    /**
     * 
     * @return the index of the current selected option
     */
    public Integer getIndex() {
        return currentSpriteIndex;
    }

    /**
     * 
     * @return the sprite currently selected
     */
    public Sprite currentSprite() {
        return spriteMap.get(currentSpriteIndex);
    }
}
