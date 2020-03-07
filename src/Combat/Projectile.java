package Combat;

import java.util.ArrayList;


import Game.Main;
import Imported.Texture;
import LowLevel.Image;
import World.Room;

public class Projectile extends Movable {
	private int ID;
	private int frameAnim;
	private int framesShot;
	private int endFrame;
	private int numHits;
	private int maxHits;
	private int pauseFrame;
	private double speed;
	private double orbitRadius;
	private boolean orbitting;
	private double damage;
	private int changeFrame;
	private Image collidingChar;
	//All the animations (anims[0] should be frame 1, anims[7] should be frame 8 etc)
	private Texture[] orbitAnims;
	private Texture[] shotAnims;
	private Texture[] anims;
	private CombatChar orbitter;
	private double orbitAngle;
	public Projectile(int inID, double x, double y, double width, double length, double angle, CombatChar orbitChar)
	{
		super(null, x, y, width, length);
		frameAnim = 0;
		ID = inID;
		switch (ID) {
			case fireball:
				orbitAnims = getAnims(fireBallOrbit, fireBallOrbit + 3);
				shotAnims = getAnims(fireBallShot, fireBallShot + 1);
				changeFrame = 3;
				endFrame = 90;
				speed = 8;
				maxHits = 4;
		}
		setAngle(angle);
		orbitting = false;
		orbitter = orbitChar;
		pauseFrame = maxPauseFrame;
		numHits = 0;
		orbitAngle = 0;
		framesShot = 0;
		orbitRadius = Math.max(orbitter.getHitWidth(), orbitter.getHitLength()) * 1.6;
		setRotation(true);
	}
	
	//When a projectile shows, it'll add to its framecount and then show
	public void show()
	{
		setImage(anims[(frameAnim / changeFrame) % anims.length]);
		frameAnim++;
		if (!orbitting)
		{
			framesShot++;
		}
		
		if (pauseFrame == maxPauseFrame)
		{
			updateCollision();
			
			if (collidingChar != null && orbitter.isEnemy(collidingChar))
			{
				
				CombatChar coll = (CombatChar)collidingChar;
				coll.setHealth(coll.getHealth() - damage);
				if (!orbitting) {numHits++;}
				pauseFrame = 0;
			}	
		}
		else {pauseFrame++;}
		if (numHits == maxHits) {framesShot = endFrame;}
		super.show();
	}
	public void setOrbit(boolean newOrbit)
	{
		orbitting = newOrbit;
		if (orbitting) {anims = orbitAnims;}
		else {anims = shotAnims;}
		frameAnim = 0;
		framesShot = 0;
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
	private void updateCollision()
	{
		Room currRoom = Main.allRooms[Main.currRoom];
		Image[] images = currRoom.getImages();
		for (int i = 0; i < images.length; i++)
		{
			Image currImg = images[i];
			if (currImg != orbitter && currImg.interactsProj() && collision(currImg))
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
			setPos(orbitter.getX() + xDist, orbitter.getY() + yDist);
		}
	}
	public static final int fireBallOrbit = 0;
	public static final int fireBallShot = 4; 
	public static final int fireball = 0;
	public static final double facingUp = 90;
	public static ArrayList<Projectile> visProj = new ArrayList<Projectile>();
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
	public static void showVisProjectiles()
	{
		visProj.forEach((proj) -> proj.show());
		visProj.clear();
	}
	public static int maxPauseFrame = 30;
}