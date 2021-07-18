package net.spirangle.minerva.gdx.sprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.spirangle.minerva.Rectangle;

/**
 * The Sprite can draw an image or animation.
 */
public interface Sprite {
    Clip getClip();

    boolean isTouched(int x,int y,int tx,int ty);

    boolean isVisible(Rectangle viewport,int x,int y);

    boolean draw(SpriteBatch batch,Rectangle viewport,int x,int y);
};

