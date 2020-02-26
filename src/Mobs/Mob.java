package Mobs;



import Game.Movable;
import Imported.Audio;
import Imported.Texture;
import LowLevel.Point;

public abstract class Mob extends Movable{
	
	protected Texture[] anims;
	private String[] walkSounds;
	
	
	private int mobID;
	private Point movementPoint;
	private double verticalMove;
	private double horizontalMove;
	private boolean startingHorizontal;
	private int stoppingFrame;
	private int maxStoppingFrame;
	private int deathAnimFrame;
	private int maxDeathAnimFrame;
	private int walkAnim;
	private int walkFrame;
	private int walkAnimSwitch;
	private int walkDirec;
	private int soundFXFrame;
	private int soundFXSwitch;
	private int firstSound;
	protected boolean followingPlayer;
	protected boolean attacking;
	
	public Mob(double x, double y, int ID)
	{
		super(null, x, y, 0, 0);
		if (ID == skeleton)
		{
			setWidth(50);
			setLength(50);
			setSpeed(MeleeMob.skeletonSpeed);
			maxStoppingFrame = MeleeMob.skeletonStopFrames;
			anims = getAnims(skelAnimInd, skelAnimInd + 19);
			
			walkSounds = getSounds(skelSoundInd, skelSoundInd + 1);
			soundFXSwitch = 20;
			firstSound = 6;
			walkAnimSwitch = 6;
			walkDirec = down;
		}
		
		int which = (int)(Math.random() * 4);
		if (which == up) {setImage(anims[uI]);}
		else if (which == right) {setImage(anims[rI]);}
		else if (which == down) {setImage(anims[dI]);}
		else if (which == left) {setImage(anims[lI]);}
		
		mobID = ID;
		followingPlayer = false;
		stoppingFrame = maxStoppingFrame;
		walkAnim = notWalking;
		deathAnimFrame = 0;
		soundFXFrame = 0;
		walkFrame = 0;
	}
	
	
	public void createMovementPoints()
	{
		stopWalk();
		createMovementPoint();
		stoppingFrame = 0;
		startingHorizontal = Math.random() < .5;
		createDistances();
	}
	
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
		int direc;
		
		//Decide which direction to move
		if (startingHorizontal)
		{
			if (!atHoriz)
			{
				if (horizontalMove < getX())
				{
					direc = 3;
				}
				else 
				{
					direc = 1;
				}
			}
			else if (!atVert)
			{
				if (verticalMove < getY())
				{
					direc = 2;
				}
				else 
				{
					direc = 0;
				}
			}
			else 
			{
				createMovementPoints();
				return;
			}
		}
		else 
		{
			if (!atVert)
			{
				if (horizontalMove < getY())
				{
					direc = 2;
				}
				else 
				{
					direc = 0;
				}
			}
			else if (!atHoriz)
			{
				if (verticalMove < getX())
				{
					direc = 3;
				}
				else 
				{
					direc = 1;
				}
			}
			else 
			{
				createMovementPoints();
				return;
			}
		}
		
		super.move(direc);
		handleAnims();
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
	private void handleAnims()
	{
		walkFrame++;
		if (walkAnim == notWalking) {walkAnim = startWalking; walkFrame = walkAnimSwitch;}
		if (walkFrame == walkAnimSwitch)
		{
			walkAnim++;
			walkFrame = 0;
			if (walkDirec == up)
			{
				if (walkAnim == 1) {setImage(anims[uW0]);}
				else if (walkAnim == 2) {setImage(anims[uW1]);}
				else if (walkAnim == 3) {setImage(anims[uW2]);}
				else if (walkAnim == 4) {setImage(anims[uW1]);}
				else if (walkAnim == 5) {walkAnim = 1; setImage(anims[uW0]);}
			}
			else if (walkDirec == right)
			{
				if (walkAnim == 1) {setImage(anims[rW0]);}
				else if (walkAnim == 2) {setImage(anims[rW1]);}
				else if (walkAnim == 3) {setImage(anims[rW2]);}
				else if (walkAnim == 4) {setImage(anims[rW1]);}
				else if (walkAnim == 5) {walkAnim = 1; setImage(anims[rW0]);}
			}
			else if (walkDirec == down)
			{
				if (walkAnim == 1) {setImage(anims[dW0]);}
				else if (walkAnim == 2) {setImage(anims[dW1]);}
				else if (walkAnim == 3) {setImage(anims[dW2]);}
				else if (walkAnim == 4) {setImage(anims[dW1]);}
				else if (walkAnim == 5) {walkAnim = 1; setImage(anims[dW0]);}
			}
			else if (walkDirec == left)
			{
				if (walkAnim == 1) {setImage(anims[lW0]);}
				else if (walkAnim == 2) {setImage(anims[lW1]);}
				else if (walkAnim == 3) {setImage(anims[lW2]);}
				else if (walkAnim == 4) {setImage(anims[lW1]);}
				else if (walkAnim == 5) {walkAnim = 1; setImage(anims[lW0]);}
			}
		}
		if (soundFXFrame == soundFXSwitch || soundFXFrame == firstSound)
		{
			playWalkSound();
			soundFXFrame = firstSound + 1;
		}
		else {soundFXFrame++;}
	}
	private void stopWalk()
	{
		walkAnim = notWalking;
		walkFrame = 0;
		soundFXFrame = 0;
		if (walkDirec == up) {setImage(anims[uI]);}
		else if (walkDirec == right) {setImage(anims[rI]);}
		else if (walkDirec == down) {setImage(anims[dI]);}
		else if (walkDirec == left) {setImage(anims[lI]);}
	}
	private Audio playWalkSound()
	{
		int which = (int)(Math.random() * walkSounds.length);
		return Audio.playSound(walkSounds[which], .3);
	}
	
	public abstract void createMovementPoint();
	public abstract void attack();
	
	public static int up = 0;
	public static int right = 1;
	public static int down = 2;
	public static int left = 3;
	
	//uW0 = up walk 0
	//uI = up idle
	//uA = up attack
	public static int uW0 = 0;
	public static int uW1 = 1;
	public static int uW2 = 2;
	public static int uI = 3;
	public static int uA = 4;
	
	public static int rW0 = 5;
	public static int rW1 = 6;
	public static int rW2 = 7;
	public static int rI = 8;
	public static int rA = 9;
	
	public static int dW0 = 10;
	public static int dW1 = 11;
	public static int dW2 = 12;
	public static int dI = 13;
	public static int dA = 14;
	
	public static int lW0 = 15;
	public static int lW1 = 16;
	public static int lW2 = 17;
	public static int lI = 18;
	public static int lA = 19;
	
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
	public static int skeleton = 0;
	public static int slime = 1;
	public static int zombie = 2;
	public static void init()
	{
		mobSounds = new String[2];
		mobSounds[skelSoundInd + 0] = "Misc/random2";
		mobSounds[skelSoundInd + 1] = "Misc/random3";
		
		mobTex = new Texture[20];
		mobTex[skelAnimInd + uW0] = new Texture("Mobs/Skeleton/IdleDown.png");
		for (int i = 1; i < 20; i++)
		{
			mobTex[i] = mobTex[0]; 
		}
	}
	private static int skelSoundInd = 0;
	private static int skelAnimInd = 0;
	
	private static int notWalking = -1;
	private static int startWalking = 0;
	//private static int 
}
