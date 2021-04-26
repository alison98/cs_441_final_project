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
    //can add more later if we start getting crazy (different damage ranges for different types, duration (heal for x turns), etc)

    public MoveData(List<Integer> r, MoveType t){
        range = r;
        movetype = t;
    }

    public List<Integer> getRange() { return range; }

    public MoveType getMoveType() { return movetype; }


}
