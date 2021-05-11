package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Item extends Interactable{
    private int floor, row, column, index;
    private List<String> droppable;
    private Random random;

    public Item(String image, int floorIn, int rowIn, int columnIn, int indexIn, int xIn, int yIn) {
        super(image, floorIn, rowIn, columnIn, indexIn, xIn, yIn);
        floor = floorIn;
        row = rowIn;
        column = columnIn;
        index = indexIn;
        random = new Random();
        droppable = new ArrayList<>();
        droppable.add("Coffee");
        droppable.add("Tea");
        droppable.add("Lemonade");
        droppable.add("Milk");
    }

    @Override
    public void interact(GameScreen game){
        game.getHud().setText("Ohh a treat!");
    }

    public void pickUp(GameScreen game, Player player){
        player.addWeapon(droppable.get(random.nextInt(droppable.size())));
        Layout.getInstance().removeInteractable(floor, row, column, index);
        game.getRoom().setUpInteractables();
    }
}
