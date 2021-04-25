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
    private int enemyNum;
    private int humanNum;
    private Random random;
    private boolean stair;
    private Texture doorImg;

    public Room(){
        random = new Random();
        layout = Layout.getInstance();
        sprite = layout.getRoom();
        doors = layout.possibleRooms();
        doorImg = new Texture("badlogic.jpg");
        setUpEnemies();
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
                batch.draw(doorImg, 0, Gdx.graphics.getHeight()/2, 50, 150);
            }else if(doors.get(i)==1){
                batch.draw(doorImg, Gdx.graphics.getWidth()-50, Gdx.graphics.getHeight()/2, 50, 150);
            }else if(doors.get(i)==2){
                batch.draw(doorImg, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()-50, 150, 50);
            }else if(doors.get(i)==3){
                batch.draw(doorImg, Gdx.graphics.getWidth()/2, 0, 150, 50);
            }
        }
        for(Enemy enemy: enemyList) {
            enemy.draw(batch, alpha);
        }
        if(stair && (layout.getBoss() || layout.downFloor())){
            batch.draw(doorImg, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 150, 150);
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
        }
        return location;
    }
}
