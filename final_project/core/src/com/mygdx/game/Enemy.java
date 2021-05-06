package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Enemy extends Actor {
    private Sprite[] sprites;
    private Sprite sprite;
    private int speed, speedX,speedY, type, floor;
    private float initX, initY;
    private boolean horizontal, direction;
    private boolean fight, key, boss;
    private Rectangle hitbox;
    private List<String> weapon;
    private List<String> ongoingStatusEffects;
    private int level, maxHealth, health, experience, money;
    private Move abilities;
    private String name;
    private Random random;
    private List<Integer> movement;
    private String bossMessage;
    private boolean isHuman;

    public Enemy(int x, int y, int speedIn, String file, int floorIn, List<Integer> movementIn){
        initSprites(file);
        sprite = sprites[0];
        initX = x;
        initY = y;
        speed = speedIn;
        speedX = speedIn;
        speedY = speedIn;
        movement = movementIn;
        random = new Random();
        if (movement != null){
            type = 1;
            initX = random.nextInt(movement.get(1)-movement.get(0)-(int)sprite.getWidth())+movement.get(0);
            initY = random.nextInt(movement.get(3)-movement.get(2)-(int)sprite.getHeight())+movement.get(2);
        }else {
            type = 0;
        }
        horizontal = true;
        direction = true;
        fight = true;
        key = false;
        boss = false;
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        hitbox = new Rectangle(getX(), getY(), getWidth(), getHeight());
        setPosition(initX, initY);

        //get the weapons based on level
        floor = floorIn;
        abilities = Move.getInstance();
        weapon = abilities.getEnemyWeapons(floor);
        ongoingStatusEffects = new ArrayList<>();


        level = random.nextInt(10)+floor;
        //maxHealth = health = 10;
        money = maxHealth = health = floor*10 + random.nextInt(10);
        experience = level*2;
        bossMessage = "";
        isHuman = false;
    }

    public Rectangle getHitbox(){
        return hitbox;
    }

    public Sprite getHumanSprite(){return sprites[1];}

    private void initSprites(String file){
        sprites = new Sprite[2];
        sprites[0] = new Sprite(new Texture(file));
        sprites[1] = new Sprite(new Texture("human2-6x.png")); //change to random human character
        name = file.substring(0,file.length()-4);
        name = name.replaceAll("-"," ");
        name = name.toUpperCase();

    }

    public void scaleSprite(float scale){
        sprite.setScale(scale);
    }

    public void tick() {
        if(!fight){
            return;
        }
        if(type ==1){
            moveRandom();
        }else if (type == 2) { // side to side
            moveHorizontal();
        }else if(type ==3){ //up and down
            moveVertical();
        }else if(type ==4){ //clockwise
            moveClockwise();
        }else if(type ==5) { //counter-clockwise
            moveCounterClockwise();
        }
    }

    public void moveRandom(){
        if(getX()< movement.get(0) || getX() + sprite.getWidth() > movement.get(1)){
            speedX = -speedX;
        }
        if(getY() < movement.get(2) || getY() + sprite.getHeight() > movement.get(3)){
            speedY = -speedY;
        }
        moveBy(speedX,speedY);
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

    //for items that can only be used once (healing for now, we can better define later)
    public void removeWeapon(String weaponToRemove){
        weapon.remove(weaponToRemove);
    }

    public List<String> getWeapon(){
        return weapon;
    }

    public List<String> getOngoingStatusEffects() { return ongoingStatusEffects; }

    public int getLevel(){
        return level;
    }

    public int getMaxHealth(){
        return maxHealth;
    }

    public int getHealth(){
        return health;
    }

    public List<Integer> getRewards(){
        List<Integer> rewards = new ArrayList<>();
        rewards.add(experience);
        rewards.add(money);
        //rewards.add(drops);
        return rewards;
    }

    public boolean getFight(){
        return fight;
    }

    public String getName(){return name;}

    public void setKey(){
        key = true;
    }

    public boolean hasKey(){ return key; }

    public boolean isBoss(){ return boss; }

    public void setBoss(String bossMessageIn){
        boss = true;
        bossMessage = bossMessageIn;
        //sprite = new Sprite(new Texture("enemy-printer-12x.png"));
        //positionChanged();
    }

    public boolean isHuman(){return isHuman;}

    public void setHuman(){
        isHuman = true;
        sprite = sprites[1];
        positionChanged();
    }

    public String getBossMessage(){
        return bossMessage;
    }

    public void setHealth(int healthIn){
        health = healthIn;
        if(health > maxHealth) health = maxHealth; //for healing (don't go past max)
    }

    public void defeated(){
        fight = false;
        sprite.setAlpha(0);
        if(key){
            Layout.getInstance().setKey(floor);
            System.out.println("Obtained Key");
            key = false;
        }
        if(boss){
            sprite = sprites[1];
            sprite.setAlpha(1f);
            Layout.getInstance().setBoss(floor);
            System.out.println("Defeated Boss");
        }
    }

    public void respawn(){
        maxHealth = health = floor * 10 + random.nextInt(10);
        if(!boss) {
            //maxHealth = health = 10;
            sprite.setAlpha(1f);
            fight = true;
            sprite = sprites[0];
            scaleSprite(1f);
            positionChanged();
        }
    }
}
