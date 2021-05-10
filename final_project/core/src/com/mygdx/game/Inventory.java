package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Inventory implements Screen {
    private Game game;
    private Stage stage;
    private Player player;
    private GameScreen gameScreen;

    public Inventory(Game gameIn, Player playerIn, GameScreen gameScreenIn){
        game = gameIn;
        player = playerIn;
        this.stage = new Stage(new ScreenViewport());
        gameScreen = gameScreenIn;

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont(Gdx.files.internal("font/font.fnt"));
        labelStyle.fontColor = Color.BLACK;

        Table table = new Table();
        Label health = new Label("Health: " + Integer.toString(player.getHealth()) + "/" + Integer.toString(player.getMaxHealth()), labelStyle);
        health.setFontScale(1.25f);
        Label level = new Label("Level: " + Integer.toString(player.getLevel()), labelStyle);
        level.setFontScale(1.25f);
        Label experience = new Label("Experience: " + Integer.toString(player.getExperience()) + "/100", labelStyle);
        experience.setFontScale(1.25f);
        table.add(health);
        table.row();
        table.add(level);
        table.row();
        table.add(experience);
        table.setPosition(Gdx.graphics.getWidth()/3 - table.getWidth(), 500);

        Table weaponTable = new Table();
        for(String item : player.getWeapon()){
            Label weapon = new Label(item, labelStyle);
            weapon.setFontScale(1.25f);
            weapon.setColor(Color.BLACK);
            weaponTable.add(weapon);
            weaponTable.row();
        }
        weaponTable.setPosition(Gdx.graphics.getWidth() * 2 / 3, 500);

        ImageButton backButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("right-button.png"))));
        backButton.setTouchable(Touchable.enabled);
        backButton.setPosition(100, 1000);
        backButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                dispose();
                game.setScreen(gameScreen);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }
        });

        stage.addActor(table);
        stage.addActor(weaponTable);
        stage.addActor(backButton);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1); //white background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
