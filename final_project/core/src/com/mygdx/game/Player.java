package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player extends Actor {

    private Sprite sprite;
    private int speedX;
    private int speedY;
    private Rectangle bounds;

    public Player(){
        sprite = new Sprite(new Texture("player-resized6x.png"));
        this.setBounds(this.sprite.getX(), this.sprite.getY(), this.sprite.getWidth(), this.sprite.getHeight());
        bounds = new Rectangle(this.sprite.getX(), this.sprite.getY(), this.sprite.getWidth(), this.sprite.getHeight());
        this.setOrigin(this.sprite.getWidth() / 2, this.sprite.getHeight() / 2);
        speedX = 0;
        speedY = 0;
    }

    public void setSpeedX(int speedXIn){
        speedX = speedXIn;
    }

    public void setSpeedY(int speedYIn){
        speedY = speedYIn;
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void scaleSprite(float scale){
        sprite.setScale(scale);
    }

    @Override
    public void draw(Batch batch, float alpha){
        sprite.draw(batch);
    }

    @Override
    public void positionChanged(){
        sprite.setPosition(getX(), getY());
        this.bounds.set(getX(), getY(), this.sprite.getWidth(), this.sprite.getHeight());
    }

    public void move(){
        if(this.getX() + speedX >= Gdx.graphics.getWidth() - this.getWidth() && this.speedX > 0){
            this.speedX = 0;
        }
        if(this.getX() + speedX <= 0 && this.speedX < 0){
            this.speedX = 0;
        }

        if(this.getY() + speedY >= Gdx.graphics.getHeight() - this.getHeight() && this.speedY > 0){
            this.speedY = 0;
        }
        if(this.getY() + speedY <= 0 && this.speedY < 0){
            this.speedY = 0;
        }
        this.moveBy(speedX, speedY);
        this.positionChanged();
    }
}
