package Mobs;

import Game.Main;
import Game.Player;
import LowLevel.Point;

public class MeleeMob extends Mob{
	public MeleeMob(double x, double y, int ID)
	{
		super(x, y, ID);
	}
	public void attack()
	{
		
	}
	public void createMovementPoint()
	{
		movementPoint = new Point(Main.cursor.getX() + Main.player.getX(), Main.cursor.getY() + Main.player.getY());
	}
	public static double skeletonSpeed = 4;
	public static int skeletonDeathFrames;
	public static int skeletonStopFrames = 50;
	
	public static double slimeSpeed = 6;
	public static int slimeDeathFrames;
	public static int slimeStopFrames = 50;
}
