package com.sparkasticgames.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class LumosSnake extends ApplicationAdapter {

    static int sizeX = 500;
    static int sizeY = 500;
    static int cellSize = 25;


    static int cellX = sizeX / cellSize;
    static int cellY = sizeY / cellSize;


    static Cell[][] board = new Cell[cellY][cellX];

    ShapeRenderer renderer;
    FitViewport viewport;

    static int ticks = 0;
    static int score = 0;
    static int SNAKESPEED = 5;

    static Array<SnakeParts> snakeParts;



    interface Cell {
    }

    static class Food implements Cell {
        static Food instance = new Food();
    }

    static class Empty implements Cell {
        static Empty instance = new Empty();
    }

    void reinit(){
        snakeParts.clear();
        score = 0;

        for (int x = 0; x < cellX; x++) {
            for (int y = 0; y < cellY; y++) {

                board[y][x] = Empty.instance;

            }
        }
        //


        board[MathUtils.random(0, cellY - 1)][MathUtils.random(0, cellY - 1)] = Food.instance;
        snakeParts.add(new SnakeParts(new GridPoint2(MathUtils.random(0, cellX - 1), MathUtils.random(0, cellY - 1)),
                Direction.UP)); // thanks lyze, myke, tettinger mranderson ,tann

    }


    static class SnakeParts implements Cell {
        GridPoint2 pos;
        Direction dir;

        SnakeParts(GridPoint2 mPos, Direction dir) {
            pos = mPos;
            this.dir = dir;
        }




        void move() {
            int ty = 0;
            int tx = 0;


            ty = pos.y + dir.val.y;
            tx = pos.x + dir.val.x;
            pos.x = tx;
            pos.y = ty;
            validatePos();
        }

        void validatePos(){
            int ty = 0;
            int tx = 0;


            ty = pos.y;
            tx = pos.x ;

            if (ty < 0) {
                ty = cellY - 1;
            }
            if (tx < 0) {
                tx = cellX - 1;
            }
            if (tx >= cellX) {
                tx = 0;
            }
            if (ty >= cellY) {
                ty = 0;
            }

            pos.x = tx;
            pos.y = ty;
        }

    }

    enum Direction {
        UP(0, 1),
        RIGHT(1, 0),
        DOWN(0, -1),
        LEFT(-1, 0);

        GridPoint2 val;


        Direction(int x, int y) {
            val = new GridPoint2(x, y);
        }


    }

    public void create() {
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        viewport = new FitViewport(sizeX, sizeY);
        snakeParts = new Array<>();


        for (int x = 0; x < cellX; x++) {
            for (int y = 0; y < cellY; y++) {

                board[y][x] = Empty.instance;

            }
        }

        board[MathUtils.random(0, cellY - 1)][MathUtils.random(0, cellY - 1)] = Food.instance;
        snakeParts.add(new SnakeParts(new GridPoint2(MathUtils.random(0, cellX - 1), MathUtils.random(0, cellY - 1)),
                Direction.UP)); // thanks lyze, myke, tettinger mranderson ,tann


    }

    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setProjectionMatrix(viewport.getCamera().combined);

        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Line);

        for (int x = 0; x < cellX; x++) {
            for (int y = 0; y < cellY; y++) {

                renderer.setColor(0.1f, 0.1f, 0.1f, 1f);
                renderer.rect(x * cellSize, y * cellSize, cellSize, cellSize);

                if (board[y][x] instanceof Food) {
                    renderer.setColor(1f, 0, 0, 1f);
                    renderer.circle(x * cellSize + cellSize / 2f, y * cellSize + cellSize / 2f, cellSize / 2.5f, 1000);
                }
                if (board[y][x] instanceof SnakeParts) {
                    board[y][x] = Empty.instance;
                }

            }
        }
        boolean isFood = false;
        for (int i = 0; i < snakeParts.size; i++) {
            SnakeParts parts = snakeParts.get(i);

            if (ticks % SNAKESPEED == 0) {

                parts.move();

            }
                parts.validatePos();


            if (board[parts.pos.y][parts.pos.x] instanceof Food) {

                isFood = true;
                board[parts.pos.y][parts.pos.x] = Empty.instance;

            }
            if(board[parts.pos.y][parts.pos.x] instanceof SnakeParts){
                reinit();
                renderer.end();
                return;
            }

            renderer.setColor(0f, 1f, 0f, 1f);
            renderer.rect(parts.pos.x * cellSize, parts.pos.y * cellSize, cellSize * (snakeParts.size - i)/(float)snakeParts.size, cellSize * (snakeParts.size - i)/(float)snakeParts.size);
            board[parts.pos.y][parts.pos.x] = parts;


        }
        if (isFood) {
            score++;

            SnakeParts tail = snakeParts.get(snakeParts.size - 1);

            Direction oppos = Direction.values()[(tail.dir.ordinal() + 2) % Direction.values().length];
            GridPoint2 p = new GridPoint2(tail.pos.x + oppos.val.x, tail.pos.y + oppos.val.y);


            SnakeParts newTail = new SnakeParts(p, tail.dir);

            snakeParts.add(newTail);

            while(true){
                int yf = MathUtils.random(0, cellY -1);
                int xF = MathUtils.random(0, cellX -1);
                if(board[yf][xF] instanceof SnakeParts){
                    continue;
                }


                board[yf][xF] = Food.instance;
                break;

            }
        }


        SnakeParts head = snakeParts.get(0);

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)) {

            if (head.dir.val.x != 0)
                head.dir = Direction.UP;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            if (head.dir.val.y != 0)
                head.dir = Direction.RIGHT;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            if (head.dir.val.x != 0)
                head.dir = Direction.DOWN;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)  || Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            if (head.dir.val.y != 0)
                head.dir = Direction.LEFT;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F5) ) {
            reinit();
            renderer.end();
            return;
        }


        for (int i = 1; i < snakeParts.size; i++) {

            SnakeParts current = snakeParts.get(i);
            SnakeParts front = snakeParts.get(i - 1);

            int subX = front.pos.x - current.pos.x;
            int subY = front.pos.y - current.pos.y;

            if (subX == 0) {
                if (subY == 1 || subY < -1) {
                    current.dir = Direction.UP;
                } else {
                    current.dir = Direction.DOWN;
                }


            } if(subY == 0) {
                if (subX == 1 || subX < -1) {
                    current.dir = Direction.RIGHT;
                } else {
                    current.dir = Direction.LEFT;
                }

            }


        }


        renderer.end();


        ticks++;
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }


}
