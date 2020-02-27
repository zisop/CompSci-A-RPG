package LowLevel;

public class Positionable extends Point
{
    private double width;
    private double length;
    private double angle;
    private double hitWidth;
    private double hitLength;
    private boolean interactsProj;
    private Point[] showBasis;
    private Point[] collisionBasis;

    
    public Positionable(double xVal, double yVal, double w, double l) {
        super(xVal, yVal);
        width = w;
        length = l;
        hitWidth = w;
        hitLength = l;
        angle = 0.0;
        interactsProj = true;
        Point p1 = new Point(xVal - w / 2, yVal - l / 2);
        Point p2 = new Point(xVal + w / 2, yVal - l / 2);
        Point p3 = new Point(xVal + w / 2, yVal + l / 2);
        Point p4 = new Point(xVal - w / 2, yVal + l / 2);
        showBasis = new Point[] {p1, p2, p3, p4};
        p1 = new Point(xVal - w / 2, yVal - l / 2);
        p2 = new Point(xVal + w / 2, yVal - l / 2);
        p3 = new Point(xVal + w / 2, yVal + l / 2);
        p4 = new Point(xVal - w / 2, yVal + l / 2);
        collisionBasis = new Point[] {p1, p2, p3, p4};
    }
    public Positionable(double xVal, double yVal, double w, double l, double hitW, double hitL) {
        super(xVal, yVal);
        width = w;
        length = l;
        hitWidth = hitW;
        hitLength = hitL;
        angle = 0.0;
        interactsProj = true;
        Point p1 = new Point(xVal - w / 2, yVal - l / 2);
        Point p2 = new Point(xVal + w / 2, yVal - l / 2);
        Point p3 = new Point(xVal + w / 2, yVal + l / 2);
        Point p4 = new Point(xVal - w / 2, yVal + l / 2);
        showBasis = new Point[] {p1, p2, p3, p4};
        p1 = new Point(xVal - hitW / 2, yVal - hitL / 2);
        p2 = new Point(xVal + hitW / 2, yVal - hitL / 2);
        p3 = new Point(xVal + hitW / 2, yVal + hitL / 2);
        p4 = new Point(xVal - hitW / 2, yVal + hitL / 2);
        collisionBasis = new Point[] {p1, p2, p3, p4};
    }
    public Positionable(double xVal, double yVal, double w, double l, double hitW, double hitL, double hitboxDown) {
        super(xVal, yVal);
        width = w;
        length = l;
        hitWidth = hitW;
        hitLength = hitL;
        angle = 0.0;
        interactsProj = true;
        Point p1 = new Point(xVal - w / 2, yVal - l / 2);
        Point p2 = new Point(xVal + w / 2, yVal - l / 2);
        Point p3 = new Point(xVal + w / 2, yVal + l / 2);
        Point p4 = new Point(xVal - w / 2, yVal + l / 2);
        showBasis = new Point[] {p1, p2, p3, p4};
        p1 = new Point(xVal - hitW / 2, yVal - hitL / 2 - hitboxDown);
        p2 = new Point(xVal + hitW / 2, yVal - hitL / 2 - hitboxDown);
        p3 = new Point(xVal + hitW / 2, yVal + hitL / 2 - hitboxDown);
        p4 = new Point(xVal - hitW / 2, yVal + hitL / 2 - hitboxDown);
        collisionBasis = new Point[] {p1, p2, p3, p4};
    }
    public void setProjInteraction(boolean newInteraction)
    {
    	interactsProj = newInteraction;
    }
    public boolean getProjInteraction()
    {
    	return interactsProj;
    }
    
    public boolean collision(Positionable otherChar) {
    	if ((otherChar) == null) {return false;}
        Point[] otherCollisionBasis = otherChar.getCollisionBasis();
        return Geometry.colliding(collisionBasis, otherCollisionBasis);
    }
    //Returns the direction of the collision with some otherChar
    //0 = other obj to north, 1 = other obj to south, 2 = other obj to east, 3 = other obj to west
    //0 = cant move north, 1 = cant move east, 2 = cant move south, 3 = cant move west
    public int relPos(Positionable otherChar, double hitboxDown) {
        double selfXMin = collisionBasis[UL].getX();
        double selfXMax = collisionBasis[UR].getX();
        double selfYMin = collisionBasis[DR].getY();
        double selfYMax = collisionBasis[UR].getY();
        

        selfYMax -= hitboxDown;
        selfYMin -= hitboxDown;
        Point[] otherBasis = otherChar.getCollisionBasis();
        double otherXMin = otherBasis[DL].getX();
        double otherXMax = otherBasis[DR].getX();
        double otherYMin = otherBasis[DL].getY();
        double otherYMax = otherBasis[UL].getY();
        for (int offset = 0; selfYMin + offset < otherYMax; ++offset) {
            if (selfYMax - offset <= otherYMin) {
                return 0;
            }
            if (selfXMin + offset >= otherXMax) {
                return 3;
            }
            if (selfXMax - offset <= otherXMin) {
                return 1;
            }
        }
        return 2;
    }
    public int relPos(Positionable otherChar) {
    	
        return relPos(otherChar, 0);
    }
    /**
     * This class stores a bunch of getter and setter methods that nobody cares about
     * Also, everything that interacts with the game extends it because it has the ability to store a lot of data
     */


    
    public void setWidth(double newWidth) {
        
    	//Assumed that hitboxwidth is always the same as hitboxlength
    	//Because we've only altered lengths so far
        showBasis[DL].setX(getX() - newWidth / 2);
        showBasis[UL].setX(getX() - newWidth / 2);
        showBasis[DR].setX(getX() + newWidth / 2);
        showBasis[UR].setX(getX() + newWidth / 2);
        
        collisionBasis[DL].setX(getX() - newWidth / 2);
        collisionBasis[UL].setX(getX() - newWidth / 2);
        collisionBasis[DR].setX(getX() + newWidth / 2);
        collisionBasis[UR].setX(getX() + newWidth / 2);
        
        width = newWidth;
        hitWidth = newWidth;
    }
    public void setX(double newX)
    {
    	double xDiff = newX - getX();
    	super.setX(newX);
    	for (int i = 0; i < showBasis.length; i++)
    	{
    		showBasis[i].setX(showBasis[i].getX() + xDiff);
    		collisionBasis[i].setX(collisionBasis[i].getX() + xDiff);
    	}
    }
    public void setY(double newY)
    {
    	double yDiff = newY - getY();
    	super.setY(newY);
    	for (int i = 0; i < showBasis.length; i++)
    	{
    		showBasis[i].setY(showBasis[i].getY() + yDiff);
    		collisionBasis[i].setY(collisionBasis[i].getY() + yDiff);
    	}
    }
    public void setPos(double newX, double newY)
    {
    	setX(newX);
    	setY(newY);
    }
    
    public void setLength(double newLength) {
    	double lengthFrac = newLength / getLength();
        double collisionCenterY = (collisionBasis[DL].getY() + collisionBasis[UL].getY()) / 2;
        double yDiff = collisionBasis[UL].getY() - collisionCenterY;
        yDiff *= lengthFrac;
        showBasis[DL].setY(getY() - newLength / 2);
        showBasis[UL].setY(getY() + newLength / 2);
        showBasis[DR].setY(getY() - newLength / 2);
        showBasis[UR].setY(getY() + newLength / 2);
        length = newLength;
        
        if (hitLength <= 0) {
        	hitLength = newLength;
        	collisionBasis[DL].setY(showBasis[DL].getY());
        	collisionBasis[UL].setY(showBasis[UL].getY());
        	collisionBasis[DR].setY(showBasis[DR].getY());
        	collisionBasis[UR].setY(showBasis[UR].getY());
        }
        else {
        	collisionBasis[DL].setY(collisionCenterY - yDiff);
        	collisionBasis[UL].setY(collisionCenterY + yDiff);
        	collisionBasis[DR].setY(collisionCenterY - yDiff);
        	collisionBasis[UR].setY(collisionCenterY + yDiff);
        	hitLength = collisionBasis[UL].getY() - collisionBasis[DL].getY();
        }
    }
    
    public void setAngle(double newAng) {angle = newAng;}
    public double getWidth() {return width;}
    public double getLength() {return length;}
    public double getHitWidth() {return hitWidth;}
    public double getHitLength() {return hitLength;}
    public double getAngle() {return angle;}
    public Point[] getShowBasis() {return showBasis;}
    public Point[] getCollisionBasis() {return collisionBasis;}
    public int compareTo(Object otherObj)
    {
    	Positionable k = (Positionable)otherObj;
    	if (getY() - getLength() / 2 < k.getY() - k.getLength() / 2)
    	{
    		return 1;
    	}
    	if (getY() - getLength() / 2 > k.getY() - k.getLength() / 2)
    	{
    		return -1;
    	}
    	return 0;
    }
    public static int DL = 0;
    public static int DR = 1;
    public static int UR = 2;
    public static int UL = 3;
}
