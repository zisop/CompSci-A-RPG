package Combat;





import java.util.Random;

import Game.Chest;
import Game.Main;
import Imported.Audio;
import LowLevel.Geometrical;
import LowLevel.Image;
import LowLevel.Point;
import LowLevel.Shape;
import UI.Item;
import UI.ItemBag;
import World.Room;

public abstract class Mob extends CombatChar{
	
	private double verticalMove;
	private double horizontalMove;
	private int longStoppingFrame;
	protected double attackRange;
	private int deathFrames;
	private ItemDrop itemReward;
	private ItemBag deathBag;
	private String[] attackSounds;
	
	//mobs disappear after 12 seconds
	private static int maxDeathFrames = 12 * Main.FPS;
	
	protected Geometrical stats;
	protected Image MN;
	protected Image maxMN;
	protected Image HP;
	protected Image maxHP;
	
	private boolean shouldCreate;
	private boolean dontFollow;
	private int mobID;
	private double attackVolume;
	
	
	protected int shortStoppingStart;
	protected int stoppingFrame;
	protected boolean startingHorizontal;
	protected Point movementPoint;
	protected boolean followingPlayer;
	protected int attackFrame;
	protected int attackSwitchFrame;
	/**
	 * Frame at which character stops animation after attacking
	 */
	protected int attackEnd;
	/**
	 * Frame at which character starts moving after attacking
	 */
	protected int pauseEnd;
	/**
	 * Velocity at which it sends targets on hit
	 */
	protected double initialDamageVelocity;
	/**
	 * Damage on hit
	 */
	protected double damage;
	/**
	 * Range of seeing player
	 */
	protected double sightRange;
	/**
	 * XP rewarded on death
	 */
	protected double xpReward;
	/**
	 * Frames of stun when attack lands on other character
	 */
	protected int attackStun;
	/**
	 * Frames of invulnerability when attack lands on other character
	 */
	protected int attackInvuln;
	
	public Mob(double x, double y, int ID)
	{
		super(null, x, y, 0, 0);
		
		int pauseAfterAttack = 0;
		switch (ID) {
		case skeleton:
			setWidth(45);
			setLength(getWidth());
			walkAnimSwitch = 9;
			anims = getAnims(skelAnimInd, skelAnimInd + 11, walkAnimSwitch, this);
			walkSounds = getSounds(skelSoundInd, skelSoundInd + 1);
			
			speed = 4;
			longStoppingFrame = 20;
			shortStoppingStart = longStoppingFrame - 5;
			
			damage = 6;
			attackRange = 60;
			sightRange = 600;
			attackSwitchFrame = 5;
			pauseAfterAttack = 10;
			initialDamageVelocity = 5;
			armor = 1;
			
			firstSound = 6;
			
			soundFXSwitch = 25;
			walkVolume = .4;
			healthRegen = .05;
			maxHealth = 20;

			attackStun = 16;
			attackInvuln = 48;
			xpReward = 10;
			
			setHitLength(getWidth() * 2 / 5);
			setProjectileLength(getWidth());
			setProjectileWidth(getWidth() * 9 / 10);
			setHitWidth(getWidth() * 7 / 10);
			hitBoxDown(getWidth() * 3 / 10);
			
			attackSounds = getSounds(skeletonAttackInd, skeletonAttackInd + 2);
			attackVolume = .5;
			
			break;

		case slime:
			setWidth(40);
			setLength(getWidth());
			walkAnimSwitch = 6;
			anims = getAnims(slimeAnimInd, slimeAnimInd + 11, walkAnimSwitch, this);
			walkSounds = getSounds(slimeSoundInd, slimeSoundInd + 9);
			
			speed = 6;
			
			damage = 4;
			attackRange = 45;
			sightRange = 600;
			attackSwitchFrame = 5;
			pauseAfterAttack = 15;
			armor = .5;
			
			initialDamageVelocity = 5;
			firstSound = 6;
			soundFXSwitch = 20;
			healthRegen = .05;
			maxHealth = 15;
			attackStun = 16;
			attackInvuln = 48;
			xpReward = 8;
			
			setProjectileLength(getWidth());
			setProjectileWidth(getWidth());
			setHitLength(getWidth() * 5 / 7);
			hitBoxDown(getWidth() / 7);
			
			attackSounds = getSounds(slimeAttackInd, slimeAttackInd);
			attackVolume = .5;
			
			break;
		case duck:
			setWidth(45);
			setLength(getWidth() * 10 / 7);
			walkAnimSwitch = 7;
			anims = getAnims(duckAnimInd, duckAnimInd + 11, walkAnimSwitch, this);
			walkSounds = getSounds(duckSoundInd, duckSoundInd);
			
			speed = 6;
			longStoppingFrame = 20;
			shortStoppingStart = longStoppingFrame - 5;
			
			damage = 7;
			attackRange = 40;
			sightRange = 600;
			attackSwitchFrame = 5;
			pauseAfterAttack = 15;
			armor = 2;
			
			initialDamageVelocity = 7;
			firstSound = 6;
			soundFXSwitch = 20;
			healthRegen = .1;
			maxHealth = 30;
			attackStun = 20;
			attackInvuln = 40;
			xpReward = 12;
			
			setProjectileWidth(getWidth());
			setProjectileLength(getLength());
			setHitWidth(getWidth() * 4 / 5);
			setHitLength(getWidth() / 2);
			hitBoxDown(getWidth() * 3 / 10);
			walkVolume = .2;
			
			attackSounds = getAttackSounds(duckAttackInd, duckAttackInd);
			attackVolume = .1;
			break;
		case archer:
			setWidth(60);
			setLength(getWidth());
			walkAnimSwitch = 9;
			anims = getAnims(archerAnimInd, archerAnimInd + 11, walkAnimSwitch, this);
			walkSounds = getSounds(duckSoundInd, duckSoundInd);
			
			speed = 6;
			damage = 11;
			attackRange = 200;
			sightRange = 600;
			attackSwitchFrame = 2;
			pauseAfterAttack = 20;
			armor = .5;
			
			initialDamageVelocity = 7;
			firstSound = 6;
			soundFXSwitch = 20;
			healthRegen = .1;
			maxHealth = 20;
			attackStun = 20;
			attackInvuln = 40;
			xpReward = 15;
			
			setProjectileWidth(getWidth() * 3 / 5);
			setProjectileLength(getLength() * 4 / 5);
			setHitWidth(getWidth() * 2 / 3);
			setHitLength(getWidth() * 5 / 12);
			hitBoxDown(getWidth() / 5);
			walkVolume = .2;
			
			attackSounds = getAttackSounds(archerAttackInd, archerAttackInd + 1);
			attackVolume = .3;
			break;
		case knight:
			setWidth(60);
			setLength(getWidth());
			walkAnimSwitch = 4;
			anims = getAnims(knightAnimInd, knightAnimInd + 11, walkAnimSwitch, this);
			walkSounds = getSounds(duckSoundInd, duckSoundInd);
			
			speed = 7;
			damage = 9;
			attackRange = 50;
			sightRange = 400;
			attackSwitchFrame = 1;
			pauseAfterAttack = 15;
			armor = 3;
			
			initialDamageVelocity = 9;
			firstSound = 6;
			soundFXSwitch = 20;
			healthRegen = .05;
			maxHealth = 40;
			attackStun = 20;
			attackInvuln = 40;
			xpReward = 20;
			
			setProjectileWidth(getWidth() * 3 / 5);
			setProjectileLength(getLength() * 4 / 5);
			setHitWidth(getWidth() * 2 / 3);
			setHitLength(getWidth() * 5 / 12);
			hitBoxDown(getWidth() / 5);
			walkVolume = .2;
			
			attackSounds = getAttackSounds(archerAttackInd, archerAttackInd + 1);
			attackVolume = .3;
			break;
		}

			
		
		attackEnd = attackSwitchFrame * anims[uA].length;
		pauseEnd = attackEnd + pauseAfterAttack;
		mobID = ID;
		followingPlayer = false;
		shouldCreate = true;
		stoppingFrame = longStoppingFrame;
		
		
		attackFrame = pauseEnd;
		createStats();
		health = maxHealth;
		setEnemyState(bad);
		createDrops();
		handleMobException();
		setAnim(dI);
	}
	public void playAttackSound()
	{
		int which = Main.random.nextInt(attackSounds.length);
		Audio.playSound(attackSounds[which], attackVolume);
	}
	public void showMovementPoint()
	{
		if (movementPoint != null) {new Shape(Shape.square, movementPoint.getX(), movementPoint.getY(), 10, 10, 255, 0, 0, 100).show();}
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
		if (!isDead)
		{
			if (!Main.alreadyInteracting)
			{
			
				updateMovement();
				if (attackFrame >= attackEnd) {
					
					if (attackFrame == pauseEnd)
					{
						if (inAttackRange() && Main.player.canBeAttacked()) {handleAttackAnims();}
						else {move();}
					}
					else {attackFrame++;}
				}
				else if (++attackFrame == attackEnd) {
					attack();
					playAttackSound();
					switch (walkDirec) {
						case up:
							setAnim(uI);
							break;
						case right:
							setAnim(rI);
							break;
						case down:
							setAnim(dI);
							break;
						case left:
							setAnim(lI);
							break;
					}
				}
				setHealth(getHealth() + healthRegen);
			
			
			}
			if (shouldDie()) {die();}
		}
		else
		{
			if (deathFrames == maxDeathFrames)
			{
				Room currRoom = Main.allRooms[Main.currRoom];
				currRoom.removeChar(this);
				deathBag.setVisibility(false);
				if (Main.interactingChar == this)
				{
					Main.interactingChar = null;
					Main.alreadyInteracting = false;
				}
			}
			else 
			{
				setAlpha(255 * (1 - (deathFrames / (float)maxDeathFrames)));
				handleDeathBag();
				if (!deathBag.getVisibility()) {deathFrames++;}
			}
		}
		super.show();
		if (!isDead) {stats.show();}
	}
	
	private void createDrops()
	{
		int[] IDs = null;
		double[] probabilities = null;
		int[] quantities = null;
		int numRolls = 0;
		switch (mobID) {
			case skeleton:
				numRolls = 4;
				IDs = new int[] {Item.ruby, Item.sapphire};
				probabilities = new double[] {.2, .2};
				quantities = new int[] {3, 3};
				break;
			case slime:
				numRolls = 3;
				IDs = new int[] {Item.sapphire};
				probabilities = new double[] {.3};
				quantities = new int[] {2};
				break;
			case duck:
				numRolls = 3;
				IDs = new int[] {Item.ruby, Item.gold, Item.amethyst};
				probabilities = new double[] {.3, .1, .2};
				quantities = new int[] {4, 2, 2};
				break;
			case archer:
				numRolls = 4;
				IDs = new int[] {Item.ruby, Item.sapphire};
				probabilities = new double[] {.2, .2};
				quantities = new int[] {3, 3};
				break;
			case knight:
				numRolls = 4;
				IDs = new int[] {Item.ruby, Item.sapphire};
				probabilities = new double[] {.2, .2};
				quantities = new int[] {3, 3};
				break;
			default:
				try {
					throw new Exception("Mob of ID: " + mobID + " didn't have drops initialized");
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(0);
				}
		}
		itemReward = new ItemDrop(IDs, probabilities, quantities, numRolls);
	}
	private void handleDeathBag()
	{
		
		if (Main.xInteraction(this) || Main.clickInteraction(this))
		{
			if (deathBag.getVisibility())
			{
				Main.interactingChar = null;
				Main.alreadyInteracting = false;
			}
			else 
			{
				Main.interactingChar = this;
				Main.alreadyInteracting = true;
			}
			deathBag.setVisibility(!deathBag.getVisibility());
		}
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
			stopWalk(shortStop);
			if (followingPlayer) {dontFollow = true;}
			return;
		}
		walkInDirec(direc);
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
		if (attackEnd == 0) { try { throw new Exception("attackEnd was 0 for Mob: " + mobID);} 
		catch (Exception e) {e.printStackTrace(); System.exit(0);}}
		if (pauseEnd == 0) { try { throw new Exception("pauseEnd was 0 for Mob: " + mobID);} 
		catch (Exception e) {e.printStackTrace(); System.exit(0);}}
		if (damage == 0) { try { throw new Exception("damage was 0 for Mob: " + mobID);} 
		catch (Exception e) {e.printStackTrace(); System.exit(0);}}
		if (sightRange == 0) { try { throw new Exception("sightRange was 0 for Mob: " + mobID);} 
		catch (Exception e) {e.printStackTrace(); System.exit(0);}}
		if (initialDamageVelocity == 0) { try { throw new Exception("damageVelocity was 0 for Mob: " + mobID);} 
		catch (Exception e) {e.printStackTrace(); System.exit(0);}}
		if (xpReward == 0) { try { throw new Exception("xpReward was 0 for Mob: " + mobID);} 
		catch (Exception e) {e.printStackTrace(); System.exit(0);}}
		if (attackSounds.length == 0) { try { throw new Exception("No sound effects for Mob: " + mobID);} 
		catch (Exception e) {e.printStackTrace(); System.exit(0);}}
		if (attackVolume == 0) { try { throw new Exception("AttackVolume was 0 for Mob: " + mobID);} 
		catch (Exception e) {e.printStackTrace(); System.exit(0);}}
	}
	private void die()
	{
		isDead = true;
		Main.player.gainXP(xpReward);
		deathBag = itemReward.generateItems();
	}
	protected boolean shouldDie() {return health <= 0;}
	
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
		attackFrame = 0;
		facePlayer();
		switch (walkDirec)
		{
			case up: setAnim(uA); break;
			case right: setAnim(rA); break;
			case down: setAnim(dA); break;
			case left: setAnim(lA); break;
		}
		currAnim.setSwitch(attackSwitchFrame);
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
		
		Random random = Main.random;
		//Cubic graph results in a tendency to move far, rather than not far
		//there are exactly 0 words that mean not far you fuck
		double cube = Math.pow(maxRadius, 3);
		double radius = Math.pow(random.nextDouble() * cube, 1/3.0);
		
		
		double angle = 2 * Math.PI * random.nextDouble();
		startingHorizontal = random.nextBoolean();
		Point testPoint = new Point(getX() + Math.cos(angle) * radius, getY() + Math.sin(angle) * radius);
		int numTries = 0;
		Room currRoom = Main.allRooms[Main.currRoom];
		while (!currRoom.pathPossible(this, testPoint))
		{
			startingHorizontal = random.nextBoolean();
			radius = Math.pow(cube * random.nextDouble(), 1/3.0);
			angle = 2 * Math.PI * random.nextDouble();
			
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
	private static String[] mobAttackSounds;
	private static String[] getAttackSounds(int start, int end)
	{
		String[] sounds = new String[end - start +  1];
		for (int i = 0; i <= end - start; i++)
		{
			sounds[i] = mobAttackSounds[start + i];
		}
		return sounds;
	}
	private static final int duckAttackInd = 0;
	private static final int archerAttackInd = duckAttackInd + 1;
	private static final int skeletonAttackInd = archerAttackInd + 2;
	private static final int slimeAttackInd = skeletonAttackInd + 3;
	protected static void initAttackSounds()
	{
		mobAttackSounds = new String[7];
		mobAttackSounds[duckAttackInd + 0] = "Batt/headbutt";
		mobAttackSounds[archerAttackInd + 0] = "Batt/arrow0";
		mobAttackSounds[archerAttackInd + 1] = "Batt/arrow1";
		mobAttackSounds[skeletonAttackInd + 0] = "Batt/swing";
		mobAttackSounds[skeletonAttackInd + 1] = "Batt/swing2";
		mobAttackSounds[skeletonAttackInd + 2] = "Batt/swing3";
		mobAttackSounds[slimeAttackInd] = "NPC/Slime/slime4";
	}
	
	public final static int skeleton = 0;
	public final static int slime = 1;
	public final static int duck = 2;
	public final static int archer = 3;
	public static final int knight = 4;

	private static final int skelSoundInd = 0;
	private static final int skelAnimInd = 0;
	private static final int slimeSoundInd = 2;
	
	private static final int shouldStopWalk = -1;
	private static final int resetWalk = -1;
}
