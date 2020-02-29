package Mobs;

import Game.Main;
import LowLevel.Point;
import World.Room;
import World.Terrain;

public class MeleeMob extends Mob{
	private double attackRangeSquared;
	public MeleeMob(double x, double y, int ID)
	{
		super(x, y, ID);
		switch (ID) {
			case skeleton: attackRangeSquared = 15 * 15; break;
			case slime: attackRangeSquared = 10 * 10; break;
		}
	}
	public void attack()
	{
		
	}
	public boolean inAttackRange()
	{
		//it's an optimization you fuck
		double xSquared = Main.player.getX() - getX();
		xSquared *= xSquared;
		double ySquared = Main.player.getY() - getY();
		ySquared *= ySquared;
		
		return xSquared + ySquared < attackRangeSquared;
	}
	public void createMovementPoint()
	{
		if (followingPlayer) {pointToPlayer();}
		else {pointRandomly();}
	}
	private void pointToPlayer()
	{
		//create point to player
	}
	/**
	 * Sends the mob in a random direction with a random radius<br>
	 * radius is bounded to individual
	 */
	private void pointRandomly()
	{
		Room currRoom = Main.allRooms[Main.currRoom];
		Terrain[] allTerrain = Main.allRooms[Main.currRoom].getTerrain();
		//keep this here for later
		Terrain currTerrain = null;
		for (int i = 0; i < allTerrain.length; i++)
		{
			if (allTerrain[i].collision(this))
			{
				currTerrain = allTerrain[i];
				break;
			}
		}
		//
		double maxRadius = 600;
		
		double radius = maxRadius * Math.random();
		double angle = 2 * Math.PI * Math.random();
		startingHorizontal = Math.random() < .5;
		Point testPoint = new Point(getX() + Math.cos(angle) * radius, getY() + Math.sin(angle) * radius);
		int numTries = 0;
		while (!currRoom.pathPossible(this, testPoint))
		{
			startingHorizontal = Math.random() < .5;
			radius = maxRadius * Math.random();
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
	public static double skeletonSpeed = 4;
	public static int skeletonDeathFrames;
	public static int skeletonStopFrames = 50;
	
	public static double slimeSpeed = 6;
	public static int slimeDeathFrames;
	public static int slimeStopFrames = 50;
	
}
