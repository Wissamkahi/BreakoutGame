/*
 * File: Breakout.java
 * -------------------
 * Name: Wissam Kahi
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {	

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 600;
	public static final int APPLICATION_HEIGHT = 700;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;
	
/** Align game board in center of application window */
	private static final int START_X = APPLICATION_WIDTH;
	private static final int START_Y = APPLICATION_HEIGHT/6;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 90;
	private static final int PADDLE_HEIGHT = 15;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 40;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 8;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 5;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW + 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 12;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 18;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 80;

/** Number of turns */
	private static final int NTURNS = 3;
	
/** Animation cycle delay */
	private static final int DELAY = 2;
		

/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		setup ();
		addMouseListeners ();
		runGame ();
	}
	
	/** Create and place the game environment */
	private void setup () {
		placeWalls();
		placeBricks ();
		placePaddle ();
	}
	
	/** places the 4 walls */
	private void placeWalls() {
		walls = new GRect (START_X,START_Y,WIDTH,HEIGHT);
		add (walls);
	}
	
	
	/** places the bricks on the top */
	private void placeBricks () {
		Color brickColor = Color.CYAN;
		for (int brickRow = 1; brickRow <= NBRICK_ROWS; brickRow++) {

			/** Selects the color for each row of bricks */
			switch (brickRow) {
			case 1: case 2:
				brickColor=Color.RED;
				break;
			case 3: case 4:
				brickColor=Color.ORANGE;
				break;
			case 5: case 6:
				brickColor=Color.YELLOW;
				break;
			case 7: case 8:
				brickColor=Color.GREEN;
				break;
			case 9: case 10:
				brickColor=Color.CYAN;
				break;
			default: break;
			}
			
			/** Draw one row of bricks */
			for (int brickColumn = 1; brickColumn <= NBRICKS_PER_ROW; brickColumn++ ) {
				addBrick (brickColumn, brickRow, brickColor);
			}
		}
	}
	
	/** Adds brick for specific column and specific row */
	private void addBrick (int col, int row, Color color) {
		
		// Set values for corner of the brick
		int x = START_X + BRICK_SEP + (col-1)*(BRICK_WIDTH+BRICK_SEP);
		int y = START_Y + BRICK_Y_OFFSET + (row-1)*(BRICK_HEIGHT+BRICK_SEP);
		
		// Create and add new brick using passed color
		GRect brick = new GRect (x,y,BRICK_WIDTH,BRICK_HEIGHT);
		brick.setFilled(true);
		brick.setColor(color);
		add(brick);
	}


	private void placeBall () {
		ball = new GImage ("CircularWissam.gif");
		double scale_factor = (BALL_RADIUS*2)/ball.getWidth();
		ball.scale(scale_factor, scale_factor);
		add(ball,START_X+WIDTH/2-BALL_RADIUS, START_Y+HEIGHT/2-BALL_RADIUS);
	}
	
	private void placePaddle () {
		
		// Determine y coordinate of paddle
		int y = START_Y + HEIGHT-PADDLE_Y_OFFSET-PADDLE_HEIGHT;
		
		// Position paddle in the middle
		paddle = new GRect (START_X + (WIDTH-PADDLE_WIDTH)/2,y,PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setColor(Color.BLACK);
		add (paddle);
	}
	
	
	private void runGame () {
		
		counter=2;
		while (counter>=0) {
			label=new GLabel ("Click mouse to start");
			while (mouse_clicked==0) {
				pause(DELAY*100);
				position (label);
			}
			remove (label);
			placeBall ();
			moveBall ();
			mouse_clicked=0;
			
			if (brickcounter==0) {
				label = new GLabel ("YOU WON");
				ball=null;
				break;
			}
			
			label = new GLabel (counter+" balls left");
			position (label);
			pause(DELAY*250);
			remove(label);
			counter--;
		}
		label = new GLabel ("GAME OVER");
		position (label);
	}
	
	private void moveBall() {
		vx = rgen.nextDouble (0.1, 0.5);
		if (rgen.nextBoolean(0.5)) vx = -vx;
		vy = 0.25;
	
		
		while (ball != null) {
			ball.move(vx, vy);
			checkForCollision();
			pause(DELAY); 
		}
		
	}
	
	private void checkForCollision () {
		
		// check for collisions and assign to collider
		GObject collider = getCollidingObject ();
				
		// remove object if brick
		if ( (collider != null) && (collider != paddle) && (collider != walls) && (collider != ball)) {
			remove(collider);
			brickcounter --;
			bounceClip.play ();
			
			// increase ball speed
			if (vy<0) {
				vy = -vy + 0.02*(NBRICK_ROWS*NBRICKS_PER_ROW - brickcounter);
				}
			else {
				vy = -vy - 0.02*(NBRICK_ROWS*NBRICKS_PER_ROW - brickcounter);
			}
		} else {
			// rebound if paddle
			if ( (collider == paddle) ) {
				vy = -vy;
				bounceClip.play();
			}
		}
		
		
		// get coordinates of the 4 ball corners
		double x = ball.getX()+BALL_RADIUS;
		double y = ball.getY()+BALL_RADIUS;
		
		// check for collision top wall
		if ((y-BALL_RADIUS<START_Y)) {
			vy=-vy;
		}
		
		// check for collision with bottom wall which means game over
		if ((y+BALL_RADIUS>START_Y+HEIGHT)) {
			remove (ball);
			ball = null;
		}
		
		
		// check for collision with vertical walls
		if ((x+BALL_RADIUS>START_X+WIDTH) || ( x-BALL_RADIUS < START_X ) ) {
			vx=-vx;
		}
		
	}
	
	private GObject getCollidingObject() {
	// get coordinates of the 4 ball corners
		double x = ball.getX()+BALL_RADIUS;
		double y = ball.getY()+BALL_RADIUS;
		double r = BALL_RADIUS;
		
		corNE = getElementAt (x+r, y-r);
		corSE = getElementAt (x+r, y+r);
		corNW = getElementAt (x-r, y-r);
		corSW = getElementAt (x-r, y+r);
		
		// check for collisions and assign object to gobj
		GObject coll = null;
		if (corNE !=null) coll = corNE;
		if (corSE !=null) coll = corSE;
		if (corNW !=null) coll = corNW;
		if (corSW !=null) coll = corSW;
		return coll;
 
	}
	
	
	private void position (GLabel label_positioned) {
		GLabel box = label_positioned;
		box.setFont(new Font("Serif", Font.BOLD, 24));
		box.setLocation(START_X+WIDTH/2-label.getWidth()/2, START_Y+HEIGHT/2);
		add(box);
	}
	
	/** Moves the paddle by the mouse speed
	* checking that the paddle is not hitting the left wall
	* nor the right wall
	*/ 
	public void mouseMoved (MouseEvent e) {
		// get paddle speed based on mouse movement speed
		paddle_speed = e.getX()-lastX;
		
		// check that not hitting walls and move by paddle speed
		if ((paddle.getX()+paddle_speed>=START_X)&&(paddle.getX()+paddle_speed+PADDLE_WIDTH<START_X+WIDTH)) {
			paddle.move(paddle_speed, 0);
		}
			//if hitting any of the walls, move just enough to stay stick to wall
			else {
				if (paddle.getX()+paddle_speed<START_X) {
					paddle.move(START_X-paddle.getX(), 0);
				}
				else {
					paddle.move( START_X+WIDTH-(PADDLE_WIDTH+paddle.getX()),0);
				}
		}
		
		lastX = e.getX();
	}
	
	/** Listens to the mouse click to start the game */
	public void mouseClicked (MouseEvent e) {
		mouse_clicked = e.getClickCount();
	}
	
	/* private instance variables */
	private GRect paddle;  /* The paddle */
	private GImage ball; /* The ball */
	private int lastX;   /* The last mouse X positionon the horizontal axis */
	private int paddle_speed; /* This defines the speed at which the paddle will move horizontally */
	private RandomGenerator rgen = RandomGenerator.getInstance (); /*Random generator */
	private double vx, vy; /* speed of the ball */
	private GRect walls; /* define the walls */
	private GObject corNE; /* North East corner of the ball */
	private GObject corSE; /* South East corner of the ball */
	private GObject corNW; /* North West corner of the ball */
	private GObject corSW; /* South West corner of the ball */
	private int counter; /* counts the number of games left */
	private int brickcounter = NBRICK_ROWS*NBRICKS_PER_ROW; /* counts the bricks */
	private GLabel label; /* a laber that will be used across */
	private AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au"); /*loads the file that plays the sound*/
	private int mouse_clicked=0; /* checks whether the mouse has been clicked or not */
}
