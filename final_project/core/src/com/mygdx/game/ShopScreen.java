package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.List;

public class ShopScreen implements Screen {
    private Game game;
    private Stage stage;
    private ShopHud shopHud;
    private SpriteBatch spriteBatch;
    private int floor;

    public ShopScreen(Game gameIn, SpriteBatch spriteBatchIn, GameScreen gameScreen, int floorIn) {
        game = gameIn;
        spriteBatch = spriteBatchIn;
        floor = floorIn;
        shopHud = new ShopHud(spriteBatch,gameScreen,floor);
        stage = gameScreen.getStage();
    }

    @Override
    public void show(){
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(shopHud.getStage());
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        spriteBatch.setProjectionMatrix(shopHud.getStage().getCamera().combined);
        shopHud.getStage().draw();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    public void dispose() {
        stage.dispose();
        shopHud.dispose();
    }
}
