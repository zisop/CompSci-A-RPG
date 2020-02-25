package Mobs;



import Game.Movable;
import Imported.Texture;
import LowLevel.Geometry;
import LowLevel.Point;

public abstract class Mob extends Movable{
	
	protected int mobID;
	protected Point movementPoint;
	protected double verticalMove;
	protected double horizontalMove;
	protected boolean startingHorizontal;
	protected int stoppingFrame;
	protected int maxStoppingFrame;
	protected int deathAnimFrame;
	protected int maxDeathAnimFrame;
	protected boolean followingPlayer;
	
	public Mob(double x, double y, int ID)
	{
		super(null, x, y, 0, 0);
		if (ID == skeleton)
		{
			setWidth(50);
			setLength(50);
			setImage(mobTex[skeletonDown]);
			setSpeed(MeleeMob.skeletonSpeed);
			maxStoppingFrame = MeleeMob.skeletonStopFrames;
			stoppingFrame = maxStoppingFrame;
		}
	}
	
	public abstract void attack();
	public void createMovementPoints()
	{
		createMovementPoint();
		stoppingFrame = 0;
		startingHorizontal = Math.random() < .5;
		createDistances();
	}
	public abstract void createMovementPoint();
	public void createDistances()
	{
		double xDist = movementPoint.getX() - getX();
		double yDist = movementPoint.getY() - getY();
		horizontalMove = xDist;
		verticalMove = yDist;
	}
	public void move()
	{
		if (stoppingFrame != maxStoppingFrame) 
		{
			stoppingFrame++;
			return;
		}
		boolean atHoriz = atHorizontal();
		boolean atVert = atVertical();
		if (startingHorizontal)
		{
			if (!atHoriz)
			{
				if (horizontalMove < getX())
				{
					super.move(3);
				}
				else 
				{
					super.move(1);
				}
			}
			else if (!atVert)
			{
				
			}
		}
	}
	public boolean atHorizontal()
	{
		double absDist = Math.abs(getX() - horizontalMove);
		return absDist <= getSpeed();
	}
	public boolean atVertical()
	{
		double absDist = Math.abs(getY() - verticalMove);
		return absDist <= getSpeed();
	}
	
	public static Texture[] mobTex;
	public static int skeleton = 0;
	public static void initTex()
	{
		mobTex = new Texture[1];
		mobTex[0] = new Texture("Mobs/Skeleton/IdleDown.png");
	}
	static int skeletonInd = 0;
	public static int skeletonDown = skeletonInd + 0;
}
