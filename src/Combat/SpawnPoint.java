package Combat;


import java.util.Random;

import Game.Main;
import LowLevel.Image;
import World.Room;

public class SpawnPoint extends Image {
	private static double width = 400;
	private static double length = 400;
	private int framesTillSpawn;
	private int[] mobGens;
	public SpawnPoint(double x, double y, int[] mobIDs)
	{
		super(null, x, y, 0, 0);
		framesTillSpawn = 0;
		mobGens = mobIDs;
	}
	public void show()
	{
		if (framesTillSpawn-- == 0)
		{
			int fps = Main.FPS;
			Random random = Main.random;
			int secondsPerMob = 15;
			int framesPerMob = secondsPerMob * fps;
			//range = * .5 -> * 2
			boolean divide = random.nextBoolean();
			if (divide) {framesTillSpawn = (int)(framesPerMob / 1.3);}
			else {framesTillSpawn = (int)(framesPerMob * 1.3);}
			generateMob();
		}
	}
	private void generateMob()
	{
		Room currRoom = Main.allRooms[Main.currRoom];
		double x = getX() + Main.random.nextDouble() * width;
		double y = getY() + Main.random.nextDouble() * length;
		int ID = mobGens[Main.random.nextInt(mobGens.length)];
		Mob mob = null;
		switch (ID) {
			case MeleeMob.slime:
			case MeleeMob.skeleton:
			case MeleeMob.zombie:
				mob = new MeleeMob(0, 0, ID);
				break;
			case MeleeMob.archer:
				mob = new RangedMob(0, 0, ID);
				break;

			default:
				try {
					throw new Exception("SpawnPoint tried to generate a mob that didnt exist (ID: " + ID + ")");
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(0);
				}
		}
		mob.setPos(x, y);
		int numTries = 1;
		while (!(currRoom.collision(mob) && !currRoom.strictCollision(mob)))
		{
			if (numTries++ == 1000)
			{
				try {
					throw new Exception("SpawnPoint failed to generate a mob in time");
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(0);
				}
			}
			x = getX() + Main.random.nextDouble() * width;
			y = getY() + Main.random.nextDouble() * length;
			mob.setPos(x, y);
		}
		currRoom.addChar(mob);
	}
}
