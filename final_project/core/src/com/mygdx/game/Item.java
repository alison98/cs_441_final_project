package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Item extends Interactable{
    private int floor, row, column, index;
    private List<String> droppable;
    private Random random;
    private boolean isMoney;
    private String object;

    public Item(String image, int floorIn, int rowIn, int columnIn, int indexIn, int xIn, int yIn) {
        super(image, floorIn, rowIn, columnIn, indexIn, xIn, yIn);
        floor = floorIn;
        row = rowIn;
        column = columnIn;
        index = indexIn;
        random = new Random();
        droppable = new ArrayList<>();
        isMoney = false;
        //add items based on floor
        if(image.equals("coffee.png")) {
            if(floor == 1){
                droppable.add("Milk");
                droppable.add("Soda");
                droppable.add("Sports Drink");
            }else if(floor == 2){
                droppable.add("Lemonade"); //only obtainable through item drop
                droppable.add("Hot Chocolate");
                droppable.add("Apple Cider");
                droppable.add("Orange Juice");
            }else if(floor == 3){
                droppable.add("Chocolate Milk"); //only obtainable through item drop
                droppable.add("Water");
                droppable.add("Soup");
                droppable.add("Iced Tea");
            }else if(floor == 4){
                droppable.add("Coffee"); //only obtainable through item drop
                droppable.add("Tea");
                droppable.add("Protein Shake");
                droppable.add("Punch");
            }
        }else if(image.equals("Money.png")){
            isMoney = true;
            if(floor == 1){
                droppable.add("10");
            }else if(floor == 2){
                droppable.add("20");
            }else if(floor == 3){
                droppable.add("30");
            }else if(floor == 4){
                droppable.add("40");
            }
        }else if(image.equals("Sword.png")){
            if(floor ==0){
                droppable.add("Mug");
                droppable.add("Ruler");//only obtainable through item drop
                droppable.add("Paper Clip");
                droppable.add("Tie");
            }else if(floor == 1){
                droppable.add("Tape");
                droppable.add("Water Bottle");
                droppable.add("Mug");
                droppable.add("Ruler");//only obtainable through item drop
            }else if(floor == 2){
                droppable.add("Stapler"); //only obtainable through item drop
                droppable.add("Laptop");
                droppable.add("Tape");
                droppable.add("Water Bottle");
            }else if(floor == 3){
                droppable.add("Scissors");//only obtainable through item drop
                droppable.add("Telephone");
                droppable.add("Chair");
                droppable.add("Stapler"); //only obtainable through item drop
            }else if(floor == 4){
                droppable.add("Desk Lamp"); //only obtainable through item drop
                droppable.add("Wired Mouse");
                droppable.add("Keyboard");
                droppable.add("Scissors");//only obtainable through item drop
            }

        }
        object = droppable.get(random.nextInt(droppable.size()));
    }

    @Override
    public void interact(GameScreen game){
        if(isMoney){
            game.getHud().setText("You obtained $"+ object);
        }else {
            game.getHud().setText("You obtained a " + object);
        }
    }

    public void pickUp(GameScreen game, Player player){
        if(isMoney){
            player.setMoney(player.getMoney()+Integer.valueOf(object));
        }else {
            player.addWeapon(object);
        }
        Layout.getInstance().removeInteractable(floor, row, column, index);
        game.getRoom().setUpInteractables();
    }
}
