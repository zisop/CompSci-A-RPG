package Combat;

import Game.Main;
import LowLevel.Geometrical;
import LowLevel.Geometry;
import LowLevel.Image;
import LowLevel.Point;
import World.Room;
import World.Terrain;

public class MeleeMob extends Mob{
	
	public MeleeMob(double x, double y, int ID)
	{
		super(x, y, ID);
	}
	public void attack()
	{
		attackFrame = 0;
		facePlayer();
		handleAttackAnims();
		dealDamage();
		Main.player.enterHitStun(Main.player.angleTo(this));
	}
	private void dealDamage()
	{
		Main.player.setHealth(Main.player.getHealth() - damage);
	}
	private void facePlayer()
	{
		int direc;
		double xDist = Main.player.getX() - getX();
		double yDist = Main.player.getY() - getY();
		double hypoLen = xDist * xDist + yDist * yDist;
		hypoLen = Math.sqrt(hypoLen);
		double angle = Math.acos(xDist / hypoLen);
		if (yDist < 0) {angle *= -1;}
		if (angle >= Math.PI / 4 && angle < 3 * Math.PI / 4) {direc = up;}
		else if ((angle >= 3 * Math.PI / 4 && angle <= Math.PI) || (angle >= -Math.PI && angle < -3 * Math.PI / 4)) {direc = left;}
		else if (angle >= -3 * Math.PI / 4 && angle < -Math.PI / 4) {direc = down;}
		else {direc = right;}
		
		walkDirec = direc;
	}
	private void handleAttackAnims()
	{
		switch (walkDirec)
		{
			case up: setImage(anims[uA]); break;
			case right: setImage(anims[rA]); break;
			case down: setImage(anims[dA]); break;
			case left: setImage(anims[lA]); break;
		}
	}
	
	
	protected void pointToPlayer()
	{
		movementPoint = Main.player;
	}
	
	protected void pointRandomly()
	{
		Room currRoom = Main.allRooms[Main.currRoom];
		double maxRadius = sightRange;
		
		//Cubic graph results in a tendency to move far, rather than not far
		//there are exactly 0 words that mean not far you fuck
		double cube = Math.pow(maxRadius, 3);
		double radius = Math.pow(cube * Math.random(), 1/3.0);
		
		
		double angle = 2 * Math.PI * Math.random();
		startingHorizontal = Math.random() < .5;
		Point testPoint = new Point(getX() + Math.cos(angle) * radius, getY() + Math.sin(angle) * radius);
		int numTries = 0;
		while (!currRoom.pathPossible(this, testPoint))
		{
			startingHorizontal = Math.random() < .5;
			radius = Math.pow(cube * Math.random(), 1/3.0);
			angle = 2 * Math.PI * Math.random();
			
			testPoint = new Point(getX() + Math.cos(angle) * radius, getY() + Math.sin(angle) * radius);
			numTries++;
			if (numTries == 1000)
			{
				System.out.println("mob couldnt find where to move");
			}
		}
		movementPoint = testPoint;
	}
	protected void createStats()
	{
		double barWidth = 30;
		double barLength = 6;
		double offset = 2;
		stats = new Geometrical();
		
		
		Image mainRect = Geometry.createRect(0, barWidth + 2 * offset, 0, 2 * barLength + 3 * offset, 100, 100, 100, 255);
		
		//MN = Geometry.createRect(offset, barWidth + offset, offset, barLength + offset, 0, 0, 255, 255); maybe mages will use mana?
		//maxMN = Geometry.createRect(offset, barWidth + offset, offset, barLength + offset, 0, 0, 150, 255); just leave this here
		HP = Geometry.createRect(offset, barWidth + offset, barLength + 2 * offset, 2 * barLength + 2 * offset, 255, 0, 0, 255);
		maxHP = Geometry.createRect(offset, barWidth + offset, barLength + 2 * offset, barLength + 2 * offset, 150, 0, 0, 255);
		stats.addShape(mainRect);
		//stats.addShape(maxMN);
		stats.addShape(maxHP);
		stats.addShape(HP);
		//stats.addShape(MN);
		stats.setX(getX());
		stats.setY(getY() + getLength() / 2 + stats.getLength() / 2 + 5);
	}
}
