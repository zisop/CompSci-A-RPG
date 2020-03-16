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
	private double orbitRadius;
	private boolean orbitting;
	private double damage;
	
	private double initialVelocity;
	private int changeFrame;
	private int attackStun;
	private int attackInvulnerability;
	private Image collidingChar;
	//All the animations (anims[0] should be frame 1, anims[7] should be frame 8 etc)
	private Texture[] orbitAnims;
	private Texture[] shotAnims;
	private Texture[] anims;
	private CombatChar owner;
	private double orbitAngle;
	public Projectile(int inID, double x, double y, double width, double length, double angle, CombatChar ownChar)
	{
		super(null, x, y, width, length);
		frameAnim = 0;
		ID = inID;
		alreadyAttacked = new ArrayList<Image>();
		switch (ID) {
			case fireball:
				orbitAnims = getAnims(fireBallOrbit, fireBallOrbit + 3);
				shotAnims = getAnims(fireBallShot, fireBallShot + 1);
				changeFrame = 3;
				endFrame = 90;
				maxHits = 4;
				setHitLength(getLength() * 4 / 5);
				setHitWidth(getWidth() * 4 / 5);
				hitBoxDown(-5);
				attackStun = 0;
				attackInvulnerability = 0;
				initialVelocity = 10;
		}
		setAngle(angle);
		orbitting = false;
		owner = ownChar;
		numHits = 0;
		orbitAngle = 0;
		framesShot = 0;
		orbitRadius = Math.max(owner.getHitWidth(), owner.getHitLength()) * 1.6;
		setRotation(true);
	}
	
	//When a projectile shows, it'll add to its framecount and then show
	public void show()
	{
		if (!Main.alreadyInteracting)
		{
			setImage(anims[(frameAnim / changeFrame) % anims.length]);
			frameAnim++;
			if (!orbitting)
			{
				framesShot++;
			}
			
			updateCollision();
			CombatChar coll = (CombatChar)collidingChar;
			if (collidingChar != null && coll.canBeAttacked() && !alreadyAttacked.contains(collidingChar) && owner.isEnemy(collidingChar))
			{
				double[] damageInfo = new double[5];
				damageInfo[Effect.damageDamage] = damage;
				damageInfo[Effect.damageFromAngle] = coll.angleTo(owner);
				damageInfo[Effect.damageStunFrames] = attackStun;
				damageInfo[Effect.damageInvulnFrames] = attackInvulnerability;
				damageInfo[Effect.damageInitialVelocity] = initialVelocity;
				Effect damage = new Effect(Effect.damage, damageInfo, owner);
				coll.receiveEffect(damage);
				if (!orbitting) {
					numHits++; alreadyAttacked.add(collidingChar);
					if (numHits == maxHits) {framesShot = endFrame;}
				}
			}
			move();
		}
		super.show();
	}
	public void setOrbit(boolean flag)
	{
		orbitting = flag;
		if (orbitting) {anims = orbitAnims;}
		else {anims = shotAnims;}
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
		ArrayList<Image> images = currRoom.getImages();
		for (int i = 0; i < images.size(); i++)
		{
			Image currImg = images.get(i);
			if (currImg != owner && currImg.interactsProj() && collision(currImg))
			{
				collidingChar = currImg;
				return;
			}
		}
		collidingChar = null;
	}
	/**
	 * determines whether the projectile should STOP EXISTING
	 * @return end == true
	 */
	public boolean isEnded()
	{
		return framesShot == endFrame;
	}
	public boolean isOrbitting()
	{
		return orbitting;
	}
	/**
	 * moves projectile where it should false
	 */
	public void move()
	{
		if (!orbitting)
		{	
			double xDist = Math.cos(getAngle() * Math.PI / 180) * speed;
			double yDist = Math.sin(getAngle() * Math.PI / 180) * speed;
			setPos(getX() + xDist, getY() + yDist);
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
	public static final int fireBallOrbit = 0;
	public static final int fireBallShot = 4; 
	public static final int fireball = 0;
	public static final int arrow = 2;
	public static final double facingUp = 90;
	public static Texture[] projTex = new Texture[6];
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
	}
	
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
