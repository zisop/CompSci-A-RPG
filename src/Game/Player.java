package Game;



import java.util.ArrayList;

import Imported.Audio;
import Imported.Texture;
import LowLevel.Positionable;

public class Player extends Movable
{
	public static Texture[] loadedTex;
	private int walkFrame;
	private int walkAnim;
	private int soundFXFrame;
	private boolean walking;
	private int walkDirec;
	private double speed;
	private ArrayList<Projectile> allSpells;
	private ArrayList<Projectile> orbit;
	private double manaRegen;
	private double mana;
	private double health;
	private double healthRegen;
	private double maxHealth;
	private double maxMana;
    public Player(Texture img, int inX, int inY, double w, double l, int charW, int charL) {
        super(img, inX, inY, w, l, charW, charL);
        walkFrame = 0;
        soundFXFrame = 0;
        walkAnim = 1;
        walking = false;
        walkDirec = 0;
        speed = 8;
        allSpells = new ArrayList<Projectile>();
        orbit = new ArrayList<Projectile>();
        maxHealth = 100;
        maxMana = 100;
        health = maxHealth;
        mana = maxMana;
        manaRegen = .5;
        healthRegen = .5;
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
    
    public void setSpeed(double newSpeed)
    {
    	speed = newSpeed;
    }
    public double getSpeed()
    {
    	return speed;
    }
    //moves the player and cycles its walk animation
    public void move(int direc)
    {
    	super.move(direc, speed);
    	walkDirec = direc;
    	walkFrame++;
    	if (!walking) {walkAnim = 0; walking = true; walkFrame = 3; soundFXFrame = 0;}
    	if (walkFrame == 3)
    	{
    		walkAnim++;
    		walkFrame = 0;
    		if (walkDirec == 0)
    		{
    			if (walkAnim == 1) {setImage(Player.loadedTex[13]);}
    			if (walkAnim == 3) {setImage(Player.loadedTex[14]);}
    			if (walkAnim == 5) {setImage(Player.loadedTex[15]);}
    			if (walkAnim == 7) {setImage(Player.loadedTex[14]);}
    			if (walkAnim == 9) {walkAnim = 1; setImage(Player.loadedTex[13]);}
    		}
    		if (walkDirec == 1)
    		{
    			if (walkAnim == 1) {setImage(Player.loadedTex[0]);}
    			if (walkAnim == 3) {setImage(Player.loadedTex[1]);}
    			if (walkAnim == 5) {setImage(Player.loadedTex[2]);}
    			if (walkAnim == 7) {setImage(Player.loadedTex[1]);}
    			if (walkAnim == 9) {walkAnim = 1; setImage(Player.loadedTex[0]);}
    		}
    		if (walkDirec == 2)
    		{
    			if (walkAnim == 1) {setImage(Player.loadedTex[10]);}
    			if (walkAnim == 3) {setImage(Player.loadedTex[11]);}
    			if (walkAnim == 5) {setImage(Player.loadedTex[12]);}
    			if (walkAnim == 7) {setImage(Player.loadedTex[11]);}
    			if (walkAnim == 9) {walkAnim = 1; setImage(Player.loadedTex[10]);}
    		}
    		if (walkDirec == 3)
    		{
    			if (walkAnim == 1) {setImage(Player.loadedTex[7]);}
    			if (walkAnim == 3) {setImage(Player.loadedTex[8]);}
    			if (walkAnim == 5) {setImage(Player.loadedTex[9]);}
    			if (walkAnim == 7) {setImage(Player.loadedTex[8]);}
    			if (walkAnim == 9) {walkAnim = 1; setImage(Player.loadedTex[7]);}
    		}
    		
    	}
    	if (soundFXFrame == 17 || soundFXFrame == 5)
    	{
    		soundFXFrame = 6;
    		Audio.playSound("Move/Steps/foot2");
    	}
    	else {
			soundFXFrame++;
		}
    	
    }
    //stops the player's walk to put it back in idle
    public void stopWalk()
    {
    	walking = false;
    	walkFrame = 0;
    	walkAnim = 1;
    	if (walkDirec == 0) {setImage(Player.loadedTex[3]);}
    	if (walkDirec == 1) {setImage(Player.loadedTex[4]);}
    	if (walkDirec == 2) {setImage(Player.loadedTex[5]);}
    	if (walkDirec == 3) {setImage(Player.loadedTex[6]);}
    }
    public int relPos(Positionable otherChar)
    {
    	return super.relPos(otherChar, getCharLength() - 10);
    }
    public boolean collision(Positionable otherChar)
    {
    	return super.collision(otherChar, getCharLength() - 10, 0);
    }
    public boolean collision(Positionable otherChar, double extraRadius)
    {
    	return super.collision(otherChar, getCharLength() - 10, extraRadius);
    }
    /**
     * returns the player's ability to move {north, east, south, west}
     */
    public boolean[] getMovement()
    {
    	Displayable[] loopRoom = Main.allRooms[Main.currRoom];
    	boolean[] movement = new boolean[]{true, true, true, true};
        for (int i = 0; i < loopRoom.length; ++i) {
            Displayable currChar = loopRoom[i];
            //movables dont collide
            if (!(currChar instanceof Movable)) {
            	
            	boolean shouldBreak = false;
            	setY(getY() + speed);
            	if (collision(currChar))
            	{
            		movement[0] = false; 
            		shouldBreak = true;
            	}
            	setPos(getX() + speed, getY() - speed);
            	if (collision(currChar))
            	{
            		movement[1] = false;
            		shouldBreak = true;
            	}
            	setPos(getX() - speed, getY() - speed);
            	if (collision(currChar))
            	{
            		movement[2] = false;
            		shouldBreak = true;
            	}
           		setPos(getX() - speed, getY() + speed);
            	if (collision(currChar))
            	{
            		movement[3] = false;
            		shouldBreak = true;
            	}
            	setX(getX() + speed);
            	if (shouldBreak) {break;}
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
}