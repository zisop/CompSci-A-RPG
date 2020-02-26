package Game;



import java.util.ArrayList;

import Imported.Audio;
import Imported.Texture;
import LowLevel.Geometry;
import LowLevel.Image;
import LowLevel.Point;
import LowLevel.Positionable;
import Mobs.Mob;
import World.Room;

public class Player extends Movable
{
	public static Texture[] loadedTex;
	private static double xInteractionRadius = 20;
	private static double clickInteractionRadius = 40;
	private int walkFrame;
	private int walkAnim;
	private int soundFXFrame;
	private int walkDirec;
	private ArrayList<Projectile> allSpells;
	private ArrayList<Projectile> orbit;
	private double manaRegen;
	private double mana;
	private double health;
	private double healthRegen;
	private double maxHealth;
	private double maxMana;
	private Point[] xInteractionPoints;
	private Point[] clickInteractionPoints;
    public Player(Texture img, int inX, int inY, double w, double l, double hitW, double hitL, double hbDown) {
        super(img, inX, inY, w, l, hitW, hitL, hbDown);
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
        setSpeed(baseSpeed);
        walkFrame = 0;
        soundFXFrame = 0;
        walkAnim = notWalking;
        walkDirec = 0;
        allSpells = new ArrayList<Projectile>();
        orbit = new ArrayList<Projectile>();
        maxHealth = 100;
        maxMana = 100;
        health = maxHealth;
        mana = maxMana;
        manaRegen = .5;
        healthRegen = .5;
    }
    public boolean xCollision(Positionable otherChar)
    {
    	Point[] otherBasis = otherChar.getCollisionBasis();
    	return Geometry.colliding(xInteractionPoints, otherBasis);
    }
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
    		mana -= 33.34;
    	}
    }
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
    public void manageProjectiles()
    {
    	int idealNum = (int)(mana / 33.33);
    	if (orbit.size() < idealNum)
    	{
    		//Figures out where to put it in the orbit (PLEASE MAKE THIS FASTER OR SOMETHING I KNOW IT SUCKS)
    		double angle = new Double(0);
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
    			angle = angles[minInd] + 120;
    			for (int i = 0; i < angles.length; i++)
    			{
    				if (Math.abs(angle - angles[i]) <= .0001)
    				{
    					angle += 120;
    				}
    			}
    		}
    		Projectile currProj = new Projectile(Projectile.fireball, 0, 0, 50, 50, 90, this);
    		currProj.setOrbitAngle(angle);
    		currProj.setOrbit(true);
    		orbit.add(currProj);
    		allSpells.add(currProj);
    	}
    	if (!Main.alreadyInteracting)
    	{
    		if (Main.one && !Main.oneLastFrame)
    		{
    			shootProjectile(0, 20);
    		}
    	}
    }
    
    //moves the player and cycles its walk animation
    public void move(int direc)
    {
    	super.move(direc);
    	walkDirec = direc;
    	walkFrame++;
    	if (walkAnim == notWalking) {walkAnim = startWalking; walkFrame = 6;}
    	if (walkFrame == 6)
    	{
    		walkAnim++;
    		walkFrame = 0;
    		if (walkDirec == 0)
    		{
    			if (walkAnim == 1) {setImage(Player.loadedTex[13]);}
    			else if (walkAnim == 2) {setImage(Player.loadedTex[14]);}
    			else if (walkAnim == 3) {setImage(Player.loadedTex[15]);}
    			else if (walkAnim == 4) {setImage(Player.loadedTex[14]);}
    			else if (walkAnim == 5) {walkAnim = 1; setImage(Player.loadedTex[13]);}
    		}
    		else if (walkDirec == 1)
    		{
    			if (walkAnim == 1) {setImage(Player.loadedTex[0]);}
    			else if (walkAnim == 2) {setImage(Player.loadedTex[1]);}
    			else if (walkAnim == 3) {setImage(Player.loadedTex[2]);}
    			else if (walkAnim == 4) {setImage(Player.loadedTex[1]);}
    			else if (walkAnim == 5) {walkAnim = 1; setImage(Player.loadedTex[0]);}
    		}
    		else if (walkDirec == 2)
    		{
    			if (walkAnim == 1) {setImage(Player.loadedTex[10]);}
    			else if (walkAnim == 2) {setImage(Player.loadedTex[11]);}
    			else if (walkAnim == 3) {setImage(Player.loadedTex[12]);}
    			else if (walkAnim == 4) {setImage(Player.loadedTex[11]);}
    			else if (walkAnim == 5) {walkAnim = 1; setImage(Player.loadedTex[10]);}
    		}
    		else if (walkDirec == 3)
    		{
    			if (walkAnim == 1) {setImage(Player.loadedTex[7]);}
    			else if (walkAnim == 2) {setImage(Player.loadedTex[8]);}
    			else if (walkAnim == 3) {setImage(Player.loadedTex[9]);}
    			else if (walkAnim == 4) {setImage(Player.loadedTex[8]);}
    			else if (walkAnim == 5) {walkAnim = 1; setImage(Player.loadedTex[7]);}
    		}
    		
    	}
    	if (soundFXFrame == 17 || soundFXFrame == 5)
    	{
    		soundFXFrame = 6;
    		Audio.playSound("Move/Steps/foot2");
    	}
    	else 
    	{
			soundFXFrame++;
		}
    	
    }
    //stops the player's walk to put it back in idle
    public void stopWalk()
    {
    	walkFrame = 0;
    	soundFXFrame = 0;
    	walkAnim = notWalking;
    	if (walkDirec == 0) {setImage(Player.loadedTex[3]);}
    	if (walkDirec == 1) {setImage(Player.loadedTex[4]);}
    	if (walkDirec == 2) {setImage(Player.loadedTex[5]);}
    	if (walkDirec == 3) {setImage(Player.loadedTex[6]);}
    }
    public boolean collision(Positionable otherChar)
    {
    	return super.collision(otherChar);
    }
    /**
     * returns the player's ability to move {north, east, south, west}
     */
    public boolean[] getMovement()
    {
    	Image[] room = Main.allRooms[Main.currRoom].getImages();
    	boolean[] movement = new boolean[]{true, true, true, true};
        for (int i = 0; i < room.length; ++i) {
            Image currChar = room[i];
            //movables dont collide
            if (!(currChar instanceof Movable)) {
            	
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
    //Stats setters and getters
    public double getHealth() {return health;}
    public double getMaxHealth() {return maxHealth;}
    public double getMana() {return mana;}
    public double getMaxMana() {return maxMana;}
    public void setHealth(double newHealth) {health = newHealth;}
    public void setMaxHealth(double newMax) {maxHealth = newMax;}
    public void setMana(double newMana) {mana = newMana;}
    public void setMaxMana(double newMax) {maxMana = newMax;}
    
  //Initializes all player textures / animations
    public static void initTex()
    {
    	Player.loadedTex = new Texture[16];
    	Player.loadedTex[0] = new Texture("WalkAnim/WalkRight/Right01.PNG");
    	Player.loadedTex[1] = new Texture("WalkAnim/WalkRight/Right02.PNG");
    	Player.loadedTex[2] = new Texture("WalkAnim/WalkRight/Right03.PNG");
    	Player.loadedTex[3] = new Texture("IdleAnim/IdleUp.PNG");
    	Player.loadedTex[4] = new Texture("IdleAnim/IdleRight.PNG");
    	Player.loadedTex[5] = new Texture("IdleAnim/IdleDown.PNG");
    	Player.loadedTex[6] = new Texture("IdleAnim/IdleLeft.PNG");
    	Player.loadedTex[7] = new Texture("WalkAnim/WalkLeft/Left01.PNG");
    	Player.loadedTex[8] = new Texture("WalkAnim/WalkLeft/Left02.PNG");
    	Player.loadedTex[9] = new Texture("WalkAnim/WalkLeft/Left03.PNG");
    	Player.loadedTex[10] = new Texture("WalkAnim/WalkDown/Down01.PNG");
    	Player.loadedTex[11] = new Texture("WalkAnim/WalkDown/Down02.PNG");
    	Player.loadedTex[12] = new Texture("WalkAnim/WalkDown/Down03.PNG");
    	Player.loadedTex[13] = new Texture("WalkAnim/WalkUp/Up01.PNG");
    	Player.loadedTex[14] = new Texture("WalkAnim/WalkUp/Up02.PNG");
    	Player.loadedTex[15] = new Texture("WalkAnim/WalkUp/Up03.PNG");
    }
    public static double baseSpeed = 8;
    public static int startWalking = 0;
    public static int notWalking = -1;
}
