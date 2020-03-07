package Combat;

import Game.Main;
import Imported.Texture;
import LowLevel.Image;
import World.Room;

public abstract class Movable extends Image{
	protected double speed;
	protected boolean[] movement;
	public Movable(Texture tex, double x, double y, double w, double l)
	{
		super(tex, x, y, w, l);
		movement = new boolean[]{true, true, true, true};
	}
	/**
     * 
     * @return boolean[] of movement capabilities {north, east, south, west}
     */
    public boolean[] getMovement()
    {
        return movement;
    }
    public void updateMovement()
    {
    	Room currRoom = Main.allRooms[Main.currRoom];
    	Image[] images = currRoom.getImages();
    	
    	movement = new boolean[] {true, true, true, true};
    	boolean shouldEnd = false;
    	
    	//for testing purposes; will be removed in final game
    	//this first test essentially is just checking that you're staying inside the room
    	if (this != Main.player)
    	{
    		setY(getY() + speed);
    		if (currRoom.strictCollision(this))
    		{
    			movement[up] = false; 
    			shouldEnd = true;
    		}
    		setPos(getX() + speed, getY() - speed);
    		if (currRoom.strictCollision(this))
    		{
    			movement[right] = false;
    			shouldEnd = true;
    		}
    		setPos(getX() - speed, getY() - speed);
    		if (currRoom.strictCollision(this))
    		{
    			movement[down] = false;
    			shouldEnd = true;
    		}
   			setPos(getX() - speed, getY() + speed);
   			if (currRoom.strictCollision(this))
    		{
    			movement[left] = false;
    			shouldEnd = true;
    		}
    		setX(getX() + speed);
    		if (shouldEnd) {
    			return;
    		}
    	}
    	
        for (int i = 0; i < images.length; ++i) {
            Image currChar = images[i];
            if (currChar.collides()) {
            	setY(getY() + speed);
            	if (collision(currChar))
            	{
            		movement[up] = false; 
            		shouldEnd = true;
            	}
            	setPos(getX() + speed, getY() - speed);
            	if (collision(currChar))
            	{
            		movement[right] = false;
            		shouldEnd = true;
            	}
            	setPos(getX() - speed, getY() - speed);
            	if (collision(currChar))
            	{
            		movement[down] = false;
            		shouldEnd = true;
            	}
           		setPos(getX() - speed, getY() + speed);
            	if (collision(currChar))
            	{
            		movement[left] = false;
            		shouldEnd = true;
            	}
            	setX(getX() + speed);
            	if (shouldEnd) {
            		return;
            	}
            }
        }
    }
    public abstract void move();
    public static final int up = 0;
	public static final int right = 1;
	public static final int down = 2;
	public static final int left = 3;
}
