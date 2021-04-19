package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

public class GameScreen implements Screen {
    private Game game;
    private Stage stage;
    private int height;
    private int width;
    private OrthographicCamera camera;
    private Hud hud;
    private SpriteBatch spriteBatch;
    Player player;
    private ArrayList<Enemy> enemies;
    private Enemy toRemove;

    public GameScreen(Game g) {
        game = g;
        stage = new Stage(new ScreenViewport());
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        spriteBatch = new SpriteBatch();
        hud = new Hud(spriteBatch, this);
        camera = new OrthographicCamera();
        player = new Player();
        player.setPosition(500, 500);
        enemies = new ArrayList<Enemy>();
        enemies.add(new Enemy(800, 800, 0, 0, 1));
        for(Enemy enemy : enemies){
            stage.addActor(enemy);
        }
        stage.addActor(player);
    }

    @Override
    public void show(){
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(hud.getStage());
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tick();
        Enemy hitEnemy = checkCollisions();
        if(hitEnemy != null){
            toRemove = hitEnemy;
            game.setScreen(new CombatScreen(game, hitEnemy, player, this));
        }
        player.move();
        stage.act(delta);
        stage.draw();
        spriteBatch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().act(delta);
        hud.getStage().draw();
    }

    private Enemy checkCollisions(){
        for(Enemy enemy : enemies){
            if(enemy.getHitbox().overlaps(player.getBounds())){
                return enemy;
            }
        }
        return null;
    }

    public void removeEnemy(){
        enemies.remove(toRemove);
        toRemove.remove();
        toRemove = null;
    }

    private void tick(){

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
        hud.dispose();
    }
}
