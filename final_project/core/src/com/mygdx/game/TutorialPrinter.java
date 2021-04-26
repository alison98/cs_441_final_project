package com.mygdx.game;

import com.badlogic.gdx.Game;

public class TutorialPrinter extends Interactable{
    private int floor, row, column, index;

    public TutorialPrinter(String image, int floorIn, int rowIn, int columnIn, int indexIn) {
        super(image, floorIn, rowIn, columnIn, indexIn);
        floor = floorIn;
        row = rowIn;
        column = columnIn;
        index = indexIn;
    }

    @Override
    public void interact(GameScreen game){
        game.getHud().setText("The printer attacks!");
    }

    public void fight(GameScreen game){
        Enemy tutorialPrinter = new Enemy(1528,200,2,0, 0);
        tutorialPrinter.setKey();
        tutorialPrinter.setBoss();
        game.getPlayer().setCombat();
        Layout.getInstance().removeInteractable(floor, row, column, index);
        game.getRoom().setUpInteractables();
        game.getGame().setScreen(new CombatScreen(game.getGame(), tutorialPrinter, game.getPlayer(), game));
    }
}
