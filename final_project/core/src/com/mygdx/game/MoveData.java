package com.mygdx.game;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


/*
 * Actual info we store for a "move" done during a turn. Instances stored in the singleton class Move's moveList.
 */
public class MoveData implements Comparable<MoveData> {

    @Override
    public int compareTo(MoveData other) {
        return this.getName().compareTo(other.getName());
    }

    @Override
    //thanks https://www.geeksforgeeks.org/overriding-equals-method-in-java/, I'm a bit rusty
    public boolean equals(Object o) {
        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }
        // typecast o to MoveData so that we can compare data members
        MoveData m = (MoveData) o;

        //so based on how we have things setup, 2 instances of same move will always have same name
        //then, to be truly equal, they need equal current durability, currentUsesPerEncounter, and turns since used
        //everything else either doesn't matter or will always be equal by definition
        return m.getName().equals(name) && m.getCurrentDurability() == currentDurability && m.getCurrentUsesPerEncounter() == currentUsesPerEncounter && m.getTurnsSinceUsed() == turnsSinceUsed;
        //for example, we have 2 paper clips - to start, they are equal, and will be listed as "Paper Clip x2"
        //then we use one - now, the durability of one has decreased, and they are now different weapons, and will be listed as different moves

    }

    //these are used to determine what the Move does
    //we can add more later if need be
    enum MoveType {
        ATTACK, //damage enemy
        HEALING//heal self
    }

    //related to figuring out how to let player and enemy use a specific random instance
    //we'll need to make sure a variation is not currently in use
    private int inUse;

    //moves now have a ranking, which can help with setting enemy moves based on level, as well as drops and prices in the shop (if we get there)
    //right now its 0-100, but we return 1-5 based on this
    private int ranking;

    //called when making the random variation (we sum up the value from 0-100 there)
    public void setRanking(int r){
        ranking = r;
    }

    //returns 1 - 5 based on the 0-100 value assigned when creating the move
    //needs some work still
    public int getRanking(){
        if(ranking < 21) return 1;
        if(ranking < 41) return 2;
        if(ranking < 61) return 3;
        if(ranking < 81) return 4;
        return 5;
    }

    //we need a range and a type - name is held in singleton map
    private List<Integer> range;
    private MoveType movetype;
    private String name;

    public String getName() { return name; }

    public List<Integer> getRange() { return range; }

    public MoveType getMoveType() { return movetype; }

    //the optional stuff
    private boolean hasDurability; //true means limited use, will break eventually
    private int currentDurability;
    private int durability; //number of total uses when hasDurability is true, undefined and unused otherwise
    //once 0, remove from move list

    public int getCurrentDurability(){ return currentDurability; }

    private boolean  hasUsesPerEncounter; //true means limited use per encounter
    private int usesPerEncounter; //number of uses per encounter when hasUsesPerEncounter is true, undefined and unused otherwise
    private int currentUsesPerEncounter;//copy used during an encounter, decrement on each move and reset on exiting
    //once 0, remove from move list, but re-add on exiting combat

    public int getCurrentUsesPerEncounter() { return currentUsesPerEncounter;}

    //could have boolean to be consistent, but 0 works here
    private int cooldownLength; //measured in turns, 0 for no cooldown
    private int turnsSinceUsed; //measure turns since the move was used, once it equals cooldownLength, it can be used again

    public int getTurnsSinceUsed() { return turnsSinceUsed; }

    //do I need a name for the status effect?
    private boolean hasStatusEffect; //true if this move has a status effect
    private MoveType statusEffectType; //type of status effect if hasStatusEffect is true, undefined otherwise
    private List<Integer> statusEffectRange; //range of amount of status effect if hasStatusEffect is true, undefined otherwise
    private int duration; //number of turns status effect is active for if hasStatusEffect is true, undefined otherwise
    private int currentDuration;

    //I need getters for these as they almost act like a MoveData inside a MoveData
    public boolean getHasStatusEffect() { return hasStatusEffect; }

    public MoveType getStatusEffectType() { return statusEffectType;}

    public List<Integer> getStatusEffectRange() { return statusEffectRange; }

    public int getStatusEffectDuration() { return duration; }


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
    public int useMove(MoveData moveObject, List<MoveData> moves, List<MoveData> statusEffects){
        //update values
        if(cooldownLength != 0) turnsSinceUsed = 0; //if this has cooldown, reset turns since used so we can start counting again
        if(hasUsesPerEncounter) currentUsesPerEncounter++; //if we can only use it a certain amount of times each battle, increment current uses
        if(hasDurability) currentDurability--; //if it can be broken, decrement the use
        if(hasDurability && currentDurability == 0) {//then check if it should be removed from player's list (if durability is 0)
            moves.remove(moveObject);//once I figure out where/hwo to pass in player move list, remove current move from it here
            currentDurability =  durability; //then reset durability in case we have copies? - this is what I'm looking into next
        }
        if(hasStatusEffect) { //right now this allows overlaps, can change (use move with status effect twice, get 2 status effects and so on)
            statusEffects.add(moveObject); //add to ongoingStatusEffects
        }
        return randomAmountInRange(getRange());
    }

    //called when using
    public int useStatusEffect(MoveData nameOfMove, List<MoveData> statusEffects){
        System.out.println("using status effect : " + currentDuration +  "/" + duration);
        currentDuration--;
        if(currentDuration == 0){
            currentDuration = duration;
            System.out.println("removing " + nameOfMove.getName());
            statusEffects.remove(nameOfMove);//remove from list
        }
        return randomAmountInRange(getStatusEffectRange());
    }

    //this would be needed for stuff like cooldowns
    //we use another move, but want to check for when any moves in cooldown would be ready
    //so we'd need to call this for all moves in the player's move list on every turn
    public void performOtherMove(){
        if(cooldownLength != 0 && turnsSinceUsed!=cooldownLength){
            turnsSinceUsed++;
        }
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
        if(hasUsesPerEncounter) currentUsesPerEncounter = 0;
        if(cooldownLength != 0) turnsSinceUsed = cooldownLength; //this will make sure the cooldown isn't active when starting next turn
    }

    @Override
    public String toString(){
        String ret = movetype + ": " + getRange().get(0) + " - " + getRange().get(1);
        if(hasDurability) ret+= ". " + currentDurability + " uses remaining.";
        else ret+= ". Infinite uses remaining.";
        if(hasUsesPerEncounter) ret+= "\nUses Per Encounter: " + currentUsesPerEncounter + "/" + usesPerEncounter;
        else ret+= "\nNo limit on uses per encounter.";
        if(cooldownLength != 0) ret+=" Cooldown: " + turnsSinceUsed + "/" + cooldownLength;
        else ret+= " No cooldown.";
        if(hasStatusEffect) ret += "\n" + statusEffectType + " status effect, " +  getStatusEffectRange().get(0) + " - " + getStatusEffectRange().get(1) + ", " + duration + " turns";
        else ret+= "\nNo status effect.";
        return ret;
    }


    public static class Builder {
        //required
        private List<Integer> range;
        private MoveType movetype;
        private String name;

        //optional
        private boolean hasDurability = false; //true means limited use, will break eventually
        private int currentDurability;
        private int durability; //number of total uses when hasDurability is true, undefined and unused otherwise
        //once 0, remove from move list

        private boolean  hasUsesPerEncounter = false; //true means limited use per encounter
        private int usesPerEncounter; //number of uses per encounter when hasUsesPerEncounter is true, undefined and unused otherwise
        private int currentUsesPerEncounter;//copy used during an encounter, decrement on each move and reset on exiting
        //once 0, remove from move list, but re-add on exiting combat

        //could have a boolean to be consistent, but 0 works here
        private int cooldownLength = 0; //measured in turns, 0 for no cooldown
        private int turnsSinceUsed; //measure turns since the move was used, once it equals cooldownLength, it can be used again



        //do I need a name for the status effect
        private boolean hasStatusEffect = false; //true if this move has a status effect
        private MoveType statusEffectType; //type of status effect if hasStatusEffect is true, undefined otherwise
        private List<Integer> statusEffectRange; //range of amount of status effect if hasStatusEffect is true, undefined otherwise
        private int duration; //number of turns status effect is active for if hasStatusEffect is true, undefined otherwise
        private int currentDuration;

        //necessary
        public Builder(String n, List<Integer> r, MoveType t) {
            this.name = n;
            this.range = r;
            this.movetype = t;
        }

        //remaining 4 are optional
        public Builder setDurability(int durabilityInTurns) {
            hasDurability = true;
            durability = currentDurability = durabilityInTurns;
            return this;
        }

        public Builder setUsesPerEncounter(int uses) {
            hasUsesPerEncounter = true;
            usesPerEncounter = uses;
            currentUsesPerEncounter = 0;
            return this;
        }

        public Builder setCooldown(int durationInTurns) {
            cooldownLength = durationInTurns;
            turnsSinceUsed = cooldownLength;
            return this;
        }

        public Builder setStatusEffect(MoveType type, List<Integer> range, int durationInTurns) {
            hasStatusEffect = true;
            statusEffectType = type;
            statusEffectRange = range;
            duration = currentDuration = durationInTurns;
            return this;
        }

        public MoveData build() {
            return new MoveData(this);
        }
    }

    public MoveData(Builder builder) {
        name = builder.name;
        range = builder.range;
        movetype = builder.movetype;
        hasDurability = builder.hasDurability;
        if(hasDurability){
            durability = currentDurability = builder.durability;
        }
        hasUsesPerEncounter = builder.hasUsesPerEncounter;
        if(hasUsesPerEncounter){
            usesPerEncounter =  builder.usesPerEncounter;
            currentUsesPerEncounter = builder.currentUsesPerEncounter;
        }
        cooldownLength = builder.cooldownLength;
        if(cooldownLength !=0){
            turnsSinceUsed = builder.turnsSinceUsed;
        }
        hasStatusEffect = builder.hasStatusEffect;
        if(hasStatusEffect){
            statusEffectType = builder.statusEffectType;
            statusEffectRange = builder.statusEffectRange;
            duration = currentDuration = builder.duration;
        }
        this.builder = builder;
    }

    private Builder builder;

    MoveData(MoveData other){
        //System.out.println("copy");
        name = other.builder.name;
        range = other.builder.range;
        movetype = other.builder.movetype;
        hasDurability = other.builder.hasDurability;
        if(hasDurability){
            durability = currentDurability = other.builder.durability;
        }
        hasUsesPerEncounter = other.builder.hasUsesPerEncounter;
        if(hasUsesPerEncounter){
            usesPerEncounter =  other.builder.usesPerEncounter;
            currentUsesPerEncounter = other.builder.currentUsesPerEncounter;
        }
        cooldownLength = other.builder.cooldownLength;
        if(cooldownLength !=0){
            turnsSinceUsed = other.builder.turnsSinceUsed;
        }
        hasStatusEffect = other.builder.hasStatusEffect;
        if(hasStatusEffect){
            statusEffectType = other.builder.statusEffectType;
            statusEffectRange = other.builder.statusEffectRange;
            duration = currentDuration = other.builder.duration;
        }
        this.builder = other.builder;
    }


}
