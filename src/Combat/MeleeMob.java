package Combat;



import Game.Main;
import LowLevel.Geometrical;
import LowLevel.Geometry;
import LowLevel.Image;

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
		Main.player.receiveHit(damage, Main.player.angleTo(this), attackStun, attackInvuln);
	}
	
	
	
	
	protected void pointToPlayer()
	{
		startingHorizontal = Math.random() < .5;
		movementPoint = Main.player;
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
}
