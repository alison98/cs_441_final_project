package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

public class Player extends Actor {

    private Sprite sprite;
    private int speedX;
    private int speedY;
    private Rectangle bounds;
    private ArrayList<Sprite> walkingSprites;
    private int walkFrame;
    private int frameCounter;
    private List<String> weapon;
    private int level, health, experience;
    private Move abilities;
    private GameScreen gameScreen;


    public Player(GameScreen gameScreenIn){
        walkingSprites = new ArrayList<Sprite>();
        walkingSprites.add(new Sprite(new Texture("player-4x/player-walk-left2.png"))); //0
        walkingSprites.add(new Sprite(new Texture("player-4x/player-walk-left3.png"))); //1
        walkingSprites.add(new Sprite(new Texture("player-4x/player-walk-left4.png"))); //2
        walkingSprites.add(new Sprite(new Texture("player-4x/player-walk-left3.png"))); //3
        walkingSprites.add(new Sprite(new Texture("player-4x/player-walk-right2.png"))); //4
        walkingSprites.add(new Sprite(new Texture("player-4x/player-walk-right3.png"))); //5
        walkingSprites.add(new Sprite(new Texture("player-4x/player-walk-right4.png"))); //6
        walkingSprites.add(new Sprite(new Texture("player-4x/player-walk-right3.png"))); //7
        walkingSprites.add(new Sprite(new Texture("player-resized6x.png"))); //8
        walkFrame = 0;
        sprite = walkingSprites.get(walkFrame);
        this.setBounds(this.sprite.getX(), this.sprite.getY(), this.sprite.getWidth(), this.sprite.getHeight()/2);
        bounds = new Rectangle(this.sprite.getX(), this.sprite.getY(), this.sprite.getWidth(), (this.sprite.getHeight()/2) - 15);
        this.setOrigin(this.sprite.getWidth() / 2, this.sprite.getHeight() / 2);
        speedX = 0;
        speedY = 0;
        frameCounter = 0;
        gameScreen = gameScreenIn;
        weapon = new ArrayList<>();
        weapon.add("sword");
        level = 1;
        health = 100;
        experience = 0;
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

    public void setHealth(int healthIn){
        health = healthIn;
    }

    public int getExperience(){
        return experience;
    }

    public void increaseExperience(int amount){
        experience += amount;
        if(experience <=100){
            level +=1;
            experience -=100;
        }
    }

    public void setSpeedX(int speedXIn){
        speedX = speedXIn;
    }

    public void setSpeedY(int speedYIn){
        speedY = speedYIn;
    }

    public void setCombat(){
        speedX = 0;
        speedY = 0;
        walkFrame = 8;
        this.sprite = walkingSprites.get(walkFrame);
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void scaleSprite(float scale){
        sprite.setScale(scale);
        positionChanged();
    }

    @Override
    public void draw(Batch batch, float alpha){
        sprite.draw(batch);
    }

    @Override
    public void positionChanged(){
        sprite.setPosition(getX(), getY());
        this.bounds.set(getX(), getY(), this.sprite.getWidth(), (this.sprite.getHeight()/2) - 15);
    }

    public void move(){
        if(this.getX() + speedX >= Gdx.graphics.getWidth() - this.getWidth() - 5 && this.speedX > 0){
            this.speedX = 0;
        }
        if(this.getX() + speedX - 5 <= 0 && this.speedX < 0){
            this.speedX = 0;
        }
        if(this.getY() + speedY >= Gdx.graphics.getHeight() - this.getHeight() - 150 && this.speedY > 0){
            this.speedY = 0;
        }
        if(this.getY() + speedY - 20 <= 0 && this.speedY < 0){
            this.speedY = 0;
        }
        Rectangle newBounds = new Rectangle(bounds.getX() + speedX, bounds.getY() + speedY, bounds.width, bounds.height);
        for(Boundary boundary : gameScreen.getRoom().getBoundaries()){
            if(newBounds.overlaps(boundary.getBounds())){
                this.speedY = 0;
                this.speedX = 0;
            }
        }
        if(frameCounter < 4){
            frameCounter++;
        } else{
            frameCounter = 0;
            animate();
            this.moveBy(speedX, speedY);
            this.positionChanged();
        }
    }

    public void animate(){
        if(0 <= walkFrame && walkFrame <= 3){ //Moving left
            if(speedX == 0 && speedY == 0){
                walkFrame = 0;
            } else if(speedX < 0 || speedY > 0 || speedY < 0){
                walkFrame = (walkFrame + 1) % 4;
            } else if(speedX > 0){
                walkFrame = 5;
            }
        } else if(4 <= walkFrame && walkFrame <= 7){ //Moving right
            if(speedX == 0 && speedY == 0){
                walkFrame = 4;
            } else if(speedX > 0 || speedY > 0 || speedY < 0){
                walkFrame = (walkFrame + 1) % 4 + 4;
            } else if(speedX < 0){
                walkFrame = 1;
            }
        } else if(walkFrame == 8){
            walkFrame = 4;
        }
        this.sprite = this.walkingSprites.get(walkFrame);
    }
}
