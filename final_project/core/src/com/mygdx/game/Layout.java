package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Layout {
    private static Layout instance;
    private List<List<List<Sprite>>> rooms;
    private List<List<List<List<Enemy>>>> enemies;
    private List<List<List<List<Integer>>>> connections;
    private List<List<List<List<Interactable>>>> interactables;
    private List<Integer[]> bossRooms;
    private List<List<Integer[]>> stairRooms;
    private int floor, row, column;
    private int maxFloor, maxRow, maxCol, maxTunnel, maxLen;
    private List<Rectangle> hitboxes;
    private List<Boolean> keys, bosses;
    private Random random;
    private Texture stairUpImg;


    //Singleton class
    private Layout(){
        rooms = new ArrayList<>();
        enemies = new ArrayList<>();
        interactables = new ArrayList<>();
        connections = new ArrayList<>();
        stairRooms = new ArrayList<>();
        bossRooms = new ArrayList<>();
        keys = new ArrayList<>();
        bosses = new ArrayList<>();

        random = new Random();
        //Map size
        maxFloor = 4;
        maxRow =3;
        maxCol =3;
        maxTunnel = 4;
        maxLen = 3;

        //Initial location
        floor = 0;
        row = random.nextInt(maxRow);
        column = random.nextInt(maxCol-1)+1;

        generateMap();

        stairUpImg = new Texture("stairs-up.png");

        hitboxes = new ArrayList<>();
        //Bounds can change based on room image
        Rectangle left = new Rectangle(0, Gdx.graphics.getHeight()/2, 50, 150);
        Rectangle right = new Rectangle(Gdx.graphics.getWidth()-50, Gdx.graphics.getHeight()/2, 50, 150);
        Rectangle top = new Rectangle(1008, 888, 72, 50);
        Rectangle bottom = new Rectangle(1008, 0, 72, 50);
        Rectangle stair = new Rectangle(Gdx.graphics.getWidth() - stairUpImg.getWidth(), Gdx.graphics.getHeight() - Gdx.graphics.getHeight()/3, stairUpImg.getWidth(), stairUpImg.getHeight());
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

    public List<Interactable> getInteractables() {return interactables.get(floor).get(row).get(column);}

    public Boolean getKey(){
        return keys.get(floor);
    }

    public void setKey(int floorIn){
        keys.set(floorIn, true);
    }

    public Boolean getBoss(){
        return bosses.get(floor);
    }

    public void setBoss(int floorIn){
        bosses.set(floorIn, true);
    }

    public boolean getDoorTouched(Player player){
        if(hitboxes.get(0).overlaps(player.getBounds())){
            if (checkRoom(0)){
                return true;
            }
        }else if(hitboxes.get(1).overlaps(player.getBounds())){
            if (checkRoom(1)){
                return true;
            }
        }else if(hitboxes.get(2).overlaps(player.getBounds())){
            if (checkRoom(2)){
                return true;
            }
        }else if(hitboxes.get(3).overlaps(player.getBounds())){
            if (checkRoom(3)){
                return true;
            }
        }else if(isStairs() && hitboxes.get(4).overlaps(player.getBounds())){
            if(getBoss() || downFloor()) {
                return true;
            }
        }

        return false;
    }

    public Integer changeRoom(Player player){
        if(bossRooms.get(floor)[0] == row && bossRooms.get(floor)[1] == column && !getBoss()){
            return -1;
        }
        if(hitboxes.get(0).overlaps(player.getBounds())){
            if (bossRooms.get(floor)[0] == row
                    && bossRooms.get(floor)[1] == column - 1 && !getKey()) {
                return -1;
            }else if (checkRoom(0)){
                column -=1;
                return 0;
            }
        }else if(hitboxes.get(1).overlaps(player.getBounds())){
            if (bossRooms.get(floor)[0] == row
                    && bossRooms.get(floor)[1] == column + 1 && !getKey()) {
                return -1;
            }else if (checkRoom(1)){
                column +=1;
                return 1;
            }
        }else if(hitboxes.get(2).overlaps(player.getBounds())){
            if (bossRooms.get(floor)[0] == row - 1
                    && bossRooms.get(floor)[1] == column && !getKey()) {
                return -1;
            }else if (checkRoom(2)){
                row -=1;
                return 2;
            }
        }else if(hitboxes.get(3).overlaps(player.getBounds())){
            if (bossRooms.get(floor)[0] == row + 1
                    && bossRooms.get(floor)[1] == column && !getKey()) {
                return -1;
            }else if (checkRoom(3)){
                row +=1;
                return 3;
            }
        }else if(isStairs() && hitboxes.get(4).overlaps(player.getBounds())){
            if(!downFloor() && getBoss()){
                respawn();
                floor +=1;
            }else{
                respawn();
                floor -=1;
            }
            //player.moveBy(200,0);
            return 4;
        }

        return null;
    }

    public boolean downFloor() {
        for (int i = 0; i < stairRooms.get(floor).size(); i++) {
            if (stairRooms.get(floor).get(i)[0] == row && stairRooms.get(floor).get(i)[1] == column && stairRooms.get(floor).get(i)[2] == 0) {
            /*if(floor +1 < rooms.size() && rooms.get(floor+1).get(row).get(column) != null &&
                rooms.get(floor+1).get(row).get(column).getTexture().toString().equals("img4.jpg")){*/
                return true;
            }
        }
        return false;
    }

    public boolean isStairs(){
        for (int i = 0; i < stairRooms.get(floor).size(); i++) {
            if (stairRooms.get(floor).get(i)[0] == row && stairRooms.get(floor).get(i)[1] == column) {
                return true;
            }
        }
        return false;
    }

    private Boolean checkRoom(int direction){
        return connections.get(floor).get(row).get(column).contains(direction);
    }

    public List<Integer> possibleRooms(){
        List<Integer> doors = new ArrayList<>();
        for(int i=0;i<4;i++) {
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
            interactables.add(new ArrayList<List<List<Interactable>>>());
            for (int j = 0; j < maxRow; j++) {
                rooms.get(i).add(new ArrayList<Sprite>());
                enemies.get(i).add(new ArrayList<List<Enemy>>());
                connections.get(i).add(new ArrayList<List<Integer>>());
                interactables.get(i).add(new ArrayList<List<Interactable>>());
                for (int k = 0; k < maxCol; k++) {
                    rooms.get(i).get(j).add(null);
                    enemies.get(i).get(j).add(new ArrayList<Enemy>());
                    connections.get(i).get(j).add(new ArrayList<Integer>());
                    interactables.get(i).get(j).add(new ArrayList<Interactable>());
                }
            }
        }
    }

    private void generateMap(){
        setupFloor();

        int originalCol =column;

        //tutorial floor
        addRoom(0, row, column, "office-space-no-printer.png", 0, 0);
        connections.get(0).get(row).get(column).add(0);
        column -=1;

        addRoom(0, row, column, "img4.jpg", 0, 0);
        connections.get(0).get(row).get(column).add(1);
        List<Integer[]> stairUp = new ArrayList<>();
        Integer[] stairs = new Integer[3];
        stairs[0] = row;
        stairs[1] = column;
        stairs[2] = 1;
        stairUp.add(stairs);
        stairRooms.add(stairUp);

        addRoom(1, row, column, "img4.jpg", 0, 0);
        List<Integer[]> stairDown = new ArrayList<>();
        Integer[] stairs2 = new Integer[3];
        stairs2[0] = row;
        stairs2[1] = column;
        stairs2[2] = 0;
        stairDown.add(stairs2);
        stairRooms.add(stairDown);
        keys.add(false);
        bosses.add(false);

        int nextRow = row;
        int nextCol = column;
        for(int j=1; j<maxFloor;j++) {
            int currRow = nextRow;
            int currCol = nextCol;
            keys.add(false);
            bosses.add(false);
            for (int k=0; k<maxTunnel; k++) {
                int length = random.nextInt(maxLen) + 1;
                List<Integer> possibleDir = new ArrayList<>();
                if (currCol != 0){ /*Left*/
                    possibleDir.add(0);
                }
                if(currCol != maxCol - 1) {/*Right*/
                    possibleDir.add(1);
                }
                if(currRow != 0){/*Up*/
                    possibleDir.add(2);
                }
                if(currRow != maxRow - 1){/*Down*/
                    possibleDir.add(3);
                }
                Collections.shuffle(possibleDir);
                int direction = possibleDir.get(0);
                for (int i = 0; i < length; i++) {
                    if ((direction == 0 && currCol == 0) || (direction == 1 && currCol == maxCol - 1)
                            || (direction == 2 && currRow == 0)|| (direction == 3 && currRow == maxRow - 1)) {
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
                        addRoom(j, currRow, currCol, "img3.jpg", random.nextInt(2) + 1, 0);

                    }
                }
            }
            if(j != maxFloor -1) {
                List<Integer> nextRoom = addStairs(j);
                nextRow = nextRoom.get(0);
                nextCol = nextRoom.get(1);
            }
            addKey(j);
            addBoss(j,"office-space-no-printer.png");
        }
        column = originalCol;
    }

    private void addRoom(int floorIn, int rowIn, int columnIn, String sprite, int enemy, int human){
        if(floorIn ==0){ //tutorial floor
            rooms.get(floorIn).get(rowIn).set(columnIn, new Sprite(new Texture(sprite)));
            if(rooms.get(floorIn).get(rowIn).get(columnIn).getTexture().toString().equals("office-space-no-printer.png")){
                interactables.get(floorIn).get(rowIn).get(columnIn).add(new TutorialPrinter("printer-shadow.png", floorIn, rowIn, columnIn, 0));
                interactables.get(floorIn).get(rowIn).get(columnIn).get(0).setPosition(1528, 200);
                Integer[] tutorial = new Integer[2];
                tutorial[0] = rowIn;
                tutorial[1] = columnIn;
                bossRooms.add(tutorial);
            }
            return;
        }
        rooms.get(floorIn).get(rowIn).set(columnIn, new Sprite(new Texture(sprite)));
        for(int i=0; i<enemy; i++){
            enemies.get(floorIn).get(rowIn).get(columnIn).add(new Enemy(random.nextInt(1000)+100,random.nextInt(700)+100,2,random.nextInt(5),floorIn));
        }


        for(int i=0; i<human; i++){
            Enemy npc = new Enemy(random.nextInt(1000)+100,random.nextInt(700)+100,2,0,floorIn);
            npc.setHuman();
            enemies.get(floorIn).get(rowIn).get(columnIn).add(npc);
        }
    }

    private List<Integer> addStairs(int floorIn){
        List<List<Integer>> possibleRooms = new ArrayList<>();

        for(int i=0; i<rooms.get(floorIn).size(); i++){
            for(int j=0; j<rooms.get(floorIn).get(i).size(); j++){
                if(rooms.get(floorIn).get(i).get(j) != null && !(stairRooms.get(floorIn).get(0)[0]==i && stairRooms.get(floorIn).get(0)[1]==j)){
                //if(rooms.get(floorIn).get(i).get(j) != null && !rooms.get(floorIn).get(i).get(j).getTexture().toString().equals("img4.jpg")){
                    List<Integer> thisRoom = new ArrayList<>();
                    thisRoom.add(i);
                    thisRoom.add(j);
                    possibleRooms.add(thisRoom);
                }
            }
        }
        int nextRoom = random.nextInt(possibleRooms.size()); //BTW I got a crash here once (1/100 chance) where possibleRooms.size() was 0 (I think)
        Integer[] stairs = new Integer[3];
        stairs[0] = possibleRooms.get(nextRoom).get(0);
        stairs[1] = possibleRooms.get(nextRoom).get(1);
        stairs[2] = 1;
        stairRooms.get(floorIn).add(stairs);
        addRoom(floorIn,possibleRooms.get(nextRoom).get(0),possibleRooms.get(nextRoom).get(1), "img4.jpg",0,0);
        addRoom(floorIn+1,possibleRooms.get(nextRoom).get(0),possibleRooms.get(nextRoom).get(1), "img4.jpg",0,0);
        List<Integer[]> stairDown = new ArrayList<>();
        Integer[] stairs2 = new Integer[3];
        stairs2[0] = possibleRooms.get(nextRoom).get(0);
        stairs2[1] = possibleRooms.get(nextRoom).get(1);
        stairs2[2] = 0;
        stairDown.add(stairs2);
        stairRooms.add(stairDown);
        return possibleRooms.get(nextRoom);
    }

    private void addKey(int floorIn){
        List<Enemy> possibleEnemy = new ArrayList<>();

        for(int i=0; i<rooms.get(floorIn).size(); i++){
            for(int j=0; j<rooms.get(floorIn).get(i).size(); j++){
                if(rooms.get(floorIn).get(i).get(j) != null && !enemies.get(floorIn).get(i).get(j).isEmpty()){
                    for(int k=0; k<enemies.get(floorIn).get(i).get(j).size(); k++){
                        if(enemies.get(floorIn).get(i).get(j).get(k).getHealth()>0){
                            possibleEnemy.add(enemies.get(floorIn).get(i).get(j).get(k));
                        }
                    }
                }
            }
        }

        int keyEnemy = random.nextInt(possibleEnemy.size());//I got an error here once, "bound must be positive" (possibleEnemy.size() <=0)
        possibleEnemy.get(keyEnemy).setKey();
    }

    private void addBoss(int floorIn, String sprite){
        List<List<Integer>> possibleRooms = new ArrayList<>();
        int originalRow = row;
        int originalCol = column;

        for(row=0; row<rooms.get(floorIn).size(); row++){
            for(column=0; column<rooms.get(floorIn).get(row).size(); column++){
                if(rooms.get(floorIn).get(row).get(column) != null){
                    if(column !=0 && rooms.get(floorIn).get(row).get(column-1) == null){// if left is null
                        List<Integer> thisRoom = new ArrayList<>();
                        thisRoom.add(row);
                        thisRoom.add(column-1);
                        thisRoom.add(1); //direction
                        possibleRooms.add(thisRoom);

                    }
                    if(column != maxCol-1 && rooms.get(floorIn).get(row).get(column+1) == null){//if right is null
                        List<Integer> thisRoom = new ArrayList<>();
                        thisRoom.add(row);
                        thisRoom.add(column+1);
                        thisRoom.add(0); //direction
                        possibleRooms.add(thisRoom);
                    }
                    if(row != 0 && rooms.get(floorIn).get(row-1).get(column) == null){//if top is null
                        List<Integer> thisRoom = new ArrayList<>();
                        thisRoom.add(row-1);
                        thisRoom.add(column);
                        thisRoom.add(3); //direction
                        possibleRooms.add(thisRoom);
                    }
                    if(row != maxRow-1 && rooms.get(floorIn).get(row+1).get(column) == null){//if bottom is null
                        List<Integer> thisRoom = new ArrayList<>();
                        thisRoom.add(row+1);
                        thisRoom.add(column);
                        thisRoom.add(2); //direction
                        possibleRooms.add(thisRoom);
                    }
                }
            }
        }
        int nextRoom = random.nextInt(possibleRooms.size());
        int newRow = possibleRooms.get(nextRoom).get(0);
        int newCol = possibleRooms.get(nextRoom).get(1);
        int direction = possibleRooms.get(nextRoom).get(2);

        Integer[] bossRoom = new Integer[2];
        bossRoom[0] = newRow;
        bossRoom[1] = newCol;
        bossRooms.add(bossRoom);

        rooms.get(floorIn).get(newRow).set(newCol, new Sprite(new Texture(sprite)));
        enemies.get(floorIn).get(newRow).get(newCol).add(new Enemy(1000,300,2,0,floorIn));
        enemies.get(floorIn).get(newRow).get(newCol).get(0).setBoss();

        connections.get(floorIn).get(newRow).get(newCol).add(direction);
        if (direction == 0) {
            newCol -= 1;
            connections.get(floorIn).get(newRow).get(newCol).add(1);
        } else if (direction == 1) {
            newCol += 1;
            connections.get(floorIn).get(newRow).get(newCol).add(0);
        } else if (direction == 2) {
            newRow -= 1;
            connections.get(floorIn).get(newRow).get(newCol).add(3);
        } else if (direction == 3) {
            newRow+= 1;
            connections.get(floorIn).get(newRow).get(newCol).add(2);
        }

        row = originalRow;
        column = originalCol;
        //return possibleRooms.get(nextRoom);
    }

    private void respawn(){
        for(int i=0; i<enemies.get(floor).size(); i++){
            for(int j=0; j<enemies.get(floor).get(i).size(); j++){
                for(int k=0; k<enemies.get(floor).get(i).get(j).size(); k++){
                    enemies.get(floor).get(i).get(j).get(k).respawn();
                }
            }
        }
    }

    public void removeInteractable(int floor, int row, int column, int index){
        interactables.get(floor).get(row).get(column).remove(index);
    }

    public void defeat(){
        for(int i=0; i<stairRooms.get(floor).size(); i++){
            if(stairRooms.get(floor).get(i)[2] == 0){
                row = stairRooms.get(floor).get(i)[0];
                column = stairRooms.get(floor).get(i)[1];
                //Player position may also need to change
                respawn();
            }
        }
    }
}
