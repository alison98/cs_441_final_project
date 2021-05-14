package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class FinalBoss extends Interactable{
    private int floor, row, column, index, x, y;
    private Enemy boss;
    private Sprite postCombatSprite;

    public FinalBoss(String image, int floorIn, int rowIn, int columnIn, int indexIn, int xIn, int yIn) {
        super(image, floorIn, rowIn, columnIn, indexIn, xIn, yIn);
        floor = floorIn;
        row = rowIn;
        column = columnIn;
        index = indexIn;
        x = xIn;
        y = yIn;
        boss = new Enemy(x, y,2, "CEO.png", floorIn, null);
        boss.setMaxHealth(200);
        String[] bossMessage = new String[]{"PUT A POST-COMBAT MESSAGE\nFOR THE FINAL BOSS HERE"};
        boss.setBoss(bossMessage, this);
    }

    @Override
    public void interact(GameScreen game){
        game.getHud().setText("PUT A PRE-COMBAT MESSAGE\nFOR THE FINAL BOSS HERE");
    }

    public void fight(GameScreen game){
        game.getPlayer().setCombat();
        //super.setSprite(boss.getHumanSprite());
        //super.positionChanged();
        Layout.getInstance().removeInteractable(floor, row, column, index);
        game.getRoom().setUpInteractables();
        game.getGame().setScreen(new CombatScreen(game.getGame(), boss, game.getPlayer(), game));
    }

    public void respawn(){
        boss.respawn();
    }
}

