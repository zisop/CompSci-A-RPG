package Combat;

import Game.Main;
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
}
