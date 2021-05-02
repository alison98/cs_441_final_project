package com.mygdx.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/*
 * This class stores all info needed for an action (Move) performed during combat.
 * We can have different weapons be represented by different moves as well.
 */
public class Move {

    private static Move instance;

    private Map<String, MoveData> movelist;//Map where key is the names of weapons and value is a MoveData object (with range and move type)
    private List<List<String>> enemyWeapons;


    public List<String> getEnemyWeapons(int level) {
        return enemyWeapons.get(level);
    }

    //Singleton constructor
    private Move(){
        //Set up all possible moves in game
        movelist = new HashMap<>();
        movelist.put("sword", new MoveData(setDamage(10, 20), MoveData.MoveType.ATTACK));
        movelist.put("coffee", new MoveData(setDamage(10, 20), MoveData.MoveType.HEALING));

        //Set up weapons/abilities for enemies
        enemyWeapons = new ArrayList<>();
        List<String> floor0 = new ArrayList<>();
        floor0.add("sword");
        //floor0.add("coffee"); //testing enemy healing itself
        List<String> floor1 = new ArrayList<>();
        floor1.add("sword");
        List<String> floor2 = new ArrayList<>();
        floor2.add("sword");
        List<String> floor3 = new ArrayList<>();
        floor3.add("sword");
        enemyWeapons.add(floor0);
        enemyWeapons.add(floor1);
        enemyWeapons.add(floor2);
        enemyWeapons.add(floor3);
    }

    public static Move getInstance(){
        if(instance == null){
            instance = new Move();
        }
        return instance;
    }

    //function called from CombatScreen to get a random value in the Move's range
    public int getDamage(String weapon){
        List<Integer> range = movelist.get(weapon).getRange();
        Random random = new Random();
        int temp = random.nextInt(range.get(1)-range.get(0));
        return range.get(0)+temp;
    }

    public MoveData.MoveType getMoveType(String weapon){
        return movelist.get(weapon).getMoveType();
    }

    public List<Integer> setDamage(int min, int max){
        List<Integer> damage = new ArrayList<>();
        damage.add(min);
        damage.add(max);
        return damage;
    }

    public String toString(String move){
        MoveData selectedMove = movelist.get(move);
        return move + "\n" + selectedMove.getMoveType() + "\n" + selectedMove.getRange().get(0) + " - " + selectedMove.getRange().get(1);
    }

    //other things this class may have/need
    //  -relation to specific weapons
    //  -a type (weapon, other abilities we may add later)
    //  -setters for max and min damage if weapons/moves can be upgraded
    //  -different damage fields (does X-Y damage against enemy Z, A-B damage against enemy C, etc.)


}
