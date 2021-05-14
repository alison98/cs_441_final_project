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
        String[] bossMessage;
        if(floor == 1){
            bossMessage = new String[]{"Thank you for saving me!",
                    "I don’t know how, but somehow I\ngot turned into a fax machine.",
                    "I don’t know where all of these\naggressive office supplies came from,",
                    "all I know is that some of them\nwere originally human like me."};
            boss.setMaxHealth(50);
        } else if(floor == 2){
            bossMessage = new String[]{"Thanks for getting me out of there!",
                    "I’m sure you’re wondering about\nwhat’s going on here.",
                    "The CEO is behind all of this!",
                    "He’s using some sort of device to\nturn employees into office supplies",
                    "because he’s trying to infiltrate and\ntake over a rival company."};
            boss.setMaxHealth(75);
        } else{
            bossMessage = new String[]{"Thank you so much!",
                    "I’m sure you’ve heard by now,\nbut the CEO is behind all of this.",
                    "He’s hiding out in his office\non the top floor of the building",
                    "and you have to stop him\nbefore he starts infecting more people!"};
            boss.setMaxHealth(100);
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
                game.getHud().setText("Kiss my fax!");
            } else if(floor == 2){
                game.getHud().setText("I hope you’re ready for a boxing match!");
            } else{
                game.getHud().setText("It’s your turn to serve me!");
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
