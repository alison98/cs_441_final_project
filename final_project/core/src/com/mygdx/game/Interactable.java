package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Interactable extends Actor {
    private Sprite sprite;
    private Rectangle bounds;
    private int floor, row, column, index;

    public Interactable(String image, int floorIn, int rowIn, int columnIn, int indexIn){
        sprite = new Sprite(new Texture(image));
        this.setBounds(this.sprite.getX(), this.sprite.getY(), this.sprite.getWidth(), this.sprite.getHeight());
        bounds = new Rectangle(this.sprite.getX() - 50, this.sprite.getY() - 50, this.sprite.getWidth() + 50, this.sprite.getHeight() + 50);
        this.setOrigin(this.sprite.getWidth() / 2, this.sprite.getHeight() / 2);
        floor = floorIn;
        row = rowIn;
        column = columnIn;
        index = indexIn;
    }

    public Rectangle getBounds(){
        return bounds;
    }

    @Override
    public void positionChanged(){
        sprite.setPosition(getX(), getY());
        this.bounds.set(getX(), getY() - 30, this.sprite.getWidth(), this.sprite.getHeight() + 30);
    }

    @Override
    public void draw(Batch batch, float alpha){
        sprite.draw(batch);
    }

    public void interact(GameScreen game){
        return;
    }

    @Override
    public boolean remove(){
        return super.remove();
    }
}
