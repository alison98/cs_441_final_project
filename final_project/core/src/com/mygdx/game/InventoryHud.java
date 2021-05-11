package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class InventoryHud {

    private Stage stage;
    private ScreenViewport stageViewport;
    private Player player;
    private GameScreen gameScreen;
    private int floor;

    public InventoryHud(final Player playerIn, final SpriteBatch spriteBatch, final GameScreen gameScreenIn, final int floorIn){
        player = playerIn;
        gameScreen = gameScreenIn;
        stageViewport = new ScreenViewport();
        stage = new Stage(stageViewport, spriteBatch);
        floor = floorIn;

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont(Gdx.files.internal("font/font.fnt"));
        labelStyle.fontColor = Color.BLACK;
        labelStyle.background = new TextureRegionDrawable(new Texture("textbox.png"));

        Table table = new Table();
        Label health = new Label("Health: " + Integer.toString(player.getHealth()) + "/" + Integer.toString(player.getMaxHealth()), labelStyle);
        health.setFontScale(1.25f);
        health.setAlignment(Align.center);

        Label level = new Label("Level: " + Integer.toString(player.getLevel()), labelStyle);
        level.setFontScale(1.25f);
        level.setAlignment(Align.center);

        Label experience = new Label("Experience: " + Integer.toString(player.getExperience()) + "/100", labelStyle);
        experience.setFontScale(1.25f);
        experience.setAlignment(Align.center);

        Label money = new Label("Money: " + Integer.toString(player.getMoney()), labelStyle);
        money.setFontScale(1.25f);
        money.setAlignment(Align.center);

        Label floor = new Label("Floor: " + Integer.toString(floorIn), labelStyle);
        floor.setFontScale(1.25f);
        floor.setAlignment(Align.center);

        table.add(health).width(600f).height(150f).pad(10);
        table.row();
        table.add(level).width(600f).height(150f).pad(10);
        table.row();
        table.add(experience).width(600f).height(150f).pad(10);
        table.row();
        table.add(money).width(600f).height(150f).pad(10);
        table.row();
        table.add(floor).width(600f).height(150f).pad(10);

        table.setPosition(Gdx.graphics.getWidth()/3 - table.getWidth(), Gdx.graphics.getHeight()/2 - table.getHeight());

        Table weaponTable = new Table();
        for(String item : player.getWeapon()){
            Label weapon = new Label(item, labelStyle);
            weapon.setFontScale(1.25f);
            weapon.setAlignment(Align.center);
            weaponTable.add(weapon).width(600f).height(150f).pad(10);
            weaponTable.row();
        }
        weaponTable.pack();

        ScrollPane scroller = new ScrollPane(weaponTable);
        scroller.setPosition(Gdx.graphics.getWidth() * 2 / 3 - scroller.getWidth(), 0);
        scroller.setWidth(weaponTable.getWidth());
        scroller.setHeight(Gdx.graphics.getHeight());
        scroller.setTouchable(Touchable.enabled);

        Texture buttonTexture = new Texture("textbox.png");
        Skin skin = new Skin();
        skin.add("buttonTexture", buttonTexture);
        ImageTextButton.ImageTextButtonStyle backButtonStyle = new ImageTextButton.ImageTextButtonStyle();
        backButtonStyle.font = new BitmapFont(Gdx.files.internal("font/font.fnt"));
        backButtonStyle.fontColor = Color.BLACK;
        backButtonStyle.up = new TextureRegionDrawable(buttonTexture);
        backButtonStyle.down = new TextureRegionDrawable(buttonTexture);
        ImageTextButton backButton = new ImageTextButton("Close", backButtonStyle);
        backButton.setTouchable(Touchable.enabled);
        backButton.setWidth(200f);
        backButton.setHeight(60f);
        backButton.getLabel().setFontScale(0.6f);
        backButton.setPosition(100, 1000);
        backButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                gameScreen.getGame().setScreen(gameScreen);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }
        });

        stage.addActor(table);
        stage.addActor(scroller);
        stage.addActor(backButton);
    }

    public Stage getStage(){
        return stage;
    }

    public void dispose(){
        stage.dispose();
    }
}
