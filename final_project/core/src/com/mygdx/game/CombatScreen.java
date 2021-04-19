package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.soap.Text;

public class CombatScreen implements Screen {

    //Fields

    private Game game;
    private Stage stage;
    private int height;
    private int width;
    private Player player;
    private Enemy enemy;
    private boolean playerTurn;
    private TextButton[] moveButtons;
    private HealthBar playerHealthBar;
    private HealthBar enemyHealthBar;
    private boolean animationActive;


    //TODO
    //talk with group to get:
    //  -getMoves, inflictDamage, getHealth for Player and Enemy
    //then some more UI
    //  -display sprites (left and right or front and far)
    //  -some sort of indication of damage done/attack (name of move and damage done, sprites shake, etc.)
    //Future issues:
    //  -how to play animations (sprite moves close on attack, shakes on damage?)
    //  -cleaning up and improving UI (can make front and far rather than side to side, use images for health bars, etc)
    //  -how to update UI elements during runtime (if HP is a picture, how many pictures do we need?)

    //TODO for Monday
    //  -better UI/animation class to handle animationActive
    //  -no hard-coded numbers (HealthBar)
    //  -clean the button code
    //  -setup going back to game screen on winning


    //basic constructor
    public CombatScreen(Game g, Enemy e, Player p) { //player is optional? might have access from elsewhere
        game = g;
        enemy = e;
        player = p;
        player = new Player();//comment out once passing in
        enemy = new Enemy(0, 0, 0, 0, 1);//comment out once passing in
        stage = new Stage(new ScreenViewport());
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        playerTurn = true;
        animationActive = false;
        initUI();

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tick();
        checkCollisions();
        stage.act();
        stage.draw();
    }

    private void checkCollisions() {//there won't be any collisions but I see the benefit of making all screens similar

    }


    //on every tick we may play an idle animation or check for something. otherwise, all combat stuff is handled by other functions
    //I can do this because its turn-based
    //i.e. we wait for player to select, handler fires, does move, then allows enemy to move, which will then start the process over
    private void tick() {
        //player.tick();
        enemy.tick();
        playerHealthBar.tick();
        enemyHealthBar.tick();
    }


    //activated on selection/handler
    private void playerTurn(String selectedWeapon) {
        animationActive = true;
        playerTurn = false;


        int damage = Move.getInstance().getDamage(selectedWeapon);//get damage in the move's range
        System.out.println("dealing " + damage + " to enemy");
        //uncomment when getters and setters exist
        //enemy.inflictDamage(damage);//update enemy's stats (health)
        //update player if need be (if they used a resource, hurt themselves?)
        enemyHealthBar.decrementHealth(damage);

        //I'll need things here to manipulate UI, play animations, etc

        /*        //uncomment when getters and setters exist
        if(enemy.getHealth() <= 0){//check if player won
            game.setScreen(new GameScreen(game));//back to game screen, might need a way to say player won
        }else{
            enemyTurn();//explicitly call function to allow enemy to go
        }
        */


    }

    //called after player's turn
    public void enemyTurn() {
        animationActive = true;
        List<String> enemyWeapons = enemy.getWeapon(); //get list of moves
        Random rand = new Random(); //pick one (random for now)
        //double check that using same Rand is random
        String selectedWeapon = enemyWeapons.get(rand.nextInt(enemyWeapons.size()));//get random weapon
        int damage = Move.getInstance().getDamage(selectedWeapon);//get damage in range of move
        System.out.println("dealing " + damage + " to player");
        //uncomment when getters and setters exist
        //player.inflictDamage(damage);//update player's stats (health)
        //update enemy if need be (if they used a resource, hurt themselves?)
        playerHealthBar.decrementHealth(damage);
        playerTurn = true;

        //I'll need things here to manipulate UI, play animations, etc

        /*        //uncomment when getters and setters exist
        if(player.getHealth() <= 0){//check if enemy won
            //game.setScreen(new GameOverScreen(game));//some sort of game over screen
            System.out.println("game over");
        }
        */
    }


    //set up the UI elements. For now, this is just a button for each move.
    //I'm using TextButtons for now, but we can easily change to images
    //will eventually include elements listed above (health, sprites, etc)
    private void initUI() {
        //Player moves located in player class?
        //for now, assume player moves will work like enemy moves (see enemyTurn above)

        //can't add buttons without skin



        /*
        //List<String> playerWeapons = player.getWeapon(); //get list of moves - uncomment once Player class setup for this
        final List<String> playerWeapons = new ArrayList<>();
        playerWeapons.add("sword");





        int prevButtonIndex = -1;
        moveButtons = new TextButton[playerWeapons.size()];//make a button for each move
        float nextY = 200;
        float nextX = 150;
        for (int i = 0; i < playerWeapons.size(); i++) {//set up buttons - a max of 2 rows
            final String currentMove = playerWeapons.get(i); //get the current move
            Skin s = new Skin(Gdx.files.internal("skin/plain-james-ui.json")); //random skin from my last project just to test - I didn't upload the skin files so this will cause an error
            TextButton newMoveButton = new TextButton(currentMove, s);//make a new button on screen with move's name
            newMoveButton.setSize(250, 100); //just for now
            newMoveButton.setPosition(nextX, nextY);
            newMoveButton.setColor(Color.WHITE);
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
                        nextPage(finalI, finalPrevButtonIndex, finalI, playerWeapons, 0);
                        return true;
                    }
                });


            } else {//normal input listener for move
                newMoveButton.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        //when the user clicks a button for a move, call playerTurn with said move
                        //don't just send with damage as parameter, as that would be same damage for same move
                        //this setup will call getDamage (to get a random damage in range) every time
                        if (playerTurn && !animationActive) playerTurn(currentMove);
                        return true;
                    }
                });
            }

            stage.addActor(newMoveButton);
            moveButtons[i] = newMoveButton;
        }


         */


        //starting to add additional UI elements (player and enemy sprites and health bars)
        //will probably but in different function or at least clean up
        player.setPosition((float) width / 6, (float) height / 2);
        stage.addActor(player);
        enemy.setPosition((float) (width - (width / 6) - enemy.getWidth()), (float) height / 2);
        stage.addActor(enemy);
        //this puts player on left, enemy on right

        //trying out this HealthBar class with a basic decrementing animation, still need to test a bit
        playerHealthBar = new HealthBar(100, 100, height - (float) (height / 5));//will also modify X, Y to be based on sprite size, health
        stage.addActor(playerHealthBar);
        //playerHealthBar.decrementHealth(50);

        enemyHealthBar = new HealthBar(100, width - 850, height - (float) (height / 5));//will also modify X, Y to be based on sprite size, health
        stage.addActor(enemyHealthBar);
        //enemyHealthBar.decrementHealth(20);

    }

    public void setHandler(TextButton button, final String content) {
        button.setText(content);
        button.clearListeners();
        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (playerTurn && !animationActive)
                    playerTurn(content);
                return true;
            }
        });
    }


    /*
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
    public void nextPage(final int lastButtonIndex, final int prevButtonIndex, final int nextStartingIndex, final List<String> playerWeapons, final int prevStartingIndex) {

        System.out.println("next" + nextStartingIndex);

        int numberOfEntries = lastButtonIndex;//how many buttons we will be adding handlers for
        //at most it is lastButtonIndex, as 1 will be used for previous, and the index is 0 based
        //at least it is lastButtonIndex-1, if we have another page after this (and thus a prev and next button)

        //start by setting up previous button - also need to shift elements
        moveButtons[prevButtonIndex].setText("prev page");
        moveButtons[prevButtonIndex].clearListeners();
        moveButtons[prevButtonIndex].addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                prevPage(lastButtonIndex, prevStartingIndex, prevButtonIndex, playerWeapons);
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
                    nextPage(lastButtonIndex, prevButtonIndex, nextStartingIndex + lastButtonIndex - 1, playerWeapons, nextStartingIndex);
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
                    setHandler(moveButtons[currentButton], playerWeapons.get(currentWeaponIndex));//add corresponding text and handler
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
    public void prevPage(final int lastButtonIndex, final int prevStartingIndex, final int prevButtonIndex, final List<String> playerWeapons) {

        System.out.println(prevStartingIndex);

        int numberOfEntries = lastButtonIndex;//how many buttons we will be adding handlers for
        boolean prevButtonPresent = false;
        //at most it is lastButtonIndex, as 1 will be used for next, and the index is 0 based
        //at least it is lastButtonIndex-1, if we have another prev page before this (and thus a prev and next button)



        //check for previous button
        if (prevStartingIndex > prevButtonIndex) {//we will have another previous page
            System.out.println("will have a prev: " + prevButtonIndex);
            moveButtons[prevButtonIndex].setText("prev page");
            moveButtons[prevButtonIndex].clearListeners();
            moveButtons[prevButtonIndex].setVisible(true);
            moveButtons[prevButtonIndex].addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    prevPage(lastButtonIndex, prevStartingIndex - lastButtonIndex, prevButtonIndex, playerWeapons);
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
                nextPage(lastButtonIndex, prevButtonIndex, prevStartingIndex + finalEntriesDisplayed, playerWeapons, prevStartingIndex);//use entries displayed to pass correct next index
                return true;
            }
        });

        //set buttons for the elements in this page/chunk/group
        int currentWeaponIndex = prevStartingIndex;//what element to pull from playerWeapons
        for (int currentButton = 0; currentButton < lastButtonIndex; currentButton++) {
            moveButtons[currentButton].setVisible(true);
            if(currentButton == prevButtonIndex && prevButtonPresent){
                System.out.println("nop");
            }else{
                System.out.println(currentButton);
                setHandler(moveButtons[currentButton], playerWeapons.get(currentWeaponIndex));//add corresponding text and handler
                currentWeaponIndex++;//increment which element to grab
                //this won't be increment when we're at previous button, keeping us in sync (otherwise we'd skip that element by setting previous)
            }
        }
    }


    //class for a health bar - total amount and handles the drawing
    //uses basic rectangles for now, can modify to images (or anything later)
    public class HealthBar extends Actor {

        private int HP;
        private ShapeRenderer backgroundBar;
        private ShapeRenderer frontBar;
        private float edgeDifference;
        private float currentLength;
        private float decrementTo;
        private float x, y;

        public HealthBar(int amount, float x, float y){
            HP = amount;
            backgroundBar = new ShapeRenderer();
            frontBar = new ShapeRenderer();
            edgeDifference = 40; //looks good
            currentLength = decrementTo = 750; //looks good, will make parameter or function of sprite
            this.x = x;
            this.y = y;
        }

        //on taking damage, call this
        public void decrementHealth(int damage){
            HP-=damage;
            decrementTo  = (float) (HP*7.5); //change to use current length
        }

        public void tick(){
            if (currentLength > decrementTo) { //keep decreasing by 1 pixel until we reach spot
                    currentLength--;
                    //this is also dealing with animationActive - don't allow player to go until enemy is done and vice versa
                    //will put somewhere better
                    if(currentLength <= decrementTo){
                        animationActive = false;
                        if(!playerTurn) enemyTurn();
                    }
            }

            //draw the shapes
            backgroundBar.setAutoShapeType(true);
            backgroundBar.begin(ShapeRenderer.ShapeType.Filled);
            backgroundBar.rect(x, y, 750 + edgeDifference, 75 + edgeDifference);
            backgroundBar.setColor(Color.BLACK);
            backgroundBar.end();
            frontBar.begin(ShapeRenderer.ShapeType.Filled);
            frontBar.rect( x + (edgeDifference/2), y + (edgeDifference/2), currentLength, 75);
            frontBar.setColor(Color.RED);
            frontBar.end();

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

    public void dispose() {
        stage.dispose();
    }
}
