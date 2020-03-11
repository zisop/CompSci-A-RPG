package Combat;



import java.util.ArrayList;

import Game.Main;
import Imported.Audio;
import Imported.Texture;
import LowLevel.Geometry;
import LowLevel.Image;
import LowLevel.Point;
import LowLevel.Positionable;

public class Player extends CombatChar
{
	private static double xInteractionRadius = 20;
	private static double clickInteractionRadius = 40;
	private ArrayList<Projectile> orbit;
	private ArrayList<AOE> allAOE;
	
	
	private Point[] xInteractionPoints;
	private Point[] clickInteractionPoints;
	private boolean casting;
	private AOE cast;
	
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
        
        orbit = new ArrayList<Projectile>();
        allAOE = new ArrayList<AOE>();
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
        walkVolume = .2;
        
        anims = getAnims(playerAnimInd, playerAnimInd + 19);
        walkSounds = getSounds(playerSoundInd, playerSoundInd + 0);
        firstSound = 6;
        setEnemyState(good);
        handleCombatException();
        casting = false;
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
    
    private void castSpell(int ID)
    {
    	switch(ID)
    	{
    		case Projectile.fireball:
    			if (mana >= fireBallCost)
    			{
    				double speed = this.speed * 5/4;
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
    				orbit.remove(smallestInd);
    				double xDiff = Main.cursor.getX() + getX() - shot.getX();
    				double yDiff = Main.cursor.getY() + getY() - shot.getY();
    				double hypoLen = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    				double shotAngle = Math.toDegrees(Math.acos(xDiff / hypoLen));
    				if (yDiff < 0) {shotAngle *= -1;}
    				shot.setOrbit(false);
    				shot.setAngle(shotAngle);
    				shot.setSpeed(speed);
    				mana -= fireBallCost;
    				shot.setDamage(10);
    				shot.setInvuln(8);
    				shot.setStun(8);
    				Audio.playSound("Batt/fireball", .48);
    				Main.allRooms[Main.currRoom].permaShow(shot);
    			}
    			break;
    		case AOE.poison:
    			if (mana >= poisonCost)
    			{
    				cast = new AOE(AOE.poison, this);
    				updateCastState(true);
    			}
    			break;
    		case heal:
    			if (mana >= healCost && health < maxHealth)
    			{
    				double conversionRatio = .2;
    				setHealth(Math.min(maxHealth, getHealth() + healCost * conversionRatio));
    				setMana(getMana() - healCost);
    				createParticles(heal);
    				Audio.playSound("Spells/blessing2", .3);
    			}
    			break;
    	}
    }
    /**
     * Shows all the player's visible spells
     * Adds them to the visProj arrayList so they will be displayed at the top of the screen
     */
    public void showSpells()
    {
    	for (int i = orbit.size() - 1; i >= 0; i--)
    	{
    		Projectile curr = orbit.get(i);
    		Main.allRooms[Main.currRoom].tempShow(curr);
    	}
    	if (casting) 
    	{
    		cast.setPos(Main.cursor.getX() + Main.player.getX(), Main.cursor.getY() + Main.player.getY());
    		Main.allRooms[Main.currRoom].tempShow(cast);
    		if (Main.leftClick && !Main.leftClickLastFrame)
    		{
    			cast.place();
    			updateCastState(false);
    			int ID = cast.getID();
    			switch(ID)
    			{
    				case AOE.poison:
    					mana -= poisonCost;
    			}
    		}
    		else if (Main.rightClick && !Main.rightClickLastFrame)
    		{
    			updateCastState(false);
    		}
    	}
    }
    private void updateCastState(boolean flag)
    {
    	casting = flag;
    	Main.alreadyInteracting = flag;
    }

    public void show()
    {
    	if (!Main.alreadyInteracting)
    	{
    		if (mana < maxMana) {mana += Math.min(manaRegen, maxMana - mana);}
    		if (health < maxHealth) {health += Math.min(healthRegen, maxHealth - health);}
    	
    		manageProjectiles();
    	}
    	manageSpells();
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
    		currProj.setDamage(.2);
    	}
    }
    private void manageSpells()
    {
    	if (!Main.alreadyInteracting)
    	{
    		if (Main.one && !Main.oneLastFrame)
    		{
    			castSpell(Projectile.fireball);
    		}
    		else if (Main.two && !Main.twoLastFrame)
    		{
    			castSpell(AOE.poison);
    		}
    		else if (Main.three && !Main.threeLastFrame)
    		{
    			castSpell(CombatChar.heal);
    		}
    	}
    }
    
    private static final double fireBallCost = 25;
    private static final double poisonCost = 30;
    private static final double healCost = 50;
    public final static double baseSpeed = 8;
}
