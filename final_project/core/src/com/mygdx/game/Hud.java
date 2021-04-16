package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Hud {
    private Stage stage;
    private ScreenViewport stageViewport;

    public Hud(final SpriteBatch spriteBatch, final GameScreen gameScreen) {
        stageViewport = new ScreenViewport();
        stage = new Stage(stageViewport, spriteBatch);
        Table table = new Table();
        Texture buttonTexture = new Texture(Gdx.files.internal("badlogic.jpg"));
        ImageButton leftButton = new ImageButton(new TextureRegionDrawable(buttonTexture));
        leftButton.setTouchable(Touchable.enabled);
        leftButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                gameScreen.player.moveBy(-20, 0);
                return true;
            }
        });
        ImageButton rightButton = new ImageButton(new TextureRegionDrawable(buttonTexture));
        rightButton.setTouchable(Touchable.enabled);
        rightButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                gameScreen.player.moveBy(20, 0);
                return true;
            }
        });
        table.add(leftButton);
        table.row();
        table.add(rightButton);
        table.row();
        table.setPosition(500, 500);
        stage.addActor(table);
    }

    public Stage getStage(){
        return stage;
    }

    public void dispose(){
        stage.dispose();
    }
}
