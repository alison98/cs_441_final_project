package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Boss extends Interactable{
    private int floor, row, column, index, x, y;
    private Enemy boss;

    public Boss(String image, int floorIn, int rowIn, int columnIn, int indexIn, int xIn, int yIn) {
        super(image, floorIn, rowIn, columnIn, indexIn, xIn, yIn);
        floor = floorIn;
        row = rowIn;
        column = columnIn;
        index = indexIn;
        x = xIn;
        y = yIn;
        boss = new Enemy(x, y,2, super.getSprite().getTexture().toString(), floorIn, null);
        String bossMessage;
        if(floor == 1){
            bossMessage = "PUT A POST-COMBAT MESSAGE\nFOR THE FLOOR 1 BOSS HERE";
        } else if(floor == 2){
            bossMessage = "PUT A POST-COMBAT MESSAGE\nFOR THE FLOOR 2 BOSS HERE";
        } else{
            bossMessage = "PUT A POST-COMBAT MESSAGE\nFOR THE FLOOR 3 BOSS HERE";
        }
        boss.setBoss(bossMessage, this);
    }

    public boolean isHuman(){return boss.isHuman();}

    @Override
    public void interact(GameScreen game){
        if(boss.isHuman()){
            game.getHud().setText("PUT A POST-COMBAT MESSAGE\nFOR THE HUMANS HERE");
        } else {
            if(floor == 1){
                game.getHud().setText("PUT A PRE-COMBAT MESSAGE\nFOR THE FLOOR 1 BOSS HERE");
            } else if(floor == 2){
                game.getHud().setText("PUT A PRE-COMBAT MESSAGE\nFOR THE FLOOR 2 BOSS HERE");
            } else{
                game.getHud().setText("PUT A PRE-COMBAT MESSAGE\nFOR THE FLOOR 3 BOSS HERE");
            }
        }
    }

    public void fight(GameScreen game){
        game.getPlayer().setCombat();
        game.getGame().setScreen(new CombatScreen(game.getGame(), boss, game.getPlayer(), game));
    }

    public void respawn(){
        boss.respawn();
    }
}
