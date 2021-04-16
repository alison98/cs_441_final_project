package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player extends Actor {

    private Sprite sprite;

    public Player(){
        sprite = new Sprite(new Texture("badlogic.jpg"));
    }
}
