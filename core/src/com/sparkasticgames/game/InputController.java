package com.sparkasticgames.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class InputController {

    public Stage stage;
    public Viewport viewport;
    public OrthographicCamera camera;

    private boolean leftPressed, rightPressed, upPressed, downPressed;


    public InputController(SpriteBatch batch) {
        viewport = new ScreenViewport();

        stage = new Stage(viewport, batch);
        initStage();
        initButtons();
    }

    private void initButtons() {
        Table table = new Table();
        table.pack();
        table.setFillParent(true);

        Image upImg, leftImg, rightImg, downImg;

        upImg = new Image(new Texture("ui/up.png"));
        leftImg = new Image(new Texture("ui/left.png"));
        rightImg = new Image(new Texture("ui/right.png"));
        downImg = new Image(new Texture("ui/down.png"));

        upImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                Gdx.input.vibrate(20);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }

        });
        leftImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });
        rightImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;

            }
        });
        downImg.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = true;

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = false;
            }

        });


        table.setPosition(0, 0);

        table.bottom().left();
        table.add(leftImg).size(leftImg.getWidth()/1.5f, leftImg.getHeight()/1.5f).pad(0, 0, 0, 20);
        table.add(rightImg).size(rightImg.getWidth()/1.5f, rightImg.getHeight()/1.5f);
        stage.addActor(table);
        /*table.right().bottom();

         */
        table = new Table();
        table.setFillParent(true);
        table.pack();
        table.setPosition(0, 0);
        table.bottom().right();

        table.add(upImg).size(rightImg.getWidth()/1.5f, rightImg.getHeight()/1.5f).pad(0, 0, 0, 20);
        table.add();
        table.add(downImg).size(downImg.getWidth() /1.5f, downImg.getHeight() /1.5f);

        stage.addActor(table);

    }

    public void render() {
        viewport.apply(true);

        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private void initStage() {
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.UP:
                        upPressed = true;
                        break;
                    case Input.Keys.SHIFT_LEFT:
                        downPressed = true;
                        break;
                    case Input.Keys.LEFT:
                        leftPressed = true;
                        break;
                    case Input.Keys.RIGHT:
                        rightPressed = true;
                        break;
                }
                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.UP:
                        upPressed = false;
                        break;
                    case Input.Keys.LEFT:
                        leftPressed = false;
                        break;
                    case Input.Keys.RIGHT:
                        rightPressed = false;
                        break;
                    case Input.Keys.SHIFT_LEFT:
                        downPressed = false;
                        break;
                }
                return true;
            }

        });

        Gdx.input.setInputProcessor(stage);


    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }


}
