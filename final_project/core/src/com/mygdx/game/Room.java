package com.mygdx.game;

import com.badlogic.gdx.Gdx;
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
    private int enemyNum;
    private int humanNum;
    private Random random;

    public Room(){
        random = new Random();
        layout = Layout.getInstance();
        sprite = layout.getRoom();
        setUpEnemies();
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    private void setUpEnemies(){
        //add enemies to the room in random locations
        enemyNum = layout.getEnemies();
        enemyList = new ArrayList<>();
        for(int i=0; i<enemyNum; i++){
            Enemy enemy = new Enemy(random.nextInt(1000)+100,random.nextInt(700)+100,2,random.nextInt(5),1);
            enemyList.add(enemy);
        }

        //add the defeated enemies in random locations
        humanNum = layout.getHumans();
        for(int i=0; i<humanNum; i++){
            Enemy enemy = new Enemy(random.nextInt(1000)+100,random.nextInt(700)+100,2,0,1);
            enemy.setHealth(0);
            enemyList.add(enemy);
        }

    }

    public void tick() {
        for(Enemy enemy: enemyList){
            enemy.tick();
        }
    }

    @Override
    public void draw(Batch batch, float alpha){
        batch.draw(sprite, getX(),getY(), Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        for(Enemy enemy: enemyList) {
            enemy.draw(batch, alpha);
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

    public Integer roomChange(Player player){
        Integer location = layout.changeRoom(player);
        if(location != null){
            sprite = layout.getRoom();
            setUpEnemies();
        }
        return location;
    }
}
