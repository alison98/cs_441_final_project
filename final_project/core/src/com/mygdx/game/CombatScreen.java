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



    //TODO
    //talk with group to get:
    //  -getMoves, inflictDamage, getHealth for Player and Enemy
    //then I'll get this working with a few buttons
    //then some more UI
    //  -respond to variable number of moves
    //  -display sprites (left and right or front and far)
    //  -some sort of indication of damage done
    //  -also some health bars
    //Future issues:
    //  -how to play animations (sprite moves close on attack, shakes on damage?)
    //  -cleaning up and improving UI (can make front and far rather than side to side, use images for health bars, etc)
    //  -how to update UI elements during runtime (if HP is a picture, how many pictures do we need?)



    //basic constructor
    public CombatScreen(Game g, Enemy e, Player p) { //player is optional? might have access from elsewhere
        game = g;
        enemy = e;
        player = p;
        player = new Player();//uncomment once passing in
        enemy = new Enemy(0,0,0,0,0);//uncomment once passing in
        stage = new Stage(new ScreenViewport());
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        playerTurn = true;
        initUI();

    }

    @Override
    public void show(){
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

    private void checkCollisions(){//there won't be any collisions but I see the benefit of making all screens similar

    }



    //on every tick we may play an idle animation or check for something. otherwise, all combat stuff is handled by other functions
    //I can do this because its turn-based
    //i.e. we wait for player to select, handler fires, does move, then allows enemy to move, which will then start the process over
    private void tick(){
        //player.tick();
        enemy.tick();
        playerHealthBar.tick();
    }



    //activated on selection/handler
    private void playerTurn(Move selectedMove){
        /*

        //uncomment when getters and setters exist

        int damage = selectedMove.getRandomDamageInRange();//get damage in the move's range
        enemy.inflictDamage(damage);//update enemy's stats (health)
        //update player if need be (if they used a resource, hurt themselves?)
        playerTurn = false;

        //I'll need things here to manipulate UI, play animations, etc

        if(enemy.getHealth() <= 0){//check if player won
            game.setScreen(new GameScreen(game));//back to game screen, might need a way to say player won
        }else{
            enemyTurn();//explicitly call function to allow enemy to go
        }

         */
    }

    //called after player's turn
    public void enemyTurn(){
        /*

        //uncomment when getters and setters exist

        Move[] enemyMoves = enemy.getMoves(); //get list of moves
        Random rand = new Random(); //pick one (random for now)
        //double check that using same Rand is random
        Move move = enemyMoves[rand.nextInt(enemyMoves.length)];//get random move
        int damage = move.getRandomDamageInRange();//get damage in range of move
        player.inflictDamage(damage);//update player's stats (health)
        //update enemy if need be (if they used a resource, hurt themselves?)
        playerTurn = true;

        //I'll need things here to manipulate UI, play animations, etc

        if(player.getHealth() <= 0){//check if enemy won
            //game.setScreen(new GameOverScreen(game));//some sort of game over screen
            System.out.println("game over");
        }

         */
    }



    //set up the UI elements. For now, this is just a button for each move.
    //I'm using TextButtons for now, but we can easily change to images
    //will eventually include elements listed above (health, sprites, etc)
    private void initUI(){
        //TODO: change to new use new Move class
        //Player moves located in player class?
        /*
        final Move[] playerMoves = {new Move("Move 1", 1, 5), new Move("stronger move", 10, 20)}; //example for now
        //final Move[] playerMoves = player.getMoves(); //get all of the players moves
        moveButtons = new TextButton[playerMoves.length];//make a button for each move
        float nextY = 50;
        for(int i = 0; i< playerMoves.length; i++){
            final Move currentMove = playerMoves[i]; //get the current move
            Skin s = new Skin(Gdx.files.internal("skin/plain-james-ui.json")); //random skin from my last project just to test - I didn't upload the skin files so this will cause an error
            TextButton newMoveButton = new TextButton(currentMove.getNameOfMove(), s);//make a new button on screen with move's name
            newMoveButton.setSize(250, 100); //just for now
            newMoveButton.setPosition((float) 50, (float) nextY); //this goes up the left side of the screen, I'll probably change to along bottom
            nextY+=150;
            newMoveButton.setColor(Color.WHITE);
            newMoveButton.addListener(new InputListener(){
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    //when the user clicks a button for a move, call playerTurn with said move
                    playerTurn(currentMove);
                    return true;
                }
            });
            stage.addActor(newMoveButton);
            //System.out.println("added " + currentMove.getNameOfMove() + " to stage");
            moveButtons[i] = newMoveButton;
        }
        */
        //starting to add additional UI elements (player and enemy sprites and health bars)
        player.setPosition((float) width/6,(float) height/2);
        stage.addActor(player);
        enemy.setPosition((float) (width - (width/6) - enemy.getWidth()), (float) height/2);
        stage.addActor(enemy);
        //this puts player on left, enemy on right
        //trying out this HealthBar class with a basic decrementing animation, still need to test a bit
        playerHealthBar = new HealthBar(100);
        stage.addActor(playerHealthBar);
        playerHealthBar.decrementHealth(50);

    }

    //class for a health bar - total amount and handles the drawing
    //uses basic rectangles for now, can modify to images (or anything later)
    public class HealthBar extends Actor {

        private int HP;
        private ShapeRenderer backgroundBar;
        private ShapeRenderer frontBar;
        private int edgeDifference;
        private boolean decrementing;
        private int currentLength;
        private float decrementTo;

        public HealthBar(int amount){
            HP = amount;
            backgroundBar = new ShapeRenderer();
            frontBar = new ShapeRenderer();
            decrementing = false;
            edgeDifference = 40; //looks good
            currentLength = 750; //looks good
        }

        //on taking damage, call this
        public void decrementHealth(int damage){
            HP-=damage;
            //decrementing = true;
            decrementTo  = (float) (HP*7.5); //change to use current length
        }

        public void tick(){
            if (currentLength > decrementTo) { //keep decreasing by 1 pixel until we reach spot
                    currentLength--;
            }

            //draw the shapes
            backgroundBar.setAutoShapeType(true);
            backgroundBar.begin(ShapeRenderer.ShapeType.Filled);
            backgroundBar.rect((float) width/3, (float) height/4, 750 + edgeDifference, 75 + edgeDifference);
            backgroundBar.setColor(Color.BLACK);
            backgroundBar.end();
            frontBar.begin(ShapeRenderer.ShapeType.Filled);
            frontBar.rect((float) ((width/3) + (edgeDifference/2)), (float) ((height/4) + (edgeDifference/2)), currentLength, 75);
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
