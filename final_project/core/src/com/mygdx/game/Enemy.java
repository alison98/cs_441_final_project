package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.List;
import java.util.Random;

public class Enemy extends Actor {
    private Sprite[] sprites;
    private Sprite sprite;
    private int speed, type, floor;
    private float initX, initY;
    private boolean horizontal, direction;
    private boolean fight, key, boss, human;
    private Rectangle hitbox;
    private List<String> weapon;
    private int level, health;
    private Move abilities;
    private String name;
    private Random random;

    public Enemy(int x, int y, int speedIn, int typeIn, int floorIn){
        initSprites();
        sprite = sprites[0];
        initX = x;
        initY = y;
        speed = speedIn;
        type = typeIn;
        horizontal = true;
        direction = true;
        fight = true;
        key = false;
        boss = false;
        human = false;
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        hitbox = new Rectangle(getX(), getY(), getWidth(), getHeight());
        setPosition(initX, initY);

        //get the weapons based on level
        floor = floorIn;
        abilities = Move.getInstance();
        weapon = abilities.getEnemyWeapons(floor);

        random = new Random();
        level = random.nextInt(10)+floor;
        health = floor*100 + random.nextInt(100);
    }

    public Rectangle getHitbox(){
        return hitbox;
    }

    private void initSprites(){
        sprites = new Sprite[2];
        sprites[0] = new Sprite(new Texture("badlogic.jpg"));
        sprites[1] = new Sprite(new Texture("player-resized6x.png")); //change to random human character
        name = "Monster";
    }

    public void scaleSprite(float scale){
        sprite.setScale(scale);
    }

    public void tick() {
        if(!fight){
            return;
        }
        if (type == 1) { // side to side
            moveHorizontal();
        }else if(type ==2){ //up and down
            moveVertical();
        }else if(type ==3){ //clockwise
            moveClockwise();
        }else if(type ==4) { //counter-clockwise
            moveCounterClockwise();
        }
    }

    public void moveHorizontal(){
        if (getX() < initX - 100 || getX() > initX + 100) {
            speed = -speed;
        }
        moveBy(speed, 0);
    }

    public void moveVertical(){
        if (getY() < initY - 100 || getY() > initY + 100) {
            speed = -speed;
        }
        moveBy(0, speed);
    }

    public void moveClockwise(){
        if(horizontal && direction) {
            if (getX() > initX + 100) {
                speed = -speed;
                horizontal = false;
            }
            moveBy(speed, 0);
        }else if(horizontal && !direction){
            if (getX() < initX - 100) {
                speed = -speed;
                horizontal = false;
            }
            moveBy(speed, 0);
        }else if(!horizontal && direction) {
            if (getY() < initY - 100) {
                direction = false;
                horizontal = true;
            }
            moveBy(0, speed);
        }else {
            if (getY() > initY + 100) {
                direction = true;
                horizontal = true;
            }
            moveBy(0, speed);
        }
    }

    public void moveCounterClockwise(){
        if (horizontal && !direction) {
            if (getX() > initX + 100) {
                speed = -speed;
                horizontal = false;
            }
            moveBy(-speed, 0);
        } else if (horizontal && direction) {
            if (getX() < initX - 100) {
                speed = -speed;
                horizontal = false;
            }
            moveBy(-speed, 0);
        } else if (!horizontal && !direction) {
            if (getY() < initY - 100) {
                direction = true;
                horizontal = true;
            }
            moveBy(0, -speed);
        } else {
            if (getY() > initY + 100) {
                direction = false;
                horizontal = true;
            }
            moveBy(0, -speed);
        }
    }

    @Override
    public void draw(Batch batch, float alpha){
        sprite.draw(batch);
    }

    @Override
    public void positionChanged(){
        sprite.setPosition(getX(), getY());
        hitbox.set(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public boolean remove(){
        return super.remove();
    }

    public List<String> getWeapon(){
        return weapon;
    }

    public int getLevel(){
        return level;
    }

    public int getHealth(){
        return health;
    }

    public boolean getFight(){
        return fight;
    }

    public String getName(){return name;}

    public void setKey(){
        key = true;
    }

    public void setBoss(){
        boss = true;
        sprite = new Sprite(new Texture("enemy1.png"));
        positionChanged();
    }

    public void setHuman(){
        human = true;
        setHealth(0);
    }

    public void setHealth(int healthIn){
        health = healthIn;
        if(health<=0) {
            fight = false;
            sprite = sprites[1];
            positionChanged();
            if(key){
                Layout.getInstance().setKey(floor);
                System.out.println("Obtained Key");
            }
            if(boss){
                Layout.getInstance().setBoss(floor);
                System.out.println("Defeated Boss");
            }
        }
    }

    public void respawn(){
        if(!boss && !human) {
            health = floor * 100 + random.nextInt(100);
            fight = true;
            sprite = sprites[0];
            scaleSprite(1f);
            positionChanged();
        }
    }
}
