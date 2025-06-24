package com.lashakita.kittypewpew;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class KittyPewPew extends Game {
    @Override
    public void create() {
        setScreen(new MainMenuScreen(this));
    }
}
