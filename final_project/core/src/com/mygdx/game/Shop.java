package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Shop extends Interactable{
    private int floor, row, column, index;

    public Shop(String image, int floorIn, int rowIn, int columnIn, int indexIn, int xIn, int yIn) {
        super(image, floorIn, rowIn, columnIn, indexIn, xIn, yIn);
        floor = floorIn;
        row = rowIn;
        column = columnIn;
        index = indexIn;
    }

    @Override
    public void interact(GameScreen game){
        game.getHud().setText("Welcome to the shop!");
    }

    public void openShop(final SpriteBatch spriteBatch, final GameScreen gameScreen){
        gameScreen.getRoom().setUpInteractables();
        gameScreen.getGame().setScreen(new ShopScreen(gameScreen.getGame(), spriteBatch,gameScreen,floor));
    }

}