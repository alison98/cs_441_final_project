package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.beans.Visibility;

public class Hud {
    private Stage stage;
    private ScreenViewport stageViewport;
    private ImageButton selectButton;
    private Label textBox;
    private ImageButton upButton;
    private ImageButton downButton;
    private ImageButton leftButton;
    private ImageButton rightButton;
    private ImageButton inventoryButton;
    private Queue<String> textQueue;


    public Hud(final SpriteBatch spriteBatch, final GameScreen gameScreen) {
        stageViewport = new ScreenViewport();
        stage = new Stage(stageViewport, spriteBatch);
        textQueue = new Queue<String>();

        textBox = new Label("Hello", new Skin(Gdx.files.internal("skin/plain-james-ui.json"), new TextureAtlas(Gdx.files.internal("skin/plain-james-ui.atlas"))));
        textBox.setWidth(1200);
        textBox.setHeight(200);
        textBox.setFontScale(2f);
        textBox.setAlignment(Align.center);
        textBox.setPosition(Gdx.graphics.getWidth()/2 - textBox.getWidth()/2, 100);
        textBox.getStyle().background = new Image(new Texture(Gdx.files.internal("textbox.png"))).getDrawable();
        textBox.setVisible(false);

        selectButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("select-button.png"))));
        selectButton.setPosition(1800, 80);
        selectButton.setVisible(false);
        selectButton.setTouchable(Touchable.disabled);
        selectButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(inText()){
                    if(textQueue.isEmpty()){
                        Interactable interactable = gameScreen.getRoom().getInteractablesTouched(gameScreen.getPlayer());
                        if(interactable != null){
                            if(interactable instanceof TutorialPrinter){
                                ((TutorialPrinter)interactable).fight(gameScreen);
                            } else if(interactable instanceof Boss){
                                if(!((Boss)interactable).isHuman()){
                                    ((Boss)interactable).fight(gameScreen);
                                }
                            } else if(interactable instanceof Item){
                                ((Item)interactable).pickUp(gameScreen,gameScreen.getPlayer());
                            }else if(interactable instanceof Shop){
                                ((Shop)interactable).openShop(gameScreen);
                            }
                        }
                        upButton.setTouchable(Touchable.enabled);
                        downButton.setTouchable(Touchable.enabled);
                        leftButton.setTouchable(Touchable.enabled);
                        rightButton.setTouchable(Touchable.enabled);
                        textBox.setVisible(false);
                    } else {
                        textBox.setText(textQueue.removeFirst());
                    }
                    return;
                }
                if(gameScreen.getRoom().getDoorTouched(gameScreen.getPlayer())){
                    gameScreen.roomChange();
                }
                Interactable interactable = gameScreen.getRoom().getInteractablesTouched(gameScreen.getPlayer());
                if(interactable != null){
                    if(interactable instanceof TutorialPrinter){
                        ((TutorialPrinter)interactable).interact(gameScreen);
                    } else if(interactable instanceof Boss){
                        ((Boss)interactable).interact(gameScreen);
                    }else if(interactable instanceof Item){
                        ((Item)interactable).interact(gameScreen);
                    }else if(interactable instanceof Shop){
                        ((Shop)interactable).interact(gameScreen);
                    }
                }
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        Table table = new Table();
        upButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("up-button.png"))));
        upButton.padLeft(160);
        upButton.setTouchable(Touchable.enabled);
        upButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                gameScreen.getPlayer().setSpeedY(0);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                gameScreen.getPlayer().setSpeedY(20);
                return true;
            }
        });

        downButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("down-button.png"))));
        downButton.padLeft(160);
        downButton.setTouchable(Touchable.enabled);
        downButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                gameScreen.getPlayer().setSpeedY(0);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                gameScreen.getPlayer().setSpeedY(-20);
                return true;
            }
        });

        leftButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("left-button.png"))));
        leftButton.setTouchable(Touchable.enabled);
        leftButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                gameScreen.getPlayer().setSpeedX(0);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                gameScreen.getPlayer().setSpeedX(-20);
                return true;
            }
        });

        rightButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("right-button.png"))));
        rightButton.setTouchable(Touchable.enabled);
        rightButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                gameScreen.getPlayer().setSpeedX(0);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                gameScreen.getPlayer().setSpeedX(20);
                return true;
            }
        });

        inventoryButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("right-button.png"))));
        inventoryButton.setTouchable(Touchable.enabled);
        inventoryButton.setPosition(100, 1000);
        inventoryButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                gameScreen.getGame().setScreen(new Inventory());
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }
        });


        table.add(upButton);
        table.row();
        table.add(leftButton);
        table.add(rightButton);
        table.row();
        table.add(downButton);
        table.setPosition(200, 200);
        stage.addActor(table);
        stage.addActor(selectButton);
        stage.addActor(textBox);
        stage.addActor(inventoryButton);
    }

    public void setInteractable(){
        selectButton.setVisible(true);
        selectButton.setTouchable(Touchable.enabled);
    }

    public void setUninteractable(){
        selectButton.setVisible(false);
        selectButton.setTouchable(Touchable.disabled);
    }

    public void setText(String... messages){
        for(String message : messages){
            textQueue.addLast(message);
        }
        textBox.setText(textQueue.removeFirst());
        textBox.setVisible(true);
        setInteractable();
        upButton.setTouchable(Touchable.disabled);
        downButton.setTouchable(Touchable.disabled);
        leftButton.setTouchable(Touchable.disabled);
        rightButton.setTouchable(Touchable.disabled);
    }

    public boolean inText(){
        return textBox.isVisible();
    }

    public Stage getStage(){
        return stage;
    }

    public void dispose(){
        stage.dispose();
    }
}
