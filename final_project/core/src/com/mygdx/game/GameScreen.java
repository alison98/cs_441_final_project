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
    private Player player;
    private Room room;

    //so I think an actor can only be on 1 stage at a time
    //and adding player to CombatScreen removes from this stage
    //and I need to re-add the player to the stage before coming back to GameScreen
    //I have no idea why I don't get this problem with the enemy
    public Stage getStage(){ return stage; }

    //also need this to make sure player spawns back in valid spot on dying
    //i.e. not on an enemy
    public Room getRoom(){ return room; }

    public Game getGame(){
        return game;
    }

    public Player getPlayer(){
        return player;
    }

    public Hud getHud(){ return hud; }

    public GameScreen(Game g) {
        game = g;
        stage = new Stage(new ScreenViewport());
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        spriteBatch = new SpriteBatch();
        hud = new Hud(spriteBatch, this);
        hud.setText("Welcome to your first day on the job!\n Press A to continue", "Your first task is to make eight thousand copies\n of the new mandatory overtime notice", "Find the printer in the break room to get started");
        camera = new OrthographicCamera();
        player = new Player(this);
        player.setPosition(500, 300);
        room = new Room();
        stage.addActor(room);
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
        checkCollisions();
        checkInteractable();
        player.move();
        stage.act(delta);
        stage.draw();
        spriteBatch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().act(delta);
        hud.getStage().draw();
    }

    private void checkCollisions(){
        Enemy hitEnemy = room.checkCollisions(player);
        if(hitEnemy != null){
            player.setCombat();
            game.setScreen(new CombatScreen(game, hitEnemy, player, this));
            //do this in the combat screen
            //hitEnemy.setHealth(0);
            //Layout.getInstance().setEnemies();
        }
    }

    private void checkInteractable(){
        boolean door = room.getDoorTouched(player);
        Interactable interactable = room.getInteractablesTouched(player);
        if(door || interactable != null || hud.inText()){
            hud.setInteractable();
        } else{
            hud.setUninteractable();
        }
    }

    public void roomChange(){
        Integer location = room.roomChange(player);
        if(location != null){
            if(location == 0){
                player.setPosition(Gdx.graphics.getWidth() - player.getWidth()-70,player.getY());
            }else if(location == 1){
                player.setPosition(70,player.getY());
            }else if(location == 2){
                player.setPosition(player.getX(),70);
            }else if(location == 3){
                player.setPosition(player.getX(),Gdx.graphics.getHeight() - player.getHeight()-180);
            }else if(location == -1){
                hud.setText("Door is locked!");
            }
        }
    }

    private void tick(){
        room.tick();
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
