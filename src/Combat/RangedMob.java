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
			if (++numTries == 1000)
			{
				try {
					throw new Exception("RangedMob couldn't find PlayerPath, tried: " + testPoint);
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(0);
				}
			}
			testPoint = testPlayerPoint();
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
		double minRadius = attackRange - 50;
		double maxRadius = attackRange;
		//mob will find an angle within a semicircle ranging from -45 degrees to 45 degrees
		double quarterCircleAngle = (random.nextDouble() - .5) * Math.PI / 2;
		//semicircle will be repositioned based on player's angle to the mob so that the mob always moves into the semicircle half facing it
		double angle = quarterCircleAngle + Math.toRadians(playerPoint.angleTo(this));
		double rangeSquared = Math.pow(maxRadius - minRadius, 2);
		double radius = Math.sqrt(rangeSquared * random.nextDouble()) + minRadius;
		Point testPoint = new Point(playerPoint.getX() + Math.cos(angle) * radius, playerPoint.getY() + Math.sin(angle) * radius);
		return testPoint;
	}
	protected void attack()
	{
		double radius = Math.max(getWidth(), getLength()) * 1.3;
		double angle = angleTo(Main.player);
		switch (walkDirec) {
			case up: if (angle < 45) {angle = 45;} else if (angle > 135) {angle = 135;} break;
			case right: if (angle <= 180 && angle > 45) {angle = 45;} else if (angle < 315 && angle > 180) {angle = 315;} break;
			case down: if (angle > 315) {angle = 315;} else if (angle < 225) {angle = 225;} break;
			case left: if (angle > 225) {angle = 225;} else if (angle < 135) {angle = 135;} break;
		}
		Projectile shot = new Projectile(projectileID, getX() + Math.cos(radius), getY() + Math.sin(radius), angle, damage, this);
		Main.allRooms[Main.currRoom].permaShow(shot);
	}
}
