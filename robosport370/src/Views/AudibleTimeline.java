package Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class AudibleTimeline {
	private Timeline timeline;
	private static Sound laser = Gdx.audio.newSound(Gdx.files.internal("assets/sound/laser.wav"));
	private static Sound boom = Gdx.audio.newSound(Gdx.files.internal("assets/sound/explosion.wav"));
	private Sprite projectile;
	private Sprite source;
	private boolean explosion;
	
	public AudibleTimeline() {
		explosion = false;
	}
	
	public void setExplosionOn() {
		explosion = true;
	}
	
	public void setExplosionOff() {
		explosion = false;
	}
	
	public void setSource(Sprite source) {
		this.source = source;
	}
	
	public void setTimeline(Timeline timeline) {
		this.timeline = timeline;
	}
	
	public Timeline getTimeline() {
		return timeline;
	}
	
	public void setProjectile(Sprite sprite) {
		this.projectile = sprite;
	}
	
	public void startTimeline(TweenManager tweenManager) {
		if(projectile != null) {
			projectile.setPosition(source.getX(), source.getY());
			timeline.push(Tween.to(projectile, SpriteAccessor.POSITION_XY, 0f).target(5000, 5000));
			laser.play(0.1f);
		}
		if(explosion) {
			
			boom.play(0.1f);
		}
		timeline.start(tweenManager);
	}
}
