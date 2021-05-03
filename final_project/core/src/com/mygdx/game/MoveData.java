package com.mygdx.game;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;


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
        if(movetype == MoveType.HEALING){ //make healing items single use for now
            //like I mention below, this constructor will be expanded once I decide on what I'm doing
            //so this is just for testing for now
            hasDurability = true;
            currentDurability = durability = 1;
        }
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
    private int currentDurability;
    private int durability; //number of total uses when hasDurability is true, undefined and unused otherwise
    //once 0, remove from move list

    private boolean  hasUsesPerEncounter; //true means limited use per encounter
    private int usesPerEncounter; //number of uses per encounter when hasUsesPerEncounter is true, undefined and unused otherwise
    private int currentUsesPerEncounter;//copy used during an encounter, decrement on each move and reset on exiting
    //once 0, remove from move list, but re-add on exiting combat

    //could have a boolean to be consistent, but 0 works here
    private int cooldownLength; //measured in turns, 0 for no cooldown
    private int turnsSinceUsed; //measure turns since the move was used, once it equals cooldownLength, it can be used again



    //do I need a name for the status effect
    private boolean hasStatusEffect; //true if this move has a status effect
    private MoveType statusEffectType; //type of status effect if hasStatusEffect is true, undefined otherwise
    private List<Integer> statusEffectRange; //range of amount of status effect if hasStatusEffect is true, undefined otherwise
    private int duration; //number of turns status effect is active for if hasStatusEffect is true, undefined otherwise
    private int currentDuration;

    //just for now, will probably go to constructor later
    public void setStatusEffect(boolean hasStatusEffect, MoveType type, List<Integer> range, int duration){
        this.hasStatusEffect = hasStatusEffect;
        this.statusEffectType = type;
        this.statusEffectRange = range;
        this.duration = this.currentDuration = duration;
    }

    public boolean getHasStatusEffect() { return hasStatusEffect; }

    public MoveType getStatusEffectType() { return statusEffectType;}

    public List<Integer> getStatusEffectRange() { return statusEffectRange; }


    //other ideas
    //probability of something happening (critical, more damage, some healing, idk)
    //probability that move will go first (for an idea I have about both sides picking a move at the same time, different advantages to going 1st or 2nd, more strategy-based by being able to see the enemy's move list)


    public int randomAmountInRange(List<Integer> range){
        Random random = new Random();
        int temp = random.nextInt(range.get(1)-range.get(0));
        return range.get(0)+temp;
    }

    //this is what we call to use a move now, rather than just getRange/damage
    //it'll automatically update all values for characteristics
    //i.e. decrement durability, currentUsesPerEncounter, etc. if need be
    //I'll need to pass in the player or move list though to propagate changes
    public int useMove(String nameOfMove, List<String> moves, List<String> statusEffects){
        //System.out.println("using " + nameOfMove);
        //update values
        if(cooldownLength != 0) turnsSinceUsed = 0; //if this has cooldown, reset turns since used so we can start counting again
        if(hasUsesPerEncounter) currentUsesPerEncounter++; //if we can only use it a certain amount of times each battle, increment current uses
        if(hasDurability) currentDurability--; //if it can be broken, decrement the use
        //System.out.println("before removing : " + moves.size());
        if(hasDurability && currentDurability == 0) {//then check if it should be removed from player's list (if durability is 0)
            moves.remove(nameOfMove);//once I figure out where/hwo to pass in player move list, remove current move from it here
            currentDurability =  durability; //then reset durability in case we have copies? - need to really test
        }
        //System.out.println("after removing: " + moves.size());
        if(hasStatusEffect) { //right now this allows overlaps, can change (use move with status effect twice, get 2 status effects and so on)
            //System.out.println("adding to status effects");
            statusEffects.add(nameOfMove); //add to ongoingStatusEffects
        }
        return randomAmountInRange(getRange());
    }

    //called when using
    public int useStatusEffect(String nameOfMove, List<String> statusEffects){
        //System.out.println("using status effect : " + duration);
        currentDuration--;
        if(currentDuration == 0){
            currentDuration = duration;
            //System.out.println("removing");
            statusEffects.remove(nameOfMove);//remove from list
        }
        return randomAmountInRange(getStatusEffectRange());
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
