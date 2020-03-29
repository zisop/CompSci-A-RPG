package Combat;

import java.util.ArrayList;

import Game.Main;
import Imported.Texture;
import LowLevel.Geometry;
import LowLevel.Image;
import LowLevel.Point;
import LowLevel.Positionable;
import World.Room;

public class Projectile extends Movable {
	private int ID;
	private int frameAnim;
	private int framesShot;
	private int endFrame;
	private int numHits;
	private int maxHits;
	private ArrayList<Image> alreadyAttacked;
	private ArrayList<Image> collidingChars;
	private double orbitRadius;
	private boolean orbitting;
	private boolean rotateWhileShot;
	private double damage;
	private double shotAngle;
	
	private double initialHitVelocity;
	private int changeFrame;
	private int attackStun;
	private int attackInvulnerability;
	//All the animations (anims[0] should be frame 1, anims[7] should be frame 8 etc)
	private Texture[] orbitAnims;
	private Texture[] shotAnims;
	private Texture[] anims;
	private CombatChar owner;
	private double orbitAngle;
	public Projectile(int inID, double x, double y, double angle, double damage, CombatChar ownChar)
	{
		super(null, x, y, 0, 0);
		frameAnim = 0;
		ID = inID;
		alreadyAttacked = new ArrayList<Image>();
		collidingChars = new ArrayList<Image>();
		switch (ID) {
			case fireball:
				setWidth(50);
				setLength(getWidth());
				orbitAnims = getAnims(fireBallOrbit, fireBallOrbit + 3);
				shotAnims = getAnims(fireBallShot, fireBallShot + 1);
				changeFrame = 3;
				endFrame = 90;
				maxHits = 4;
				setHitLength(getLength() * 4 / 5);
				setHitWidth(getWidth() * 4 / 5);
				attackStun = 0;
				attackInvulnerability = 0;
				initialHitVelocity = 10;
				rotateWhileShot = false;
				break;
			case door:
				speed = 20;
				setLength(80);
				setWidth(getLength() * 3 / 2);
				
				int whichDoor = Main.random.nextInt(numDoors);
				shotAnims = getAnims(doorInd + whichDoor, doorInd + whichDoor);
				changeFrame = 10;
				endFrame = 90;
				maxHits = 100;
				attackStun = 45;
				attackInvulnerability = 10;
				initialHitVelocity = speed * 2 / 3;
				rotateWhileShot = true;
				break;
			case arrow:
				speed = 10;
				setLength(40);
				setWidth(getLength());
				setHitLength(getLength() / 2);
				
				shotAnims = getAnims(arrowInd, arrowInd);
				changeFrame = 50;
				endFrame = 50;
				maxHits = 1;
				attackStun = 10;
				attackInvulnerability = 10;
				initialHitVelocity = speed * 2 / 3;
				rotateWhileShot = false;
				break;
			default:
				try {throw new Exception("Projectile: " + inID + " didn't exist");} 
				catch (Exception e) {e.printStackTrace(); System.exit(0);}
		}
		setAngle(angle);
		setOrbit(false);
		setProjInteraction(true);
		owner = ownChar;
		this.damage = damage;
		numHits = 0;
		orbitAngle = 0;
		framesShot = 0;
		orbitRadius = Math.max(owner.getWidth(), owner.getLength()) * .85;
		setRotation(true);
		setEnemyState(owner.enemyState());
	}
	
	//When a projectile shows, it'll add to its framecount and then show
	public void show()
	{
		if (!Main.alreadyInteracting)
		{
			setImage(anims[(frameAnim++ / changeFrame) % anims.length]);
			if (!orbitting)
			{
				framesShot++;
			}
			
			updateCollision();
			for (Image coll : collidingChars)
			{
				if (coll instanceof CombatChar)
				{
					CombatChar enemy = (CombatChar)(coll);
					if (enemy.canBeAttacked() && !alreadyAttacked.contains(enemy) && isEnemy(enemy))
					{
					
						double[] damageInfo = new double[5];
						damageInfo[Effect.damageDamage] = damage;
						damageInfo[Effect.damageFromAngle] = enemy.angleTo(owner);
						damageInfo[Effect.damageStunFrames] = attackStun;
						damageInfo[Effect.damageInvulnFrames] = attackInvulnerability;
						damageInfo[Effect.damageInitialVelocity] = initialHitVelocity;
						Effect damage = new Effect(Effect.damage, damageInfo, owner);
						enemy.receiveEffect(damage);
						
					}
				}
				if (!orbitting && isEnemy(coll)) {
					numHits++; alreadyAttacked.add(coll);
					if (numHits == maxHits) {framesShot = endFrame;}
				}
			}
			collidingChars.clear();
			move();
		}
		super.show();
	}
	public void setOrbit(boolean flag)
	{
		orbitting = flag;
		if (orbitting) {anims = orbitAnims;}
		else {anims = shotAnims; shotAngle = getAngle();}
		frameAnim = 0;
		framesShot = 0;
	}
	public boolean collision(Positionable otherChar)
	{
		Point[] collBasis = getCollisionBasis();
		Point[] projBasis = otherChar.getProjectileBasis();
		Point[] temp = Geometry.rotatePoints(collBasis, this, getAngle());
		//saves time to check only line intersections
		return Geometry.strictCollision(projBasis, temp);
	}
	/**
	 * sets orbit angle
	 * @param newAngle
	 */
	public void setOrbitAngle(double newAngle)
	{
		orbitAngle = newAngle;
	}
	/**
	 *
	 * @return orbit angle
	 */
	public double getOrbitAngle()
	{
		return orbitAngle;
	}
	public double getDamage() {return damage;}
	public void setDamage(double newDamage) {damage = newDamage;}
	public void setStun(int newStun) {attackStun = newStun;}
	public void setInvuln(int newInvuln) {attackInvulnerability = newInvuln;}
	private void updateCollision()
	{
		Room currRoom = Main.allRooms[Main.currRoom];
		
		ArrayList<Image> temp = new ArrayList<Image>();
		ArrayList<Image> images = currRoom.getImages();
		ArrayList<Projectile> proj = currRoom.getShotProj();
		
		temp.addAll(images);
		temp.addAll(proj);
		for (int i = 0; i < temp.size(); i++)
		{
			Image currImg = temp.get(i);
			if (currImg != owner && currImg.interactsProj() && collision(currImg))
			{
				collidingChars.add(currImg);
			}
		}
	}
	/**
	 * determines whether the projectile should STOP EXISTING
	 * @return end == true
	 */
	public boolean isEnded() {return framesShot >= endFrame;}
	public boolean isOrbitting() {return orbitting;}
	public double getOrbitRadius() {return orbitRadius;}
	/**
	 * moves projectile where it should false
	 */
	public void move()
	{
		if (!orbitting)
		{	
			double xDist = Math.cos(shotAngle * Math.PI / 180) * speed;
			double yDist = Math.sin(shotAngle * Math.PI / 180) * speed;
			setPos(getX() + xDist, getY() + yDist);
			if (rotateWhileShot) {setAngle(getAngle() + 5);}
		}
		else
		{
			orbitAngle += 5;
			if (orbitAngle >= 360) {orbitAngle = orbitAngle - 360;}
			//setAngle(orbitAngle);
			double mathAngle = Math.toRadians(orbitAngle);
			double xDist = orbitRadius * Math.cos(mathAngle);
			double yDist = orbitRadius * Math.sin(mathAngle);
			setPos(owner.getX() + xDist, owner.getY() + yDist);
		}
	}
	private static final int fireBallOrbit = 0;
	private static final int fireBallShot = fireBallOrbit + 4; 
	private static final int doorInd = fireBallShot + 2;
	private static final int arrowInd = doorInd + 6;
	public static final int fireball = 0;
	public static final int door = 5;
	public static final int arrow = 2;
	public static final double facingUp = 90;
	public static Texture[] projTex = new Texture[13];
	/**
	 * init
	 */
	public static void initProj()
	{
		projTex[fireBallOrbit + 0] = new Texture("Projectiles/Fireball0/fire0.png");
		projTex[fireBallOrbit + 1] = new Texture("Projectiles/Fireball0/fire1.png");
		projTex[fireBallOrbit + 2] = new Texture("Projectiles/Fireball0/fire2.png");
		projTex[fireBallOrbit + 3] = projTex[1];
		projTex[fireBallShot + 0] = new Texture("Projectiles/Fireball1/fire0.png");
		projTex[fireBallShot + 1] = new Texture("Projectiles/Fireball1/fire1.png");
		projTex[doorInd] = new Texture("Projectiles/Door/door0.png");
		projTex[doorInd + 1] = new Texture("Projectiles/Door/door1.png");
		projTex[doorInd + 2] = new Texture("Projectiles/Door/door2.png");
		projTex[doorInd + 3] = new Texture("Projectiles/Door/door3.png");
		projTex[doorInd + 4] = new Texture("Projectiles/Door/door4.png");
		projTex[doorInd + 5] = new Texture("Projectiles/Door/door5.png");
		projTex[arrowInd] = new Texture("Projectiles/arrow.png");
	}
	private static final int numDoors = 6;
	
	/**
	 * 
	 * @param startInd
	 * @param endInd
	 * @return array of textures
	 */
	public static Texture[] getAnims(int startInd, int endInd)
	{
		Texture[] anims = new Texture[endInd - startInd + 1];
		for (int i = startInd; i <= endInd; i++)
		{
			anims[i - startInd] = projTex[i]; 
		}
		return anims;
	}
}
