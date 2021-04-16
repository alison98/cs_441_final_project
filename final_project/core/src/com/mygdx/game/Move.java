package com.mygdx.game;

import java.util.Random;

/*
 * This class stores all info needed for an action (Move) performed during combat.
 * We can have different weapons be represented by different moves as well.
 */
public class Move {

    //Fields

    //Moves have a range of damage they can do (from minDamage to maxDamage)
    private int maxDamage;
    private int minDamage;
    private String nameOfMove;//Moves also have a name

    //Getters

    public int getMaxDamage() {
        return maxDamage;
    }

    public int getMinDamage() {
        return minDamage;
    }

    public String getNameOfMove() {
        return nameOfMove;
    }


    //basic constructor
    public Move(String name, int min, int max){
        nameOfMove = name;
        maxDamage = max;
        minDamage = min;
    }

    //function called from CombatScreen to get a random value in the Move's range
    public int getRandomDamageInRange(){
        Random rand = new Random();
        return rand.nextInt(maxDamage) + minDamage;
    }

    //other things this class may have/need
    //  -relation to specific weapons
    //  -a type (weapon, other abilities we may add later)
    //  -setters for max and min damage if weapons/moves can be upgraded
    //  -different damage fields (does X-Y damage against enemy Z, A-B damage against enemy C, etc.)


}
