package Combat;



import Game.Main;
import Imported.Audio;
import Imported.Texture;
import LowLevel.Geometrical;
import LowLevel.Geometry;
import LowLevel.Image;
import LowLevel.Point;

public abstract class Mob extends CombatChar{
	
	private double verticalMove;
	private double horizontalMove;
	private int longStoppingFrame;
	private double attackRange;
	
	protected Geometrical stats;
	protected Image MN;
	protected Image maxMN;
	protected Image HP;
	protected Image maxHP;
	
	private boolean shouldCreate;
	private boolean dontFollow;
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
	protected double damage;
	protected double sightRange;
	
	public Mob(double x, double y, int ID)
	{
		super(null, x, y, 0, 0);
		
		

		switch (ID) {
		case skeleton:
			setWidth(50);
			setLength(50);
			anims = getAnims(skelAnimInd, skelAnimInd + 19);
			walkSounds = getSounds(skelSoundInd, skelSoundInd + 1);
			
			speed = 4;
			longStoppingFrame = 40;
			shortStoppingStart = longStoppingFrame - 5;
			
			damage = 10;
			attackRange = 80;
			sightRange = 600;
			attackEnd = 20;
			pauseEnd = attackEnd + 30;
			
			firstSound = 6;
			walkAnimSwitch = 6;
			soundFXSwitch = 20;
			
			hitBoxDown(20);
			setHitLength(10);
			break;

		case slime:
			setWidth(35);
			setLength(35);
			anims = getAnims(slimeAnimInd, slimeAnimInd + 19);
			walkSounds = getSounds(slimeSoundInd, slimeSoundInd + 9);
			
			speed = 6;
			longStoppingFrame = 40;
			shortStoppingStart = longStoppingFrame - 5;
			
			damage = 5;
			attackRange = 45;
			sightRange = 600;
			attackEnd = 15;
			pauseEnd = attackEnd + 30;
			
			firstSound = 6;
			walkAnimSwitch = 6;
			soundFXSwitch = 20;
			
			hitBoxDown(10);
			setHitLength(15);
			break;
		}

			
		
		
		int which = (int)(Math.random() * 4);
		walkDirec = which;
		switch (which)
		{
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
		attackFrame = pauseEnd;
		
		
	}
	public void setX(double newX)
	{
		double xDiff = newX - getX();
		stats.setX(stats.getX() + xDiff);
		super.setX(newX);
	}
	public void setY(double newY)
	{
		double yDiff = newY - getY();
		stats.setY(stats.getY() + yDiff);
		super.setY(newY);
	}
	
	public void show()
	{
		if (attackFrame >= attackEnd) {
			if (attackFrame == pauseEnd)
			{
				if (inAttackRange() && Main.player.canBeAttacked()) {attack();}
				else {move();}
			}
			else {attackFrame++;}
		}
		else {attackFrame++;}
		super.show();
		stats.show();
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
			if (seeingPlayer() && !dontFollow)
			{
				followingPlayer = true;
			}
			else {
				followingPlayer = false;
			}
			createMovementPoints();
			shouldCreate = false;
			dontFollow = false;
		}
		int direc = findDirection();
		if (direc == shouldStopWalk) {
			if (!followingPlayer) {stopWalk(longStop);}
			else {stopWalk(noStop);}
			return;
		}
		if (movement[direc] == false)
		{
			direc = shouldStopWalk;
			stopWalk(shortStop);
			if (followingPlayer) {dontFollow = true;}
			return;
		}
		if (walkDirec != direc) {walkAnim = resetWalk;}
		walkDirec = direc;
		
		super.move();
	}
	public boolean seeingPlayer()
	{
		double xDiff = Main.player.getX() - getX();
		double yDiff = Main.player.getY() - getY();
		return xDiff * xDiff + yDiff * yDiff <= sightRange * sightRange;
	}
	public void stopWalk(int durationModifier)
	{
		super.stopWalk();
		switch (durationModifier)
		{
			case noStop: stoppingFrame = longStoppingFrame; break;
			case shortStop: stoppingFrame = shortStoppingStart; break;
			case longStop: stoppingFrame = 0; break;
		}
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
	
	protected void createMovementPoint()
	{
		if (followingPlayer) {pointToPlayer();}
		else {pointRandomly();}
	}
	/**
	 * Sends the mob in a direction toward the player <br>
	 * Mob will start moving randomly if it collides with terrain <br>
	 * This seems like a smart way to allow players to get away from mobs<br>
	 * but really I just don't know how pathing algorithms work
	 */
	protected abstract void pointToPlayer();
	/**
	 * Sends the mob in a random direction with a random radius<br>
	 * radius is bounded to individual
	 */
	protected abstract void pointRandomly();
	protected abstract void attack();
	protected abstract void createStats();
	
	public void setHealth(double newHealth)
	{
		double HPVal = getHealth();
		double maxHPVal = getMaxHealth();
	    
		double HPfrac1 = HP.getWidth() / maxHP.getWidth();
		double HPfrac2 = Math.max(0, HPVal / maxHPVal);
		double HPxDiff = (HPfrac2 - HPfrac1) / 2 * maxHP.getWidth();
	    
		HP.setWidth(maxHP.getWidth() * HPfrac2);
		HP.setX(HP.getX() + HPxDiff);
	    super.setHealth(newHealth);
	}
	public void setMana(double newMana)
	{
		double MNVal = getMana();
		double maxMNVal = getMaxMana();
	    
		double MNfrac1 = MN.getWidth() / maxMN.getWidth();
		double MNfrac2 = Math.max(0, MNVal / maxMNVal);
		double MNxDiff = (MNfrac2 - MNfrac1) / 2 * maxMN.getWidth();
	    
		MN.setWidth(maxMN.getWidth() * MNfrac2);
		MN.setX(MN.getX() + MNxDiff);
	    super.setMana(newMana);
	}
	
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
