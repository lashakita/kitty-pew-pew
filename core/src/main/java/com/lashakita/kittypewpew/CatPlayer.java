package com.lashakita.kittypewpew;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class CatPlayer {
    private Texture texture;
    private float x, y;
    private float width, height;
    private final float speed = 300;
    private final float scale = 0.15f;

    public CatPlayer() {
        texture = new Texture(Gdx.files.internal("cats/sumail.png"));
        x = 300;
        y = 50;
        width = texture.getWidth() * scale;
        height = texture.getHeight() * scale;
    }

    public void update(float delta) {
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) x -= speed * delta;
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) x += speed * delta;
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP)) y += speed * delta;
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN)) y -= speed * delta;

        x = MathUtils.clamp(x, 0, Gdx.graphics.getWidth() - width);
        y = MathUtils.clamp(y, 0, Gdx.graphics.getHeight() / 2f);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public void dispose() {
        texture.dispose();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
