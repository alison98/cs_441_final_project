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
        String[] bossMessage = new String[]{"Congratulations, you’ve defeated\nthe CEO and ruined his plans to",
                "take over the corporate world\nwith mutant office supplies!",
                "This is the end of your adventure,\nbut you’re free to return",
                "to the lower floors to continue\nexploring the office building."};
        boss.setBoss(bossMessage, this);
    }

    @Override
    public void interact(GameScreen game){
        game.getHud().setText("Impossible, how did you make it past\nall of my office supplies!?",
                "Do you have any idea how much money\nI spent on this device?",
                "Those idiot scientists told me that\nI’d be able to take over any company",
                "in the world from the inside using\ntheir own office supplies against them,",
                "and no one would be able to stop me!",
                "But somehow a nine-to-five button pusher\nlike you destroyed them all?",
                "I guess I’ll have to deal with you myself!");
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

