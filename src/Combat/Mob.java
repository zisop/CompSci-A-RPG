package Combat;





import Game.Main;
import LowLevel.Geometrical;
import LowLevel.Image;
import LowLevel.Point;
import World.Room;

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
	protected int attackStun;
	protected int attackInvuln;
	
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
			attackRange = 60;
			sightRange = 600;
			attackEnd = 20;
			pauseEnd = attackEnd + 30;
			
			firstSound = 6;
			walkAnimSwitch = 6;
			soundFXSwitch = 25;
			walkVolume = .4;
			healthRegen = .05;
			maxHealth = 30;
			//one attack will affect the player for 40 frames
			attackStun = 16;
			attackInvuln = 48;
			
			setHitLength(10);
			setProjectileLength(50);
			setProjectileWidth(45);
			setHitWidth(35);
			hitBoxDown(20);
			
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
			healthRegen = .05;
			maxHealth = 20;
			//one attack will affect the player for 40 frames
			attackStun = 16;
			attackInvuln = 48;
			
			setProjectileLength(35);
			setProjectileWidth(35);
			setHitLength(15);
			hitBoxDown(10);
			
			break;
		}

			
		
		
		walkDirec = down;
		
		mobID = ID;
		followingPlayer = false;
		shouldCreate = true;
		stoppingFrame = longStoppingFrame;
		walkAnim = resetWalk;
		deathAnimFrame = 0;
		soundFXFrame = 0;
		walkFrame = 0;
		attackFrame = pauseEnd;
		createStats();
		health = maxHealth;
		setEnemyState(bad);
		handleMobException();
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
		if (!Main.alreadyInteracting)
		{
			updateMovement();
			if (attackFrame >= attackEnd) {
				if (attackFrame == pauseEnd)
				{
					if (inAttackRange() && Main.player.canBeAttacked()) {attack();}
					else {move();}
				}
				else {attackFrame++;}
			}
			else {attackFrame++;}
			setHealth(Math.min(getHealth() + healthRegen, maxHealth));
		}
		super.show();
		stats.show();
		if (shouldDie()) {die();}
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
	private void handleMobException()
	{
		handleCombatException();
		if (attackEnd == 0) { try { throw new Exception("attackEnd was 0 for Mob " + mobID);} 
		catch (Exception e) {e.printStackTrace(); System.exit(0);}}
		if (pauseEnd == 0) { try { throw new Exception("pauseEnd was 0 for Mob " + mobID);} 
		catch (Exception e) {e.printStackTrace(); System.exit(0);}}
		if (damage == 0) { try { throw new Exception("damage was 0 for Mob " + mobID);} 
		catch (Exception e) {e.printStackTrace(); System.exit(0);}}
		if (sightRange == 0) { try { throw new Exception("sightRange was 0 for Mob " + mobID);} 
		catch (Exception e) {e.printStackTrace(); System.exit(0);}}
	}
	public void die()
	{
		Room currRoom = Main.allRooms[Main.currRoom];
		currRoom.removeChar(this);
	}
	protected boolean shouldDie()
	{
		return health <= 0;
	}
	
	protected void createMovementPoint()
	{
		if (followingPlayer) {pointToPlayer();}
		else {pointRandomly();}
	}
	protected void facePlayer()
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
	protected void handleAttackAnims()
	{
		switch (walkDirec)
		{
			case up: setImage(anims[uA]); break;
			case right: setImage(anims[rA]); break;
			case down: setImage(anims[dA]); break;
			case left: setImage(anims[lA]); break;
		}
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
	protected void pointRandomly()
	{
		
		double maxRadius = sightRange;
		
		//Cubic graph results in a tendency to move far, rather than not far
		//there are exactly 0 words that mean not far you fuck
		double cube = Math.pow(maxRadius, 3);
		double radius = Math.pow(cube * Math.random(), 1/3.0);
		
		
		double angle = 2 * Math.PI * Math.random();
		startingHorizontal = Math.random() < .5;
		Point testPoint = new Point(getX() + Math.cos(angle) * radius, getY() + Math.sin(angle) * radius);
		int numTries = 0;
		Room currRoom = Main.allRooms[Main.currRoom];
		while (!currRoom.pathPossible(this, testPoint))
		{
			startingHorizontal = Math.random() < .5;
			radius = Math.pow(cube * Math.random(), 1/3.0);
			angle = 2 * Math.PI * Math.random();
			
			testPoint = new Point(getX() + Math.cos(angle) * radius, getY() + Math.sin(angle) * radius);
			numTries++;
			if (numTries == 1000)
			{
				try {
					throw new Exception("mob couldn't find path");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		movementPoint = testPoint;
	}
	/**
	 * makes the mob attack
	 */
	protected abstract void attack();
	/**
	 * creates the mob's stat display (health, mana)
	 */
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
	public final static int archer = 3;

	private static final int skelSoundInd = 0;
	private static final int skelAnimInd = 0;
	private static final int slimeAnimInd = 20;
	private static final int slimeSoundInd = 2;
	
	private static final int shouldStopWalk = -1;
	private static final int resetWalk = -1;
}
