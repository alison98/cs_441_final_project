package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Room extends Actor {
    private Sprite sprite;

    private Layout layout;
    private List<Enemy> enemyList;
    private List<Integer> doors;
    private List<Interactable> interactables;
    private int enemyNum;
    private int humanNum;
    private Random random;
    private Texture sideDoorImg;
    private Texture doorImg;
    private Texture stairUpImg;
    private Texture stairDownImg;
    private Boundary boundary;

    public Room(){
        random = new Random();
        boundary = Boundary.getInstance();
        layout = Layout.getInstance();
        sprite = layout.getRoom();
        doors = layout.possibleRooms();
        doorImg = new Texture("door.png");
        sideDoorImg = new Texture("small/floor.png");
        stairUpImg = new Texture("stairs-up.png");
        stairDownImg = new Texture("stairs-down.png");
        enemyList = layout.getEnemies();
        setUpInteractables();
        setUpBoundaries();
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void setUpInteractables(){
        interactables = layout.getInteractables();
    }

    public void setUpBoundaries(){
        boundary.setBoundaries(sprite.getTexture().toString());
    }

    public void tick() {
        for(Enemy enemy: enemyList){
            enemy.tick();
        }
    }

    @Override
    public void draw(Batch batch, float alpha){
        batch.draw(sprite, getX(),getY(), Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        doors = layout.possibleRooms();
        for(int i=0; i<doors.size(); i++){
            if(doors.get(i)==0){
                batch.draw(sideDoorImg, 0, Gdx.graphics.getHeight()/2, 50, 150);
            }else if(doors.get(i)==1){
                batch.draw(sideDoorImg, Gdx.graphics.getWidth()-50, Gdx.graphics.getHeight()/2, 50, 150);
            }else if(doors.get(i)==2){
                batch.draw(doorImg, Gdx.graphics.getWidth()/2-doorImg.getWidth()/2, 888);
            }else if(doors.get(i)==3){
                batch.draw(sideDoorImg, Gdx.graphics.getWidth()/2-63, 0, 126, 50);
            }
        }
        for(Enemy enemy: enemyList) {
            enemy.draw(batch, alpha);
        }
        for(Interactable interactable : interactables){
            interactable.draw(batch, alpha);
        }

        if(layout.isStairs() && (layout.getBoss() || layout.downFloor())){
            if(layout.downFloor()){
                batch.draw(stairDownImg, Gdx.graphics.getWidth() - stairDownImg.getWidth(), Gdx.graphics.getHeight()  - Gdx.graphics.getHeight()/3);
            } else{
                batch.draw(stairUpImg, Gdx.graphics.getWidth() - stairUpImg.getWidth(), Gdx.graphics.getHeight() - Gdx.graphics.getHeight()/3);
            }
        }
    }

    @Override
    public void positionChanged(){
        sprite.setPosition(getX(), getY());
    }

    @Override
    public boolean remove(){
        return super.remove();
    }

    public Enemy checkCollisions(Player player){
        for(Enemy enemy : enemyList){
            if(enemy.getFight() && enemy.getHitbox().overlaps(player.getBounds())){
                return enemy;
            }
        }
        return null;
    }

    public boolean getDoorTouched(Player player){
        return layout.getDoorTouched(player);
    }

    public Interactable getInteractablesTouched(Player player) {
        for(Interactable interactable : interactables){
            if(interactable.getBounds().overlaps(player.getBounds())){
                return interactable;
            }
        }
        return null;
    }

    public Integer roomChange(Player player){
        Integer location = layout.changeRoom(player);
        if(location != null){
            sprite = layout.getRoom();
            if(sprite.getTexture().toString().equals("office-space-no-printer.png")){
                player.setSmall();
            } else{
                player.setBig();
            }
            enemyList = layout.getEnemies();
            setUpInteractables();
            setUpBoundaries();
        }
        return location;
    }
}
