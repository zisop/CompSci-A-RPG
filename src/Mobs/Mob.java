package Mobs;



import Game.Main;
import Game.Movable;
import Imported.Audio;
import Imported.Texture;
import LowLevel.Image;
import LowLevel.Point;

public abstract class Mob extends Movable{
	
	protected Texture[] anims;
	private String[] walkSounds;
	
	
	private int mobID;
	private double verticalMove;
	private double horizontalMove;
	private int stoppingFrame;
	private int longStoppingFrame;
	private int shortStoppingStart;
	private int deathAnimFrame;
	private int maxDeathAnimFrame;
	private int walkAnim;
	private int walkFrame;
	private int walkAnimSwitch;
	private int soundFXFrame;
	private int soundFXSwitch;
	private int firstSound;
	private double attackRange;
	private boolean shouldCreate;
	
	protected int walkDirec;
	protected boolean startingHorizontal;
	protected Point movementPoint;
	protected boolean followingPlayer;
	protected int attackFrame;
	protected int attackMaxFrame;
	protected double sightRange;
	
	public Mob(double x, double y, int ID)
	{
		super(null, x, y, 0, 0);

		switch (ID) {
		case skeleton:
			setWidth(50);
			setLength(50);
			setSpeed(MeleeMob.skeletonSpeed);
			longStoppingFrame = MeleeMob.skeletonLongStop;
			shortStoppingStart = longStoppingFrame - MeleeMob.skeletonShortStop;
			attackMaxFrame = 20;
			anims = getAnims(skelAnimInd, skelAnimInd + 19);
			
			walkSounds = getSounds(skelSoundInd, skelSoundInd + 1);
			soundFXSwitch = 20;
			firstSound = 6;
			walkAnimSwitch = 6;
			hitBoxDown(20);
			setHitLength(10);
			attackRange = 70;
			sightRange = 600;
			break;

		case slime:
			setWidth(35);
			setLength(35);
			setSpeed(MeleeMob.slimeSpeed);
			longStoppingFrame = MeleeMob.slimeLongStop;
			shortStoppingStart = longStoppingFrame - MeleeMob.slimeShortStop;
			anims = getAnims(slimeAnimInd, slimeAnimInd + 19);
			walkSounds = getSounds(slimeSoundInd, slimeSoundInd + 9);
			soundFXSwitch = 20;
			firstSound = 6;
			walkAnimSwitch = 6;
			hitBoxDown(10);
			setHitLength(15);
			attackRange = 200;
			sightRange = 600;
			attackMaxFrame = 15;
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
		attackFrame = attackMaxFrame;
	}
	
	public void show()
	{
		if (attackFrame == attackMaxFrame) {
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
		
		super.move(direc);
		if (walkDirec != direc) {walkAnim = resetWalk;}
		
		walkDirec = direc;
		handleAnims();
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
		return absDist <= getSpeed();
	}
	private boolean atVertical()
	{
		double absDist = Math.abs(getY() - verticalMove);
		return absDist <= getSpeed();
	}
	/**
	 * handles walk animations
	 */
	private void handleAnims()
	{
		walkFrame++;
		if (walkAnim == resetWalk) {walkAnim = startWalking; walkFrame = walkAnimSwitch;}
		if (walkFrame == walkAnimSwitch)
		{
			walkAnim++;
			walkFrame = 0;
			switch (walkDirec)
			{
				case up:
					switch (walkAnim)
					{
						case 1: setImage(anims[uW0]); break;
						case 2: setImage(anims[uW1]); break;
						case 3: setImage(anims[uW2]); break;
						case 4: setImage(anims[uW1]); break;
						case 5: walkAnim = 1; setImage(anims[uW0]); break;
					}
					break;
				case right:
					switch (walkAnim)
					{
						case 1: setImage(anims[rW0]); break;
						case 2: setImage(anims[rW1]); break;
						case 3: setImage(anims[rW2]); break;
						case 4: setImage(anims[rW1]); break;
						case 5: walkAnim = 1; setImage(anims[rW0]); break;
					}
					break;
				case down:
					switch (walkAnim)
					{
						case 1: setImage(anims[dW0]); break;
						case 2: setImage(anims[dW1]); break;
						case 3: setImage(anims[dW2]); break;
						case 4: setImage(anims[dW1]); break;
						case 5: walkAnim = 1; setImage(anims[dW0]); break;
					}
				break;
				case left: 
					switch (walkAnim)
					{
						case 1: setImage(anims[lW0]); break;
						case 2: setImage(anims[lW1]); break;
						case 3: setImage(anims[lW2]); break;
						case 4: setImage(anims[lW1]); break;
						case 5: walkAnim = 1; setImage(anims[lW0]); break;
					}
				break;
			}
		}
		if (soundFXFrame == soundFXSwitch || soundFXFrame == firstSound)
		{
			playWalkSound();
			soundFXFrame = firstSound + 1;
		}
		else {soundFXFrame++;}
	}
	private void stopWalk(int durationModifier)
	{
		walkAnim = resetWalk;
		walkFrame = 0;
		soundFXFrame = 0;
		if (durationModifier == shortStop) {stoppingFrame = shortStoppingStart;}
		else {stoppingFrame = 0;}
		shouldCreate = true;
		switch (walkDirec)
		{
			case up: setImage(anims[uI]); break;
			case right: setImage(anims[rI]); break;
			case down: setImage(anims[dI]); break;
			case left: setImage(anims[lI]); break;
		}
	}
	private Audio playWalkSound()
	{
		int which = (int)(Math.random() * walkSounds.length);
		return Audio.playSound(walkSounds[which], .6);
	}
	
	public abstract void createMovementPoint();
	public abstract void attack();
	
	public static final int up = 0;
	public static final int right = 1;
	public static final int down = 2;
	public static final int left = 3;
	
	//uW0 = up walk 0
	//uI = up idle
	//uA = up attack
	public static final int uW0 = 0;
	public static final int uW1 = 1;
	public static final int uW2 = 2;
	public static final int uI = 3;
	public static final int uA = 4;
	
	public static final int rW0 = 5;
	public static final int rW1 = 6;
	public static final int rW2 = 7;
	public static final int rI = 8;
	public static final int rA = 9;
	
	public static final int dW0 = 10;
	public static final int dW1 = 11;
	public static final int dW2 = 12;
	public static final int dI = 13;
	public static final int dA = 14;
	
	public static final int lW0 = 15;
	public static final int lW1 = 16;
	public static final int lW2 = 17;
	public static final int lI = 18;
	public static final int lA = 19;
	
	private static Texture[] getAnims(int start, int end)
	{
		Texture[] anims = new Texture[end - start + 1];
		for (int i = 0; i < anims.length; i++)
		{
			anims[i] = mobTex[start + i];
		}
		return anims;
	}
	private static String[] getSounds(int start, int end)
	{
		String[] sounds = new String[end - start + 1];
		for (int i = 0; i < sounds.length; i++)
		{
			sounds[i] = mobSounds[i + start]; 
		}
		return sounds;
	}
	
	private static String[] mobSounds;
	private static Texture[] mobTex;
	public final static int skeleton = 0;
	public final static int slime = 1;
	public final static int zombie = 2;
	public static void init()
	{
		mobSounds = new String[12];
		mobSounds[skelSoundInd + 0] = "Misc/random2";
		mobSounds[skelSoundInd + 1] = "Misc/random3";
		mobSounds[slimeSoundInd + 0] = "NPC/Slime/slime1";
		mobSounds[slimeSoundInd + 1] = "NPC/Slime/slime2";
		mobSounds[slimeSoundInd + 2] = "NPC/Slime/slime3";
		mobSounds[slimeSoundInd + 3] = "NPC/Slime/slime4";
		mobSounds[slimeSoundInd + 4] = "NPC/Slime/slime5";
		mobSounds[slimeSoundInd + 5] = "NPC/Slime/slime6";
		mobSounds[slimeSoundInd + 6] = "NPC/Slime/slime7";
		mobSounds[slimeSoundInd + 7] = "NPC/Slime/slime8";
		mobSounds[slimeSoundInd + 8] = "NPC/Slime/slime9";
		mobSounds[slimeSoundInd + 9] = "NPC/Slime/slime10";
		
		
		mobTex = new Texture[40];
		mobTex[skelAnimInd + uW0] = new Texture("Mobs/Skeleton/IdleDown.png");
		for (int i = 1; i < 20; i++)
		{
			mobTex[i] = mobTex[0]; 
		}
		mobTex[slimeAnimInd + uW0] = new Texture("Mobs/Slime/IdleUp.png");
		mobTex[slimeAnimInd + uW1] = mobTex[slimeAnimInd + uW0];
		mobTex[slimeAnimInd + uW2] = mobTex[slimeAnimInd + uW0];
		mobTex[slimeAnimInd + uI] = mobTex[slimeAnimInd + uW0];
		mobTex[slimeAnimInd + uA] = mobTex[slimeAnimInd + uW0];
		mobTex[slimeAnimInd + rW0] = new Texture("Mobs/Slime/IdleRight.png");
		mobTex[slimeAnimInd + rW1] = mobTex[slimeAnimInd + rW0];
		mobTex[slimeAnimInd + rW2] = mobTex[slimeAnimInd + rW0];
		mobTex[slimeAnimInd + rI] = mobTex[slimeAnimInd + rW0];
		mobTex[slimeAnimInd + rA] = mobTex[slimeAnimInd + rW0];
		mobTex[slimeAnimInd + dW0] = new Texture("Mobs/Slime/IdleDown.png");
		mobTex[slimeAnimInd + dW1] = mobTex[slimeAnimInd + dW0];
		mobTex[slimeAnimInd + dW2] = mobTex[slimeAnimInd + dW0];
		mobTex[slimeAnimInd + dI] = mobTex[slimeAnimInd + dW0];
		mobTex[slimeAnimInd + dA] = mobTex[slimeAnimInd + dW0];
		mobTex[slimeAnimInd + lW0] = new Texture("Mobs/Slime/IdleLeft.png");
		mobTex[slimeAnimInd + lW1] = mobTex[slimeAnimInd + lW0];
		mobTex[slimeAnimInd + lW2] = mobTex[slimeAnimInd + lW0];
		mobTex[slimeAnimInd + lI] = mobTex[slimeAnimInd + lW0];
		mobTex[slimeAnimInd + lA] = mobTex[slimeAnimInd + lW0];
	}
	private static final int skelSoundInd = 0;
	private static final int skelAnimInd = 0;
	private static final int slimeAnimInd = 20;
	private static final int slimeSoundInd = 2;
	
	private static final int shouldStopWalk = -1;
	private static final int resetWalk = -1;
	private static final int startWalking = 0;
	private static final int shortStop = 0;
	private static final int longStop = 1;
	//private static int 
}
