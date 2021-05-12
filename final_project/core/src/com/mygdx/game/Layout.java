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
    private List<String> roomNames, enemyNames, bossNames;
    private List<List<List<String>>> shopItems;


    //Singleton class
    private Layout(){
        rooms = new ArrayList<>();
        enemies = new ArrayList<>();
        interactables = new ArrayList<>();
        connections = new ArrayList<>();
        stairRooms = new ArrayList<>();
        bossRooms = new ArrayList<>();
        //keyRooms = new ArrayList<>();
        keys = new ArrayList<>();
        bosses = new ArrayList<>();
        shopItems = new ArrayList<>();

        random = new Random();
        //Map size
        maxFloor = 4;
        maxRow =3;
        maxCol =3;
        maxTunnel = 4;//needs to be impossible to fill map in this many moves
        maxLen = 3;// or decrease len to make it impossible to fill map

        //Initial location
        floor = 0;
        row = random.nextInt(maxRow);
        column = random.nextInt(maxCol-1)+1;

        addRoomNames();
        addEnemyNames();
        addBossNames();
        generateMap();

        setupShop();

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

    //matt needed for putting back at stairs
    public Texture getStairUpImg(){return stairUpImg;}

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

    public List<List<String>> getShopItems(int floorIn){
        return shopItems.get(floorIn-1);
    }

    public int getFloor(){return floor;}

    private void addRoomNames(){
        //Add room names here to be randomly chosen
        roomNames = new ArrayList<>();
        //roomNames.add("blank-room.png");
        roomNames.add("server-closet.png");
        roomNames.add("conference-room.png");
        roomNames.add("cafeteria.png");
    }
    private void addEnemyNames(){
        //Add enemy names here to be randomly chosen during room creation
        enemyNames = new ArrayList<>();
        enemyNames.add("100 Meter Print.png");
        enemyNames.add("Battlefax.png");
        enemyNames.add("X-Box 360.png");
    }
    private void addBossNames(){
        //Add Bosses here
        //Size must equal the maxFloor+1
        bossNames = new ArrayList<>();
        bossNames.add("An Angry Printer.png");
        bossNames.add("Death and Faxes.png");
        bossNames.add("Box Ness Monster.png");
        bossNames.add("Printer is Coming.png");
        bossNames.add("The CEO.png");
    }

    private void setupShop(){
        //max 8 items including close
        List<List<String>> floor1 = new ArrayList<>();
        floor1.add(addItem("Coffee","1", "multi"));
        floor1.add(addItem("Coffee","1", "multi"));
        floor1.add(addItem("Coffee","1", "multi"));
        floor1.add(addItem("Tea","0", "single"));
        floor1.add(addItem("Tea","0", "multi"));
        floor1.add(addItem("Coffee","0", "single"));
        floor1.add(addItem("Coffee","0", "multi"));
        floor1.add(addItem("close","0", "multi"));
        shopItems.add(floor1);
        List<List<String>> floor2 = new ArrayList<>();
        floor2.add(addItem("Coffee","1", "multi"));
        floor2.add(addItem("Coffee","1", "multi"));
        floor2.add(addItem("Coffee","1", "multi"));
        floor2.add(addItem("Coffee","1", "multi"));
        floor2.add(addItem("Coffee","1", "multi"));
        floor2.add(addItem("Coffee","1", "multi"));
        floor2.add(addItem("Coffee","0", "multi"));
        floor2.add(addItem("close","0", "multi"));
        shopItems.add(floor2);
        List<List<String>> floor3 = new ArrayList<>();
        floor3.add(addItem("Coffee","1", "multi"));
        floor3.add(addItem("Coffee","1", "multi"));
        floor3.add(addItem("Coffee","1", "multi"));
        floor3.add(addItem("Coffee","1", "multi"));
        floor3.add(addItem("Coffee","1", "multi"));
        floor3.add(addItem("Coffee","1", "multi"));
        floor3.add(addItem("Coffee","0", "multi"));
        floor3.add(addItem("close","0", "multi"));
        shopItems.add(floor3);
        List<List<String>> floor4 = new ArrayList<>();
        floor4.add(addItem("Coffee","1", "multi"));
        floor4.add(addItem("Coffee","1", "multi"));
        floor4.add(addItem("Coffee","1", "multi"));
        floor4.add(addItem("Coffee","1", "multi"));
        floor4.add(addItem("Coffee","1", "multi"));
        floor4.add(addItem("Coffee","1", "multi"));
        floor4.add(addItem("Coffee","0", "multi"));
        floor4.add(addItem("close","0", "multi"));
        shopItems.add(floor4);
    }

    private List<String> addItem(String item, String price, String type){
        List<String> newItem = new ArrayList<>();
        newItem.add(item);
        newItem.add(price);
        newItem.add(type);
        return newItem;
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
            return -1;//Door locked
        }
        if(hitboxes.get(0).overlaps(player.getBounds())){
            if (bossRooms.get(floor)[0] == row
                    && bossRooms.get(floor)[1] == column - 1 && !getKey()) {
                return -1;//Door locked
            }else if (checkRoom(0)){
                column -=1;
                return 0;
            }
        }else if(hitboxes.get(1).overlaps(player.getBounds())){
            if (bossRooms.get(floor)[0] == row
                    && bossRooms.get(floor)[1] == column + 1 && !getKey()) {
                return -1;//Door locked
            }else if (checkRoom(1)){
                column +=1;
                return 1;
            }
        }else if(hitboxes.get(2).overlaps(player.getBounds())){
            if (bossRooms.get(floor)[0] == row - 1
                    && bossRooms.get(floor)[1] == column && !getKey()) {
                return -1;//Door locked
            }else if (checkRoom(2)){
                row -=1;
                return 2;
            }
        }else if(hitboxes.get(3).overlaps(player.getBounds())){
            if (bossRooms.get(floor)[0] == row + 1
                    && bossRooms.get(floor)[1] == column && !getKey()) {
                return -1;//Door locked
            }else if (checkRoom(3)){
                row +=1;
                return 3;
            }
        }else if(isStairs() && hitboxes.get(4).overlaps(player.getBounds())){
            if(floor == maxFloor){
                floor = floor-1;
                for (int i = 0; i < stairRooms.get(floor).size(); i++) {
                    if (stairRooms.get(floor).get(i)[2] == 1) {
                        row = stairRooms.get(floor).get(i)[0];
                        column = stairRooms.get(floor).get(i)[1];
                    }
                }
            }else if(floor == maxFloor-1 && !downFloor() && getBoss()){
                respawn();
                floor = floor+1;
                row = 1;
                column = 0;
            }else if(!downFloor() && getBoss()){
                respawn();
                floor +=1;
            }else{
                respawn();
                floor -=1;
            }
            return 4;
        }

        return null;
    }

    /*Determine if the current room has stairs that go down*/
    public boolean downFloor() {
        if(floor == maxFloor && row ==1 && column == 0){
            return true;
        }
        for (int i = 0; i < stairRooms.get(floor).size(); i++) {
            if (stairRooms.get(floor).get(i)[0] == row && stairRooms.get(floor).get(i)[1] == column && stairRooms.get(floor).get(i)[2] == 0) {
                return true;
            }
        }
        return false;
    }

    /*Determine if the current room contains stairs*/
    public boolean isStairs(){
        for (int i = 0; i < stairRooms.get(floor).size(); i++) {
            if (stairRooms.get(floor).get(i)[0] == row && stairRooms.get(floor).get(i)[1] == column) {
                return true;
            }
        }
        return false;
    }

    /*Check if there is a door in this direction*/
    private Boolean checkRoom(int direction){
        return connections.get(floor).get(row).get(column).contains(direction);
    }

    /*Return the direction of all the rooms from the current room*/
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

        //Final Boss floor
        rooms.add(new ArrayList<List<Sprite>>());
        rooms.add(new ArrayList<List<Sprite>>());
        enemies.add(new ArrayList<List<List<Enemy>>>());
        connections.add(new ArrayList<List<List<Integer>>>());
        interactables.add(new ArrayList<List<List<Interactable>>>());

        for (int i = 0; i < 2; i++) {
            rooms.get(maxFloor).add(new ArrayList<Sprite>());
            enemies.get(maxFloor).add(new ArrayList<List<Enemy>>());
            connections.get(maxFloor).add(new ArrayList<List<Integer>>());
            interactables.get(maxFloor).add(new ArrayList<List<Interactable>>());

            rooms.get(maxFloor).get(i).add(null);
            enemies.get(maxFloor).get(i).add(new ArrayList<Enemy>());
            connections.get(maxFloor).get(i).add(new ArrayList<Integer>());
            interactables.get(maxFloor).get(i).add(new ArrayList<Interactable>());
        }


    }

    private void generateMap(){
        setupFloor();

        int originalCol =column;

        //tutorial floor
        addRoom(0, row, column, "office-space-no-printer.png", 0);
        connections.get(0).get(row).get(column).add(0);
        column -=1;

        int firstRoom = random.nextInt(roomNames.size());
        addRoom(0, row, column, roomNames.get(firstRoom), 0);
        connections.get(0).get(row).get(column).add(1);
        List<Integer[]> stairUp = new ArrayList<>();
        Integer[] stairs = new Integer[3];
        stairs[0] = row;
        stairs[1] = column;
        stairs[2] = 1;
        stairUp.add(stairs);
        stairRooms.add(stairUp);

        List<String> available = new ArrayList<>(roomNames);
        available.remove(firstRoom);
        addRoom(1, row, column, available.get(random.nextInt(available.size())), 0);
        List<Integer[]> stairDown = new ArrayList<>();
        Integer[] stairs2 = new Integer[3];
        stairs2[0] = row;
        stairs2[1] = column;
        stairs2[2] = 0;
        stairDown.add(stairs2);
        stairRooms.add(stairDown);
        keys.add(false);
        bosses.add(false);
        addShop(1,row,column);

        //Create middle floors
        int nextRow = row;
        int nextCol = column;
        for(int j=1; j<maxFloor;j++) {
            int currRow = nextRow;
            int currCol = nextCol;
            keys.add(false);
            bosses.add(false);
            for (int k=0; k<maxTunnel; k++) {
                int length = random.nextInt(maxLen) + 1;

                //To try to assure that the floor is at least 2 rooms before boss room added
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
                        addRoom(j, currRow, currCol, roomNames.get(random.nextInt(roomNames.size())), random.nextInt(2) + 1);

                    }
                }
            }
            if(j != maxFloor -1) {
                List<Integer> nextRoom = addStairs(j);
                nextRow = nextRoom.get(0);
                nextCol = nextRoom.get(1);
            }
            addKey(j);
            addBoss(j,"blank-room.png"); //may not need sprite if all boss rooms the same
        }
        column = originalCol;

        addFinalBoss();
        bosses.add(false);
        keys.add(true);

    }

    private void addRoom(int floorIn, int rowIn, int columnIn, String sprite, int enemy){
        if(floorIn ==0){ //tutorial floor
            rooms.get(floorIn).get(rowIn).set(columnIn, new Sprite(new Texture(sprite)));
            if(rooms.get(floorIn).get(rowIn).get(columnIn).getTexture().toString().equals("office-space-no-printer.png")){
                interactables.get(floorIn).get(rowIn).get(columnIn).add(new TutorialPrinter("printer-shadow.png", floorIn, rowIn, columnIn, 0, 1528, 200));
                //interactables.get(floorIn).get(rowIn).get(columnIn).get(0).setPosition(1528, 200);
                Integer[] tutorial = new Integer[2];
                tutorial[0] = rowIn;
                tutorial[1] = columnIn;
                bossRooms.add(tutorial);
            }
            return;
        }
        Boundary.getInstance().setBoundaries(sprite);
        List<List<Integer>> spawnLocation = Boundary.getInstance().getEnemyLocations();
        rooms.get(floorIn).get(rowIn).set(columnIn, new Sprite(new Texture(sprite)));
        for(int i=0; i<enemy; i++){
            List<Integer> range = spawnLocation.get(random.nextInt(spawnLocation.size()));
            enemies.get(floorIn).get(rowIn).get(columnIn).add(
                    new Enemy(0,0,2,enemyNames.get(random.nextInt(enemyNames.size())),floorIn,range));
            if(spawnLocation.size() > 1){
                spawnLocation.remove(range);
            }
        }
        int possibility = random.nextInt(5);
        if(possibility ==0) {
            List<List<Integer>> itemLocations = Boundary.getInstance().getItemLocations();
            List<Integer> itemL = itemLocations.get(random.nextInt(itemLocations.size()));
            interactables.get(floorIn).get(rowIn).get(columnIn).add(new Item("coffee.png", floorIn, rowIn, columnIn, 0, itemL.get(0), itemL.get(1)));
        }
    }

    private List<Integer> addStairs(int floorIn){
        List<List<Integer>> possibleRooms = new ArrayList<>();

        for(int i=0; i<rooms.get(floorIn).size(); i++){
            for(int j=0; j<rooms.get(floorIn).get(i).size(); j++){
                if(rooms.get(floorIn).get(i).get(j) != null && !(stairRooms.get(floorIn).get(0)[0]==i && stairRooms.get(floorIn).get(0)[1]==j)){
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
        //addRoom(floorIn,possibleRooms.get(nextRoom).get(0),possibleRooms.get(nextRoom).get(1), "blank-room.png",0,0);
        String prevFloor = rooms.get(floorIn).get(stairs[0]).get(stairs[1]).getTexture().toString();
        List<String> available = new ArrayList<>(roomNames);
        available.remove(prevFloor);
        addRoom(floorIn+1,possibleRooms.get(nextRoom).get(0),possibleRooms.get(nextRoom).get(1), available.get(random.nextInt(available.size())),0);
        List<Integer[]> stairDown = new ArrayList<>();
        Integer[] stairs2 = new Integer[3];
        stairs2[0] = possibleRooms.get(nextRoom).get(0);
        stairs2[1] = possibleRooms.get(nextRoom).get(1);
        stairs2[2] = 0;
        stairDown.add(stairs2);
        stairRooms.add(stairDown);
        addShop(floorIn+1,stairs2[0],stairs2[1]);
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

        int keyEnemy = random.nextInt(possibleEnemy.size());
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
        interactables.get(floorIn).get(newRow).get(newCol).add(new Boss(bossNames.get(floorIn), floorIn, newRow, newCol, 0, 1000, 400));

        //enemies.get(floorIn).get(newRow).get(newCol).add(new Enemy(1000,400,2,bossNames.get(floorIn),floorIn, null));
        //enemies.get(floorIn).get(newRow).get(newCol).get(0).setBoss();

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
    }

    private void addFinalBoss(){
        //Stairs of top floor
        List<List<Integer>> possibleRooms = new ArrayList<>();

        for(int i=0; i<rooms.get(maxFloor-1).size(); i++){
            for(int j=0; j<rooms.get(maxFloor-1).get(i).size(); j++){
                if(rooms.get(maxFloor-1).get(i).get(j) != null && !(stairRooms.get(maxFloor-1).get(0)[0]==i && stairRooms.get(maxFloor-1).get(0)[1]==j)
                        && !(bossRooms.get(maxFloor-1)[0]== i && bossRooms.get(maxFloor-1)[1] == j)){
                    List<Integer> thisRoom = new ArrayList<>();
                    thisRoom.add(i);
                    thisRoom.add(j);
                    possibleRooms.add(thisRoom);
                }
            }
        }
        int nextRoom = random.nextInt(possibleRooms.size());
        Integer[] stairs = new Integer[3];
        stairs[0] = possibleRooms.get(nextRoom).get(0);
        stairs[1] = possibleRooms.get(nextRoom).get(1);
        stairs[2] = 1;
        stairRooms.get(maxFloor-1).add(stairs);

        //Connect the stairs to the boss floor
        String prevFloor = rooms.get(maxFloor-1).get(stairs[0]).get(stairs[1]).getTexture().toString();
        List<String> available = new ArrayList<>(roomNames);
        available.remove(prevFloor);
        addRoom(maxFloor,1,0, available.get(random.nextInt(available.size())),0);
        List<Integer[]> stairDown = new ArrayList<>();
        Integer[] stairs2 = new Integer[3];
        stairs2[0] = 1;
        stairs2[1] = 0;
        stairs2[2] = 0;
        stairDown.add(stairs2);
        stairRooms.add(stairDown);
        addShop(maxFloor,stairs2[0],stairs2[1]);

        rooms.get(maxFloor).get(0).set(0, new Sprite(new Texture("boss-room.png")));
        interactables.get(maxFloor).get(0).get(0).add(new Boss(bossNames.get(maxFloor), maxFloor, 0, 0, 0, 1000, 400));

        //enemies.get(maxFloor).get(0).get(0).add(new Enemy(1000,400,2,bossNames.get(maxFloor),maxFloor, null));
        //enemies.get(maxFloor).get(0).get(0).get(0).setBoss("PUT A MESSAGE FOR THE FINAL BOSS TO SAY AFTER DYING HERE");

        connections.get(maxFloor).get(0).get(0).add(3);
        connections.get(maxFloor).get(1).get(0).add(2);

        Integer[] bossRoom = new Integer[2];
        bossRoom[0] = 0;
        bossRoom[1] = 0;
        bossRooms.add(bossRoom);
    }

    private void addShop(int floorIn, int rowIn, int columnIn){
        Boundary.getInstance().setBoundaries(rooms.get(floorIn).get(rowIn).get(columnIn).getTexture().toString());
        List<Integer> spawnLocation = Boundary.getInstance().getShopLocation();
        interactables.get(floorIn).get(rowIn).get(columnIn).add(new Shop("human2-6x.png",floorIn,rowIn,columnIn,1,spawnLocation.get(0),spawnLocation.get(1)));
    }

    private void respawn(){
        for(int i=0; i<enemies.get(floor).size(); i++){
            for(int j=0; j<enemies.get(floor).get(i).size(); j++){
                for(int k=0; k<enemies.get(floor).get(i).get(j).size(); k++){
                    enemies.get(floor).get(i).get(j).get(k).respawn();
                }
            }
        }
        if(floor >0) {
            Integer[] boss = bossRooms.get(floor);
            ((Boss) interactables.get(floor).get(boss[0]).get(boss[1]).get(0)).respawn();
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
                respawn();
            }
        }
    }
}
