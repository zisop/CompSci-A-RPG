package Combat;



import Game.Main;
import Imported.Audio;
import Imported.Texture;
import LowLevel.Image;
import LowLevel.Point;

public abstract class Mob extends Movable{
	
	private double verticalMove;
	private double horizontalMove;
	private int longStoppingFrame;
	private double attackRange;
	private boolean shouldCreate;
	private int mobID;
	private int deathAnimFrame;
	private int deathAnimMaxFrame;
	
	protected int shortStoppingStart;
	protected int stoppingFrame;
	protected boolean startingHorizontal;
	protected Point movementPoint;
	protected boolean followingPlayer;
	protected int attackFrame;
	protected int attackEnd;
	protected int pauseEnd;
	protected double sightRange;
	
	public Mob(double x, double y, int ID)
	{
		super(null, x, y, 0, 0);

		switch (ID) {
		case skeleton:
			setWidth(50);
			setLength(50);
			speed = MeleeMob.skeletonSpeed;
			longStoppingFrame = MeleeMob.skeletonLongStop;
			shortStoppingStart = longStoppingFrame - MeleeMob.skeletonShortStop;
			attackEnd = 20;
			pauseEnd = attackEnd + 60;
			anims = getAnims(skelAnimInd, skelAnimInd + 19);
			
			walkSounds = getSounds(skelSoundInd, skelSoundInd + 1);
			soundFXSwitch = 20;
			firstSound = 6;
			walkAnimSwitch = 6;
			hitBoxDown(20);
			setHitLength(10);
			attackRange = 120;
			sightRange = 600;
			break;

		case slime:
			setWidth(35);
			setLength(35);
			speed = MeleeMob.slimeSpeed;
			longStoppingFrame = MeleeMob.slimeLongStop;
			shortStoppingStart = longStoppingFrame - MeleeMob.slimeShortStop;
			anims = getAnims(slimeAnimInd, slimeAnimInd + 19);
			walkSounds = getSounds(slimeSoundInd, slimeSoundInd + 9);
			soundFXSwitch = 20;
			firstSound = 6;
			walkAnimSwitch = 6;
			hitBoxDown(10);
			setHitLength(15);
			attackRange = 120;
			sightRange = 600;
			attackEnd = 15;
			pauseEnd = attackEnd + 60;
			break;
		}

			
		
		
		int which = (int)(Math.random() * 4);
		walkDirec = which;
		switch (which) {
		case up: setImage(anims[uI]); break;
		case right: setImage(anims[rI]); break;
		case down: setImage(anims[dI]); break;
		case left: setImage(anims[lI]); break;
		}
		
		mobID = ID;
		followingPlayer = false;
		shouldCreate = true;
		stoppingFrame = longStoppingFrame;
		walkAnim = resetWalk;
		deathAnimFrame = 0;
		soundFXFrame = 0;
		walkFrame = 0;
		attackFrame = attackEnd;
	}
	
	public void show()
	{
		
		if (attackFrame == attackEnd) {
			if (inAttackRange()) {attack();}
			else {move();}
		}
		else {attackFrame++;}
		super.show();
	}
	public boolean inAttackRange()
	{
		//it's an optimization you fuck
		double xSquared = Main.player.getX() - getX();
		xSquared *= xSquared;
		double ySquared = Main.player.getY() - getY();
		ySquared *= ySquared;
		return xSquared + ySquared < attackRange * attackRange;
	}
	/**
	 * moves the mob towards its movement points<br>
	 * or doesn't move it if it's in a state of pausing<br>
	 */
	public void move()
	{
		if (stoppingFrame != longStoppingFrame) 
		{
			stoppingFrame++;
			return;
		}
		if (shouldCreate)
		{
			createMovementPoints();
			shouldCreate = false;
		}
		int direc = findDirection();
		if (direc == shouldStopWalk) {stopWalk(longStop); return;}
		boolean[] movement = getMovement();
		if (movement[direc] == false)
		{
			direc = shouldStopWalk;
			stopWalk(shortStop);
			return;
		}
		if (walkDirec != direc) {walkAnim = resetWalk;}
		walkDirec = direc;
		
		super.move();
		
	}
	public void stopWalk(int durationModifier)
	{
		super.stopWalk();
		if (durationModifier == shortStop) {stoppingFrame = shortStoppingStart;}
		else {stoppingFrame = 0;}
		shouldCreate = true;
	}
	/**
	 * determines what direction mob should move after movement point generated<br>
	 * recommended to use Mob.(up, right, down, left) for each int direction<br>
	 * mob.up == 0
	 * @return {0 = north, 1 = east, 2 = south, 3 = west}
	 */
	private int findDirection()
	{
		boolean atHoriz = atHorizontal();
		boolean atVert = atVertical();
		//Decide which direction to move
		if (startingHorizontal)
		{
			if (!atHoriz)
			{
				if (horizontalMove < getX()) {return left;}
				else {return right;}
			}
			else if (!atVert)
			{
				if (verticalMove < getY()) {return down;}
				else {return up;}
			}
			else {return shouldStopWalk;}
		}
		else 
		{
			if (!atVert)
			{
				if (verticalMove < getY()) {return down;}
				else {return up;}
			}
			else if (!atHoriz)
			{
				if (horizontalMove < getX()) {return left;}
				else {return right;}
			}
			else {return shouldStopWalk;}
		}
	}
	private void createMovementPoints()
	{
		createMovementPoint();
		createDistances();
	}
	public boolean startingHorizontal()
	{
		return startingHorizontal;
	}
	
	private void createDistances()
	{
		horizontalMove = movementPoint.getX();
		verticalMove = movementPoint.getY();
	}
	private boolean atHorizontal()
	{
		double absDist = Math.abs(getX() - horizontalMove);
		return absDist <= speed;
	}
	private boolean atVertical()
	{
		double absDist = Math.abs(getY() - verticalMove);
		return absDist <= speed;
	}
	
	public abstract void createMovementPoint();
	public abstract void attack();
	public final static int skeleton = 0;
	public final static int slime = 1;
	public final static int zombie = 2;

	private static final int skelSoundInd = 0;
	private static final int skelAnimInd = 0;
	private static final int slimeAnimInd = 20;
	private static final int slimeSoundInd = 2;
	
	private static final int shouldStopWalk = -1;
	private static final int resetWalk = -1;
}
