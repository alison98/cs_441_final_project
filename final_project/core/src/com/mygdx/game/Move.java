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
    private Map<String, List<MoveData>> randomMovelist; //this is what I'll use when we need a random move, still need to figure out how to use elsewhere now
    private Map<String, List<String>> enemyWeapons;


    //rather than using floor, use sprite image name to get correct move set
    public List<MoveData> getEnemyWeapons(String file) {
        String name = file.substring(0,file.length()-4);
        name = name.replaceAll("-"," ");
        name = name.toUpperCase();
        //System.out.println(name);
        List<String> weaponList = new ArrayList<>();
        if(name.contains("PRINT")) weaponList = enemyWeapons.get("Printer");
        else if(name.contains("FAX")) weaponList = enemyWeapons.get("Fax");
        else if(name.contains("BOX")) weaponList =  enemyWeapons.get("Box");
        else if(name.contains("CEO")) weaponList =  enemyWeapons.get("CEO");
        else if(name.contains("SERVER") || name.contains("CLOUD")) weaponList =  enemyWeapons.get("Server");
        else if(name.contains("CABINET")) weaponList =  enemyWeapons.get("Cabinet");
        else {
            System.out.println("\n\n MATT NEEDS TO ADJUST FOR AN IMAGE NAME \n\n");
            weaponList =  enemyWeapons.get("Printer");
        }
        List<MoveData> returnList = new ArrayList<>();
        for(String currentWeapon : weaponList){
            returnList.add(Move.getInstance().getCopy(currentWeapon));
        }
        return returnList;
    }

    //Singleton constructor
    private Move(){
        //Set up all possible moves in game
        movelist = new HashMap<>();
        randomMovelist = new HashMap<>();//likely to change in some capacity
        //I switched to a builder

        //starting to set up some more moves (I need more and will keep thinking, especially for different enemy sprites)
        //player and enemy cannot use the same moves right now, so I've separated here and may take further precautions (or fix the limitation)
        //I also need to think about balance and making things interesting


        //roughly organized from best (more expensive) to worst (cheapest)
        movelist.put("Desk Lamp", new MoveData.Builder("Desk Lamp", setDamage(20,40), MoveData.MoveType.ATTACK)
                .setDurability(4)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(15, 30), 3)
                .build());
        movelist.put("Wired Mouse", new MoveData.Builder("Wired Mouse", setDamage(25,35), MoveData.MoveType.ATTACK)
                .setDurability(3)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(15, 20), 2)
                .build());
        movelist.put("Keyboard", new MoveData.Builder("Keyboard", setDamage(25, 35), MoveData.MoveType.ATTACK)
                .setDurability(3)
                .build());
        movelist.put("Scissors", new MoveData.Builder("Scissors", setDamage(25,30), MoveData.MoveType.ATTACK)
                .setDurability(3)
                .build());
        movelist.put("Telephone", new MoveData.Builder("Telephone", setDamage(20, 35), MoveData.MoveType.ATTACK)
                .setDurability(3)
                .setCooldown(1)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(15, 20), 2)
                .build());
        movelist.put("Chair", new MoveData.Builder("Chair", setDamage(20,30), MoveData.MoveType.ATTACK)
                .setDurability(3)
                .build());
        movelist.put("Stapler", new MoveData.Builder("Stapler", setDamage(15, 25), MoveData.MoveType.ATTACK)
                .setCooldown(1)
                .setDurability(2)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(5, 10), 2) //bleeding
                .build());
        movelist.put("Laptop", new MoveData.Builder("Laptop", setDamage(15,25), MoveData.MoveType.ATTACK)
                .setUsesPerEncounter(1)
                .setDurability(4)
                .build());
        movelist.put("Sword", new MoveData.Builder("Sword", setDamage(10, 20), MoveData.MoveType.ATTACK)
                .setDurability(3)
                .setCooldown(2)
                .build());
        movelist.put("Tape", new MoveData.Builder("Tape", setDamage(15, 20), MoveData.MoveType.ATTACK)
                .setDurability(2)
                .setUsesPerEncounter(2)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(10, 20), 1)
                .build());
        movelist.put("Water Bottle", new MoveData.Builder("Water Bottle", setDamage(10, 15), MoveData.MoveType.ATTACK)
                .setUsesPerEncounter(1)
                .setDurability(3)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(10, 20), 2)
                .build());
        movelist.put("Mug", new MoveData.Builder("Mug", setDamage(10,15), MoveData.MoveType.ATTACK)
                .setDurability(1)
                .setCooldown(1)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(10, 20), 1) //burning
                .build());
        movelist.put("Ruler", new MoveData.Builder("Ruler", setDamage(10, 15), MoveData.MoveType.ATTACK)
                .setDurability(2)
                .build());
        movelist.put("Fists", new MoveData.Builder("Fists", setDamage(2, 7), MoveData.MoveType.ATTACK)
                .build());
        movelist.put("Paper Clip", new MoveData.Builder("Paper Clip", setDamage(5, 10), MoveData.MoveType.ATTACK)
                .setDurability(2)
                .build());
        movelist.put("Tie", new MoveData.Builder("Tie", setDamage(1,5), MoveData.MoveType.ATTACK).build());

        //some player healing moves
        //I'm going to make all healing items single use for now, which means no cooldown or uses per turn limit
        //roughly organized from best (more expensive) to worst (cheapest)
        movelist.put("Coffee", new MoveData.Builder("Coffee",setDamage(20, 40), MoveData.MoveType.HEALING)
                .setDurability(1) //single use
                .setStatusEffect(MoveData.MoveType.HEALING, setDamage(10, 20), 3) //adding a status effect to this one
                .build());
        movelist.put("Tea", new MoveData.Builder("Tea",setDamage(20, 40), MoveData.MoveType.HEALING)
                .setDurability(1) //single use
                .setStatusEffect(MoveData.MoveType.HEALING, setDamage(10, 20), 2)
                .build());
        movelist.put("Protein Shake", new MoveData.Builder("Protein Shake",setDamage(25,40), MoveData.MoveType.HEALING)
                .setDurability(1)
                .build());
        movelist.put("Punch", new MoveData.Builder("Punch",setDamage(20,30), MoveData.MoveType.HEALING)
                .setDurability(1)
                .setStatusEffect(MoveData.MoveType.HEALING, setDamage(15, 30), 1)
                .build());
        movelist.put("Chocolate Milk", new MoveData.Builder("Chocolate Milk",setDamage(20,30), MoveData.MoveType.HEALING)
                .setDurability(1)
                .build());
        movelist.put("Water", new MoveData.Builder("Water",setDamage(20,25), MoveData.MoveType.HEALING)
                .setDurability(1)
                .build());
        movelist.put("Soup", new MoveData.Builder("Soup",setDamage(15,30), MoveData.MoveType.HEALING)
                .setDurability(1)
                .build());
        movelist.put("Iced Tea", new MoveData.Builder("Iced Tea",setDamage(15, 20), MoveData.MoveType.HEALING)
                .setDurability(1)
                .build());
        movelist.put("Lemonade", new MoveData.Builder("Lemonade",setDamage(10, 20), MoveData.MoveType.HEALING)
                .setDurability(1)
                .setStatusEffect(MoveData.MoveType.HEALING, setDamage(5, 20), 2)
                .build());
        movelist.put("Hot Chocolate", new MoveData.Builder("Hot Chocolate",setDamage(10, 15), MoveData.MoveType.HEALING)
                .setDurability(1)
                .build());
        movelist.put("Apple Cider", new MoveData.Builder("Apple Cider",setDamage(10, 15), MoveData.MoveType.HEALING)
                .setDurability(1)
                .build());
        movelist.put("Orange Juice", new MoveData.Builder("Orange Juice",setDamage(10, 15), MoveData.MoveType.HEALING)
                .setDurability(1)
                .build());
        movelist.put("Milk", new MoveData.Builder("Milk",setDamage(5,10), MoveData.MoveType.HEALING)
                .setDurability(1)
                .setStatusEffect(MoveData.MoveType.HEALING, setDamage(5, 10), 2)
                .build());
        movelist.put("Soda", new MoveData.Builder("Soda",setDamage(5,10), MoveData.MoveType.HEALING)
                .setDurability(1)
                .build());
        movelist.put("Sports Drink", new MoveData.Builder("Sports Drink",setDamage(5,10), MoveData.MoveType.HEALING)
                .setDurability(1)
                .build());


        //some enemy attacks
        //probably won't use durability, but cooldown and uses per turn could be helpful (but I need to test)
        //it also creates variety, which is why I used it a lot below

        //printer moves
        movelist.put("Paper Cut", new MoveData.Builder("Paper Cut",setDamage(5, 10), MoveData.MoveType.ATTACK)
                .setCooldown(3)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(1, 5), 1)
                .build());
        movelist.put("Slam Tray", new MoveData.Builder("Slam Tray",setDamage(1,3), MoveData.MoveType.ATTACK)
                .setCooldown(1)
                .build());
        movelist.put("Toner Leak", new MoveData.Builder("Toner Leak",setDamage(1, 5), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .build());
        movelist.put("Short Circuit", new MoveData.Builder("Short Circuit",setDamage(2, 4), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .build());
        movelist.put("Electrical Fire", new MoveData.Builder("Electrical Fire",setDamage(2, 4), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .build());
        movelist.put("Shoot Ink", new MoveData.Builder("Shoot Ink",setDamage(1, 5), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .build());
        movelist.put("Fan Blades", new MoveData.Builder("Fan Blades",setDamage(5, 10), MoveData.MoveType.ATTACK)
                .setCooldown(3)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(5, 10), 1)
                .build());


        //fax moves
        movelist.put("Dial-up Noise", new MoveData.Builder("Dial-up Noise",setDamage(5, 15), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(5, 10), 1)
                .build());
        movelist.put("Shoot Paper", new MoveData.Builder("Shoot Paper",setDamage(1, 10), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(1, 5), 1)
                .build());
        movelist.put("Overheating", new MoveData.Builder("Overheating",setDamage(1, 5), MoveData.MoveType.ATTACK)
                .setCooldown(4)
                .build());
        movelist.put("Paper Jam", new MoveData.Builder("Paper Jam",setDamage(3, 7), MoveData.MoveType.ATTACK)
                .setCooldown(1)
                .build());
        movelist.put("Power Surge", new MoveData.Builder("Power Surge",setDamage(2, 10), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .build());
        movelist.put("Dust Cloud", new MoveData.Builder("Dust Cloud",setDamage(5, 10), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .build());

        //box moves
        movelist.put("Slam Lid", new MoveData.Builder("Slam Lid",setDamage(5, 10), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(1, 5), 1)
                .build());
        movelist.put("Heavy Lifting", new MoveData.Builder("Heavy Lifting",setDamage(1, 5), MoveData.MoveType.ATTACK)
                .setCooldown(1)
                .build());
        movelist.put("Unorganized Files", new MoveData.Builder("Unorganized Files",setDamage(1, 10), MoveData.MoveType.ATTACK)
                .setCooldown(3)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(1, 5), 1)
                .build());
        movelist.put("Packing Tape", new MoveData.Builder("Packing Tape", setDamage(2, 7), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .build());

        //server moves
        movelist.put("Flashing Lights", new MoveData.Builder("Flashing Lights",setDamage(5, 20), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(5, 10), 1)
                .build());
        movelist.put("Loud Beeping", new MoveData.Builder("Loud Beeping",setDamage(10, 20), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(1, 10), 1)
                .build());
        movelist.put("404 Error", new MoveData.Builder("404 Error",setDamage(5, 10), MoveData.MoveType.ATTACK)
                .setCooldown(4)
                .build());
        movelist.put("Unscheduled Maintenance", new MoveData.Builder("Unscheduled Maintenance", setDamage(1, 5), MoveData.MoveType.ATTACK)
                .setCooldown(1)
                .build());
        movelist.put("Crash", new MoveData.Builder("Crash",setDamage(5, 7), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .build());
        movelist.put("DDoS Attack", new MoveData.Builder("DDoS Attack",setDamage(1, 15), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .build());

        //filing cabinet moves
        movelist.put("Slam Drawers", new MoveData.Builder("Slam Drawers",setDamage(10, 25), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(5, 10), 1)
                .build());
        movelist.put("Pinch Fingers", new MoveData.Builder("Pinch Fingers",setDamage(1, 20), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .build());
        movelist.put("Ink Leak", new MoveData.Builder("Ink Leak",setDamage(1, 5), MoveData.MoveType.ATTACK)
                .setCooldown(4)
                .build());
        movelist.put("Tipping Over", new MoveData.Builder("Tipping Over",setDamage(1, 10), MoveData.MoveType.ATTACK)
                .setCooldown(1)
                .build());


        //ceo moves
        //8 possible, each with a 2 turn cooldown so something new is selected most turns
        //as he is the final boss, also has increased damage and half of his attacks have status effects
        movelist.put("Layoffs", new MoveData.Builder("Layoffs",setDamage(15, 25), MoveData.MoveType.ATTACK)
                .setCooldown(3)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(5, 15), 3)
                .build());
        movelist.put("Budget Cuts", new MoveData.Builder("Budget Cuts",setDamage(15, 25), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(5, 10), 2)
                .build());
        movelist.put("Audit", new MoveData.Builder("Audit", setDamage(15, 25), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(5, 15), 1)
                .build());
        movelist.put("Downsizing", new MoveData.Builder("Downsizing",setDamage(15, 25), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .build());
        movelist.put("Performance Review", new MoveData.Builder("Performance Review",setDamage(5, 10), MoveData.MoveType.ATTACK)
                .setCooldown(1)
                .build());
        movelist.put("Demotion", new MoveData.Builder("Demotion",setDamage(10, 15), MoveData.MoveType.ATTACK)
                .setCooldown(1)
                .build());
        movelist.put("Wage Cut", new MoveData.Builder("Wage Cut",setDamage(10, 15), MoveData.MoveType.ATTACK)
                .setCooldown(1)
                .build());




        //Set up weapons/abilities for enemies
        //easiest way is to make higher floors have better moves
        //I'll add more when I start testing and balancing
        /*
        //this is the old way, based on floor
        //I may still use floor to increase damage of moves, but have prioritized unique moves for different enemy types
        enemyWeapons = new ArrayList<>();
        List<String> floor0 = new ArrayList<>();
        floor0.add("Paper Cut");
        floor0.add("Paper Cut");
        floor0.add("Slam Tray");
        //floor0.add("sword");
        //floor0.add("coffee"); //testing enemy healing itself
        List<String> floor1 = new ArrayList<>();
        floor1.add("Slam Tray");
        List<String> floor2 = new ArrayList<>();
        floor2.add("Slam Tray");;
        List<String> floor3 = new ArrayList<>();
        floor3.add("Slam Tray");
        List<String> floor4 = new ArrayList<>();
        floor4.add("Slam Tray");
        enemyWeapons.add(floor0);
        enemyWeapons.add(floor1);
        enemyWeapons.add(floor2);
        enemyWeapons.add(floor3);
        enemyWeapons.add(floor4);
         */

        //new way - moves are unique to each enemy "type" / sprite
        //I may still use floors (increase damage based on floor)
        enemyWeapons = new HashMap<>();
        List<String> printer = new ArrayList<>();
        printer.add("Paper Cut");
        printer.add("Toner Leak");
        printer.add("Short Circuit");
        printer.add("Electrical Fire");
        printer.add("Shoot Ink");
        printer.add("Fan Blades");
        printer.add("Slam Tray");
        List<String> fax = new ArrayList<>();
        fax.add("Dial-up Noise");
        fax.add("Shoot Paper");
        fax.add("Overheating");
        fax.add("Paper Jam");
        fax.add("Power Surge");
        fax.add("Dust Cloud");
        fax.add("Slam Tray");
        List<String> box = new ArrayList<>();
        box.add("Slam Lid");
        box.add("Dust Cloud");
        box.add("Paper Cut");
        box.add("Heavy Lifting");
        box.add("Unorganized Files");
        box.add("Packing Tape");
        List<String> server = new ArrayList<>();
        server.add("Flashing Lights");
        server.add("Loud Beeping");
        server.add("404 Error");
        server.add("Unscheduled Maintenance");
        server.add("Overheating");
        server.add("Crash");
        server.add("DDoS Attack");
        List<String> cabinet = new ArrayList<>();
        cabinet.add("Slam Drawers");
        cabinet.add("Pinch Fingers");
        cabinet.add("Paper Cut");
        cabinet.add("Unorganized Files");
        cabinet.add("Ink Leak");
        cabinet.add("Tipping Over");
        List<String> ceo = new ArrayList<>();
        ceo.add("Layoffs");
        ceo.add("Budget Cuts");
        ceo.add("Audit");
        ceo.add("Downsizing");
        ceo.add("Performance Review");
        ceo.add("Demotion");
        ceo.add("Wage Cut");
        enemyWeapons.put("Printer", printer);
        enemyWeapons.put("Fax", fax);
        enemyWeapons.put("Box", box);
        enemyWeapons.put("Server", server);
        enemyWeapons.put("Cabinet", cabinet);
        enemyWeapons.put("CEO", ceo);
    }

    public static Move getInstance(){
        if(instance == null){
            instance = new Move();
        }
        return instance;
    }

    public MoveData getCopy(String name){
        MoveData newInstance = new MoveData(movelist.get(name));
        return newInstance;
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

    public boolean getHasStatusEffect(String moveContainingStatusEffect){
        return movelist.get(moveContainingStatusEffect).getHasStatusEffect();
    }

    public int getStatusEffectDuration(String moveContainingStatusEffect){
        return movelist.get(moveContainingStatusEffect).getStatusEffectDuration();
    }

    public int useMove(MoveData move, List<MoveData> moves, List<MoveData> statusEffects){
        //first, call perform other move on all other moves
        for(MoveData otherMove : moves){//also call preformOtherMove() on all other moves in the list I pass in
            if(otherMove != move){
                otherMove.performOtherMove();
            }
        }
        return move.useMove(move, moves, statusEffects); //then use the selected move, and return amount
    }

    public int useStatusEffect(MoveData nameOfMove, List<MoveData> statusEffects){
        return nameOfMove.useStatusEffect(nameOfMove, statusEffects);
    }

    public void resetMoves(List<MoveData> moves){
        for(MoveData currentMove : moves) currentMove.resetMove();
    }

    public boolean isCurrentlyAvailable(String nameOfMove){
        return movelist.get(nameOfMove).getCurrentlyAvailable();
    }

}
