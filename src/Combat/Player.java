package Combat;



import java.util.ArrayList;

import Game.Main;
import Imported.Texture;
import LowLevel.Geometry;
import LowLevel.Point;
import LowLevel.Positionable;

public class Player extends CombatChar
{
	private static double xInteractionRadius = 20;
	private static double clickInteractionRadius = 40;
	private ArrayList<Projectile> allSpells;
	private ArrayList<Projectile> orbit;
	
	private Point[] xInteractionPoints;
	private Point[] clickInteractionPoints;
	
    public Player(Texture img, int inX, int inY, double w, double l, double hitW, double hitL, double hbDown) {
        super(img, inX, inY, w, l);
        setHitWidth(hitW);
        setHitLength(hitL);
        hitBoxDown(hbDown);
        Point[] collisionBasis = getCollisionBasis();
        Point p1 = new Point(collisionBasis[DL].getX() - xInteractionRadius, collisionBasis[DL].getY() - xInteractionRadius);
        Point p2 = new Point(collisionBasis[DR].getX() + xInteractionRadius, collisionBasis[DR].getY() - xInteractionRadius);
        Point p3 = new Point(collisionBasis[UR].getX() + xInteractionRadius, collisionBasis[UR].getY() + xInteractionRadius);
        Point p4 = new Point(collisionBasis[UL].getX() - xInteractionRadius, collisionBasis[UL].getY() + xInteractionRadius);
        xInteractionPoints = new Point[] {p1, p2, p3, p4};
        
        p1 = new Point(collisionBasis[DL].getX() - clickInteractionRadius, collisionBasis[DL].getY() - clickInteractionRadius);
        p2 = new Point(collisionBasis[DR].getX() + clickInteractionRadius, collisionBasis[DR].getY() - clickInteractionRadius);
        p3 = new Point(collisionBasis[UR].getX() + clickInteractionRadius, collisionBasis[UR].getY() + clickInteractionRadius);
        p4 = new Point(collisionBasis[UL].getX() - clickInteractionRadius, collisionBasis[UL].getY() + clickInteractionRadius);
        clickInteractionPoints = new Point[] {p1, p2, p3, p4};
        speed = baseSpeed;
        
        allSpells = new ArrayList<Projectile>();
        orbit = new ArrayList<Projectile>();
        maxHealth = 100;
        maxMana = 100;
        health = maxHealth;
        mana = maxMana;
        manaRegen = .5;
        healthRegen = .02;
        
        walkFrame = 0;
        soundFXFrame = 0;
        walkAnim = resetWalk;
        walkDirec = down;
        walkAnimSwitch = 6;
        soundFXSwitch = 20;
        anims = getAnims(playerAnimInd, playerAnimInd + 19);
        walkSounds = getSounds(playerSoundInd, playerSoundInd + 0);
        firstSound = 6;
        setEnemyState(good);
    }
    /**
     * Determines whether the character was within player's X button pressing collision radius
     * @param otherChar
     * @return player can interact with the x button
     */
    public boolean xCollision(Positionable otherChar)
    {
    	Point[] otherBasis = otherChar.getCollisionBasis();
    	return Geometry.colliding(xInteractionPoints, otherBasis);
    }
    /**
     * Determines whether the character was within player's click collision radius
     * @param otherChar
     * @return player can interact with click
     */
    public boolean clickCollision(Positionable otherChar)
    {
    	Point[] otherBasis = otherChar.getCollisionBasis();
    	return Geometry.colliding(clickInteractionPoints, otherBasis);
    }
    public void setWidth(double newWidth)
    {
    	super.setWidth(newWidth);
    	Point[] collisionBasis = getCollisionBasis();
        xInteractionPoints[DL].setX(collisionBasis[DL].getX() - xInteractionRadius);
        xInteractionPoints[UL].setX(collisionBasis[UL].getX() - xInteractionRadius);
        xInteractionPoints[UR].setX(collisionBasis[UR].getX() + xInteractionRadius);
        xInteractionPoints[DR].setX(collisionBasis[DR].getX() + xInteractionRadius);
        clickInteractionPoints[DL].setX(collisionBasis[DL].getX() - clickInteractionRadius);
        clickInteractionPoints[UL].setX(collisionBasis[UL].getX() - clickInteractionRadius);
        clickInteractionPoints[UR].setX(collisionBasis[UR].getX() + clickInteractionRadius);
        clickInteractionPoints[DR].setX(collisionBasis[DR].getX() + clickInteractionRadius);
    }
    public void setLength(double newLength)
    {
    	super.setLength(newLength);
    	Point[] collisionBasis = getCollisionBasis();
        xInteractionPoints[DL].setY(collisionBasis[DL].getY() - xInteractionRadius);
        xInteractionPoints[UL].setY(collisionBasis[UL].getY() - xInteractionRadius);
        xInteractionPoints[UR].setY(collisionBasis[UR].getY() + xInteractionRadius);
        xInteractionPoints[DR].setY(collisionBasis[DR].getY() + xInteractionRadius);
        clickInteractionPoints[DL].setY(collisionBasis[DL].getY() - clickInteractionRadius);
        clickInteractionPoints[UL].setY(collisionBasis[UL].getY() - clickInteractionRadius);
        clickInteractionPoints[UR].setY(collisionBasis[UR].getY() + clickInteractionRadius);
        clickInteractionPoints[DR].setY(collisionBasis[DR].getY() + clickInteractionRadius);
    }
    public void setX(double newX)
    {
    	double xDiff = newX - getX();
    	super.setX(newX);
    	for (int i = 0; i < clickInteractionPoints.length; i++)
    	{
    		clickInteractionPoints[i].setX(clickInteractionPoints[i].getX() + xDiff);
    		xInteractionPoints[i].setX(xInteractionPoints[i].getX() + xDiff);
    	}
    }
    public void setY(double newY)
    {
    	double yDiff = newY - getY();
    	super.setY(newY);
    	for (int i = 0; i < clickInteractionPoints.length; i++)
    	{
    		clickInteractionPoints[i].setY(clickInteractionPoints[i].getY() + yDiff);
    		xInteractionPoints[i].setY(xInteractionPoints[i].getY() + yDiff);
    	}
    }
    
    public void shootProjectile(int ID, double speed)
    {
    	if (orbit.size() > 0)
    	{
    		double mouseAngle = Main.cursorAngle();
    		int smallestInd = 0;
    		for (int i = 1; i < orbit.size(); i++)
    		{
    			double smallestDist = Math.abs(orbit.get(smallestInd).getOrbitAngle() - mouseAngle);
    			double currDist = Math.abs(orbit.get(i).getOrbitAngle() - mouseAngle);
    			if (currDist < smallestDist)
    			{
    				smallestInd = i;
    			}
    		}
    		Projectile shot = orbit.get(smallestInd);
    		double xDiff = Main.cursor.getX() + getX() - shot.getX();
    		double yDiff = Main.cursor.getY() + getY() - shot.getY();
    		double hypoLen = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    		double shotAngle = Math.toDegrees(Math.acos(xDiff / hypoLen));
    		if (yDiff < 0) {shotAngle *= -1;}
    		shot.setOrbit(false);
    		shot.setAngle(shotAngle);
    		orbit.remove(smallestInd);
    		mana -= fireBallCost;
    		switch(ID)
    		{
    			case Projectile.fireball:
    				shot.setDamage(10);
    		}
    	}
    }
    /**
     * Shows all the player's visible spells
     * Adds them to the visProj arrayList so they will be displayed at the top of the screen
     */
    public void showSpells()
    {
    	for (int i = allSpells.size() - 1; i >= 0; i--)
    	{
    		allSpells.get(i).move();
    		Projectile.visProj.add(allSpells.get(i));
    		if (allSpells.get(i).isEnded())
    		{
    			allSpells.remove(i);
    		}
    	}
    }

    public void show()
    {
    	if (mana < maxMana) {mana += Math.min(manaRegen, maxMana - mana);}
    	if (health < maxHealth) {health += Math.min(healthRegen, maxHealth - health);}
    	
    	manageProjectiles();
    	showSpells();
    	
    	super.show();
    }
    /**
     * Determines where to place projectiles
     */
    public void manageProjectiles()
    {
    	int numProj = (int)(100 / fireBallCost);
    	int idealNum = (int)(mana / fireBallCost);
    	if (orbit.size() < idealNum)
    	{
    		double angle = 0;
    		if (orbit.size() > 0)
    		{
    			double[] angles = new double[orbit.size()];
    			for (int i = 0; i < angles.length; i++)
    			{
    				angles[i] = orbit.get(i).getOrbitAngle(); 
    			}
    			int minInd = 0;
    			for (int i = 1; i < angles.length; i++)
    			{
    				if (angles[i] < angles[minInd])
    				{
    					minInd = i;
    				}
    			}
    			angle = angles[minInd] + 360 / numProj;
    			for (int i = 0; i < angles.length; i++)
    			{
    				if (Math.abs(angle - angles[i]) <= .0001)
    				{
    					angle += 360 / numProj;
    				}
    			}
    		}
    		Projectile currProj = new Projectile(Projectile.fireball, 0, 0, 50, 50, Projectile.facingUp, this);
    		currProj.setOrbitAngle(angle);
    		currProj.setOrbit(true);
    		orbit.add(currProj);
    		allSpells.add(currProj);
    		currProj.setDamage(1);
    	}
    	if (!Main.alreadyInteracting)
    	{
    		if (Main.one && !Main.oneLastFrame)
    		{
    			shootProjectile(Projectile.fireball, 20);
    		}
    	}
    }
    
    private static final double fireBallCost = 25;
    public final static double baseSpeed = 8;
}
