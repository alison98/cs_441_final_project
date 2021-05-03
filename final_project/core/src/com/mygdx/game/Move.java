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
        //movelist.get("sword").setStatusEffect(true, MoveData.MoveType.ATTACK, setDamage(50, 60), 10); //another test case
        movelist.put("coffee", new MoveData(setDamage(10, 20), MoveData.MoveType.HEALING));
        movelist.get("coffee").setStatusEffect(true, MoveData.MoveType.HEALING, setDamage(50, 60), 2);//test case - 2 turn healing status effect

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

    public MoveData.MoveType getMoveType(String weapon){
        return movelist.get(weapon).getMoveType();
    }

    public List<Integer> setDamage(int min, int max){
        List<Integer> damage = new ArrayList<>();
        damage.add(min);
        damage.add(max);
        return damage;
    }

    //for printing info about a move in combat screen
    //will need to add other info (cooldowns, uses per encounter, etc)
    public String toString(String move){
        MoveData selectedMove = movelist.get(move);
        String ret = move + " - " + selectedMove.getMoveType() + "\n" + selectedMove.getRange().get(0) + " - " + selectedMove.getRange().get(1);
        if(movelist.get(move).getHasStatusEffect()) ret += "\n" + movelist.get(move).getStatusEffectType() + " status effect, " +  movelist.get(move).getStatusEffectRange().get(0) + " - " + movelist.get(move).getStatusEffectRange().get(1);
        else ret+= "\nno status effect";
        return ret;
    }


    public MoveData.MoveType getStatusEffectMoveType(String moveContainingStatusEffect) {
        return movelist.get(moveContainingStatusEffect).getStatusEffectType();
    }

    public int useMove(String nameOfMove, List<String> moves, List<String> statusEffects){
        //first, call perform other move on all other moves
        for(String otherMove : moves){//also call preformOtherMove() on all other moves in the list I pass in
            if(movelist.get(otherMove) != movelist.get(nameOfMove)){
                movelist.get(otherMove).performOtherMove();
            }
        }
        return movelist.get(nameOfMove).useMove(nameOfMove, moves, statusEffects); //then use the selected move, and return amount
    }

    public int useStatusEffect(String nameOfMove, List<String> statusEffects){
        return movelist.get(nameOfMove).useStatusEffect(nameOfMove, statusEffects);
    }

    public void resetMoves(List<String> moves){
        for(String moveName : moves) movelist.get(moveName).resetMove();
    }

    public boolean isCurrentlyAvailable(String nameOfMove){
        return movelist.get(nameOfMove).getCurrentlyAvailable();
    }

}
