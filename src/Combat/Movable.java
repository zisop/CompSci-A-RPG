package Combat;

import Game.Main;
import Imported.Audio;
import Imported.Texture;
import LowLevel.Image;

public class Movable extends Image{
	
	
	protected double speed;
	protected double hitAngle;
	
	protected int hitStunFrames;
	protected int walkAnim;
	protected int walkFrame;
	protected int soundFXFrame;
	protected int walkDirec;
	protected int walkAnimSwitch;
	protected int soundFXSwitch;
	protected int firstSound;
	
	protected String[] walkSounds;
	protected Texture[] anims;
	
	public Movable(Texture img, double inX, double inY, double w, double l) {
        super(img, inX, inY, w, l);
        hitStunFrames = maxHitStun;
    }
    
    public Movable(Texture img, double inX, double inY, double w, double l, double hitW, double hitL) {
        super(img, inX, inY, w, l, hitW, hitL);
        hitStunFrames = maxHitStun;
    }
    public Movable(Texture img, double inX, double inY, double w, double l, double hitW, double hitL, double hitboxDown) {
        super(img, inX, inY, w, l, hitW, hitL, hitboxDown);
        hitStunFrames = maxHitStun;
    }
    public void move() {
    	switch (walkDirec)
    	{
    	case up: setY(getY() + speed); break;
    	case right: setX(getX() + speed); break;
    	case down: setY(getY() - speed); break;
    	case left: setX(getX() - speed); break;
    	}
        handleAnims();
    }

	/**
     * 
     * @return boolean[] of movement capabilities {north, east, south, west}
     */
    public boolean[] getMovement()
    {
    	Image[] room = Main.allRooms[Main.currRoom].getImages();
    	boolean[] movement = new boolean[]{true, true, true, true};
        for (int i = 0; i < room.length; ++i) {
            Image currChar = room[i];
            if (currChar.collides()) {
            	
            	boolean shouldBreak = false;
            	setY(getY() + speed);
            	if (collision(currChar))
            	{
            		movement[0] = false; 
            		shouldBreak = true;
            	}
            	setPos(getX() + speed, getY() - speed);
            	if (collision(currChar))
            	{
            		movement[1] = false;
            		shouldBreak = true;
            	}
            	setPos(getX() - speed, getY() - speed);
            	if (collision(currChar))
            	{
            		movement[2] = false;
            		shouldBreak = true;
            	}
           		setPos(getX() - speed, getY() + speed);
            	if (collision(currChar))
            	{
            		movement[3] = false;
            		shouldBreak = true;
            	}
            	setX(getX() + speed);
            	if (shouldBreak) {
            		break;
            	}
            }
        }
        return movement;
    }
    public void enterHitStun(double angle)
    {
    	hitAngle = angle;
    	hitStunFrames = 0;
    }
    
    /**
	 * handles walk animations and sounds
	 */
    protected void handleAnims()
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
	protected static final int startWalking = 0;
	/**
	 * plays walk sound
	 * @return an Audio object that can be manipulated or just left alone, usually
	 */
	protected Audio playWalkSound()
	{
		int which = (int)(Math.random() * walkSounds.length);
		return Audio.playSound(walkSounds[which], .6);
	}
	
	/**
	 * stops movable's walk animation
	 */
    public void stopWalk()
	{
    	walkAnim = resetWalk;
		walkFrame = 0;
		soundFXFrame = 0;
		switch (walkDirec)
		{
			case up: setImage(anims[uI]); break;
			case right: setImage(anims[rI]); break;
			case down: setImage(anims[dI]); break;
			case left: setImage(anims[lI]); break;
		}
	}
    protected static final int shortStop = 0;
    protected static final int longStop = 1;
    protected static final int resetWalk = -1;
    
    protected static Texture[] getAnims(int start, int end)
	{
		Texture[] anims = new Texture[end - start + 1];
		for (int i = 0; i < anims.length; i++)
		{
			anims[i] = loadedTex[start + i];
		}
		return anims;
	}
	protected static String[] getSounds(int start, int end)
	{
		String[] sounds = new String[end - start + 1];
		for (int i = 0; i < sounds.length; i++)
		{
			sounds[i] = loadedSounds[i + start]; 
		}
		return sounds;
	}
    
    public static String[] loadedSounds;
    public static Texture[] loadedTex;
    
	protected static final int skelAnimInd = 0;
	protected static final int slimeAnimInd = 20;
	protected static final int playerAnimInd = 40;
	
	protected static final int skelSoundInd = 0;
	protected static final int slimeSoundInd = 2;
	protected static final int playerSoundInd = 12;
	
	
	
    public static void init ()
    {
    	loadedSounds = new String[13];
		loadedSounds[skelSoundInd + 0] = "Misc/random2";
		loadedSounds[skelSoundInd + 1] = "Misc/random3";
		loadedSounds[slimeSoundInd + 0] = "NPC/Slime/slime1";
		loadedSounds[slimeSoundInd + 1] = "NPC/Slime/slime2";
		loadedSounds[slimeSoundInd + 2] = "NPC/Slime/slime3";
		loadedSounds[slimeSoundInd + 3] = "NPC/Slime/slime4";
		loadedSounds[slimeSoundInd + 4] = "NPC/Slime/slime5";
		loadedSounds[slimeSoundInd + 5] = "NPC/Slime/slime6";
		loadedSounds[slimeSoundInd + 6] = "NPC/Slime/slime7";
		loadedSounds[slimeSoundInd + 7] = "NPC/Slime/slime8";
		loadedSounds[slimeSoundInd + 8] = "NPC/Slime/slime9";
		loadedSounds[slimeSoundInd + 9] = "NPC/Slime/slime10";
		loadedSounds[playerSoundInd + 0] = "Move/Steps/foot2";
		
		
		loadedTex = new Texture[60];
		loadedTex[skelAnimInd + uW0] = new Texture("Mobs/Skeleton/IdleDown.png");
		for (int i = 1; i < 20; i++)
		{
			loadedTex[i] = loadedTex[0]; 
		}
		
		loadedTex[slimeAnimInd + uW0] = new Texture("Mobs/Slime/IdleUp.png");
		loadedTex[slimeAnimInd + uW1] = loadedTex[slimeAnimInd + uW0];
		loadedTex[slimeAnimInd + uW2] = loadedTex[slimeAnimInd + uW0];
		loadedTex[slimeAnimInd + uI] = loadedTex[slimeAnimInd + uW0];
		loadedTex[slimeAnimInd + uA] = loadedTex[slimeAnimInd + uW0];
		loadedTex[slimeAnimInd + rW0] = new Texture("Mobs/Slime/IdleRight.png");
		loadedTex[slimeAnimInd + rW1] = loadedTex[slimeAnimInd + rW0];
		loadedTex[slimeAnimInd + rW2] = loadedTex[slimeAnimInd + rW0];
		loadedTex[slimeAnimInd + rI] = loadedTex[slimeAnimInd + rW0];
		loadedTex[slimeAnimInd + rA] = loadedTex[slimeAnimInd + rW0];
		loadedTex[slimeAnimInd + dW0] = new Texture("Mobs/Slime/IdleDown.png");
		loadedTex[slimeAnimInd + dW1] = loadedTex[slimeAnimInd + dW0];
		loadedTex[slimeAnimInd + dW2] = loadedTex[slimeAnimInd + dW0];
		loadedTex[slimeAnimInd + dI] = loadedTex[slimeAnimInd + dW0];
		loadedTex[slimeAnimInd + dA] = loadedTex[slimeAnimInd + dW0];
		loadedTex[slimeAnimInd + lW0] = new Texture("Mobs/Slime/IdleLeft.png");
		loadedTex[slimeAnimInd + lW1] = loadedTex[slimeAnimInd + lW0];
		loadedTex[slimeAnimInd + lW2] = loadedTex[slimeAnimInd + lW0];
		loadedTex[slimeAnimInd + lI] = loadedTex[slimeAnimInd + lW0];
		loadedTex[slimeAnimInd + lA] = loadedTex[slimeAnimInd + lW0];
		
		loadedTex[playerAnimInd + uW0] = new Texture("WalkAnim/WalkUp/Up01.PNG");
    	loadedTex[playerAnimInd + uW1] = new Texture("WalkAnim/WalkUp/Up02.PNG");
    	loadedTex[playerAnimInd + uW2] = new Texture("WalkAnim/WalkUp/Up03.PNG");
    	loadedTex[playerAnimInd + uI] = new Texture("IdleAnim/IdleUp.PNG");
    	loadedTex[playerAnimInd + uA] = loadedTex[playerAnimInd + uI];
    	loadedTex[playerAnimInd + rW0] = new Texture("WalkAnim/WalkRight/Right01.PNG");
    	loadedTex[playerAnimInd + rW1] = new Texture("WalkAnim/WalkRight/Right02.PNG");
    	loadedTex[playerAnimInd + rW2] = new Texture("WalkAnim/WalkRight/Right03.PNG");
    	loadedTex[playerAnimInd + rI] = new Texture("IdleAnim/IdleRight.PNG");
    	loadedTex[playerAnimInd + rA] = loadedTex[playerAnimInd + rI];
    	loadedTex[playerAnimInd + dW0] = new Texture("WalkAnim/WalkDown/Down01.PNG");
    	loadedTex[playerAnimInd + dW1] = new Texture("WalkAnim/WalkDown/Down02.PNG");
    	loadedTex[playerAnimInd + dW2] = new Texture("WalkAnim/WalkDown/Down03.PNG");
    	loadedTex[playerAnimInd + dI] = new Texture("IdleAnim/IdleDown.PNG");
    	loadedTex[playerAnimInd + dA] = loadedTex[playerAnimInd + dI];
    	loadedTex[playerAnimInd + lW0] = new Texture("WalkAnim/WalkLeft/Left01.PNG");
    	loadedTex[playerAnimInd + lW1] = new Texture("WalkAnim/WalkLeft/Left02.PNG");
    	loadedTex[playerAnimInd + lW2] = new Texture("WalkAnim/WalkLeft/Left03.PNG");
    	loadedTex[playerAnimInd + lI] = new Texture("IdleAnim/IdleLeft.PNG");
    	loadedTex[playerAnimInd + lA] = loadedTex[playerAnimInd + lI];
    }
    
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
    
    public static final int maxHitStun = 20;
    
    public void setDirec(int newDirec) {walkDirec = newDirec;}
    public int getDirec() {return walkDirec;}
    
}
