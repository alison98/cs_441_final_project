package com.mygdx.game;

import java.util.List;


/*
 * Actual info we store for a "move" done during a turn. Instances stored in the singleton class Move's moveList.
 */
public class MoveData {

    //these are used to determine what the Move does
    //we can add more later if need be
    enum MoveType {
        ATTACK, //damage enemy
        HEALING//heal self
    }

    //we need a range and a type - name is held in singleton map
    private List<Integer> range;
    private MoveType movetype;

    public MoveData(List<Integer> r, MoveType t){
        range = r;
        movetype = t;
    }

    public List<Integer> getRange() { return range; }

    public MoveType getMoveType() { return movetype; }


    //starting to think about other effects and ideas, and how they'd work
    //I haven't hooked anything up yet, and might not use all/any of these ideas

    //could use enums or booleans, I haven't decided yet
    enum Durability {
        LIMITED_USE,
        UNLIMITED_USE,
    }
    enum EncounterUses {
        LIMITED_USE,
        UNLIMITED_USE
    }

    private boolean hasDurability; //true means limited use, will break eventually
    //doesn't need a copy, value persists throughout encounters
    private int durability; //number of total uses when hasDurability is true, undefined and unused otherwise
    //once 0, remove from move list

    private boolean  hasUsesPerEncounter; //true means limited use per encounter
    private int usesPerEncounter; //number of uses per encounter when hasUsesPerEncounter is true, undefined and unused otherwise
    private int currentUsesPerEncounter;//copy used during an encounter, decrement on each move and reset on exiting
    //once 0, remove from move list, but re-add on exiting combat

    //could have a boolean to be consistent, but 0 works here
    private int cooldownLength; //measured in turns, 0 for no cooldown
    private int turnsSinceUsed; //measure turns since the move was used, once it equals cooldownLength, it can be used again

    //could be very generalized - attack or heal
    enum  StatusEffect {
        BURNING,
        BLEEDING,
        HEALING
    }

    //other ideas
    //status effect (does damage or healing over a number of turns)
    //probability of something happening (critical, more damage, some healing, idk)
    //probability that move will go first (for an idea I have about both sides picking a move at the same time, different advantages to going 1st or 2nd, more strategy-based by being able to see the enemy's move list)


    //this is what we'd call instead of just getRange (probably via Move.java)
    //it'll automatically update all values for characteristics
    //i.e. decrement durability, currentUsesPerEncounter, etc. if need be
    //I'll need to pass in the player or move list though to propagate changes
    public List<Integer> useMove(){
        //update values
        if(cooldownLength != 0) turnsSinceUsed = 0; //if this has cooldown, reset turns since used so we can start counting again
        if(hasUsesPerEncounter) currentUsesPerEncounter++; //if we can only use it a certain amount of times each battle, increment current uses
        if(hasDurability) durability--; //if it can be broken, decrement the use
        if(hasDurability && durability == 0) {//then check if it should be removed from player's list (if durability is 0)
            //once I figure out where/hwo to pass in player move list, remove current move from it here
        }
        //also call preformOtherMove() on all other moves in the list I pass in
        return getRange();
    }

    //this would be needed for stuff like cooldowns
    //we use another move, but want to check for when any moves in cooldown would be ready
    //so we'd need to call this for all moves in the player's move list on every turn
    public void performOtherMove(){
        if(cooldownLength != 0) turnsSinceUsed++;
    }

    //CombatScreen will call this when setting up moves to display (only valid moves shown)
    //move is currentlyAvailable when its durability is above 0, isn't in cooldown, and still has uses per encounter
    //example of not being currentlyAvailable is when a move is in cooldown - it shouldn't be removed from the player's move list, but the player cannot use it right now
    //I originally had a boolean I set in multiple places, but this is clearer for now
    public boolean getCurrentlyAvailable(){
        if(cooldownLength != 0 && turnsSinceUsed < cooldownLength) return false;
        if(hasUsesPerEncounter && currentUsesPerEncounter >= usesPerEncounter) return false;
        return true;
    }

    //this is what we'd call at end of combat
    //reset any temporary changes (like moves per encounter)
    public void resetMove(){
        if(hasUsesPerEncounter) currentUsesPerEncounter = usesPerEncounter;
        if(cooldownLength != 0) turnsSinceUsed = cooldownLength; //this will make sure the cooldown isn't active when starting next turn
    }


}
