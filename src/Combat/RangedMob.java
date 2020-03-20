package Combat;

import java.util.Random;

import Game.Main;
import LowLevel.Geometrical;
import LowLevel.Geometry;
import LowLevel.Image;
import LowLevel.Point;
import World.Room;

public class RangedMob extends Mob{
	private int projectileID;
	public RangedMob(double x, double y, int ID)
	{
		super(x, y, ID);
		switch (ID) {
		case archer:
			projectileID = Projectile.arrow;
			//TODO: handle this
			break;
		}
	}
	protected void createStats()
	{
		double barWidth = 30;
		double barLength = 6;
		double offset = 2;
		stats = new Geometrical();
		
		
		Image mainRect = Geometry.createRect(0, barWidth + 2 * offset, barLength + offset, 2 * barLength + 3 * offset, 100, 100, 100, 255);
		
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
	protected void pointToPlayer()
	{
		Point testPoint = testPlayerPoint();
		int numTries = 0;
		Room currRoom = Main.allRooms[Main.currRoom];
		while (!currRoom.pathPossible(this, testPoint))
		{
			testPoint = testPlayerPoint();
			numTries++;
			if (numTries == 1000)
			{
				try {
					throw new Exception("RangedMob couldn't find PlayerPath");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		movementPoint = testPoint;
	}
	/**
	 * Generates an attempt point for the ranged mob to move to
	 * @return new point nearby player
	 */
	private Point testPlayerPoint()
	{
		Random random = Main.random;
		Point playerPoint = Main.player;
		double minRadius = 100;
		double maxRadius = 250;
		//mob will find an angle within a semicircle ranging from -90 degrees to 90 degrees
		double semiCircleAngle = (random.nextDouble() - .5) * Math.PI;
		//semicircle will be repositioned based on player's angle to the mob so that the mob always moves into the semicircle half facing it
		double angle = semiCircleAngle + Main.player.angleTo(this);
		double rangeSquared = Math.pow(maxRadius - minRadius, 2);
		double radius = Math.sqrt(rangeSquared * random.nextDouble()) + minRadius;
		Point testPoint = new Point(playerPoint.getX() + Math.cos(angle) * radius, playerPoint.getY() + Math.sin(angle) * radius);
		return testPoint;
	}
	protected void attack()
	{
		//TODO: implement this with projectiles
	}
}
