package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    private List<Boundary> boundaries;
    private int enemyNum;
    private int humanNum;
    private Random random;
    private Texture sideDoorImg;
    private Texture doorImg;
    private Texture stairUpImg;
    private Texture stairDownImg;

    public Room(){
        random = new Random();
        layout = Layout.getInstance();
        sprite = layout.getRoom();
        doors = layout.possibleRooms();
        doorImg = new Texture("door.png");
        sideDoorImg = new Texture("small/floor.png");
        stairUpImg = new Texture("stairs-up.png");
        stairDownImg = new Texture("stairs-down.png");
        boundaries = new ArrayList<Boundary>();
        enemyList = layout.getEnemies();
        setUpInteractables();
        setUpBoundaries();
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void setUpInteractables(){
        interactables = layout.getInteractables();
    }

    public void setUpBoundaries(){
        boundaries.clear();
        if(sprite.getTexture().toString().equals("office-space-no-printer.png")){
            boundaries.add(new Boundary(208, 432, 440, 409));
            boundaries.add(new Boundary(848, 432, 440, 409));
            boundaries.add(new Boundary(1488, 432, 440, 409));
            boundaries.add(new Boundary(208, 16, 448, 216));
            boundaries.add(new Boundary(848, 16, 448, 216));
            boundaries.add(new Boundary(1488, 16, 16, 56));
            boundaries.add(new Boundary(1488, 192, 584, 150));
            boundaries.add(new Boundary(1720, 136, 152, 40));
        }else {
            boundaries.clear();
        }
    }

    public void tick() {
        for(Enemy enemy: enemyList){
            enemy.tick();
        }
    }

    public List<Boundary> getBoundaries(){
        return boundaries;
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
                batch.draw(doorImg, 1008, 888);
            }else if(doors.get(i)==3){
                batch.draw(sideDoorImg, 1008, 0, 72, 50);
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
            enemyList = layout.getEnemies();
            setUpInteractables();
            setUpBoundaries();
        }
        return location;
    }
}
