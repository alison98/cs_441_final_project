package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Shop extends Interactable{
    private int floor, row, column, index;
    private boolean shopFlag;

    public Shop(String image, int floorIn, int rowIn, int columnIn, int indexIn, int xIn, int yIn) {
        super(image, floorIn, rowIn, columnIn, indexIn, xIn, yIn);
        floor = floorIn;
        row = rowIn;
        column = columnIn;
        index = indexIn;
        shopFlag = false;
    }

    @Override
    public void interact(GameScreen game){
        if(shopFlag == false && floor == 1){
            shopFlag = true;
            game.getHud().setText("Thank god, another human!",
                    "Have you seen the mutant office supplies\neverywhere? Someone needs to stop them!",
                    "If you think you’re up for the task,\nI can sell you some of my items.",
                    "If you don’t have enough money, you\ncan always take out some of those monsters.",
                    "They seem to carry cash\non them for some reason.");
        } else{
            game.getHud().setText("Welcome to the shop!");
        }
    }

    public void openShop(final SpriteBatch spriteBatch, final GameScreen gameScreen){
        gameScreen.getRoom().setUpInteractables();
        gameScreen.getGame().setScreen(new ShopScreen(gameScreen.getGame(), spriteBatch,gameScreen,floor));
    }

}