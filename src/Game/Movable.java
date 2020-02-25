package Game;

import Imported.Texture;

public class Movable extends Displayable{
	public Displayable lastCollision;
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
    
}
