package Views;

import com.badlogic.gdx.graphics.g2d.Sprite;

import aurelienribon.tweenengine.TweenAccessor;

public class SpriteAccessor implements TweenAccessor<Sprite> {
    public static final int FLOAT = 1;
    public static final int POSITION_XY = 3;

    @Override
    public int getValues(Sprite target, int tweenType, float[] returnValues) {
        switch (tweenType) {
        case FLOAT:

        case POSITION_XY:
            returnValues[0] = target.getX();
            returnValues[1] = target.getY();
            return 2;
        default:
            assert false;
            return -1;
        }
    }

    @Override
    public void setValues(Sprite target, int tweenType, float[] newValues) {
        switch (tweenType) {
        case POSITION_XY:
            target.setX(newValues[0]);
            target.setY(newValues[1]);
            break;
        default:
            assert false;
            break;
        }
    }

    public int getValues(Float target, int tweenType, float[] returnValues) {
        returnValues[0] = target.floatValue();
        return 1;
    }

    public void setValues(Float target, int tweenType, float[] newValues) {
        target = newValues[0];
    }
}
