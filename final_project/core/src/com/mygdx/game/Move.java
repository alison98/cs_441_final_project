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
        //I switched to a builder

        //starting to set up some more moves (I need more and will keep thinking, especially for different enemy sprites)
        //player and enemy cannot use the same moves right now, so I've separated here and may take further precautions (or fix the limitation)
        //I'd also like to randomize some stats, but I'd need to change a bit to do so (same limitation as point above)
        //I also need to think about balance and making things interesting

        //test case I didn't feel like removing yet
        movelist.put("sword", new MoveData.Builder(setDamage(10, 20), MoveData.MoveType.ATTACK).build()); //bare minimum  - name, range, type

        //some player attacks
        movelist.put("Stapler", new MoveData.Builder(setDamage(15, 25), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(5, 10), 2) //bleeding
                .build()); //bare minimum  - name, range, type
        movelist.put("Paper Clip", new MoveData.Builder(setDamage(5, 10), MoveData.MoveType.ATTACK)
                .setDurability(3)
                .build());
        movelist.put("Keyboard", new MoveData.Builder(setDamage(10, 20), MoveData.MoveType.ATTACK)
                .setDurability(2)
                .build());
        movelist.put("Mug", new MoveData.Builder(setDamage(10, 20), MoveData.MoveType.ATTACK)
                .setCooldown(1)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(10, 20), 1) //burning
                .build());
        movelist.put("Tie", new MoveData.Builder(setDamage(5, 10), MoveData.MoveType.ATTACK).build());

        //some player healing moves
        movelist.put("coffee", new MoveData.Builder(setDamage(20, 40), MoveData.MoveType.HEALING)
                .setDurability(1) //single use
                .setStatusEffect(MoveData.MoveType.HEALING, setDamage(10, 20), 2) //adding a status effect to this one
                .build());
        movelist.put("Tea", new MoveData.Builder(setDamage(20, 40), MoveData.MoveType.HEALING)
                .setDurability(1) //single use
                .setStatusEffect(MoveData.MoveType.HEALING, setDamage(10, 20), 2)
                .build());
        movelist.put("Donut", new MoveData.Builder(setDamage(10, 15), MoveData.MoveType.HEALING)
                .setDurability(2) //can use twice
                .build());
        movelist.put("Sandwich", new MoveData.Builder(setDamage(10, 20), MoveData.MoveType.HEALING)
                .setDurability(1) //single use
                .build());

        //some enemy attacks
        movelist.put("Paper Cut", new MoveData.Builder(setDamage(10, 20), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(5, 10), 2)
                .build());
        movelist.put("Slam Lid", new MoveData.Builder(setDamage(5, 10), MoveData.MoveType.ATTACK)
                .build());


        //enemy healing?
        //I haven't decided if these should exist
        //if they do, they'll be much weaker, rarer, and I need a smarter AI
        movelist.put("Paper Refill", new MoveData.Builder(setDamage(5, 10), MoveData.MoveType.HEALING)
                .build());




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
        List<String> floor4 = new ArrayList<>();
        floor4.add("sword");
        enemyWeapons.add(floor0);
        enemyWeapons.add(floor1);
        enemyWeapons.add(floor2);
        enemyWeapons.add(floor3);
        enemyWeapons.add(floor4);
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
        return movelist.get(move).toString();
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
