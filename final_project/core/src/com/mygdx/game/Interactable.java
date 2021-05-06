package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Interactable extends Actor {
    private Sprite sprite;
    private Rectangle interactionBounds;
    private Rectangle bounds;
    private int floor, row, column, index, x, y;

    public Interactable(String image, int floorIn, int rowIn, int columnIn, int indexIn, int xIn, int yIn){
        sprite = new Sprite(new Texture(image));
        sprite.setPosition(xIn, yIn);
        interactionBounds = new Rectangle(this.sprite.getX() - 30, this.sprite.getY() - 30, this.sprite.getWidth() + 60, this.sprite.getHeight() + 60);
        bounds = new Rectangle(this.sprite.getX(), this.sprite.getY(), this.sprite.getWidth(), this.sprite.getHeight());
        this.setBounds(this.sprite.getX(), this.sprite.getY(), this.sprite.getWidth(), this.sprite.getHeight());
        this.setOrigin(this.sprite.getWidth() / 2, this.sprite.getHeight() / 2);
        floor = floorIn;
        row = rowIn;
        column = columnIn;
        index = indexIn;
        x = xIn;
        y = yIn;
    }

    public Rectangle getInteractionBounds(){
        return interactionBounds;
    }

    public Rectangle getBounds(){
        return bounds;
    }


    public Sprite getSprite(){return sprite;}

    public void setSprite(Sprite spriteIn){sprite = spriteIn;}

    @Override
    public void positionChanged(){
        sprite.setPosition(getX(), getY());
        this.interactionBounds.set(getX() - 30, getY() - 30, this.sprite.getWidth() + 60, this.sprite.getHeight() + 60);
        this.bounds.set(getX(), getY(), this.sprite.getWidth(), this.sprite.getHeight());
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
