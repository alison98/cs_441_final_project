package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Layout {
    private static Layout instance;
    private List<Sprite> rooms;
    private List<Integer> enemies;
    private List<Integer> humans;
    private int index;
    private List<Rectangle> hitboxes;


    //Singleton class
    private Layout(){
        rooms = new ArrayList<>();
        enemies = new ArrayList<>();
        humans = new ArrayList<>();
        rooms.add(new Sprite(new Texture("img3.jpg")));
        enemies.add(1);
        humans.add(0);
        rooms.add(new Sprite(new Texture("img4.jpg")));
        enemies.add(2);
        humans.add(0);


        index = 0;

        hitboxes = new ArrayList<>();
        //Bounds can change based on room image
        Rectangle left = new Rectangle(0, Gdx.graphics.getHeight()/2, 50, 150);
        Rectangle right = new Rectangle(Gdx.graphics.getWidth()-50, Gdx.graphics.getHeight()/2, 50, 150);
        Rectangle top = new Rectangle(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()-50, 150, 50);
        Rectangle bottom = new Rectangle(Gdx.graphics.getWidth()/2, 0, 150, 50);
        hitboxes.add(left);
        hitboxes.add(right);
        hitboxes.add(top);
        hitboxes.add(bottom);

    }

    public static Layout getInstance(){
        if(instance == null){
            instance = new Layout();
        }
        return instance;
    }

    public Sprite getRoom(){
        return rooms.get(index);
    }

    public Integer getEnemies(){
        return enemies.get(index);
    }
    public Integer getHumans(){
        return humans.get(index);
    }

    public Integer changeRoom(Player player){
        for(int i=0; i<hitboxes.size(); i++){
            if(hitboxes.get(i).overlaps(player.getBounds())){
                if(index ==0){
                    index = 1;
                }else{
                    index= 0;
                }
                return i;
            }
        }
        return null;
    }

    public void setEnemies(){
        int enemy = enemies.get(index)-1;
        int human = humans.get(index)+1;
        enemies.set(index, enemy);
        humans.set(index, human);
    }
}
