package LowLevel;

public class Positionable extends Point
{
    private double width;
    private double length;
    private double angle;
    private double hitWidth;
    private double hitLength;
    private double hitboxDown;
    private boolean interactsProj;
    private boolean shouldRotate;
    private Point[] showBasis;
    private Point[] collisionBasis;

    
    public Positionable(double xVal, double yVal, double w, double l) {
        this(xVal, yVal, w, l, w, l);
    }
    public Positionable(double xVal, double yVal, double w, double l, double hitW, double hitL) {
        this(xVal, yVal, w, l, hitW, hitL, 0);
    }
    public Positionable(double xVal, double yVal, double w, double l, double hitW, double hitL, double hitboxDown) {
        super(xVal, yVal);
        width = Math.abs(w);
        length = Math.abs(l);
        hitWidth = Math.abs(hitW);
        hitLength = Math.abs(hitL);
        angle = 0.0;
        interactsProj = false;
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
        this.hitboxDown = hitboxDown;
        shouldRotate = false;
    }
    public void setProjInteraction(boolean newInteraction) {interactsProj = newInteraction;}
    public boolean shouldRotate() {return shouldRotate;}
    public void setRotation(boolean newRotation) {shouldRotate = newRotation;}
    public boolean interactsProj() {return interactsProj;}
    /**
     * determines whether two characters' hitboxes intersect
     * @param otherChar
     * @return collision == true
     */
    public boolean collision(Positionable otherChar) {
    	Point[] myCollBasis = getCollisionBasis();
    	Point[] otherCollisionBasis = otherChar.getCollisionBasis();
    	if (otherChar.shouldRotate())
		{
			Point otherCenter = new Point(otherChar.getX(), otherChar.getY() - otherChar.getHitDown());
			otherCollisionBasis = Geometry.rotatePoints(otherCollisionBasis, otherCenter, otherChar.getAngle());
		}
    	if (shouldRotate())
    	{
    		Point myCenter = new Point(getX(), getY() - getHitDown());
			myCollBasis = Geometry.rotatePoints(myCollBasis, myCenter, getAngle());
    	}
    	return Geometry.colliding(myCollBasis, otherCollisionBasis);
    }
    /**
     * Determines whether two characters' hitbox LINES intersect; does not determine if this is inside of otherChar
     * @param otherChar
     * @return collision == true
     */
    public boolean strictCollision(Positionable otherChar)
    {
    	Point[] otherCollisionBasis = otherChar.getCollisionBasis();
    	Point[] myCollBasis = getCollisionBasis();
    	if (otherChar.shouldRotate())
		{
			Point otherCenter = new Point(otherChar.getX(), otherChar.getY() - otherChar.getHitDown());
			otherCollisionBasis = Geometry.rotatePoints(otherCollisionBasis, otherCenter, otherChar.getAngle());
		}
    	if (shouldRotate())
    	{
    		Point myCenter = new Point(getX(), getY() - getHitDown());
			myCollBasis = Geometry.rotatePoints(myCollBasis, myCenter, getAngle());
    	}
    	return Geometry.strictCollision(myCollBasis, otherCollisionBasis);
    }
    //Returns the direction of the collision with some otherChar
    //0 = other obj to north, 1 = other obj to south, 2 = other obj to east, 3 = other obj to west
    //0 = cant move north, 1 = cant move east, 2 = cant move south, 3 = cant move west
    public int relPos(Positionable otherChar) {
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
    /**
     * adjusts width
     * @param newWidth
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
    
    /**
     * 
     * adjusts x
     */
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
    /**
     * adjusts y
     */
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
    /**
     * Moves the collisionBasis (hitbox) of a character down by yValue
     * @param yVal
     */
    public void hitBoxDown(double yVal)
    {
    	hitboxDown += yVal;
    	collisionBasis[DL].setY(collisionBasis[DL].getY() - yVal);
    	collisionBasis[UL].setY(collisionBasis[UL].getY() - yVal);
    	collisionBasis[DR].setY(collisionBasis[DR].getY() - yVal);
    	collisionBasis[UR].setY(collisionBasis[UR].getY() - yVal);
    }
    /**
     * Sets the length of collisionBasis (hitbox) to newLength
     * @param newLength
     */
    public void setHitLength(double newLength)
    {
    	double currCenterY = collisionBasis[UL].getY() - getHitLength() / 2;
    	collisionBasis[UL].setY(currCenterY + newLength / 2);
    	collisionBasis[UR].setY(currCenterY + newLength / 2);
    	collisionBasis[DL].setY(currCenterY - newLength / 2);
    	collisionBasis[DR].setY(currCenterY - newLength / 2);
    	hitLength = newLength;
    }
    /**
     * sets the width of collisionBasis (hitbox) to hitWidth
     * @param newWidth
     */
    public void setHitWidth(double newWidth)
    {
    	double currCenterX = collisionBasis[UL].getX() + getHitWidth() / 2;
    	collisionBasis[UL].setX(currCenterX - newWidth / 2);
    	collisionBasis[DL].setX(currCenterX - newWidth / 2);
    	collisionBasis[UR].setX(currCenterX + newWidth / 2);
    	collisionBasis[DR].setX(currCenterX + newWidth / 2);
    	hitWidth = newWidth;
    }
    /**
     * sets position to (newX, newY)
     */
    public void setPos(double newX, double newY)
    {
    	setX(newX);
    	setY(newY);
    }
    /**
     * Sets the length of a character to newLength
     * @param newLength
     */
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
    public void showShowBox()
	{
    	Point[] basis = getShowBasis();
    	Point[] rotatedBasis = Geometry.rotatePoints(basis, this, getAngle());
		Polygon temp = new Polygon(rotatedBasis);
		temp.show();
	}
	public void showCollisionBox()
	{
		Point[] basis = getCollisionBasis();
		Point myCenter = new Point(getX(), getY() - getHitDown());
    	Point[] rotatedBasis = Geometry.rotatePoints(basis, myCenter, getAngle());
		Polygon temp = new Polygon(rotatedBasis);
		temp.show();
	}
	
	public void showProjectileBox()
	{
		Point[] basis = getProjectileBasis();
		Point myCenter = new Point(getX(), getY() - getHitDown());
    	Point[] rotatedBasis = Geometry.rotatePoints(basis, myCenter, getAngle());
		Polygon temp = new Polygon(rotatedBasis);
		temp.show();
	}
    
    public void setAngle(double newAng) {angle = newAng;}
    public double getWidth() {return width;}
    public double getLength() {return length;}
    public double getHitWidth() {return hitWidth;}
    public double getHitLength() {return hitLength;}
    public double getAngle() {return angle;}
    public double getHitDown() {return hitboxDown;}
    public void setCollisionBasis(Point[] newBasis) {collisionBasis = newBasis;}
    public void setShowBasis(Point[] newBasis) {showBasis = newBasis;}
    public Point[] getShowBasis() {return showBasis;}
    public Point[] getCollisionBasis() {return collisionBasis;}
    public Point[] getProjectileBasis() {return collisionBasis;}
    /**
     * Comparison basis for determining order to be shown in
     * @param otherObj
     * @return {0 -> same, 1 -> this was lower on the screen, -1 -> this was higher on the screen}
     */
    public int compareTo(Positionable otherObj)
    {
    	if (getY() - getLength() / 2 < otherObj.getY() - otherObj.getLength() / 2)
    	{
    		return 1;
    	}
    	if (getY() - getLength() / 2 > otherObj.getY() - otherObj.getLength() / 2)
    	{
    		return -1;
    	}
    	return 0;
    }
    public static final int DL = 0;
    public static final int DR = 1;
    public static final int UR = 2;
    public static final int UL = 3;
}
