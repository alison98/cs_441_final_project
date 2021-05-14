package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
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

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont(Gdx.files.internal("font/font.fnt"));
        labelStyle.fontColor = Color.BLACK;
        labelStyle.background = new TextureRegionDrawable(new Texture("textbox.png"));
        Label title = new Label("(S)layoff Season", labelStyle);
        title.setAlignment(Align.center);
        title.setFontScale(2.5f);
        title.setWidth(1100f);
        title.setPosition(Gdx.graphics.getWidth()/2 - title.getWidth()/2, 700);

        Texture buttonTexture = new Texture("textbox.png");
        Skin skin = new Skin();
        skin.add("buttonTexture", buttonTexture);
        ImageTextButton.ImageTextButtonStyle playButtonStyle = new ImageTextButton.ImageTextButtonStyle();
        playButtonStyle.font = new BitmapFont(Gdx.files.internal("font/font.fnt"));
        playButtonStyle.fontColor = Color.BLACK;
        playButtonStyle.up = new TextureRegionDrawable(buttonTexture);
        playButtonStyle.down = new TextureRegionDrawable(buttonTexture);
        ImageTextButton playButton = new ImageTextButton("New Game", playButtonStyle);
        playButton.setWidth(400f);
        playButton.setHeight(100f);
        playButton.getLabel().setFontScale(1f);
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

        ImageTextButton howToPlayButton = new ImageTextButton("How To Play", playButtonStyle);
        howToPlayButton.setWidth(400f);
        howToPlayButton.setHeight(100f);
        howToPlayButton.getLabel().setFontScale(1f);
        howToPlayButton.setPosition(Gdx.graphics.getWidth()/2 - playButton.getWidth()/2, Gdx.graphics.getHeight()/2 - playButton.getHeight() * 4);
        howToPlayButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                dispose();
                game.setScreen(new HowToPlay(game));
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }
        });

        stage.addActor(room);
        stage.addActor(title);
        stage.addActor(playButton);
        stage.addActor(howToPlayButton);
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
