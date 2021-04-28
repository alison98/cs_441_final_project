package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

public class Boundary extends Actor {

    private List<Rectangle> boundaries;
    private static Boundary instance;

    private Boundary(){
        this.boundaries = new ArrayList<Rectangle>();
    }

    public static Boundary getInstance(){
        if(instance == null){
            instance = new Boundary();
        }
        return instance;
    }

    public void setBoundaries(String filename){
        boundaries.clear();
        if(filename.equals("office-space-no-printer.png")){
            boundaries.add(new Rectangle(208, 432, 440, 409));
            boundaries.add(new Rectangle(848, 432, 440, 409));
            boundaries.add(new Rectangle(1488, 432, 440, 409));
            boundaries.add(new Rectangle(208, 16, 448, 216));
            boundaries.add(new Rectangle(848, 16, 448, 216));
            boundaries.add(new Rectangle(1488, 16, 16, 56));
            boundaries.add(new Rectangle(1488, 192, 584, 150));
            boundaries.add(new Rectangle(1720, 136, 152, 40));
        } else if(filename.equals("server-closet.png")){
            boundaries.add(new Rectangle(280, 344, 160, 336));
            boundaries.add(new Rectangle(776, 344, 160, 336));
            boundaries.add(new Rectangle(968, 344, 160, 336));
            boundaries.add(new Rectangle(520, 752, 96, 328));
            boundaries.add(new Rectangle(1704, 560, 16, 520));
        }
    }

    public List<Rectangle> getBoundaries(){
        return this.boundaries;
    }

}
