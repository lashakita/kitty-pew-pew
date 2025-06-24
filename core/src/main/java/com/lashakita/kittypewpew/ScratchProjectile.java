package com.lashakita.kittypewpew;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class ScratchProjectile {
    private float x, y;
    private final float speed = 400f;
    private Texture texture;
    private Animation<TextureRegion> animation;
    private float stateTime;
    private float width, height;


    public ScratchProjectile(float startX, float startY) {
        texture = new Texture(Gdx.files.internal("effects/All_Fire_Bullet_Pixel_16x16_00.png"));

        TextureRegion[][] textureRegions = TextureRegion.split(texture, 16, 16);
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(texture, (13 + i) * 16, 10 * 16, 16, 16);
        }

        animation = new Animation<>(0.1f, frames);
        stateTime = 0f;

        this.x = startX;
        this.y = startY;
        this.width = 16;
        this.height = 16;
    }

    public void update(float delta) {
        stateTime += delta;
        y += speed * delta;
    }

    public void draw(SpriteBatch spriteBatch) {
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        spriteBatch.draw(currentFrame, x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isOffScreen() {
        return y > Gdx.graphics.getHeight();
    }

    public void dispose() {
        texture.dispose();
    }
}
