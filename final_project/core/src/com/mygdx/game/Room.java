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
    private boolean stair;
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
        setUpEnemies();
        setUpInteractables();
        setUpBoundaries();
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        stair = false;
    }

    private void setUpEnemies(){
        //add enemies to the room in random locations
        enemyList = layout.getEnemies();
        /*for(int i=0; i<enemyNum; i++){
            Enemy enemy = new Enemy(random.nextInt(1000)+100,random.nextInt(700)+100,2,random.nextInt(5),1);
            enemyList.add(enemy);
        }

        //add the defeated enemies in random locations
        humanNum = layout.getHumans();
        for(int i=0; i<humanNum; i++){
            Enemy enemy = new Enemy(random.nextInt(1000)+100,random.nextInt(700)+100,2,0,1);
            enemy.setHealth(0);
            enemyList.add(enemy);
        },*/

    }

    public void setUpInteractables(){
        interactables = layout.getInteractables();
    }

    public void setUpBoundaries(){
        if(sprite.getTexture().toString().equals("office-space-no-printer.png")){
            boundaries.add(new Boundary(208, 432, 440, 408));
            boundaries.add(new Boundary(848, 432, 440, 408));
            boundaries.add(new Boundary(1488, 432, 440, 408));
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
        if(stair && (layout.getBoss() || layout.downFloor())){
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
        return layout.getDoorTouched(player, stair);
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
        Integer location = layout.changeRoom(player, stair);
        if(location != null){
            sprite = layout.getRoom();
            if(sprite.getTexture().toString().equals("img4.jpg")){
                stair = true;
            }else{
                stair = false;
            }
            setUpEnemies();
            setUpInteractables();
            setUpBoundaries();
        }
        return location;
    }
}
