package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CombatScreen implements Screen {

    //Fields

    private Game game;
    private Stage stage;
    private int height;
    private int width;
    private Player player;
    private Enemy enemy;
    private boolean isPlayerTurn;
    private TextButton[] moveButtons;
    private HealthBar playerHealthBar;
    private HealthBar enemyHealthBar;
    private boolean animationActive; //is there an active animation/movement on screen? if so, we're mid-turn and need to wait
    private AnimationManager animationManager;//this keeps track of/sets the state of animationActive
    private GameScreen gameScreen;
    private Attack currentAttack;
    private float playerX, playerY, enemyX, enemyY;
    private Label textBox; //to display move info
    private Label currentMoveLabel; //to display name of current move
    private boolean touchDown; //to only display move info when mousing over (not on actually using)
    private boolean statusEffectsInProgress;
    private int statusEffectsIndex;
    private Label playerHealthStatusEffect, playerAttackStatusEffect, enemyHealthStatusEffect, enemyAttackStatusEffect;
    private int playerHealthStatusDuration, playerAttackStatusDuration, enemyHealthStatusDuration, enemyAttackStatusDuration;


    //TODO
    // -FEATURES:
    //      -other things to add based on how Moves work:
    //          --Can AI heal? if so make their choices smarter
    //      -use player.getRewards() for drops
    // -BUGS:
    //      -when status effect is brief, name flashes too quick
    //      -fix so player cannot consume move that does nothing
    //          --i.e. healing at 100% won't do anything, but it will use their move - pick one or the other
    //      -test all the new stuff I added to moves
    //          --multiple simultaneous effects might break
    //      -fix bug of going back into combat
    //          --I haven't been able to replicate yet
    //      -fix bug where printer disappears when player loses
    //          --this is because the interactable is removed before combat ends
    //          --decide what happens when player dies first - we might want predefined save points or something to go back to
    // -OTHER:
    //      -different background
    //      -different skin for buttons
    //      -update comments
    //      -less hard-coded numbers (placing UI elements)
    //      -clean the button code




    public CombatScreen(Game g, Enemy e, Player p, GameScreen gameScreen) {
        game = g;
        enemy = e;
        player = p;
        //need to save starting positions so we can place back after
        enemyX = e.getX();
        enemyY = e.getY();
        playerX = p.getX();
        playerY = p.getY();
        stage = new Stage(new ScreenViewport());
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        //next 3 deal with the turn-based system
        isPlayerTurn = true; //player can only go when its their turn
        animationActive = false; //a move can only begin once all animations are done (set by animation manager)
        animationManager = new AnimationManager(2);//right now, max animations at one time is 2 - decreasing health bar and sprite moving across screen
        this.gameScreen = gameScreen;
        initUI(); //set up on screen elements
        touchDown = false;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1); //white background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tick();
        if(stage != null) { //get GL 0x501 error without this check
            stage.act();
            stage.draw();
        }
    }


    //on every tick we play any active animations. all combat stuff is handled by other functions
    //I can do this because its turn-based
    //i.e. we wait for player to select, handler fires, does move, then allows enemy to move, which will then start the process over
    private void tick() {
        //health bar's tick function causes health to change in real-time
        playerHealthBar.tick();
        enemyHealthBar.tick();
        if(currentAttack!=null) currentAttack.tick(); //if there's an attack ongoing, call it's tick
    }

    //start chain of events for the player's turn
    //begin their status effects
    private void beginPlayerTurn() {
        //System.out.println("setup player turn");
        statusEffectsInProgress = true;
        statusEffectsIndex = 0;
        performPlayerStatusEffects();
    }

    //called for every one of the player's current status effects, then playerTurn() can be called
    //i.e. all status effects will go, then player can do move for current turn
    public void performPlayerStatusEffects(){
        System.out.println("enter player status effect");
        System.out.println("num of status effects : " + player.getOngoingStatusEffects().size());
        if(statusEffectsIndex < player.getOngoingStatusEffects().size()){
            String statusEffect = player.getOngoingStatusEffects().get(statusEffectsIndex);
            //System.out.println("performing " + statusEffect);
            int amount = Move.getInstance().useStatusEffect(statusEffect, player.getOngoingStatusEffects());//change to status effect range
            playerTurn(Move.getInstance().getStatusEffectMoveType(statusEffect), amount, statusEffect, true);
            statusEffectsIndex++;
            //System.out.println("next index : " + statusEffectsIndex + " size of list " + player.getOngoingStatusEffects().size());
        }else{
            statusEffectsInProgress = false;
            statusEffectsIndex = 0;
        }
    }

    //activated on selection/handler, after all status effects are done
    private void performSelectedPlayerMove(String selectedWeapon) {
        //System.out.println("player's turn");
        //System.out.println(player.getWeapon().size());
        int amount = Move.getInstance().useMove(selectedWeapon, player.getWeapon(), player.getOngoingStatusEffects());
        //System.out.println(player.getWeapon().size());
        playerTurn(Move.getInstance().getMoveType(selectedWeapon), amount, selectedWeapon, false);
        isPlayerTurn = false;//it is now the enemy's turn, they can go once animations finish
    }

    public void playerTurn(MoveData.MoveType type, int amount, String nameOfMove, boolean StatusEffect){
        if(StatusEffect) currentMoveLabel.setText("Bill Gates" + " used " + nameOfMove + " status effect");
        else currentMoveLabel.setText("Bill Gates" + " used " + nameOfMove);
        switch (type){ //deal with different types of moves
            case ATTACK:
                System.out.println("dealing " + amount + " to enemy");
                enemy.setHealth(enemy.getHealth() - amount);
                enemyHealthBar.decreaseHealth(amount);
                currentAttack = new Attack(player, enemy);
                if(StatusEffect){
                    enemyAttackStatusDuration--;
                    if(enemyAttackStatusDuration == 0) enemyAttackStatusEffect.setVisible(false);
                }else if(Move.getInstance().getHasStatusEffect(nameOfMove)){
                    enemyAttackStatusDuration += Move.getInstance().getStatusEffectDuration(nameOfMove);
                    enemyAttackStatusEffect.setVisible(true);
                }
                break;
            case HEALING:
                System.out.println("healing " + amount + " to self");
                player.setHealth(player.getHealth() + amount);
                playerHealthBar.increaseHealth(amount);
                currentAttack = new Attack(player, player); //don't remove - it doesn't do anything yet, but ensures nothing breaks when healing has no effect (healing at 100% for example)
                //once I decide what I want for healing, I'll replace the attack with that
                if(StatusEffect){
                    playerHealthStatusDuration--;
                    if(playerHealthStatusDuration == 0) playerHealthStatusEffect.setVisible(false);
                }else if(Move.getInstance().getHasStatusEffect(nameOfMove)){
                    System.out.println("before "  + playerHealthStatusDuration);
                    playerHealthStatusDuration += Move.getInstance().getStatusEffectDuration(nameOfMove);
                    playerHealthStatusEffect.setVisible(true);
                }
        }
    }


    public void beginEnemyTurn(){
        //System.out.println("setup enemy's turn");
        statusEffectsInProgress = true;
        statusEffectsIndex = 0;
        performEnemyStatusEffects();
    }


    public void performEnemyStatusEffects(){
        //System.out.println("enter enemy status effect");
        if(statusEffectsIndex < enemy.getOngoingStatusEffects().size()){
            String statusEffect = enemy.getOngoingStatusEffects().get(statusEffectsIndex);
            //System.out.println("performing " + statusEffect);
            int amount = Move.getInstance().useStatusEffect(statusEffect, enemy.getOngoingStatusEffects());//change to status effect range
            enemyTurn(Move.getInstance().getStatusEffectMoveType(statusEffect), amount, statusEffect, false);
            statusEffectsIndex++;
        }else{
            statusEffectsInProgress = false;
            statusEffectsIndex = 0;
            performSelectedEnemyMove();
        }
    }



    //called once all animations finish (the player's turn ends)
    //named "selected" move, but that's for consistency for now (until I make an AI and stop random choices)
    public void performSelectedEnemyMove() {
        //System.out.println("enemy's turn");
        List<String> enemyWeapons = enemy.getWeapon(); //get list of moves
        Random rand = new Random(); //pick one (random for now)
        //I'll probably want to make a smarter choice - only heal when low
        List<String> availableEnemyWeapons = new ArrayList<>();
        for (String currentWeapon : enemyWeapons) //only deal with moves that aren't in cooldown, haven't hit use limit
            if(Move.getInstance().isCurrentlyAvailable(currentWeapon) ) availableEnemyWeapons.add(currentWeapon);
        String selectedWeapon;
        selectedWeapon = availableEnemyWeapons.get(rand.nextInt(availableEnemyWeapons.size()));//get random weapon
        System.out.println("selected " + selectedWeapon);
        int amount = Move.getInstance().useMove(selectedWeapon, enemy.getWeapon(), enemy.getOngoingStatusEffects());
        enemyTurn(Move.getInstance().getMoveType(selectedWeapon), amount, selectedWeapon, false);
        isPlayerTurn = true;//it is now the player's turn, they can go once animations finish
    }

    //common functionality for a turn and a status effect
    //look at type, use amount, modify health bars, etc.
    public void enemyTurn(MoveData.MoveType type, int amount, String nameOfMove, boolean StatusEffect) {
        if(StatusEffect) currentMoveLabel.setText(enemy.getName() + " used " + nameOfMove + " status effect");
        else currentMoveLabel.setText(enemy.getName() + " used " + nameOfMove);
        switch (type) { //deal with different types of moves
            case ATTACK:
                //System.out.println("dealing " + amount + " to player");
                player.setHealth(player.getHealth() - amount);
                playerHealthBar.decreaseHealth(amount);//start animation
                currentAttack = new Attack(enemy, player);
                if(StatusEffect){
                    playerAttackStatusDuration--;
                    if(playerAttackStatusDuration == 0) playerAttackStatusEffect.setVisible(false);
                }else if(Move.getInstance().getHasStatusEffect(nameOfMove)){
                    playerAttackStatusDuration += Move.getInstance().getStatusEffectDuration(nameOfMove);
                    playerAttackStatusEffect.setVisible(true);
                }
                break;
            case HEALING:
                //System.out.println("healing " + amount + " to enemy");
                enemy.setHealth(enemy.getHealth() + amount);
                enemyHealthBar.increaseHealth(amount);
                currentAttack = new Attack(enemy, enemy); //don't remove - it doesn't do anything yet, but ensures nothing breaks when healing has no effect (healing at 100% for example)
                //once I decide what I want for healing, I'll replace the attack with that
                if(StatusEffect){
                    enemyHealthStatusDuration--;
                    if(enemyHealthStatusDuration == 0) enemyHealthStatusEffect.setVisible(false);
                }else if(Move.getInstance().getHasStatusEffect(nameOfMove)){
                    enemyHealthStatusDuration += Move.getInstance().getStatusEffectDuration(nameOfMove);
                    enemyHealthStatusEffect.setVisible(true);
                }
        }
    }


    //called when either side wins
    //called from animationsManager (we don't want to exit until animations are done)
    private void combatOver(boolean playerWon) {
        if(playerWon){//the player won
            player.setPosition(playerX, playerY);//put back in original spot
            if(enemy.isBoss()){
                enemy.setHuman();
                String bossMessage = enemy.getBossMessage();
                if(bossMessage != ""){
                    gameScreen.getHud().setText(bossMessage);
                }
            }
            if(enemy.hasKey()){
                gameScreen.getHud().setText("The enemy dropped a key!");
            }
            List<Integer> rewards = enemy.getRewards(); //0 is xp, 1 is money, 2 will be drops
            player.increaseExperience(rewards.get(0));
            player.setMoney(player.getMoney() + rewards.get(1));
            //player.addWeapons(rewards.get(2));//something like this
            enemy.defeated();
        }else{//player lost
            //place player back at this floor's stairs
            player.setHealth(100);//I also want to reset moves
            //change room back to stairs, reset enemies
            Layout.getInstance().defeat();
            gameScreen.getRoom().defeat();
            Texture stairUpImg = Layout.getInstance().getStairUpImg();
            player.setPosition(Gdx.graphics.getWidth() - stairUpImg.getWidth(), Gdx.graphics.getHeight() - Gdx.graphics.getHeight()/3); //this should put us back at stairs, still need to double check with derrick
        }

        //common to both
        stage.clear();//does clearing stage remove player? - trying to figure out why I need to re-add player to game screen
        enemy.setPosition(enemyX, enemyY); //reset enemy back to starting position
        enemy.scaleSprite(1f);
        player.scaleSprite(1f);//back to original size
        gameScreen.getStage().addActor(player);//necessary, or player won't reappear - IDK why I don't need for enemy
        //gameScreen.getStage().addActor(enemy); //unnecessary
        this.dispose();
        Move.getInstance().resetMoves(enemy.getWeapon());//for any moves that have "per encounter" variables
        Move.getInstance().resetMoves(player.getWeapon());
        player.getOngoingStatusEffects().clear(); //status effects don't transfer between encounters
        enemy.getOngoingStatusEffects().clear();
        game.setScreen(gameScreen);

    }



    //set up the UI elements. Health bar, sprites on screen, names, etc.
    //a lot of this has random hard-coded numbers I need to turn into constants once I have better idea of everything we want here
    private void initUI() {
        setupButtons();

        //put sprites on screen
        //probably need to use size of sprites to determine positions better
        player.setPosition( width / 5f,  (height / 2f) - 75);
        player.scaleSprite(2f);
        stage.addActor(player);
        player.positionChanged();
        enemy.setPosition( width - (width / 5f) - enemy.getWidth(),  (height / 2f) - 75);
        enemy.scaleSprite(2f);
        stage.addActor(enemy);
        //this puts player on left, enemy on right

        //set up label style - I just copied the font from Alison's last project, we can change
        //and we can change to images later if need be
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont(Gdx.files.internal("font/font.fnt"));

        //these 2 labels display the change in health per turn
        Label playerHealthChange = new Label("", labelStyle);
        playerHealthChange.setSize(250, 200);
        playerHealthChange.setPosition(((float) width / 4) + player.getWidth()/2, height - (height / 2.25f));
        playerHealthChange.setAlignment(Align.center);
        playerHealthChange.setFontScale(1.25f);
        playerHealthChange.setColor(Color.BLACK);
        stage.addActor(playerHealthChange);

        Label enemyHealthChange = new Label("", labelStyle);
        enemyHealthChange.setSize(250, 200);
        enemyHealthChange.setPosition((width - (width / 2.5f)), height -  (height / 2.25f));
        enemyHealthChange.setAlignment(Align.center);
        enemyHealthChange.setFontScale(1.25f);
        enemyHealthChange.setColor(Color.BLACK);
        stage.addActor(enemyHealthChange);

        playerHealthStatusEffect = new Label("", labelStyle);
        playerHealthStatusEffect.setSize(250, 200);
        playerHealthStatusEffect.setPosition(125, height -  (height / 2.25f));
        playerHealthStatusEffect.setAlignment(Align.center);
        playerHealthStatusEffect.setFontScale(1.25f);
        playerHealthStatusEffect.setColor(Color.BLUE);
        playerHealthStatusEffect.setText("+");
        playerHealthStatusEffect.setVisible(false);
        stage.addActor(playerHealthStatusEffect);

        playerAttackStatusEffect = new Label("", labelStyle);
        playerAttackStatusEffect.setSize(250, 200);
        playerAttackStatusEffect.setPosition(125, height -  (height / 2.25f) - 50);
        playerAttackStatusEffect.setAlignment(Align.center);
        playerAttackStatusEffect.setFontScale(1.25f);
        playerAttackStatusEffect.setColor(Color.RED);
        playerAttackStatusEffect.setText("-");
        playerAttackStatusEffect.setVisible(false);
        stage.addActor(playerAttackStatusEffect);


        enemyHealthStatusEffect = new Label("", labelStyle);
        enemyHealthStatusEffect.setSize(250, 200);
        enemyHealthStatusEffect.setPosition((width - (width / 5f) + enemy.getWidth()/2) + enemy.getWidth()/2, height -  (height / 2.25f));
        enemyHealthStatusEffect.setAlignment(Align.center);
        enemyHealthStatusEffect.setFontScale(1.25f);
        enemyHealthStatusEffect.setColor(Color.BLUE);
        enemyHealthStatusEffect.setText("+");
        enemyHealthStatusEffect.setVisible(false);
        stage.addActor(enemyHealthStatusEffect);


        enemyAttackStatusEffect = new Label("", labelStyle);
        enemyAttackStatusEffect.setSize(250, 200);
        enemyAttackStatusEffect.setPosition((width - (width / 5f) + enemy.getWidth()/2) + enemy.getWidth()/2, height -  (height / 2.25f) - 50);
        enemyAttackStatusEffect.setAlignment(Align.center);
        enemyAttackStatusEffect.setFontScale(1.25f);
        enemyAttackStatusEffect.setColor(Color.RED);
        enemyAttackStatusEffect.setText("-");
        enemyAttackStatusEffect.setVisible(false);
        stage.addActor(enemyAttackStatusEffect);


        //global label for displaying the current move's name
        currentMoveLabel = new Label("", labelStyle);
        currentMoveLabel.setSize(500, 200);
        currentMoveLabel.setPosition(width/2f - 250, height - height/1.3f);
        currentMoveLabel.setFontScale(1f);//looks better smaller (and I am running out of room)
        currentMoveLabel.setAlignment(Align.center);
        currentMoveLabel.setColor(Color.BLACK);
        stage.addActor(currentMoveLabel);

        //I'm trying to replace some of the hard-coded numbers with constants
        final int LABEL_AND_HEALTHBAR_LENGTH = 750;
        final int LABEL_AND_HEALTHBAR_HEIGHT = 75;
        final int LABEL_AND_HEALTHBAR_OFFSET = 40; //a health bar is 2 rectangles - the background/longer one has length =  LABEL_AND_HEALTHBAR_LENGTH + LABEL_AND_HEALTHBAR_OFFSET

        //2 labels for name and current/total health
        //these look the best when they are directly above the health bars and the same length, so they reuse the constants
        //will probably need to define these numbers differently/less hard-coding and make sure it looks good at different scales(?)
        Label playerNameLabel = new Label("Level " + player.getLevel() + "\nBill Gates", labelStyle);
        playerNameLabel.setSize(LABEL_AND_HEALTHBAR_LENGTH + LABEL_AND_HEALTHBAR_OFFSET, 200);
        playerNameLabel.setPosition(100, height - (height / 5.5f));
        playerNameLabel.setAlignment(Align.center);
        playerNameLabel.setFontScale(1.25f);
        playerNameLabel.setColor(Color.BLACK);
        stage.addActor(playerNameLabel);

        Label enemyNameLabel = new Label("Level " + enemy.getLevel() + "\n" + enemy.getName(), labelStyle);
        enemyNameLabel.setSize(LABEL_AND_HEALTHBAR_LENGTH + LABEL_AND_HEALTHBAR_OFFSET, 200);
        enemyNameLabel.setPosition(width - 850, height -(height / 5.5f));
        enemyNameLabel.setAlignment(Align.center);
        enemyNameLabel.setFontScale(1.25f);
        enemyNameLabel.setColor(Color.BLACK);
        stage.addActor(enemyNameLabel);

        //HealthBar class with a basic decrementing animation
        //I also put the labels with the health bar as well to keep organized (and modify them as part of the animation, which makes sense)
        playerHealthBar = new HealthBar(player.getHealth(), player.getMaxHealth(), 100, height - (height / 3.5f), playerHealthChange, playerNameLabel, LABEL_AND_HEALTHBAR_LENGTH, LABEL_AND_HEALTHBAR_HEIGHT,LABEL_AND_HEALTHBAR_OFFSET);//will also modify X, Y to be based on sprite size, health
        stage.addActor(playerHealthBar);

        enemyHealthBar = new HealthBar(enemy.getHealth(), enemy.getMaxHealth() ,width - 850, height -  (height / 3.5f),enemyHealthChange, enemyNameLabel, LABEL_AND_HEALTHBAR_LENGTH, LABEL_AND_HEALTHBAR_HEIGHT,LABEL_AND_HEALTHBAR_OFFSET);//will also modify X, Y to be based on sprite size, health
        stage.addActor(enemyHealthBar);

        //Label to display info about a move
        //I was going to use Drew's Hud, but I would have needed to set the direction buttons as invisible, move the text box up, reset for the game screen, show/hide text without interacting, etc.
        //so I just made my own to keep things simple (but used same textBox setup)
        textBox = new Label("Hello", new Skin(Gdx.files.internal("skin/plain-james-ui.json"), new TextureAtlas(Gdx.files.internal("skin/plain-james-ui.atlas"))));
        textBox.setWidth(1200);
        textBox.setHeight(200);
        textBox.setFontScale(2f);
        textBox.setAlignment(Align.center);
        textBox.setPosition(width/2f - textBox.getWidth()/2, height/2f - textBox.getHeight());
        textBox.getStyle().background = new Image(new Texture(Gdx.files.internal("textbox.png"))).getDrawable();
        textBox.setVisible(false);

        stage.addActor(textBox);

    }

    //I'm using TextButtons for now, but we can easily change to images
    //this is called once in setupUI, then on every turn that changes the moves available to the player
    //for example, player heals, decreases healing item by 1, we need to update buttons to reflect that
    private void setupButtons(){
        final List<String> playerWeapons = player.getWeapon(); //get list of moves

        final Map<String, Integer> moveOccurrences = new HashMap<>(); //map of (weapon name : number of occurrences of this weapon the player has)
        //we can work this this to prevent making 10 buttons for the same 10 healing items
        //we remove occurrences when used in playerMove, so when setupButtons is called again, this list is smaller, has the accurate info

        //actually count the occurrences
        for (String currentWeapon : playerWeapons){
            int occurrences =  Collections.frequency(playerWeapons, currentWeapon);//thanks https://stackoverflow.com/questions/505928/how-to-count-the-number-of-occurrences-of-an-element-in-a-list
            //System.out.println(currentWeapon + " has " + occurrences);
            if(Move.getInstance().isCurrentlyAvailable(currentWeapon) ){
                //System.out.println("and isCurrentlyAvailable");
                moveOccurrences.put(currentWeapon, occurrences);//probably add a check for if the move is valid (hasn't exceeded uses per turn, cooldown ready)
            }//else System.out.println("and is not CurrentlyAvailable");
        }

        int prevButtonIndex = -1;//index of the button which will be used to go back a "page". Will only be used if we have more moves than we can display at once
        moveButtons = new TextButton[moveOccurrences.size()];//make a button for each unique move
        //starting position of 1st button
        float nextY = 200;
        float nextX = 150;

        List<String> alphabeticalList = new ArrayList<>(moveOccurrences.keySet());
        Collections.sort(alphabeticalList);

        int i = 0;
        for(String currentMove : alphabeticalList){ //set up buttons - a max of 2 rows
            Skin s = new Skin(Gdx.files.internal("skin/plain-james-ui.json")); //random skin from my last project just to test, very ugly, replace (can also switch to image buttons)
            TextButton newMoveButton;
            //if(moveButtons.length > i) newMoveButton = moveButtons[i]; //this doesn't work, find another way to reuse/check
            newMoveButton = new TextButton(currentMove, s);//make a new button on screen with move's name
            newMoveButton.setSize(250, 100); //good size for now
            newMoveButton.setPosition(nextX, nextY);
            newMoveButton.setColor(Color.WHITE);
            newMoveButton.getLabel().setFontScale(2f, 2f);
            nextX += 300;
            if (nextX + 250 >= width) {//hit side of screen, start new row
                nextX = 150;
                nextY -= 150;
                if (prevButtonIndex == -1)
                    prevButtonIndex = i + 1; //used to track index of which button will be used to "scroll" between pages
            }
            if (nextY <= 0) {//bottom of screen, make next page button
                newMoveButton.setText("next page");
                final int finalPrevButtonIndex = prevButtonIndex;
                final int finalI = i;
                newMoveButton.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        nextPage(finalI, finalPrevButtonIndex, finalI, playerWeapons, 0, moveOccurrences);
                        return true;
                    }
                });
            } else {//normal input listener for move
                setHandler(newMoveButton, currentMove, moveOccurrences.get(currentMove));
            }

            stage.addActor(newMoveButton);
            moveButtons[i] = newMoveButton;
            i++;
        }
    }

    /*
     * Given a text button and a String representing the name of a move,
     *      -set the button's text to the string
     *      -clear the button's existing listeners
     *      -set the buttons color based on the move type
     *      -add a new listener where the player attacks with the move represented by this string
     * @param button the button to modify
     * @content the string to display on the button and make listener for, represents a move
     */
    public void setHandler(TextButton button, final String content, int occurrences) {
        if(occurrences > 1) button.setText(content + " x" + occurrences); //used to display how many of a certain move we have
        else button.setText(content);
        button.clearListeners();
        button.setVisible(true);
        switch (Move.getInstance().getMoveType(content)){ //we can set color of button based on move type
            case ATTACK: //red for attack
                button.getStyle().fontColor = Color.WHITE;
                button.setColor(Color.RED);
                break;
            case HEALING: //blue for healing
                button.getStyle().fontColor = Color.WHITE;
                button.setColor(Color.BLUE);
                break;
            default: //white for other
                button.getStyle().fontColor = Color.BLACK;
                button.setColor(Color.WHITE);
        }
        button.addListener(new InputListener() {

            //click then drag over button to display its info
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if(!touchDown) { //touch down is always fired first, so we know if its a click or enter at this point
                    textBox.setText(Move.getInstance().toString(content));
                    textBox.setVisible(true);
                }
            }

            //release click/drag to stop showing info
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor){
                textBox.setText("sword\nattack\nx-y dmg");
                textBox.setVisible(false);
            }

            //this is the beginning of actually preforming a move
            //its always fired before enter(), so I use it to tell enter if we should display info or not
            //i.e. don't display info if we just clicked and are using move
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchDown = true;
                return true;
            }

            //actually preform the move
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                touchDown = false;
                //when the user clicks a button for a move, call playerTurn with said move
                //don't just send with damage as parameter, as that would be same damage for same move
                //this setup will call getDamage (to get a random damage in range) every time
                //but only call playerTurn if it is the player's turn (set to true at start and at end of enemyTurn)
                //and if there's no active animations (dealt with by animationManager)
                if (isPlayerTurn && !animationActive && !statusEffectsInProgress) {
                    performSelectedPlayerMove(content); //player does their turn

                    //a possibility is that a move can only be used once
                    //in that case, we need to reset the buttons with the player's newly decreased move list
                    //NOTE - I'd like a slightly better system (some moves won't require a reset, we only need to change at most one button per turn) but this is by far the easiest solution
                    //we could easily add a check here - save the size of the move list before calling player turn, compare here, if its different, need a button reset
                    for (TextButton currentButton : moveButtons) { //we reset buttons
                        currentButton.setText("");
                        currentButton.clearListeners();
                        currentButton.setVisible(false);
                    }
                    setupButtons();//and then setup buttons again, we'll be using an updated move list
                }
            }

        });
    }


    /*
     * This is pretty messy and I want to fix it
     *
     * This function is called when moving to the next "page". It
     *      -sets up a previous page button
     *      -sets up an additional next page button if needed
     *      -resets all text and event handlers of the buttons for the next group of moves
     *
     * @param lastButtonIndex - index of the last button we can fit on screen
     * @param prevButtonIndex - index of the 1st button on the last row - its text will say "prev button" and allow us to "scroll" between pages
     * @param nextStartingIndex - index of playerWeapons to start at for this group/page of buttons
     * @param prevStartingIndex - index of the start of the last page, useful for setting up prevPage
     */
    public void nextPage(final int lastButtonIndex, final int prevButtonIndex, final int nextStartingIndex, final List<String> playerWeapons, final int prevStartingIndex, final Map<String, Integer> moveOccurrences) {

        //System.out.println("next" + nextStartingIndex);

        int numberOfEntries = lastButtonIndex;//how many buttons we will be adding handlers for
        //at most it is lastButtonIndex, as 1 will be used for previous, and the index is 0 based
        //at least it is lastButtonIndex-1, if we have another page after this (and thus a prev and next button)

        //start by setting up previous button - also need to shift elements
        moveButtons[prevButtonIndex].setText("prev page");
        moveButtons[prevButtonIndex].clearListeners();
        moveButtons[prevButtonIndex].addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                prevPage(lastButtonIndex, prevStartingIndex, prevButtonIndex, playerWeapons, moveOccurrences);
                return true;
            }
        });

        //check for next button
        if (playerWeapons.size() > lastButtonIndex + nextStartingIndex) {//we will have another page
            moveButtons[lastButtonIndex].setText("next page");
            moveButtons[lastButtonIndex].clearListeners();
            moveButtons[lastButtonIndex].addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    nextPage(lastButtonIndex, prevButtonIndex, nextStartingIndex + lastButtonIndex - 1, playerWeapons, nextStartingIndex, moveOccurrences);
                    return true;
                }
            });
        } else {//we won't have another page
            numberOfEntries++;//this will set text and handler normally on last one
        }

        //set buttons for the elements in this page/chunk/group
        int currentWeaponIndex = nextStartingIndex;//what element to pull from playerWeapons
        for (int currentButton = 0; currentButton < numberOfEntries; currentButton++) {
            if (playerWeapons.size() > currentWeaponIndex) {//have an element to display
                if (currentButton != prevButtonIndex) {//if its not the previous button
                    setHandler(moveButtons[currentButton], playerWeapons.get(currentWeaponIndex), moveOccurrences.get(playerWeapons.get(currentWeaponIndex)));//add corresponding text and handler
                    currentWeaponIndex++;//increment which element to grab
                    //this won't be increment when we're at previous button, keeping us in sync (otherwise we'd skip that element by setting previous)
                }
            } else {//out of elements
                if (currentButton != prevButtonIndex) {//hide current button as long as its not the previous button
                    moveButtons[currentButton].setVisible(false);
                }
            }
        }

    }

    /*
     * This is also pretty messy and I want to fix it
     *
     * This function is called when moving to the previous "page". It
     *      -resets all text and event handlers of the buttons for the previous group of moves
     *      -sets up a next page button
     *      -sets up an additional previous page button if needed
     *
     * @param lastButtonIndex - index of the last button we can fit on screen
     * @param prevStartingIndex - index of playerWeapons to start at for this group/page of buttons
     * @param prevButtonIndex - index of the 1st button on the last row - its text will say "prev button" and allow us to "scroll" between pages
     * @param nextButtonIndex - index of the last button on the last row - its text will say "next button" and allow us to "scroll" between pages
     */
    public void prevPage(final int lastButtonIndex, final int prevStartingIndex, final int prevButtonIndex, final List<String> playerWeapons, final Map<String, Integer> moveOccurrences) {

        //System.out.println(prevStartingIndex);

        int numberOfEntries = lastButtonIndex;//how many buttons we will be adding handlers for
        boolean prevButtonPresent = false;
        //at most it is lastButtonIndex, as 1 will be used for next, and the index is 0 based
        //at least it is lastButtonIndex-1, if we have another prev page before this (and thus a prev and next button)

        //check for previous button
        if (prevStartingIndex > prevButtonIndex) {//we will have another previous page
            //System.out.println("will have a prev: " + prevButtonIndex);
            moveButtons[prevButtonIndex].setText("prev page");
            moveButtons[prevButtonIndex].clearListeners();
            moveButtons[prevButtonIndex].setVisible(true);
            moveButtons[prevButtonIndex].addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    prevPage(lastButtonIndex, prevStartingIndex - lastButtonIndex, prevButtonIndex, playerWeapons, moveOccurrences);
                    return true;
                }
            });
            prevButtonPresent = true;
            numberOfEntries--;
        }

        //setting up next page
        moveButtons[lastButtonIndex].setText("next page");
        moveButtons[lastButtonIndex].clearListeners();
        moveButtons[lastButtonIndex].setVisible(true);
        final int finalEntriesDisplayed = numberOfEntries;//nested, must be final
        moveButtons[lastButtonIndex].addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                nextPage(lastButtonIndex, prevButtonIndex, prevStartingIndex + finalEntriesDisplayed, playerWeapons, prevStartingIndex, moveOccurrences);//use entries displayed to pass correct next index
                return true;
            }
        });

        //set buttons for the elements in this page/chunk/group
        int currentWeaponIndex = prevStartingIndex;//what element to pull from playerWeapons
        for (int currentButton = 0; currentButton < lastButtonIndex; currentButton++) {
            moveButtons[currentButton].setVisible(true);
            if(currentButton == prevButtonIndex && prevButtonPresent){
                //System.out.println("nop");
            }else{
                //System.out.println(currentButton);
                setHandler(moveButtons[currentButton], playerWeapons.get(currentWeaponIndex), moveOccurrences.get(playerWeapons.get(currentWeaponIndex)));//add corresponding text and handler
                currentWeaponIndex++;//increment which element to grab
                //this won't be increment when we're at previous button, keeping us in sync (otherwise we'd skip that element by setting previous)
            }
        }
    }


    //class for a health bar - total amount and handles the drawing
    //uses basic rectangles for now, can modify to images (or anything later)
    public class HealthBar extends Actor {

        private final float maxHealth;
        private float currentHealth;
        private final ShapeRenderer backgroundBar, frontBar;
        private final float x, y, edgeDifference, lengthOfBar, heightOfBar; //needed to draw bars
        private float currentLength;
        private float moveTo; //pixel to decrease or increase health bar to
        private boolean lastAttack;
        private final Label healthChangeLabel, nameLabel; //used to show damage/amount on each move and name with current/total health
        private final String name;


        public HealthBar(int currHealth, int maximumHealth, float x, float y, Label labelForHealthChange, Label labelForName, int length, int height, int offset){
            currentHealth  = currHealth;
            maxHealth = maximumHealth;
            this.x = x;
            this.y = y;
            healthChangeLabel = labelForHealthChange;
            nameLabel = labelForName;
            name = nameLabel.getText().toString(); //grab name of actor from existing label
            nameLabel.setText(name + "\t " + (int) currentHealth + "/" + (int) maxHealth);
            lengthOfBar = length;
            heightOfBar = height;
            edgeDifference = offset;
            backgroundBar = new ShapeRenderer();
            frontBar = new ShapeRenderer();
            currentLength = moveTo = (currentHealth/maxHealth) * (lengthOfBar);
            lastAttack = false;
        }

        //on taking damage, call this
        public void decreaseHealth(int damage){
            currentHealth -= damage;
            if(currentHealth<=0){
                //this is the last attack
                currentHealth = 0;//prevent from going below 0%
                if(isPlayerTurn) {
                    lastAttack = true; //we can use this to switch sprites as soon as health bar reaches 0
                    //kinda hacky and relies on health bar going faster than player attack
                    //I'll try to think of something better
                }
            }
            moveTo =  (currentHealth/maxHealth) * (lengthOfBar);
            if (moveTo != currentLength){//only preform animation if health changes
                healthChangeLabel.setText("-" + damage);
                nameLabel.setText(name + "\t " + (int) currentHealth + "/" + (int) maxHealth); //if we want this to decrease in real time or at end, we can
                animationManager.startAnimation();
            }
        }

        //on healing, call this
        public void increaseHealth(int amount){
            currentHealth+=amount;
            if(currentHealth > maxHealth) currentHealth = maxHealth; //prevent from going over 100%
            moveTo =  (currentHealth/maxHealth) * (lengthOfBar);
            if(moveTo != currentLength){ //only preform animation if health changes
                healthChangeLabel.setText("+" + amount);
                nameLabel.setText(name + "\t " + (int) currentHealth + "/" + (int) maxHealth); //if we want this to decrease in real time or at end, we can
                animationManager.startAnimation();
            }
        }



        public void tick(){
            if (currentLength > moveTo) { //keep decreasing by 3 pixels until we reach spot (smooth but quick value, can tweak)
                    currentLength-=3;
                    if(currentLength <= moveTo){//end once we reach desired spot or 0 (and 1 side dies)
                        currentLength = moveTo; //set exactly equal (moveTo is a float or we could have gone past it)
                        if(lastAttack) {
                            //commented out for now but if we're switching sprites, we can do here
                            //enemy.sprite = enemy.sprites[1]; //switch enemy sprite as soon as health reaches 0 - as I mentioned above, this is kinda hacky and I need something better
                            //enemy.positionChanged();
                        }
                        healthChangeLabel.setText("");
                        animationManager.endAnimation();
                    }
            }else if (currentLength < moveTo){ //keep increasing by 3 pixels until we reach spot (smooth but quick value, can tweak)
                currentLength+=3;
                if(currentLength >= moveTo){
                    currentLength = moveTo; //set exactly equal (moveTo is a float or we could have gone past it)
                    healthChangeLabel.setText("");
                    animationManager.endAnimation();
                }
            }

            //draw the shapes - a smaller red bar on top of a longer black bar
            backgroundBar.setAutoShapeType(true);
            backgroundBar.begin(ShapeRenderer.ShapeType.Filled);
            backgroundBar.rect(x, y, lengthOfBar + edgeDifference, heightOfBar + edgeDifference);
            backgroundBar.setColor(Color.BLACK);
            backgroundBar.end();
            frontBar.begin(ShapeRenderer.ShapeType.Filled);
            frontBar.rect( x + (edgeDifference/2), y + (edgeDifference/2), currentLength, heightOfBar);
            frontBar.setColor(Color.RED);
            frontBar.end();

        }


    }

    /*
     * Start of an animation for an attack. Right now, one side moves towards the other, then moves back.
     *
     * This is all subject to change as we'll probably use a real animation, not just this sliding, but hopefully we can reuse this and its a good start.
     * I just wanted something basic for now so I could try to deal with multiple animations at once
     */

    public class Attack {

        //attacker moves towards victim
        //reaches victim
        //moves back (probably slower speed)
        //stops once they're back at original position


        float startingPosition;
        float currentPosition;
        float targetPosition;
        float pixelsPerTick;
        Actor attacker, victim;
        boolean goingRight;

        public Attack(Actor a, Actor v){
            attacker = a;
            victim = v;
            startingPosition = currentPosition = a.getX();
            if(v == enemy) targetPosition = enemy.getHitbox().x;
            else targetPosition = v.getX(); //change to hitbox
            goingRight = targetPosition > currentPosition;
            if(goingRight) pixelsPerTick = 25;//increase x
            else pixelsPerTick = -25;
            //System.out.println(goingRight + " from " + currentPosition + " to " + targetPosition);
            animationManager.startAnimation();
        }

        public void tick(){

            if(goingRight){
                if(currentPosition >= targetPosition){
                    //if(attacker == player) player.setSpeedX(-50);
                    pixelsPerTick*=-1;//once we reach victim, move back
                }
                if(currentPosition <= startingPosition && pixelsPerTick<0){

                    /*
                    if(attacker == player){
                        //player.walkFrame = 8;
                        player.move();
                        player.setSpeedX(0);
                    }
                     */


                    pixelsPerTick = 0;//stop movement
                    animationManager.endAnimation();//and tell manager we're done
                }
            }else{//same thing but for going left
                if(currentPosition <= targetPosition) pixelsPerTick*=-1;
                if(currentPosition >= startingPosition && pixelsPerTick>0){
                    pixelsPerTick = 0;//stop movement
                    animationManager.endAnimation();//and tell manager we're done
                }
            }

            /*
            if(attacker == player){
                player.move();
                player.scaleSprite(2f);
            }
            else attacker.moveBy(pixelsPerTick, 0);
             */


            attacker.moveBy(pixelsPerTick, 0);
            currentPosition = attacker.getX();
        }

    }



    /*
     * Basic class to manage animations. Need to prevent either side from starting a new turn while
     * still in the middle of the previous. I figured we could use booleans and a check in the tick,
     * but I wanted to try to be as efficient as possible.
     *
     * Works like a basic lock - constructor says how many animations can be active at once. Then,
     * when starting each animation, increment the number of active animations. While there are active
     * animations (>0) animations active will be true, which will prevent the player from attacking during this time.
     * When an animation ends, decrement the number of active animations. Once hitting 0, we can go to the next turn.
     *
     * Same goes for the AI - it would automatically try to go without this class. With it, we actually don't call "enemyTurn"
     * until animationActive is false (seen below).
     */
    public class AnimationManager{

        private final int totalAnimations;
        private int currentActiveAnimations;

        //constructor, called with the maximum number of animations that can be active at once
        public AnimationManager(int num){
            totalAnimations = num;
            currentActiveAnimations = 0;
        }

        //increment amount of active animations. also set "animationsActive" to true if its not already.
        //I also added a check for making sure we don't go over number of active animations, but we probably don't need it
        public void startAnimation(){
            if(currentActiveAnimations < totalAnimations){
                animationActive = true;
                currentActiveAnimations++;
            }
        }

        //decrement amount of active animations. once hitting 0, set animationsActive to false.
        //if it is also the player's turn, they will be able to attack now by hitting a button
        //if it is the enemy's turn, explicitly call "enemyTurn()" to allow them to attack
        //I also added a check for making sure we don't go below 0, but we probably don't need it
        public void endAnimation(){
            if(currentActiveAnimations > 0) currentActiveAnimations--;
            if(currentActiveAnimations == 0){
                //System.out.println("done with all animations");
                animationActive = false;
                currentMoveLabel.setText("");
                //can call an event to trigger next function
                //playerTurn is never explicitly called, its done via buttons
                //so we just check if animations are active and its the player's turn
                //but enemy will automatically go without a class like this
                if(!isPlayerTurn && enemyHealthBar.currentHealth <= 0) combatOver(true); //enemy's health is <=0 and animations are done, player wins
                else if(!isPlayerTurn && statusEffectsInProgress) performEnemyStatusEffects(); //in the middle of the enemy's turn, performing their status effects
                else if(!isPlayerTurn && !statusEffectsInProgress) beginEnemyTurn();//enemy is not dead, and its their turn
                else if(isPlayerTurn && playerHealthBar.currentHealth <= 0) combatOver(false);//player died, animations over enemy wins
                else if(isPlayerTurn && statusEffectsInProgress) performPlayerStatusEffects(); //in the middle of the player's turn, performing their status effects
                else if(isPlayerTurn && !statusEffectsInProgress) beginPlayerTurn(); //player is not dead and its the start of their turn
            }
        }


    }


    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    //I am not a libGDX expert, if there's more I should be doing here lmk or add it
    public void dispose() {
        stage.dispose();
        stage = null; //we get the GL 0x501 error without this - we still try to have the stage act in render for some reason
    }
}
