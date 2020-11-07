package com.sparkasticgames.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;


/**Bugs maybe present also this is not a way to code, it was made in 32 mins */
public class LumosSnake extends ApplicationAdapter {

    public static boolean isMobileDevice; //Ignore
    static int sizeX = 500; //Width of Board
    static int sizeY = 500;//Height of Board
    static int cellSize = 25; // Size of each Cell in a board(grid)


    static int cellX = sizeX / cellSize; //Total number of cells on X axis
    static int cellY = sizeY / cellSize;  //Total number of cells on Y axis


    static Cell[][] board = new Cell[cellY][cellX]; // grid to contain the data of types of cells contained cells : eg, EMPTY, FOOD, SNAKEPARTS

    ShapeRenderer renderer;  //IGNORE USE YOUR OWN RENDERER OF YOUR ENGINE
    ExtendViewport viewport; //IGNORE USED TO SET THE CORRECT RESOLUTION RATIO
    InputController controller; //IGNORE (USED FOR MOBILE DEVICES)

    static int ticks = 0;   // IMPORTANT BASICALLY KEEP TRACKS OF SPEED AND INTERVALS MOVEMENT OF SNAKE IS RELIABLE ON THIS
    static int score = 0;   //Oh yea score whatever
    static int SNAKESPEED = 5; // SPEEED OF SNAKE in TICKS, that is the snake will move 1 cell every 5 frames!

    static Array<SnakeParts> snakeParts; //Array of our snakes growing body parts



    interface Cell {
    }

    static class Food implements Cell { //FOOD SINGLETON
        static Food instance = new Food();
    }

    static class Empty implements Cell { //EMPTY SINGLETON
        static Empty instance = new Empty();
    }

    static class SnakeParts implements Cell { //EVERY INDIVIDUAL SNAKE PARTS
        GridPoint2 pos;
        Direction dir;

        SnakeParts(GridPoint2 mPos, Direction dir) {
            pos = mPos;
            this.dir = dir;
        }

        void move() {			//used to move snakepart one cell
            int ty = 0;
            int tx = 0;


            ty = pos.y + dir.val.y;
            tx = pos.x + dir.val.x;
            pos.x = tx;
            pos.y = ty;
            validatePos();
        }

        void validatePos(){  //validates the pos and wraps in withing range of board
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
    enum Direction {//IMPLEMENTATION OF DIRECTIONS AND ITS VALUES IN X AND Y AXIS
        UP(0, 1),
        RIGHT(1, 0),
        DOWN(0, -1),
        LEFT(-1, 0);

        GridPoint2 val;


        Direction(int x, int y) {
            val = new GridPoint2(x, y);
        }


    }


    void reinit(){ //resets the game
        snakeParts.clear();
        score = 0;

        for (int x = 0; x < cellX; x++) {
            for (int y = 0; y < cellY; y++) {

                board[y][x] = Empty.instance;

            }
        }
        //

        board[MathUtils.random(0, cellY - 1)][MathUtils.random(0, cellY - 1)] = Food.instance; //spawns food at random location
        snakeParts.add(new SnakeParts(new GridPoint2(MathUtils.random(0, cellX - 1), MathUtils.random(0, cellY - 1)),
                Direction.UP));//spawns first head part

    }






    public void create() { //INITILISES ALL THE ARRAYS AND VARIABLES
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        viewport = new ExtendViewport(sizeX, sizeY);
        snakeParts = new Array<>();
        controller = new InputController(new SpriteBatch());

        for (int x = 0; x < cellX; x++) {
            for (int y = 0; y < cellY; y++) {

                board[y][x] = Empty.instance; //EMptys all cells inititally

            }
        }

        board[MathUtils.random(0, cellY - 1)][MathUtils.random(0, cellY - 1)] = Food.instance;  //spawns food at random lcoation
        snakeParts.add(new SnakeParts(new GridPoint2(MathUtils.random(0, cellX - 1), MathUtils.random(0, cellY - 1)),
                Direction.UP)); //spawns snakes head


    }
    int lt = 0;
    public void render() { //openGL render method 
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);//PAINT SCREEN BLACK and make it fresh
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //clear screen

        renderer.setProjectionMatrix(viewport.getCamera().combined);//ignore

        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Line);

        for (int x = 0; x < cellX; x++) {
            for (int y = 0; y < cellY; y++) {

                renderer.setColor(0.1f, 0.1f, 0.1f, 1f);
                renderer.rect(x * cellSize, y * cellSize, cellSize, cellSize);//render every grid lines

                if (board[y][x] instanceof Food) {//render food
                    renderer.setColor(1f, 0, 0, 1f);
                    renderer.circle(x * cellSize + cellSize / 2f, y * cellSize + cellSize / 2f, cellSize / 2.5f, 1000);
                }
                if (board[y][x] instanceof SnakeParts) {//emptying any snake presence for now
                    board[y][x] = Empty.instance; 
                }

            }
        }
        boolean isFood = false;
		
        for (int i = 0; i < snakeParts.size; i++) {
            SnakeParts parts = snakeParts.get(i); //current snake part

            if (ticks % SNAKESPEED == 0) {

                parts.move(); //move

            }
                parts.validatePos();


            if (board[parts.pos.y][parts.pos.x] instanceof Food) {//if snake has eaten food set the variable and remove the food

                isFood = true;
                board[parts.pos.y][parts.pos.x] = Empty.instance;

            }
            if(board[parts.pos.y][parts.pos.x] instanceof SnakeParts ){ //game over condition
                reinit();
                renderer.end();

                return;
            }

            renderer.setColor(0f, 1f, 0f, 1f);
            renderer.rect(parts.pos.x * cellSize, 
			parts.pos.y * cellSize, 
			cellSize * (snakeParts.size - i)/(float)snakeParts.size, 
			cellSize * (snakeParts.size - i)/(float)snakeParts.size);    //makes snake more fancy by making size descending as per the position(HEAD IS THE LARGEST< TAIL IS THE SMALLESt)
			
			
            board[parts.pos.y][parts.pos.x] = parts;


        }
        if (isFood) { //if snake ate food
            score++; //score

            SnakeParts tail = snakeParts.get(snakeParts.size - 1);//get tail

            Direction oppos = Direction.values()[(tail.dir.ordinal() + 2) % Direction.values().length]; //find the opposite direction to that of tail
            GridPoint2 p = new GridPoint2(tail.pos.x + oppos.val.x, tail.pos.y + oppos.val.y); //find the position of where we need to put the snake


            SnakeParts newTail = new SnakeParts(p, tail.dir); //make new tail with the paramters

            snakeParts.add(newTail);//add i narray

            while(true){
                int yf = MathUtils.random(0, cellY -1);
                int xF = MathUtils.random(0, cellX -1);    //spawn food sowme where at random. not the best i guess
                if(board[yf][xF] instanceof SnakeParts){
                    continue;
                }


                board[yf][xF] = Food.instance;
                break;

            }
        }


        SnakeParts head = snakeParts.get(0); // get HEAD 
		
		/**TAKE INPUTS!*/
		

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W) || controller.isUpPressed()) {

            if (head.dir.val.x != 0 && ticks - lt > SNAKESPEED) {
                head.dir = Direction.UP;
                lt = ticks;

            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D) || controller.isRightPressed()) {
            if (head.dir.val.y != 0&& ticks - lt > SNAKESPEED){
                head.dir = Direction.RIGHT;
                lt = ticks;
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)||controller.isDownPressed()) {
            if (head.dir.val.x != 0&& ticks - lt >  SNAKESPEED) {
                head.dir = Direction.DOWN;
                lt = ticks;
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)  || Gdx.input.isKeyJustPressed(Input.Keys.A)||controller.isLeftPressed()) {
            if (head.dir.val.y != 0&& ticks - lt >  SNAKESPEED) {
                head.dir = Direction.LEFT;
                lt = ticks;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F5) ) {
            reinit();
            renderer.end();
            return;
        }

		
		/**Update direction of every single parts such that it moves just like a snake should be moving*/
        for (int i = 1; i < snakeParts.size; i++) {

            SnakeParts current = snakeParts.get(i);
            SnakeParts front = snakeParts.get(i - 1); //get front part

            int subX = front.pos.x - current.pos.x;  //find difference of positions
            int subY = front.pos.y - current.pos.y;


			
			//based on the difference set the direction
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
        if(isMobileDevice)//ignpre
         controller.render();

        ticks++;
    }

    public void resize(int width, int height) { //ignore
        viewport.update(width, height, true);
        controller.resize(width, height);
    }


}
