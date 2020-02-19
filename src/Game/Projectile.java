package Game;

import java.util.ArrayList;

import Imported.Texture;
import LowLevel.Image;
import LowLevel.Positionable;

public class Projectile extends Image {
	//Projectile textures or something
	public static ArrayList<Projectile> visProj = new ArrayList<Projectile>();
	public static Texture[] projTex = new Texture[6];
	public static void initProj()
	{
		projTex[0] = new Texture("Projectiles/Fireball0/fire0.png");
		projTex[1] = new Texture("Projectiles/Fireball0/fire1.png");
		projTex[2] = new Texture("Projectiles/Fireball0/fire2.png");
		projTex[3] = projTex[1];
		projTex[4] = new Texture("Projectiles/Fireball1/fire0.png");
		projTex[5] = new Texture("Projectiles/Fireball1/fire1.png");
	}
	//Gives a projectile its animations from startInd to endInd
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
		//visProj.forEach((proj) -> proj.show());
		//visProj.clear();
	}
	
	
	private int ID;
	private int frameAnim;
	private int framesShot;
	private int endFrame;
	private double speed;
	private boolean orbitting;
	private int changeFrame;
	//All the animations (anims[0] should be frame 1, anims[7] should be frame 8 etc)
	private Texture[] orbitAnims;
	private Texture[] shotAnims;
	private Texture[] anims;
	private Positionable orbitter;
	private double orbitAngle;
	public Projectile(int inID, double x, double y, double width, double length, double angle, Positionable orbitChar)
	{
		super(null, x, y, width, length);
		frameAnim = 0;
		ID = inID;
		if (ID == 0)
		{
			orbitAnims = getAnims(0, 3);
			shotAnims = getAnims(4, 5);
			changeFrame = 3;
			endFrame = 90;
			speed = 8;
		}
		setAngle(angle);
		orbitting = false;
		orbitter = orbitChar;
		orbitAngle = 0;
		framesShot = 0;
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
		super.show(shouldRotate);
	}
	public void setOrbit(boolean newOrbit)
	{
		orbitting = newOrbit;
		if (orbitting) {anims = orbitAnims;}
		else {anims = shotAnims;}
		frameAnim = 0;
		framesShot = 0;
	}
	public void setOrbitAngle(double newAngle)
	{
		orbitAngle = newAngle;
	}
	public double getOrbitAngle()
	{
		return orbitAngle;
	}
	//Needs to be accessed by player to determine whether to THROW THIS GARBAGE OUTTA THE LIST YEE BOI
	public boolean isEnded()
	{
		return framesShot == endFrame;
	}
	//Moves the projectile wherever it's angled
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
			double radius = Math.max(orbitter.getCharWidth(), orbitter.getCharLength()) * 1.3;
			orbitAngle += 5;
			if (orbitAngle >= 360) {orbitAngle = orbitAngle - 360;}
			//setAngle(orbitAngle);
			double mathAngle = Math.toRadians(orbitAngle);
			double xDist = radius * Math.cos(mathAngle);
			double yDist = radius * Math.sin(mathAngle);
			setPos(orbitter.getX() + xDist, orbitter.getY() + yDist);
		}
	}
	public static int fireball = 0;
}
