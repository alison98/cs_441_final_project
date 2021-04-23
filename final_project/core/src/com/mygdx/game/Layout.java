package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Layout {
    private static Layout instance;
    private List<List<List<Sprite>>> rooms;
    private List<List<List<List<Enemy>>>> enemies;
    private List<List<List<List<Integer>>>> connections;
    private int floor, row, column;
    private int maxFloor, maxRow, maxCol, maxTunnel, maxLen;
    private List<Rectangle> hitboxes;
    private Random random;


    //Singleton class
    private Layout(){
        rooms = new ArrayList<>();
        enemies = new ArrayList<>();
        connections = new ArrayList<>();

        random = new Random();
        //Map size
        maxFloor = 3;
        maxRow =3;
        maxCol =3;
        maxTunnel = 20;
        maxLen = 3;

        //Initial location
        floor = 0;
        row = random.nextInt(maxRow);
        column = random.nextInt(maxCol);
        setupFloor();
        addRoom(0, row, column, "img3.jpg", 1, 0);

        generateMap();

        hitboxes = new ArrayList<>();
        //Bounds can change based on room image
        Rectangle left = new Rectangle(0, Gdx.graphics.getHeight()/2, 50, 150);
        Rectangle right = new Rectangle(Gdx.graphics.getWidth()-50, Gdx.graphics.getHeight()/2, 50, 150);
        Rectangle top = new Rectangle(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()-50, 150, 50);
        Rectangle bottom = new Rectangle(Gdx.graphics.getWidth()/2, 0, 150, 50);
        Rectangle stair = new Rectangle(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 150, 150);
        hitboxes.add(left);
        hitboxes.add(right);
        hitboxes.add(top);
        hitboxes.add(bottom);
        hitboxes.add(stair);

    }

    public static Layout getInstance(){
        if(instance == null){
            instance = new Layout();
        }
        return instance;
    }

    public Sprite getRoom(){
        return rooms.get(floor).get(row).get(column);
    }

    public List<Enemy> getEnemies(){
        return enemies.get(floor).get(row).get(column);
    }
    /*public Integer getHumans(){
        return enemies.get(floor).get(row).get(column)[1];
    }*/

    public Integer changeRoom(Player player, boolean stair){
        if(hitboxes.get(0).overlaps(player.getBounds())){
            if (checkRoom(0)){
                column -=1;
                return 0;
            }
        }else if(hitboxes.get(1).overlaps(player.getBounds())){
            if (checkRoom(1)){
                column +=1;
                return 1;
            }
        }else if(hitboxes.get(2).overlaps(player.getBounds())){
            if (checkRoom(2)){
                row -=1;
                return 2;
            }
        }else if(hitboxes.get(3).overlaps(player.getBounds())){
            if (checkRoom(3)){
                row +=1;
                return 3;
            }
        }else if(stair && hitboxes.get(4).overlaps(player.getBounds())){
            if(floor +1 < rooms.size() && rooms.get(floor+1).get(row).get(column) != null &&
                    rooms.get(floor+1).get(row).get(column).getTexture().toString().equals("img4.jpg")){
                floor +=1;
            }else{
                floor -=1;
            }
            player.moveBy(300,300);
            return 4;
        }

        return null;
    }

    private boolean checkRoom(int direction){
        return connections.get(floor).get(row).get(column).contains(direction);
        /*if(direction ==0){
            if(connections.get(floor).get(row).get(column).contains(0))
            //if(column == 0 || rooms.get(floor).get(row).get(column-1) == null){
                return false;
            }else{
                return true;
            }
        }else if(direction == 1){
            if(column+1 >= rooms.get(floor).get(row).size() || rooms.get(floor).get(row).get(column+1) == null){
                return false;
            }else{
                return true;
            }
        }else if(direction == 2){
            if(row == 0 /*|| column >= rooms.get(floor).get(row-1).size()*/ /*|| rooms.get(floor).get(row-1).get(column) == null){
                return false;
            }else{
                return true;
            }
        }else if(direction == 3){
            if(row+1 >= rooms.get(floor).size() /*|| column >= rooms.get(floor).get(row+1).size()*/ /*|| rooms.get(floor).get(row+1).get(column) == null){
                return false;
            }else{
                return true;
            }
        } else{
            return false;
        }*/
    }

    public List<Integer> possibleRooms(){
        List<Integer> doors = new ArrayList<>();
        for(int i=0;i<4;i++){
            if(checkRoom(i)){
                doors.add(i);
            }
        }
        return doors;
    }

    private void setupFloor(){
        for(int i=0; i<maxFloor; i++) {
            rooms.add(new ArrayList<List<Sprite>>());
            enemies.add(new ArrayList<List<List<Enemy>>>());
            connections.add(new ArrayList<List<List<Integer>>>());
            for (int j = 0; j < maxRow; j++) {
                rooms.get(i).add(new ArrayList<Sprite>());
                enemies.get(i).add(new ArrayList<List<Enemy>>());
                connections.get(i).add(new ArrayList<List<Integer>>());
                for (int k = 0; k < maxCol; k++) {
                    rooms.get(i).get(j).add(null);
                    enemies.get(i).get(j).add(new ArrayList<Enemy>());
                    connections.get(i).get(j).add(new ArrayList<Integer>());
                }
            }
        }
    }

    private void generateMap(){
        //setupFloor();
        int nextRow = row;
        int nextCol = column;
        for(int j=0; j<maxFloor;j++) {
            int currRow = nextRow;
            int currCol = nextCol;
            boolean stair =true;
            if(j == maxFloor -1){
                stair = false;
            }
            for (int k=0; k<maxTunnel; k++) {
                int length = random.nextInt(maxLen) + 1;
                int direction = random.nextInt(4);
                for (int i = 0; i < length; i++) {
                    if ((direction == 0 && currCol == 0) /*Left*/ || (direction == 1 && currCol == maxCol - 1)/*Right*/
                            || (direction == 2 && currRow == 0)/*Up*/ || (direction == 3 && currRow == maxRow - 1)/*Down*/) {
                        break;
                    }
                    connections.get(j).get(currRow).get(currCol).add(direction);
                    if (direction == 0) {
                        currCol -= 1;
                        connections.get(j).get(currRow).get(currCol).add(1);
                    } else if (direction == 1) {
                        currCol += 1;
                        connections.get(j).get(currRow).get(currCol).add(0);
                    } else if (direction == 2) {
                        currRow -= 1;
                        connections.get(j).get(currRow).get(currCol).add(3);
                    } else if (direction == 3) {
                        currRow += 1;
                        connections.get(j).get(currRow).get(currCol).add(2);
                    }
                    if (rooms.get(j).get(currRow).get(currCol) == null) {
                        //need to change to random room
                        //if certain room do something else
                        int stairRoom = random.nextInt(2);
                        if(stairRoom == 1 && stair) {
                            addRoom(j, currRow, currCol, "img4.jpg", 0, 0);
                            stair = false;
                            nextRow = currRow;
                            nextCol = currCol;
                            addRoom(j+1, currRow, currCol, "img4.jpg", 0, 0);
                        }else {
                            addRoom(j, currRow, currCol, "img3.jpg", random.nextInt(2) + 1, 0);
                        }
                    }
                }
            }
        }
    }

    private void addRoom(int floorIn, int rowIn, int columnIn, String sprite, int enemy, int human){
        rooms.get(floorIn).get(rowIn).set(columnIn, new Sprite(new Texture(sprite)));
        for(int i=0; i<enemy; i++){
            enemies.get(floorIn).get(rowIn).get(columnIn).add(new Enemy(random.nextInt(1000)+100,random.nextInt(700)+100,2,random.nextInt(5),1));
        }
        for(int i=0; i<human; i++){
            Enemy npc = new Enemy(random.nextInt(1000)+100,random.nextInt(700)+100,2,random.nextInt(5),1);
            npc.setHealth(0);
            enemies.get(floorIn).get(rowIn).get(columnIn).add(npc);
        }
    }

    public void setEnemies(){
        //enemies.get(floor).get(row).get(column)[0] -= 1;
        //enemies.get(floor).get(row).get(column)[1] += 1;
    }
}
