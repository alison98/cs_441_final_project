package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Boundary extends Actor {

    private Rectangle bounds;

    public Boundary(int x, int y, int width, int height){
        this.setBounds(x, y, width, height);
        this.bounds = new Rectangle(x, y, width, height);
        //this.setOrigin(x, y);
    }

    public Rectangle getBounds(){
        return bounds;
    }

}
