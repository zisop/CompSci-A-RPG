package Game;

import Imported.Texture;
import LowLevel.Image;

public class Movable extends Image{
	private double speed;
	public Movable(Texture img, double inX, double inY, double w, double l) {
        super(img, inX, inY, w, l);
    }
    
    public Movable(Texture img, double inX, double inY, double w, double l, double hitW, double hitL) {
        super(img, inX, inY, w, l, hitW, hitL);
    }
    public Movable(Texture img, double inX, double inY, double w, double l, double hitW, double hitL, double hitboxDown) {
        super(img, inX, inY, w, l, hitW, hitL, hitboxDown);
    }
    public void move(int direc) {
    	double dist = getSpeed();
        if (direc == 0)
        {
            setY(getY() + dist);
            return;
        }
        if (direc == 1)
        {
            setX(getX() + dist);
            return;
        }
        if (direc == 2)
        {
            setY(getY() - dist);
            return;
        }
        if (direc == 3)
        {
            setX(getX() - dist);
        }
    }
    public void setSpeed(double newSpeed)
    {
    	speed = newSpeed;
    }
    public double getSpeed()
    {
    	return speed;
    }

	/**
     * 
     * @return boolean[] of movement capabilities {north, east, south, west}
     */
    public boolean[] getMovement()
    {
    	Image[] room = Main.allRooms[Main.currRoom].getImages();
    	boolean[] movement = new boolean[]{true, true, true, true};
        for (int i = 0; i < room.length; ++i) {
            Image currChar = room[i];
            if (currChar.collides()) {
            	
            	boolean shouldBreak = false;
            	setY(getY() + getSpeed());
            	if (collision(currChar))
            	{
            		movement[0] = false; 
            		shouldBreak = true;
            	}
            	setPos(getX() + getSpeed(), getY() - getSpeed());
            	if (collision(currChar))
            	{
            		movement[1] = false;
            		shouldBreak = true;
            	}
            	setPos(getX() - getSpeed(), getY() - getSpeed());
            	if (collision(currChar))
            	{
            		movement[2] = false;
            		shouldBreak = true;
            	}
           		setPos(getX() - getSpeed(), getY() + getSpeed());
            	if (collision(currChar))
            	{
            		movement[3] = false;
            		shouldBreak = true;
            	}
            	setX(getX() + getSpeed());
            	if (shouldBreak) {
            		break;
            	}
            }
        }
        return movement;
    }
    
}
