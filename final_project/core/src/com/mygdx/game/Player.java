package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player extends Actor {

    private Sprite sprite;

    public Player(){
        sprite = new Sprite(new Texture("badlogic.jpg"));
        this.setBounds(this.sprite.getX(), this.sprite.getY(), this.sprite.getWidth(), this.sprite.getHeight());
        this.setOrigin(this.sprite.getWidth() / 2, this.sprite.getHeight() / 2);
    }

    @Override
    public void draw(Batch batch, float alpha){
        sprite.draw(batch);
    }
}
