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
    private List<List<String>> enemyWeapons;


    public List<String> getEnemyWeapons(int level) {
        return enemyWeapons.get(level);
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

        //test case I didn't feel like removing yet
        movelist.put("sword", new MoveData.Builder(setDamage(10, 20), MoveData.MoveType.ATTACK).build()); //bare minimum  - name, range, type

        //some player attacks
        //note that we can control what moves are sold on each floor
        movelist.put("Stapler", new MoveData.Builder(setDamage(15, 25), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .setDurability(4)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(5, 10), 2) //bleeding
                .build()); //bare minimum  - name, range, type
        movelist.put("Paper Clip", new MoveData.Builder(setDamage(5, 10), MoveData.MoveType.ATTACK)
                .setDurability(2)
                .build());
        movelist.put("Keyboard", new MoveData.Builder(setDamage(25, 35), MoveData.MoveType.ATTACK)
                .setDurability(2)
                .build());
        movelist.put("Mug", new MoveData.Builder(setDamage(10,15), MoveData.MoveType.ATTACK)
                .setDurability(1)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(10, 20), 1) //burning
                .build());
        movelist.put("Tie", new MoveData.Builder(setDamage(0,5), MoveData.MoveType.ATTACK).build()); //no durability, but weak, prevents player from having no moves
        movelist.put("Water Bottle", new MoveData.Builder(setDamage(10, 15), MoveData.MoveType.ATTACK)
                .setCooldown(3)
                .setUsesPerEncounter(1)
                .setDurability(3)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(10, 20), 2)
                .build());
        movelist.put("Chair", new MoveData.Builder(setDamage(20,30), MoveData.MoveType.ATTACK)
                .setDurability(1)
                .build());
        movelist.put("Telephone", new MoveData.Builder(setDamage(20, 35), MoveData.MoveType.ATTACK)
                .setDurability(2)
                .setCooldown(2)
                .build());
        movelist.put("Ruler", new MoveData.Builder(setDamage(10, 15), MoveData.MoveType.ATTACK)
                .setDurability(2)
                .build());
        movelist.put("Glue", new MoveData.Builder(setDamage(10, 15), MoveData.MoveType.ATTACK)
                .setUsesPerEncounter(1)
                .setDurability(3)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(10, 20), 1)
                .build());
        movelist.put("Tape", new MoveData.Builder(setDamage(15, 20), MoveData.MoveType.ATTACK)
                .setDurability(1)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(10, 20), 1)
                .build());
        movelist.put("Scissors", new MoveData.Builder(setDamage(25,30), MoveData.MoveType.ATTACK)
                .setUsesPerEncounter(1)
                .setDurability(2)
                .build());
        movelist.put("Wired Mouse", new MoveData.Builder(setDamage(25,35), MoveData.MoveType.ATTACK)
                .setDurability(1)
                .build());
        movelist.put("Desk Lamp", new MoveData.Builder(setDamage(20,40), MoveData.MoveType.ATTACK)
                .setCooldown(3)
                .setDurability(2)
                .build());
        movelist.put("Laptop", new MoveData.Builder(setDamage(15,25), MoveData.MoveType.ATTACK)
                .setUsesPerEncounter(1)
                .setDurability(3)
                .build());


        //some player healing moves
        //I'm going to make all healing items single use for now, which means no cooldown or uses per turn limit
        movelist.put("coffee", new MoveData.Builder(setDamage(20, 40), MoveData.MoveType.HEALING)
                .setDurability(1) //single use
                .setStatusEffect(MoveData.MoveType.HEALING, setDamage(10, 20), 2) //adding a status effect to this one
                .build());
        movelist.put("Tea", new MoveData.Builder(setDamage(20, 40), MoveData.MoveType.HEALING)
                .setDurability(1) //single use
                .setStatusEffect(MoveData.MoveType.HEALING, setDamage(10, 20), 2)
                .build());
        movelist.put("Hot Chocolate", new MoveData.Builder(setDamage(10, 15), MoveData.MoveType.HEALING)
                .setDurability(1) //can use twice
                .build());
        movelist.put("Lemonade", new MoveData.Builder(setDamage(10, 20), MoveData.MoveType.HEALING)
                .setDurability(1) //single use
                .setStatusEffect(MoveData.MoveType.HEALING, setDamage(5, 20), 2)
                .build());
        movelist.put("Iced Tea", new MoveData.Builder(setDamage(15, 20), MoveData.MoveType.HEALING)
                .setDurability(1)
                .build());
        movelist.put("Milk", new MoveData.Builder(setDamage(5,10), MoveData.MoveType.HEALING)
                .setDurability(1)
                .setStatusEffect(MoveData.MoveType.HEALING, setDamage(0, 10), 2)
                .build());
        movelist.put("Chocolate Milk", new MoveData.Builder(setDamage(20,30), MoveData.MoveType.HEALING)
                .setDurability(1)
                .build());
        movelist.put("Soda", new MoveData.Builder(setDamage(5,10), MoveData.MoveType.HEALING)
                .setDurability(1)
                .build());
        movelist.put("Soup", new MoveData.Builder(setDamage(15,30), MoveData.MoveType.HEALING)
                .setDurability(1)
                .build());
        movelist.put("Sports Drink", new MoveData.Builder(setDamage(5,10), MoveData.MoveType.HEALING)
                .setDurability(1)
                .build());
        movelist.put("Apple Cider", new MoveData.Builder(setDamage(10, 15), MoveData.MoveType.HEALING)
                .setDurability(1)
                .build());
        movelist.put("Water", new MoveData.Builder(setDamage(20,25), MoveData.MoveType.HEALING)
                .setDurability(1)
                .build());
        movelist.put("Protein Shake", new MoveData.Builder(setDamage(25,40), MoveData.MoveType.HEALING)
                .setDurability(1)
                .build());
        movelist.put("Orange Juice", new MoveData.Builder(setDamage(10, 15), MoveData.MoveType.HEALING)
                .setDurability(1)
                .build());
        movelist.put("Punch", new MoveData.Builder(setDamage(20,30), MoveData.MoveType.HEALING)
                .setDurability(1)
                .setStatusEffect(MoveData.MoveType.HEALING, setDamage(15, 30), 1)
                .build());

        //some enemy attacks
        //probably won't use durability, but cooldown and uses per turn could be helpful (but I need to test)
        //it also creates variety, which is why I used it a lot below
        movelist.put("Paper Cut", new MoveData.Builder(setDamage(10, 20), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(5, 10), 2)
                .build());
        movelist.put("Slam Tray", new MoveData.Builder(setDamage(0,5), MoveData.MoveType.ATTACK) //make sure the enemy doesn't get stuck - a move with no cooldown, no uses limit, but weak - I might make it so the enemy only picks when nothing else is available
                .build());
        movelist.put("Toner Leak", new MoveData.Builder(setDamage(5, 10), MoveData.MoveType.ATTACK)
                .setCooldown(1)
                .build());
        movelist.put("Short Circuit", new MoveData.Builder(setDamage(5, 10), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(5, 15), 1)
                .build());
        movelist.put("Electrical Fire", new MoveData.Builder(setDamage(5, 10), MoveData.MoveType.ATTACK)
                .setUsesPerEncounter(1)
                .setCooldown(2)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(5, 15), 2)
                .build());
        movelist.put("Power Surge", new MoveData.Builder(setDamage(5, 10), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(5, 10), 2)
                .build());
        movelist.put("Shoot Ink", new MoveData.Builder(setDamage(15, 25), MoveData.MoveType.ATTACK)
                .setUsesPerEncounter(1)
                .build());
        movelist.put("Shoot Paper", new MoveData.Builder(setDamage(15, 20), MoveData.MoveType.ATTACK)
                .setUsesPerEncounter(1)
                .build());
        movelist.put("Paper Jam", new MoveData.Builder(setDamage(5, 10), MoveData.MoveType.ATTACK)
                .setCooldown(1)
                .build());
        movelist.put("Dial-up Noise", new MoveData.Builder(setDamage(15, 25), MoveData.MoveType.ATTACK)
                .setUsesPerEncounter(1)
                .build());
        movelist.put("Overheating", new MoveData.Builder(setDamage(5, 10), MoveData.MoveType.ATTACK)
                .setUsesPerEncounter(1)
                .setCooldown(2)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(5, 10), 2)
                .build());
        movelist.put("Fan Blades", new MoveData.Builder(setDamage(5, 10), MoveData.MoveType.ATTACK)
                .setCooldown(2)
                .setStatusEffect(MoveData.MoveType.ATTACK, setDamage(5, 10), 2)
                .build());
        movelist.put("Dust Cloud", new MoveData.Builder(setDamage(5, 15), MoveData.MoveType.ATTACK)
                .setUsesPerEncounter(1)
                .build());

        //enemy healing?
        //I haven't decided if these should exist
        //if they do, they'll be much weaker, rarer, and I need a smarter AI
        movelist.put("Paper Refill", new MoveData.Builder(setDamage(5, 10), MoveData.MoveType.HEALING)
                .build());


        //Set up weapons/abilities for enemies
        //easiest way is to make higher floors have better moves
        //I'll add more when I start testing and balancing
        enemyWeapons = new ArrayList<>();
        List<String> floor0 = new ArrayList<>();
        floor0.add("Paper Cut");
        floor0.add("Slam Tray");
        //floor0.add("sword");
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

    public boolean getHasStatusEffect(String moveContainingStatusEffect){
        return movelist.get(moveContainingStatusEffect).getHasStatusEffect();
    }

    public int getStatusEffectDuration(String moveContainingStatusEffect){
        return movelist.get(moveContainingStatusEffect).getStatusEffectDuration();
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

    //I'll come back to this later if we end up using it
    public void setupRandomMoves(){
        //here's how random moves work for now
        //its unbalanced, but I've added the ability for us to test and balance easily
        //  -we set up moves and their ranges above, then make random variations for each
        //  -we always keep the type of move, and can never do less damage than the min or more than the max
        //  -all other possible features/stats are randomized, as seen below
        //  -these will be added to a Map<String, List<MoveData>>
        //  -when we give the player or enemy a new move, we can go fully random, or provide a parameter based on the move's ranking (1-5 for now, also needs tweaking)
        //  -same thing with shop, can make a price based on stats of weapon
        //  -I can also mark MoveData objects that are in use - prevent issues above and keeps "duplicates" different

        //here's the current stats on randomization and ranking, which need a lot of adjustments
        //also the ranking system is far from perfect, and should be more representative of the %'s I've listed below
        //  -range: worth 35% of ranking
        //      -we use the original range to determine which slice to use in this variation's range
        //      -10% chance of range = minimum to minimum
        //      -20% chance of range = minimum to 25% of max
        //      -30% chance of range = 25% of max to 50% of max
        //      -20% chance of range = 50% of max to 75% of max
        //      -10% chance of range = maximum to maximum
        //  -durability: worth 10% of ranking (I might go higher)
        //      -1% chance of infinite/no durability (might adjust)
        //      -10% chance of 1, 10% chance of 2, ... 10 % chance of 9 (should adjust)
        //      -9% chance of 10 (should adjust)
        //  -uses per turn: worth 10% of ranking (I might go lower)
        //      -1% chance of no limit (might adjust)
        //      -20% chance of 1, 20% chance of 2, ... 20 % chance of 4 (should adjust)
        //      -19% chance of 5 (should adjust)
        //  -cooldown: worth 10% of ranking (I might go lower)
        //      -1% chance of no cooldown (might adjust)
        //      -33.33% chance of 1 (should adjust)
        //      -32.33% chance of 2 (should adjust)
        //      -33.33% chance of 3 (should adjust)
        //  -status effect: worth 35% of ranking
        //      -33.33% chance of having a status effect (which is worth 20% out of the 35%)
        //      -50% chance of being healing, 50% chance of being attack
        //      -33% chance of duration being 1, 33% chance of duration being 2, 33% chance of duration being 3 (adjust)
        //      -then the range stuff:
        //          -25% chance of having a higher maximum range than the original (adjust and fix ranking amount here)
        //          -then the same percentages as the range above, again based on the original range (unless max was adjusted in step before)

        //further balance issues:
        //  -running out of moves (weapon breaks, have no more and cannot get any more)
        //  -overpowered (infinite healing items)
        //  -underpowered


        //used for stats
        double hasStatusEffect=0, highDurability=0, highUsesPerTurn=0, lowCooldown=0, weak=0, strong = 0, one=0, two=0, three=0, four=0, five=0;

        Random rand = new Random();
        for(String move : movelist.keySet()){//go through all moves created above
            List<MoveData> currentMovelist = new ArrayList<>();//make a new list to store all random variations for this move
            MoveData originalMove = movelist.get(move); //get the original move
            for(int i =0 ; i< 100; i++){ //make 100 random variations
                //variables we'll set through randomization
                //I do things this way so I can use the builder at the end for clarity
                List<Integer> range, statusEffectRange = new ArrayList<>();
                int durability = 0;
                int usesPerTurn = 0;
                int cooldown = 0;
                boolean statusEffect = false;
                MoveData.MoveType statusType = null;
                int duration = 0;
                int ranking = 0;

                //1st up is damage/amount range - based this on the starting one, won't go higher or lower, but will modify range
                int damageRandomAssignment = rand.nextInt(10) + 1; //roll 1-10
                //get min and max from the original move's min and max
                int min = originalMove.getRange().get(0);
                int max = originalMove.getRange().get(1);
                int halfway = ((max-min)/2) + min; //50% of max
                int minToHalf = ((halfway-min) / 2) + min; //25% of max
                int halfToMax = ((max-halfway) / 2) + halfway; //75% of max
                if(damageRandomAssignment == 1){//1 - do min damage every time
                    range = setDamage(min, min);
                    ranking+=0;
                }else if(damageRandomAssignment < 4){//2, 3 - do min to a little above min
                    range = setDamage(min, minToHalf);
                    ranking+=5;
                }else if(damageRandomAssignment < 7) { //4,5,6 - do more than min to half
                    range = setDamage(minToHalf, halfway);
                    ranking+=10;
                }else if(damageRandomAssignment <9){//7,8 - do half to max
                    range = setDamage(halfway, halfToMax);
                    ranking+=20;
                }else if(damageRandomAssignment < 10){ //75 - 100%
                    range = setDamage(halfToMax, max);
                    ranking+=30;
                }
                else { //10 - do max
                    range = setDamage(max, max);
                    ranking+=35;
                }

                //next is durability
                //1-100 - num % 10 is durability, 100 is infinite use
                //remember to tweak healing items here
                int durabilityRandomAssignment = rand.nextInt(100) + 1; //roll 1-100
                if(durabilityRandomAssignment != 100){
                    durability = (durabilityRandomAssignment % 10) + 1; //1 - 10
                    ranking+= (durability-1);
                }else{
                    ranking+=10;
                }

                //next is uses per turn
                //1-100 - num % 5 is uses per turn, 100 is no limit
                int usesPerTurnRandomAssignment = rand.nextInt(100) + 1; //roll 1-100
                if(usesPerTurnRandomAssignment != 100){
                    usesPerTurn = (usesPerTurnRandomAssignment % 5) + 1; //1-5
                    if(usesPerTurn == 1) ranking+=0;
                    if(usesPerTurn == 2) ranking+=3;
                    if(usesPerTurn == 3) ranking+=6;
                    if(usesPerTurn == 4) ranking+=8;
                    if(usesPerTurn == 5) ranking+=9;
                }else{
                    ranking+=10;
                }

                //next is cooldown
                //1-100 - num % 3 is cooldown, 100 is no cooldown
                int cooldownRandomAssignment = rand.nextInt(100) + 1; //roll 1-100
                if(cooldownRandomAssignment != 100){
                    cooldown = (cooldownRandomAssignment % 3) + 1; //1-3
                    if(cooldown == 1) ranking+=9;
                    if(cooldown == 2) ranking+=5;
                    if(cooldown == 3) ranking+=0;
                }else{
                    ranking+=10;
                }

                //then status effect
                int statusEffectRandomAssignment = rand.nextInt(3) + 1; //roll 1-3
                if(statusEffectRandomAssignment == 3){//1/3 chance for now
                    statusEffect = true;
                    ranking+=20;
                    int typeRandomAssignment = rand.nextInt(1) + 1; //50% chance of either
                    if(typeRandomAssignment == 1){
                        statusType = MoveData.MoveType.ATTACK;
                    }else{
                        statusType = MoveData.MoveType.HEALING;
                    }
                    duration = rand.nextInt(3) + 1; //roll 1-3, determines duration right from that
                    ranking+=duration;
                    int statusEffectRangeRandomAssignment = rand.nextInt(4) + 1;
                    int maxRange, minRange;
                    if(statusEffectRangeRandomAssignment == 4) { //25% chance of higher range than original
                        maxRange = rand.nextInt(max) + max;//up to double
                        minRange = min;
                        //really need a ranking adjustment here
                    }
                    else{
                        maxRange = max;
                        minRange = min;
                    }
                    //same as the original damage stuff, can make a function
                    damageRandomAssignment = rand.nextInt(10) + 1; //roll 1-10
                    halfway = ((maxRange-minRange)/2) + minRange; //50%
                    minToHalf = ((halfway-minRange) / 2) + minRange; //25%
                    halfToMax = ((maxRange-halfway) / 2) + halfway;//75%
                    if(damageRandomAssignment == 1){//1 - do min damage every time
                        statusEffectRange = setDamage(minRange, minRange);
                        ranking+=0;
                    }else if(damageRandomAssignment < 4){//2, 3 - do min to a little above min
                        statusEffectRange = setDamage(minRange, minToHalf);
                        ranking+=2;
                    }else if(damageRandomAssignment < 7) { //4,5,6 - do more than min to half
                        statusEffectRange = setDamage(minToHalf, halfway);
                        ranking+=5;
                    }else if(damageRandomAssignment <9){//7,8 - do half to half to max
                        statusEffectRange = setDamage(halfway, halfToMax);
                        ranking+=8;
                    } else if(damageRandomAssignment <10){ // 9 - do half to max to max
                        statusEffectRange = setDamage(halfToMax, maxRange);
                        ranking+=10;
                    }else { //10 - do max
                        statusEffectRange = setDamage(maxRange, maxRange);
                        ranking+=12;
                    }
                }

                //finally, start building the move
                MoveData.Builder builder = new MoveData.Builder(range, originalMove.getMoveType());

                if(durability != 0){
                    builder.setDurability(durability);
                }
                if(cooldown != 0){
                    builder.setCooldown(cooldown);
                }
                if(usesPerTurn != 0){
                    builder.setUsesPerEncounter(usesPerTurn);
                }
                if(statusEffect){
                    builder.setStatusEffect(statusType, statusEffectRange, duration);
                }

                MoveData currentMove = builder.build();//actually build the MoveData object
                currentMove.setRanking(ranking);//set its ranking, which we've been building up here
                currentMovelist.add(currentMove);//add to current arraylist

                //stuff for testing:
                //System.out.println(move + " " + i + ": "+ currentMove.toString() + "\n\n"); //print move variation number and info

                //collect stats on percentage of moves with certain criteria
                if(currentMove.getHasStatusEffect()) hasStatusEffect++;
                if(durability == 0 ) highDurability++;
                if(usesPerTurn == 0 ) highUsesPerTurn++;
                if(cooldown == 0 ) lowCooldown++;
                //if(durability == 0 || durability > 8) highDurability++;
                //if(usesPerTurn == 0 || usesPerTurn > 4) highUsesPerTurn++;
                //if(cooldown == 0 || cooldown <2) lowCooldown++;

                if(currentMove.getHasStatusEffect() && durability ==0 && usesPerTurn == 0 && cooldown ==0) strong++;
                if(!currentMove.getHasStatusEffect() && durability<2 && usesPerTurn < 2 && cooldown>2) weak++;

                int r = currentMove.getRanking();
                if(r == 1) one++;
                if(r==2) two++;
                if(r==3) three++;
                if(r==4) four++;
                if(r==5) five++;

            }
            randomMovelist.put(move, currentMovelist); //add all the variations to the random list
        }

        //stats to print out to help with balance and testing
        double maxMoves = 100 * movelist.keySet().size();
        System.out.println((hasStatusEffect/maxMoves) * 100 + "% have status effects");
        System.out.println( (highDurability/maxMoves) * 100 + "% have high durability");
        System.out.println((highUsesPerTurn/maxMoves) * 100 + "% have high uses per turn");
        System.out.println((lowCooldown/maxMoves) * 100 + "% have low cooldown");
        System.out.println((strong/maxMoves) * 100 + "% are overpowered");
        System.out.println((weak/maxMoves) * 100 + "% are weak");
        System.out.println((one/maxMoves) * 100 + "% are ranked 1");
        System.out.println((two/maxMoves) * 100 + "% are ranked 2");
        System.out.println((three/maxMoves) * 100 + "% are ranked 3");
        System.out.println((four/maxMoves) * 100 + "% are ranked 4");
        System.out.println((five/maxMoves) * 100 + "% are ranked 5");
    }

}
