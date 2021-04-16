package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Enemy  extends Actor {
    private Sprite[] sprites;
    private Sprite sprite;
    private int speed;
    private int type;
    private float initX;
    private float initY;
    private boolean horizontal;
    private boolean clockwise;
    private Rectangle hitbox;

    public Enemy(int x, int y,int speedIn, int typeIn){
        initSprites();
        sprite = sprites[0];
        initX = x;
        initY = y;
        speed = speedIn;
        type = typeIn;
        horizontal = true;
        clockwise = true;
        this.setBounds(this.sprite.getX(), this.sprite.getY(), this.sprite.getWidth(), this.sprite.getHeight());
        hitbox = new Rectangle(getX() + 32, getY() + 144, 176, 64);
        this.setPosition(initX, initY);
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
            if (getX() < initX - 100 || getX() > initX + 100) {
                speed = -speed;
            }
            this.setPosition(getX() + speed, getY());
        }else if(type ==2){ //up and down
            if (getY() < initY - 100 || getY() > initY + 100) {
                speed = -speed;
            }
            this.setPosition(getX() , getY()+ speed);
        }else if(type ==3){ //clockwise
            if(horizontal && clockwise) {
                if (getX() > initX + 100) {
                    speed = -speed;
                    horizontal = false;
                }
                this.setPosition(getX() + speed, getY());
            }else if(horizontal && !clockwise){
                if (getX() < initX - 100) {
                    speed = -speed;
                    horizontal = false;
                }
                this.setPosition(getX() + speed, getY());
            }else if(!horizontal && clockwise) {
                if (getY() < initY - 100) {
                    clockwise = false;
                    horizontal = true;
                }
                this.setPosition(getX(), getY()+speed);
            }else {
                if (getY() > initY + 100) {
                    clockwise = true;
                    horizontal = true;
                }
                this.setPosition(getX(), getY()+speed);
            }
        }else if(type ==4) { //counter-clockwise
            if (horizontal && !clockwise) {
                if (getX() > initX + 100) {
                    speed = -speed;
                    horizontal = false;
                }
                this.setPosition(getX() - speed, getY());
            } else if (horizontal && clockwise) {
                if (getX() < initX - 100) {
                    speed = -speed;
                    horizontal = false;
                }
                this.setPosition(getX() - speed, getY());
            } else if (!horizontal && !clockwise) {
                if (getY() < initY - 100) {
                    clockwise = true;
                    horizontal = true;
                }
                this.setPosition(getX(), getY() - speed);
            } else {
                if (getY() > initY + 100) {
                    clockwise = false;
                    horizontal = true;
                }
                this.setPosition(getX(), getY() - speed);
            }
        }
        //positionChanged();
    }

    @Override
    public void draw(Batch batch, float alpha){
        sprite.draw(batch);
    }

    @Override
    public void positionChanged(){


        sprite.setPosition(getX(), getY());
        hitbox.set(getX() + 32, getY() + 144, 176, 64);
    }

    @Override
    public boolean remove(){
        return super.remove();
    }
}
