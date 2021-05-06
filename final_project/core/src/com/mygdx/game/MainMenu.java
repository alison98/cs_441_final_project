package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenu implements Screen {
    private Sprite background;
    private Game game;
    private Stage stage;
    private SpriteBatch spriteBatch;
    private Room room;

    public MainMenu(Game GameIn){
        this.stage = new Stage(new ScreenViewport());
        spriteBatch = new SpriteBatch();
        spriteBatch.begin();
        game = GameIn;
        room = new Room();

        Texture buttonTexture = new Texture("textbox.png");
        Skin skin = new Skin();
        skin.add("buttonTexture", buttonTexture);
        ImageTextButton.ImageTextButtonStyle playButtonStyle = new ImageTextButton.ImageTextButtonStyle();
        playButtonStyle.font = new BitmapFont();
        playButtonStyle.up = new TextureRegionDrawable(buttonTexture);
        playButtonStyle.down = new TextureRegionDrawable(buttonTexture);
        ImageTextButton playButton = new ImageTextButton("New Game", playButtonStyle);
        playButton.setWidth(400f);
        playButton.setHeight(100f);
        playButton.getLabel().setFontScale(2f);
        playButton.setPosition(Gdx.graphics.getWidth()/2 - playButton.getWidth()/2, Gdx.graphics.getHeight()/2 - playButton.getHeight() * 2);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                dispose();
                game.setScreen(new GameScreen(game));
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }
        });

        stage.addActor(room);
        stage.addActor(playButton);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
