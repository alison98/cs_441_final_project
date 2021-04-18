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
    //Fields

    private Map<String, List<Integer>> movelist;//Map where key is the names of weapons and value is min and max damage
    private List<List<String>> enemyWeapons;


    //Getters
    public List<String> getEnemyWeapons(int level) {
        return enemyWeapons.get(level-1);
    }

    //Singleton constructor
    private Move(){
        //Set up all possible moves in game
        movelist = new HashMap<>();
        movelist.put("sword", setDamage(10,20));

        //Set up weapons/abilities for enemies
        enemyWeapons = new ArrayList<>();
        List<String> floor = new ArrayList<>();
        floor.add("sword");
        enemyWeapons.add(floor);
    }

    public static Move getInstance(){
        if(instance == null){
            instance = new Move();
        }
        return instance;
    }

    //function called from CombatScreen to get a random value in the Move's range
    public int getDamage(String weapon){
        List<Integer> range = movelist.get(weapon);
        Random random = new Random();
        int temp = random.nextInt(range.get(1)-range.get(0));
        return range.get(0)+temp;
    }

    public List<Integer> setDamage(int min, int max){
        List<Integer> damage = new ArrayList<>();
        damage.add(min);
        damage.add(max);
        return damage;
    }

    //other things this class may have/need
    //  -relation to specific weapons
    //  -a type (weapon, other abilities we may add later)
    //  -setters for max and min damage if weapons/moves can be upgraded
    //  -different damage fields (does X-Y damage against enemy Z, A-B damage against enemy C, etc.)


}
