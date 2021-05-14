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
        game.getHud().setText("You accidentally type 488 instead of 48,\nand the printer display shuts off.", "Suddenly, you see two red eyes open under\nthe scanner, and the printer attacks!", "You pick up the nearest stapler\nto defend yourself.");
    }

    public void fight(GameScreen game){
        Enemy tutorialPrinter = new Enemy(x,y,2,"An Angry Printer.png", floor, null);
        tutorialPrinter.setMaxHealth(5);
        tutorialPrinter.setBoss(new String[]{"You manage to fight off the printer,\nand among the broken parts you find a key.", "You can’t go back to your boss\nafter destroying the printer,", "but what you’re more concerned about is\nwhy the printer attacked you in the first place.", "Maybe this key holds the answer..."}, this);
        game.getPlayer().setCombat();
        Layout.getInstance().removeInteractable(floor, row, column, index);
        game.getRoom().setUpInteractables();
        game.getGame().setScreen(new CombatScreen(game.getGame(), tutorialPrinter, game.getPlayer(), game));
    }
}
