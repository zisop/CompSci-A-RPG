package LowLevel;

public class Positionable extends Point
{
    private double width;
    private double length;
    private double angle;
    private boolean interactsProj;

    
    public Positionable(double xVal, double yVal, double w, double l) {
        super(xVal, yVal);
        width = w;
        length = l;
        angle = 0.0;
        interactsProj = true;
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
        double selfXMin = getX() - getCharWidth() / 2.0;
        double selfXMax = getX() + getCharWidth() / 2.0;
        double selfYMin = getY() - getCharLength() / 2.0;
        double selfYMax = getY() + getCharLength() / 2.0;
        
        double otherXMin = otherChar.getX() - otherChar.getCharWidth() / 2.0;
        double otherXMax = otherChar.getX() + otherChar.getCharWidth() / 2.0;
        double otherYMin = otherChar.getY() - otherChar.getCharLength() / 2.0;
        double otherYMax = otherChar.getY() + otherChar.getCharLength() / 2.0;
        
        Point p1 = new Point(selfXMin, selfYMin);
        Point p2 = new Point(selfXMin, selfYMax);
        Point p3 = new Point(selfXMax, selfYMax);
        Point p4 = new Point(selfXMax, selfYMin);
        
        Point p5 = new Point(otherXMin, otherYMin);
        Point p6 = new Point(otherXMin, otherYMax);
        Point p7 = new Point(otherXMax, otherYMax);
        Point p8 = new Point(otherXMax, otherYMin);
        
        Point[] selfPoints = new Point[] {p1, p2, p3, p4};
        Point[] otherPoints = new Point[] {p5, p6, p7, p8};
        return Geometry.colliding(selfPoints, otherPoints);
    }
    public boolean collision(Positionable otherChar, double offSetTop, double extraRadius) {
    	if ((otherChar) == null) {return false;}
        double selfXMin = getX() - getCharWidth() / 2.0;
        double selfXMax = getX() + getCharWidth() / 2.0;
        double selfYMin = getY() - getCharLength() / 2.0;
        double selfYMax = getY() + getCharLength() / 2.0;
        selfYMax -= offSetTop;
        double otherXMin = otherChar.getX() - otherChar.getCharWidth() / 2.0 - extraRadius;
        double otherXMax = otherChar.getX() + otherChar.getCharWidth() / 2.0 + extraRadius;
        double otherYMin = otherChar.getY() - otherChar.getCharLength() / 2.0 - extraRadius;
        double otherYMax = otherChar.getY() + otherChar.getCharLength() / 2.0 + extraRadius;
        
        Point p1 = new Point(selfXMin, selfYMin);
        Point p2 = new Point(selfXMax, selfYMin);
        Point p3 = new Point(selfXMax, selfYMax);
        Point p4 = new Point(selfXMin, selfYMax);
        
        Point p5 = new Point(otherXMin, otherYMin);
        Point p6 = new Point(otherXMax, otherYMin);
        Point p7 = new Point(otherXMax, otherYMax);
        Point p8 = new Point(otherXMin, otherYMax);
        
        Point[] selfPoints = new Point[] {p1, p2, p3, p4};
        Point[] otherPoints = new Point[] {p5, p6, p7, p8};
        return Geometry.colliding(selfPoints, otherPoints);
    }
    //Returns the direction of the collision with some otherChar
    //0 = other obj to north, 1 = other obj to south, 2 = other obj to east, 3 = other obj to west
    //0 = cant move north, 1 = cant move east, 2 = cant move south, 3 = cant move west
    public int relPos(Positionable otherChar, double offSetTop) {
        double selfXMin = getX() - getCharWidth() / 2.0;
        double selfXMax = getX() + getCharWidth() / 2.0;
        double selfYMin = getY() - getCharLength() / 2.0;
        double selfYMax = getY() + getCharLength() / 2.0;
        selfYMax -= offSetTop;
        double otherXMin = otherChar.getX() - otherChar.getCharWidth() / 2.0;
        double otherXMax = otherChar.getX() + otherChar.getCharWidth() / 2.0;
        double otherYMin = otherChar.getY() - otherChar.getCharLength() / 2.0;
        double otherYMax = otherChar.getY() + otherChar.getCharLength() / 2.0;
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
        double selfXMin = getX() - getCharWidth() / 2.0;
        double selfXMax = getX() + getCharWidth() / 2.0;
        double selfYMin = getY() - getCharLength() / 2.0;
        double selfYMax = getY() + getCharLength() / 2.0;
        double otherXMin = otherChar.getX() - otherChar.getCharWidth() / 2.0;
        double otherXMax = otherChar.getX() + otherChar.getCharWidth() / 2.0;
        double otherYMin = otherChar.getY() - otherChar.getCharLength() / 2.0;
        double otherYMax = otherChar.getY() + otherChar.getCharLength() / 2.0;
        for (int offset = 0; selfYMin + offset < otherYMax; ++offset) {
            if (selfYMax - offset <= otherYMin) {
                return 1;
            }
            if (selfXMin + offset >= otherXMax) {
                return 2;
            }
            if (selfXMax - offset <= otherXMin) {
                return 3;
            }
        }
        return 0;
    }
    /**
     * This class stores a bunch of getter and setter methods that nobody cares about
     * Also, everything that interacts with the game extends it because it has the ability to store a lot of data
     */


    
    public void setWidth(double newWidth) {
        width = newWidth;
    }
    
    public void setLength(double newLength) {
        length = newLength;
    }
    
    public void setAngle(double newAng) {
        angle = newAng;
    }
    
    
    public double getWidth() {
        return width;
    }
    
    public double getLength() {
        return length;
    }
    
    public double getCharWidth() {
        return width;
    }
    
    public double getCharLength() {
        return length;
    }
    
    public double getAngle() {
        return angle;
    }
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
}
