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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Hud {
    private Stage stage;
    private ScreenViewport stageViewport;

    public Hud(final SpriteBatch spriteBatch, final GameScreen gameScreen) {
        stageViewport = new ScreenViewport();
        stage = new Stage(stageViewport, spriteBatch);
        Table table = new Table();
        ImageButton upButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("up-button.png"))));
        upButton.padLeft(53);
        upButton.setTouchable(Touchable.enabled);
        upButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                gameScreen.player.moveBy(0, 20);
                gameScreen.player.positionChanged();
                System.out.println("Touched");
                return true;
            }
        });
        ImageButton downButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("down-arrow.png"))));
        downButton.padLeft(53);
        downButton.setTouchable(Touchable.enabled);
        downButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                gameScreen.player.moveBy(0, -20);
                gameScreen.player.positionChanged();
                System.out.println("Touched");
                return true;
            }
        });
        ImageButton leftButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("left-button.png"))));
        leftButton.setTouchable(Touchable.enabled);
        leftButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                gameScreen.player.moveBy(-20, 0);
                gameScreen.player.positionChanged();
                System.out.println("Touched");
                return true;
            }
        });
        ImageButton rightButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("right-button.png"))));
        rightButton.setTouchable(Touchable.enabled);
        rightButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                gameScreen.player.moveBy(20, 0);
                System.out.println("Touched");
                return true;
            }
        });
        table.add(upButton);
        table.row();
        table.add(leftButton);
        table.add(rightButton);
        table.row();
        table.add(downButton);
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
