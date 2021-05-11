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
    private List<List<Integer>> enemyLocations;
    private List<List<Integer>> itemLocations;
    private List<Integer> shopLocation;
    private static Boundary instance;

    private Boundary(){
        this.boundaries = new ArrayList<Rectangle>();
        this.enemyLocations = new ArrayList<>();
        this.itemLocations = new ArrayList<>();
        this.shopLocation = new ArrayList<>();
    }

    public static Boundary getInstance(){
        if(instance == null){
            instance = new Boundary();
        }
        return instance;
    }

    public void setBoundaries(String filename){
        boundaries.clear();
        enemyLocations.clear();
        itemLocations.clear();
        shopLocation.clear();
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
            boundaries.add(new Rectangle(1392, 40, 496, 160));
            boundaries.add(new Rectangle(1512, 200, 376, 96));
            boundaries.add(new Rectangle(1528, 296, 128, 112));
            boundaries.add(new Rectangle(1656, 296, 80, 64));
            List<Integer> location1 = new ArrayList<>();
            location1.add(450);//X start
            location1.add(776);//X end
            location1.add(50);//Y start
            location1.add(752);//Y end
            enemyLocations.add(location1);
            List<Integer> location2 = new ArrayList<>();
            location2.add(1150);
            location2.add(1700);
            location2.add(450);
            location2.add(900);
            enemyLocations.add(location2);
            List<Integer> itemL1 = new ArrayList<>();
            itemL1.add(50);//X
            itemL1.add(50);//Y
            itemLocations.add(itemL1);
            List<Integer> itemL2 = new ArrayList<>();
            itemL2.add(300);
            itemL2.add(750);
            itemLocations.add(itemL2);
            shopLocation.add(1750);
            shopLocation.add(800);
        } else if(filename.equals("conference-room.png")){
            boundaries.add(new Rectangle(240, 584, 360, 320));
            boundaries.add(new Rectangle(1480, 584, 360, 320));
            boundaries.add(new Rectangle(16, 256, 328, 144));
            boundaries.add(new Rectangle(496, 256, 464, 144));
            boundaries.add(new Rectangle(1112, 256, 464, 144));
            boundaries.add(new Rectangle(1728, 256, 344, 144));
            boundaries.add(new Rectangle(824, 400, 16, 504));
            boundaries.add(new Rectangle(1232, 400, 16, 504));
            List<Integer> location1 = new ArrayList<>();
            location1.add(48);
            location1.add(880);
            location1.add(40);
            location1.add(224);
            enemyLocations.add(location1);
            List<Integer> location2 = new ArrayList<>();
            location2.add(56);
            location2.add(776);
            location2.add(424);
            location2.add(536);
            enemyLocations.add(location2);
            List<Integer> location3 = new ArrayList<>();
            location3.add(1280);
            location3.add(2032);
            location3.add(424);
            location3.add(536);
            enemyLocations.add(location3);
            List<Integer> location4 = new ArrayList<>();
            location4.add(880);
            location4.add(1184);
            location4.add(432);
            location4.add(680);
            enemyLocations.add(location4);
            List<Integer> location5 = new ArrayList<>();
            location5.add(1184);
            location5.add(2032);
            location5.add(40);
            location5.add(224);
            enemyLocations.add(location5);
            List<Integer> itemL1 = new ArrayList<>();
            itemL1.add(1300);
            itemL1.add(700);
            itemLocations.add(itemL1);
            List<Integer> itemL2 = new ArrayList<>();
            itemL2.add(650);
            itemL2.add(700);
            itemLocations.add(itemL2);
            shopLocation.add(1900);
            shopLocation.add(50);
        } else if(filename.equals("cafeteria.png")){
            boundaries.add(new Rectangle(488, 208, 320, 240)); //Bottom Left Table
            boundaries.add(new Rectangle(488, 608, 320, 248)); //Top Left Table
            boundaries.add(new Rectangle(1128, 208, 320, 248)); //Bottom Right Table
            boundaries.add(new Rectangle(1128, 608, 320, 240)); //Top Right Table
            boundaries.add(new Rectangle(1728, 328, 160, 576)); //Food Bar
            List<Integer> location1 = new ArrayList<>();
            location1.add(200);
            location1.add(440);
            location1.add(48);
            location1.add(856);
            enemyLocations.add(location1);
            List<Integer> location2 = new ArrayList<>();
            location2.add(840);
            location2.add(1096);
            location2.add(300);
            location2.add(600);
            enemyLocations.add(location2);
            List<Integer> location3 = new ArrayList<>();
            location3.add(1480);
            location3.add(1704);
            location3.add(48);
            location3.add(856);
            enemyLocations.add(location3);
            List<Integer> location4 = new ArrayList<>();
            location4.add(48);
            location4.add(840);
            location4.add(48);
            location4.add(184);
            enemyLocations.add(location4);
            List<Integer> location5 = new ArrayList<>();
            location5.add(1480);
            location5.add(2032);
            location5.add(48);
            location5.add(184);
            enemyLocations.add(location5);
            List<Integer> itemL1 = new ArrayList<>();
            itemL1.add(600);
            itemL1.add(450);
            itemLocations.add(itemL1);
            List<Integer> itemL2 = new ArrayList<>();
            itemL2.add(1200);
            itemL2.add(450);
            itemLocations.add(itemL2);
            shopLocation.add(1500);
            shopLocation.add(800);
        } else if(filename.equals("blank-room.png")){
            List<Integer> location1 = new ArrayList<>();
            location1.add(150);
            location1.add(1900);
            location1.add(150);
            location1.add(800);
            enemyLocations.add(location1);
        } else if(filename.equals("boss-room.png")){
            boundaries.add(new Rectangle(24, 48, 168, 368)); //Left Tree
            boundaries.add(new Rectangle(1888, 48, 168, 368)); //Right Tree
            boundaries.add(new Rectangle(256, 112, 152, 544)); //Left Table
            boundaries.add(new Rectangle(1580, 112, 152, 544)); //Right Table
            boundaries.add(new Rectangle(704, 216, 184, 240)); //Left Chair
            boundaries.add(new Rectangle(1200, 216, 184, 240)); //Right Chair
            boundaries.add(new Rectangle(832, 528, 424, 312)); //Desk REMOVE THIS ONCE FINAL BOSS INTERACTABLE IS CREATED
        }

    }

    public List<Rectangle> getBoundaries(){
        return this.boundaries;
    }

    public List<List<Integer>> getEnemyLocations(){
        return enemyLocations;
    }

    public List<List<Integer>> getItemLocations(){
        return itemLocations;
    }

    public List<Integer> getShopLocation(){
        return shopLocation;
    }

}
