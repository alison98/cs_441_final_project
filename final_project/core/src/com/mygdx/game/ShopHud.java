package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.List;

public class ShopHud {
    private Stage stage;
    private ScreenViewport stageViewport;
    private ImageButton selectButton;
    private Label textBox,shopText, priceText;
    private ImageButton upButton;
    private ImageButton downButton;
    private ImageButton leftButton;
    private ImageButton rightButton;
    private Actor arrow;
    private List<List<String>> items;
    private int floor, position;
    private Player player;

    public ShopHud(final SpriteBatch spriteBatch, final GameScreen gameScreen, int floorIn){
        stageViewport = new ScreenViewport();
        stage = new Stage(stageViewport, spriteBatch);
        floor = floorIn;
        items = Layout.getInstance().getShopItems(floor);
        position = 0;

        player = gameScreen.getPlayer();

        shopText = new Label("Hello", new Skin(Gdx.files.internal("skin/plain-james-ui.json"), new TextureAtlas(Gdx.files.internal("skin/plain-james-ui.atlas"))));
        shopText.setWidth(1000);
        shopText.setHeight(900);
        shopText.setFontScale(2f);
        shopText.setAlignment(Align.topLeft);
        shopText.setPosition(Gdx.graphics.getWidth()/2 - shopText.getWidth()/2, 100);
        shopText.getStyle().background = new Image(new Texture(Gdx.files.internal("textbox.png"))).getDrawable();
        shopText.setVisible(false);

        priceText = new Label("Hello", new Skin(Gdx.files.internal("skin/plain-james-ui.json"), new TextureAtlas(Gdx.files.internal("skin/plain-james-ui.atlas"))));
        priceText.setWidth(400);
        priceText.setHeight(900);
        priceText.setFontScale(2f);
        priceText.setAlignment(Align.topLeft);
        priceText.setPosition(Gdx.graphics.getWidth()/2, 100);
        priceText.setVisible(false);
        setText();

        textBox = new Label("Hello", new Skin(Gdx.files.internal("skin/plain-james-ui.json"), new TextureAtlas(Gdx.files.internal("skin/plain-james-ui.atlas"))));
        textBox.setWidth(1200);
        textBox.setHeight(200);
        textBox.setFontScale(2f);
        textBox.setAlignment(Align.center);
        textBox.setPosition(Gdx.graphics.getWidth()/2 - textBox.getWidth()/2, 100);
        textBox.getStyle().background = new Image(new Texture(Gdx.files.internal("textbox.png"))).getDrawable();
        textBox.setVisible(false);

        arrow = new Arrow();

        selectButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("select-button.png"))));
        selectButton.setPosition(1800, 80);
        selectButton.setVisible(false);
        selectButton.setTouchable(Touchable.disabled);
        selectButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(inText()){
                    upButton.setTouchable(Touchable.enabled);
                    downButton.setTouchable(Touchable.enabled);
                    leftButton.setTouchable(Touchable.enabled);
                    rightButton.setTouchable(Touchable.enabled);
                    textBox.setVisible(false);
                }else if(items.get(position).get(0) == "close"){
                    gameScreen.getGame().setScreen(gameScreen);
                }else if(items.get(position).get(2).equals("single")){
                    player.setMoney(player.getMoney() - Integer.valueOf(items.get(position).get(1)));
                    player.addWeapon(items.get(position).get(0));
                    items.remove(position); //Removes from Layout?
                    setText();
                    purchase("Thank you for your purchase!");
                }else { //use for multiple purchase
                    player.setMoney(player.getMoney() - Integer.valueOf(items.get(position).get(1)));
                    player.addWeapon(items.get(position).get(0));
                    purchase("Thank you for your purchase!");
                }

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        Table table = new Table();
        upButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("up-button.png"))));
        upButton.padLeft(160);
        upButton.setTouchable(Touchable.enabled);
        upButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(position >0) {
                    arrow.setPosition(arrow.getX(), arrow.getY() + 46);
                    position -= 1;
                    if(player.getMoney()>= Integer.valueOf(items.get(position).get(1))) {
                        setInteractable();
                    }else{
                        setUninteractable();
                    }
                }
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        downButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("down-button.png"))));
        downButton.padLeft(160);
        downButton.setTouchable(Touchable.enabled);
        downButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(position <items.size()-1) {
                    arrow.setPosition(arrow.getX(), arrow.getY() - 46);
                    position += 1;
                    if(player.getMoney()>= Integer.valueOf(items.get(position).get(1))) {
                        setInteractable();
                    }else{
                        setUninteractable();
                    }
                }
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        leftButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("left-button.png"))));
        leftButton.setTouchable(Touchable.enabled);
        leftButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        rightButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("right-button.png"))));
        rightButton.setTouchable(Touchable.enabled);
        rightButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        table.add(upButton);
        table.row();
        table.add(leftButton);
        table.add(rightButton);
        table.row();
        table.add(downButton);
        table.setPosition(200, 200);
        stage.addActor(table);
        stage.addActor(selectButton);
        stage.addActor(shopText);
        stage.addActor(priceText);
        stage.addActor(arrow);
        stage.addActor(textBox);
    }

    private class Arrow extends Actor {
        private Sprite sprite;

        public Arrow(){
            sprite = new Sprite(new Texture("left-button.png"));
            setPosition(525,800);
        }

        @Override
        public void draw(Batch batch, float alpha){
            sprite.draw(batch);
        }

        @Override
        public void positionChanged(){
            sprite.setPosition(getX(), getY());
        }

        @Override
        public boolean remove(){
            return super.remove();
        }
    }

    public void setInteractable(){
        selectButton.setVisible(true);
        selectButton.setTouchable(Touchable.enabled);
    }

    public void setUninteractable(){
        selectButton.setVisible(false);
        selectButton.setTouchable(Touchable.disabled);
    }

    public void setText(){
        String allItems = "\n\n\n";
        String prices = "\n\n\n";
        for(List<String> item : items){
            if(item.get(0).equals("close")){
                allItems += "\t" + item.get(0);
            } else {
                allItems += "\t" + item.get(0) + "\n";
                prices += item.get(1) + "\n";
            }
        }
        shopText.setText(allItems);
        shopText.setVisible(true);
        priceText.setText(prices);
        priceText.setVisible(true);
    }

    public void purchase(String message){
        textBox.setText(message);
        textBox.setVisible(true);
        setInteractable();
        upButton.setTouchable(Touchable.disabled);
        downButton.setTouchable(Touchable.disabled);
        leftButton.setTouchable(Touchable.disabled);
        rightButton.setTouchable(Touchable.disabled);
    }

    public boolean inText(){
        return textBox.isVisible();
    }

    public Stage getStage(){
        return stage;
    }

    public void dispose(){
        stage.dispose();
    }
}
