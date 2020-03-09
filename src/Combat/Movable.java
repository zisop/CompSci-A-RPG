package Combat;

import LowLevel.Polygon;
import java.util.ArrayList;

import Game.Main;
import Imported.Texture;
import LowLevel.Geometry;
import LowLevel.Image;
import LowLevel.Point;
import LowLevel.Shape;
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
    	ArrayList<Image> images = currRoom.getImages();
    	
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
    	
        for (int i = 0; i < images.size(); ++i) {
            Image currChar = images.get(i);
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
    protected void setProjectileWidth(double newWidth)
    {
    	Point[] projBasis = getProjectileBasis();
    	projBasis[DL].setX(projBasis[DL].getX() - newWidth / 2);
    	projBasis[UL].setX(projBasis[UL].getX() - newWidth / 2);
    	projBasis[UR].setX(projBasis[UR].getX() + newWidth / 2);
    	projBasis[DR].setX(projBasis[DR].getX() + newWidth / 2);
    }
    protected void setProjectileLength(double newLength)
    {
    	Point[] projBasis = getProjectileBasis();
    	projBasis[DL].setY(projBasis[DL].getY() - newLength / 2);
    	projBasis[UL].setY(projBasis[UL].getY() + newLength / 2);
    	projBasis[UR].setY(projBasis[UR].getY() + newLength / 2);
    	projBasis[DR].setY(projBasis[DR].getY() - newLength / 2);
    }
    public void setSpeed(double newSpeed) {speed = newSpeed;}
    public double getSpeed() {return speed;}
    
    public abstract void move();
    public static final int up = 0;
	public static final int right = 1;
	public static final int down = 2;
	public static final int left = 3;
}
