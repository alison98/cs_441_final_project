package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class HowToPlay implements Screen {
    private Sprite background;
    private Game game;
    private Stage stage;
    private SpriteBatch spriteBatch;
    private Room room;
    private ScrollPane scroller;
    private Table table;

    public HowToPlay(Game GameIn){
        this.stage = new Stage(new ScreenViewport());
        spriteBatch = new SpriteBatch();
        spriteBatch.begin();
        game = GameIn;
        room = new Room();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont(Gdx.files.internal("font/font.fnt"));
        labelStyle.fontColor = Color.BLACK;
        labelStyle.background = new TextureRegionDrawable(new Texture("textbox.png"));

        table = new Table();

        String[] tips = new String[]{"Scroll through this list\nfor some helpful tips!",
                "Press the A button to interact with\ndoors, stairs, people, enemies, or items",
                "Fight enemies to gain\nexperience and money",
                "Each floor has a boss in a locked room.\nDefeat enemies to find the key",
                "Defeating a boss reveals stairs to the\nnext floor somewhere on the current floor",
                "In combat, blue moves heal\nand red moves deal damage",
                "Status effects heal or damage over time,\nrepresented by + and - respectively",
                "Healing moves can be used outside of\ncombat by clicking on them in the menu",
                "Touch and hold away from any buttons then\ndrag onto buttons to see move descriptions",
                "Almost all weapons have durability, so you\nwill constantly need to find new weapons",
                "Some attack moves have cooldowns or\na max number of uses in a single fight",
                "Traveling between floors will cause\nnon-boss enemies to respawn",
        };

        for(String tip : tips){
            Label tipLabel = new Label(tip, labelStyle);
            tipLabel.setFontScale(1f);
            tipLabel.setAlignment(Align.center);
            table.add(tipLabel).width(1100f).height(250f).pad(10);
            table.row();
        }


        table.pack();
        scroller = new ScrollPane(table);
        scroller.setWidth(table.getWidth());
        scroller.setPosition(Gdx.graphics.getWidth()/2 - scroller.getWidth()/2, 0);
        scroller.setHeight(Gdx.graphics.getHeight());
        scroller.setTouchable(Touchable.enabled);

        Texture buttonTexture = new Texture("textbox.png");
        Skin skin = new Skin();
        skin.add("buttonTexture", buttonTexture);
        ImageTextButton.ImageTextButtonStyle backButtonStyle = new ImageTextButton.ImageTextButtonStyle();
        backButtonStyle.font = new BitmapFont(Gdx.files.internal("font/font.fnt"));
        backButtonStyle.fontColor = Color.BLACK;
        backButtonStyle.up = new TextureRegionDrawable(buttonTexture);
        backButtonStyle.down = new TextureRegionDrawable(buttonTexture);
        ImageTextButton backButton = new ImageTextButton("Back", backButtonStyle);
        backButton.setWidth(200f);
        backButton.setHeight(80f);
        backButton.getLabel().setFontScale(0.75f);
        backButton.setPosition(50, Gdx.graphics.getHeight() - backButton.getHeight() * 2);
        backButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                game.setScreen(new MainMenu(game));
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }
        });

        stage.addActor(room);
        //stage.addActor(table);
        stage.addActor(scroller);
        stage.addActor(backButton);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
