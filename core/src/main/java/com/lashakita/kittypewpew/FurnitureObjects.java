package com.lashakita.kittypewpew;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class FurnitureObjects {
    private float x, y;
    private Texture texture;
    private float baseSpeed;
    private float width, height;
    private float timeElapsed = 0f;
    private final float speedIncrement = 2f;
    private final float descendingSpeed = 5f;

    public FurnitureObjects(float x, float y, Texture texture, float initialSpeed) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        this.baseSpeed = initialSpeed;
    }

    public void update(float delta) {
        timeElapsed += delta;
        float currentSpeed = baseSpeed + speedIncrement * timeElapsed;

        x += currentSpeed * delta;

        if (x < 0) {
            x = 0;
            baseSpeed = -Math.abs(currentSpeed);
        } else if (x + width > Gdx.graphics.getWidth()) {
            x = Gdx.graphics.getWidth() - width;
            baseSpeed = Math.abs(currentSpeed) * -1;
        }

        y -= descendingSpeed * delta;

        if (y < 100) {
            y = 100;
        }

    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void dispose() {
        texture.dispose();
    }

}
