package com.mygdx.game;

import com.badlogic.gdx.Game;

public class TutorialPrinter extends Interactable{
    private int floor, row, column, index, x, y;

    public TutorialPrinter(String image, int floorIn, int rowIn, int columnIn, int indexIn, int xIn, int yIn) {
        super(image, floorIn, rowIn, columnIn, indexIn, xIn, yIn);
        x = xIn;
        y = yIn;
        floor = floorIn;
        row = rowIn;
        column = columnIn;
        index = indexIn;
    }

    @Override
    public void interact(GameScreen game){
        game.getHud().setText("Just as you begin to enter 8000 into\nthe machine, it suddenly starts moving!", "Two red eyes open under the scanner,\nand the printer attacks!");
    }

    public void fight(GameScreen game){
        Enemy tutorialPrinter = new Enemy(x,y,2,"An Angry Printer.png", floor, null);
        tutorialPrinter.setMaxHealth(5);
        tutorialPrinter.setBoss(new String[]{"You managed to fight off the printer,\nbut what's going on upstairs?"}, this);
        game.getPlayer().setCombat();
        Layout.getInstance().removeInteractable(floor, row, column, index);
        game.getRoom().setUpInteractables();
        game.getGame().setScreen(new CombatScreen(game.getGame(), tutorialPrinter, game.getPlayer(), game));
    }
}
