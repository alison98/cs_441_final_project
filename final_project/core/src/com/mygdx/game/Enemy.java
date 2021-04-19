package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.List;
import java.util.Random;

public class Enemy  extends Actor {
    private Sprite[] sprites;
    private Sprite sprite;
    private int speed;
    private int type;
    private float initX;
    private float initY;
    private boolean horizontal;
    private boolean direction;
    private Rectangle hitbox;
    private List<String> weapon;
    private int floor;
    private int level;
    private Move abilities;

    public Enemy(int x, int y, int speedIn, int typeIn, int floorIn){
        initSprites();
        sprite = sprites[0];
        initX = x;
        initY = y;
        speed = speedIn;
        type = typeIn;
        horizontal = true;
        direction = true;
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        hitbox = new Rectangle(getX(), getY(), getWidth(), getHeight());
        setPosition(initX, initY);

        //get the weapons based on level
        floor = floorIn;
        abilities = Move.getInstance();
        weapon = abilities.getEnemyWeapons(floor);

        Random random = new Random();
        level = random.nextInt(10)+floor;
    }

    public Rectangle getHitbox(){
        return hitbox;
    }

    private void initSprites(){
        sprites = new Sprite[1];
        sprites[0] = new Sprite(new Texture("badlogic.jpg"));
        //sprites[0] = new Sprite(new Texture(Gdx.files.internal("rocket/rocket-1-resized.png")));
        //sprites[1] = new Sprite(new Texture(Gdx.files.internal("rocket/rocket-2-resized.png")));
        //sprites[2] = new Sprite(new Texture(Gdx.files.internal("rocket/rocket-3-resized.png")));
    }

    public void tick() {
        if (type == 1) { // side to side
            moveHorizontal();
        }else if(type ==2){ //up and down
            moveVertical();
        }else if(type ==3){ //clockwise
            moveClockwise();
        }else if(type ==4) { //counter-clockwise
            moveCounterClockwise();
        }
        //positionChanged();
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
}
