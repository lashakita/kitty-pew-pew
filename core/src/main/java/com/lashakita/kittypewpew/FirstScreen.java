package com.lashakita.kittypewpew;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;


public class FirstScreen implements Screen {

    private final KittyPewPew game;
    private Texture background;
    private SpriteBatch batch;
    private CatPlayer catPlayer;
    private Array<FurnitureObjects> furnitureObjects;
    private Array<ScratchProjectile> scratches;
    private ShapeRenderer shapeRenderer;
    private float timeSinceLastShot = 0f;
    private final float shootCooldown = 0.25f;

    private BitmapFont font;
    private BitmapFont smallFont;
    private GlyphLayout layout;
    private boolean isGameOver = false;

    public FirstScreen(KittyPewPew game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        catPlayer = new CatPlayer();
        shapeRenderer = new ShapeRenderer();
        furnitureObjects = new Array<>();
        scratches = new Array<>();
        background = new Texture(Gdx.files.internal("backgrounds/background.png"));

        // Fonts setup
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/upheavtt.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36;
        parameter.color = Color.WHITE;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        font = generator.generateFont(parameter);

        // Small font
        FreeTypeFontGenerator.FreeTypeFontParameter smallParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        smallParam.size = 20;
        smallParam.color = Color.WHITE;
        smallParam.borderColor = Color.BLACK;
        smallParam.borderWidth = 2;
        smallFont = generator.generateFont(smallParam);
        generator.dispose();


        layout = new GlyphLayout();

        // Furniture textures
        Texture plantTexture = new Texture(Gdx.files.internal("objects/plant.png"));
        Texture bookTexture = new Texture(Gdx.files.internal("objects/book.png"));
        Texture canTexture = new Texture(Gdx.files.internal("objects/light-bulb.png"));
        Texture coffeeTexture = new Texture(Gdx.files.internal("objects/cup-of-coffee.png"));

        Texture[] furnitureTextures = { plantTexture, bookTexture, canTexture, coffeeTexture };

        int objectsPerRow = 6;
        int spacingX = 80;
        int spacingY = 40;
        int baseY = Gdx.graphics.getHeight() - 80;

        for (int i = 0; i < furnitureTextures.length; i++) {
            for (int j = 0; j < objectsPerRow; j++) {
                float x = 100 + j * spacingX;
                float y = baseY - i * spacingY;
                furnitureObjects.add(new FurnitureObjects(x, y, furnitureTextures[i], MathUtils.randomSign() * 50));
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.9f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!isGameOver && !furnitureObjects.isEmpty()) {
            catPlayer.update(delta);
        }

        for (FurnitureObjects f : furnitureObjects) f.update(delta);
        for (ScratchProjectile s : scratches) s.update(delta);

        timeSinceLastShot += delta;
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && timeSinceLastShot >= shootCooldown && !isGameOver) {
            float scratchX = catPlayer.getX() + catPlayer.getWidth() / 2f;
            float scratchY = catPlayer.getY() + 20;
            scratches.add(new ScratchProjectile(scratchX, scratchY));
            timeSinceLastShot = 0f;
        }

        for (int i = scratches.size - 1; i >= 0; i--) {
            ScratchProjectile s = scratches.get(i);
            for (int j = furnitureObjects.size - 1; j >= 0; j--) {
                FurnitureObjects f = furnitureObjects.get(j);
                if (s.getBounds().overlaps(f.getBounds())) {
                    scratches.removeIndex(i);
                    furnitureObjects.removeIndex(j);
                    break;
                }
            }
        }

        for (int i = scratches.size - 1; i >= 0; i--) {
            if (scratches.get(i).isOffScreen()) {
                scratches.removeIndex(i);
            }
        }

        if (!isGameOver) {
            Rectangle catBounds = catPlayer.getBounds();
            for (FurnitureObjects f : furnitureObjects) {
                if (f.getBounds().overlaps(catBounds)) {
                    isGameOver = true;
                    break;
                }
            }
        }

        if ((isGameOver || furnitureObjects.isEmpty()) && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new FirstScreen(game));
            dispose();
            return;
        }

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (!isGameOver && !furnitureObjects.isEmpty()) {
            for (FurnitureObjects f : furnitureObjects) f.draw(batch);
            for (ScratchProjectile s : scratches) s.draw(batch);
            catPlayer.draw(batch);
        }

        if (!furnitureObjects.isEmpty() && !isGameOver) {
            font.getData().setScale(0.5f);
            layout.setText(font, "Press SPACE to shoot!");
            font.draw(batch, layout, (Gdx.graphics.getWidth() - layout.width) / 2, 40);
            font.getData().setScale(1f);;
        }

        if (furnitureObjects.isEmpty()) {
            String winText = "ALL FURNITURE DESTROYED!";
            layout.setText(font, winText);
            font.draw(batch, layout, (Gdx.graphics.getWidth() - layout.width) / 2, Gdx.graphics.getHeight() / 2 + 20);

            String restartText = "PRESS ESC TO RESTART";
            layout.setText(font, restartText);
            font.draw(batch, layout, (Gdx.graphics.getWidth() - layout.width) / 2, Gdx.graphics.getHeight() / 2 - 20);
        }

        if (isGameOver) {
            String gameOverText = "GAME OVER! KITTY GOT HIT!";
            layout.setText(font, gameOverText);
            font.draw(batch, layout, (Gdx.graphics.getWidth() - layout.width) / 2, Gdx.graphics.getHeight() / 2 + 20);

            String restartText = "PRESS ESC TO RESTART";
            layout.setText(font, restartText);
            font.draw(batch, layout, (Gdx.graphics.getWidth() - layout.width) / 2, Gdx.graphics.getHeight() / 2 - 20);
        }

        batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        catPlayer.dispose();
        shapeRenderer.dispose();
        font.dispose();
        smallFont.dispose();
        background.dispose();
    }
}
