package Combat;


import java.util.ArrayList;

import Imported.Audio;
import Imported.Texture;
import LowLevel.Geometry;
import LowLevel.Image;
import LowLevel.Point;

public class CombatChar extends Movable{
	private Point[] projectileBasis;
	
	private Texture[] particleAnims;
	private ArrayList<Effect> currentEffects;
	
	private Image particleEffect;
	private int particleFrames;
	private int particleAnim;
	private int particleSwitch;
	
	protected double manaRegen;
	protected double mana;
	protected double health;
	protected double healthRegen;
	protected double maxHealth;
	protected double maxMana;
	
	protected double hitAngle;
	
	protected int hitStunFrames;
	protected int invulnerabilityLength;
	protected int stunLength;
	protected int walkAnim;
	protected int walkFrame;
	protected int soundFXFrame;
	protected int walkDirec;
	protected int walkAnimSwitch;
	protected int soundFXSwitch;
	protected int firstSound;
	protected double walkVolume;
	
	protected double damageMultiplier;
	
	protected String[] walkSounds;
	protected Texture[] anims;
	
	public CombatChar(Texture img, double inX, double inY, double w, double l) {
        super(img, inX, inY, w, l);
        Point[] showBox = getShowBasis();
        projectileBasis = new Point[4];
        projectileBasis[DL] = new Point(showBox[DL].getX(), showBox[DL].getY());
        projectileBasis[DR] = new Point(showBox[DR].getX(), showBox[DR].getY());
        projectileBasis[UR] = new Point(showBox[UR].getX(), showBox[UR].getY());
        projectileBasis[UL] = new Point(showBox[UL].getX(), showBox[UL].getY());
        hitStunFrames = 0;
        setProjInteraction(true);
        walkVolume = .6;
        currentEffects = new ArrayList<Effect>();
        damageMultiplier = 1;
    }
	public Point[] getProjectileBasis()
	{
		return projectileBasis;
	}
	public boolean projCollision(Projectile projectile)
	{
		Point[] projHitBox = projectile.getCollisionBasis();
		Point[] temp = Geometry.rotatePoints(projHitBox, projectile, projectile.getAngle());
		return Geometry.strictCollision(projectileBasis, temp);
	}
	public void setX(double newX)
	{
		double xDiff = newX - getX();
		super.setX(newX);
		for (int i = 0; i < projectileBasis.length; i++)
		{
			projectileBasis[i].setX(projectileBasis[i].getX() + xDiff);
		}
	}
	public void setY(double newY)
	{
		double yDiff = newY - getY();
		super.setY(newY);
		for (int i = 0; i < projectileBasis.length; i++)
		{
			projectileBasis[i].setY(projectileBasis[i].getY() + yDiff);
		}
	}
	
    public void move() {
    	if (hitStunFrames <= invulnerabilityLength)
    	{
    		switch (walkDirec)
    		{
    			case up: setY(getY() + speed); break;
    			case right: setX(getX() + speed); break;
    			case down: setY(getY() - speed); break;
    			case left: setX(getX() - speed); break;
    		}
    		handleAnims();
    	}
    }
    /**
     * one blink upon hit = pauseLength (currently 8 frames)
     * @param damage
     * @param pauses
     */
    public void receiveEffect(Effect effect)
    {
    	int type = effect.getType();
    	double[] information = effect.getInformation();
    	switch(type)
    	{
    		case Effect.damage:
    			double fromAngle = information[Effect.fromAngle];
    			int invulnLen = (int)information[Effect.invulnFrames];
    			int stunLen = (int)information[Effect.stunFrames];
    			double damage = information[Effect.hitDamage];
    			hitAngle = fromAngle + 180;
    			invulnerabilityLength = invulnLen;
    			stunLength = stunLen;
    			hitStunFrames = invulnerabilityLength + stunLength;
    			setHealth(getHealth() - damage);
    			break;
    		case Effect.heal:
    			createParticles(Effect.heal);
    			currentEffects.add(effect);
    			
    			break;
    		case Effect.poison:
    			createParticles(Effect.poison);
    			currentEffects.add(effect);
    			
    			break;
    		case Effect.frost:
    			createParticles(Effect.frost);
    			currentEffects.add(effect);
    			setSpeed(information[Effect.speedMultipier] * getSpeed());
    			break;
    		case Effect.powerUp:
    			createParticles(Effect.powerUp);
    			currentEffects.add(effect);
    			setDamageMultiplier(effect.getInformation()[Effect.powerMultiplier]);
    			break;
    	}
    }
    private void receiveStuckEffects()
    {
    	
    	for (int i = currentEffects.size() - 1; i >= 0; i--)
    	{
    		
    		Effect currEffect = currentEffects.get(i);
    		currEffect.updateFrame();
    		int type = currEffect.getType();
    		int frame = currEffect.getFrame();
    		double[] information = currEffect.getInformation();
    		switch(type)
    		{
    			case Effect.poison:
    				if (frame % (int)information[Effect.tickFrame] == 0)
    				{
    					//Ticks the health down on the tickframe
    					setHealth(getHealth() - information[Effect.poisonTick]);
    					
    				}
    				break;
    			case Effect.heal:
    				if (frame % (int)information[Effect.tickFrame] == 0)
    				{
    					//Ticks the health up on the tickframe
    					setHealth(getHealth() + information[Effect.healTick]);
    				}
    				break;
    		}
    		if (currEffect.isEnded()) {
    			currentEffects.remove(i);
    			switch (type) {
				case Effect.poison:
					setRGBA(getRed() / poisonMult[0], getGreen() / poisonMult[1], getBlue() / poisonMult[2], getAlpha() / poisonMult[3]);
					break;
				case Effect.frost:
					setRGBA(getRed() / frostMult[0], getGreen() / frostMult[1], getBlue() / frostMult[2], getAlpha() / frostMult[3]);
					setSpeed(getSpeed() / information[Effect.speedMultipier]);
					break;
				case Effect.powerUp:
					setRGBA(getRed() / powerMult[0], getGreen() / powerMult[1], getBlue() / powerMult[2], getAlpha() / powerMult[3]);
					setDamageMultiplier(getDamageMultiplier() / information[Effect.powerMultiplier]);
					break;
				}
    		}
    	}
    }
    public void show()
    {
    	if (hitStunFrames > 0)
    	{
    		if (hitStunFrames - invulnerabilityLength > 0)
    		{
    			boolean[] moveDirecs = findDirecs(hitAngle);
    			boolean[] movement = getMovement();
    			if (canMove(moveDirecs, movement))
    			{
    				double angle = Math.toRadians(hitAngle);
    				double velocity = initialHitVelocity * ((hitStunFrames - invulnerabilityLength) / (double)(stunLength));
    				setPos(getX() + Math.cos(angle) * velocity, getY() + Math.sin(angle) * velocity);
    			}
    		}
    		if (hitStunFrames % pauseLength == 0)
    		{
    			if (getAlpha() == 255) {setAlpha(100);}
    			else {setAlpha(255);}
    		}
    		hitStunFrames--;
    	}
    	else {setAlpha(255);}
    	super.show();
    	receiveStuckEffects();
    	if (particleFrames != 0) {
    		if (particleFrames % particleSwitch == 0) {
    			particleAnim += 1;
    			particleEffect.setImage(particleAnims[particleAnim]);
    		}
    		particleEffect.setPos(getX(), getY());
    		particleEffect.show();
    		particleFrames--;
    	}
    	else if (particleFrames < 0)
    	{
    		try {
				throw new Exception("Particle frames shouldn't have been lower than 0");
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
    	}
    }
    public boolean canBeAttacked()
    {
    	return hitStunFrames == 0;
    }
    public void createParticles(int ID)
    {
    	boolean shouldError = true;
    	switch (ID) {
		case Effect.heal:
			particleAnims = getAnims(healAnimInd, healAnimInd + 21);
			particleEffect = new Image(null, 0, 0, getWidth(), getLength());
			particleEffect.setAlpha(150);
			particleSwitch = 2;
			particleAnim = startParticles;
			particleFrames = (particleAnims.length - 1) * particleSwitch;
			shouldError = false;
			break;
		case Effect.poison:
			setRGBA(getRed() * poisonMult[0], getGreen() * poisonMult[1], getBlue() * poisonMult[2], getAlpha() * poisonMult[3]);
			shouldError = false;
			break;
		case Effect.frost:
			setRGBA(getRed() * frostMult[0], getGreen() * frostMult[1], getBlue() * frostMult[2], getAlpha() * frostMult[3]);
			shouldError = false;
			break;
		case Effect.powerUp:
			setRGBA(getRed() * powerMult[0], getGreen() * powerMult[1], getBlue() * powerMult[2], getAlpha() * powerMult[3]);
			shouldError = false;
			break;
		}
    	
    	if (shouldError)
    	{
    		try {
				throw new Exception("Particles weren't configured for effect " + ID);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
    	}
    }
    
    private boolean[] findDirecs(double angle)
    {
    	boolean[] moveDirecs = new boolean[4];
    	if (angle < 0) {angle += 360;}
    	angle %= 360;
    	if (angle == 0) {moveDirecs[right] = true;}
    	else if (angle > 0 && angle < 90) {moveDirecs[right] = true; moveDirecs[up] = true;}
    	else if (angle == 90) {moveDirecs[up] = true;}
    	else if (angle > 90 && angle < 180) {moveDirecs[left] = true; moveDirecs[up] = true;}
    	else if (angle == 180) {moveDirecs[left] = true;}
    	else if (angle > 180 && angle < 270) {moveDirecs[down] = true; moveDirecs[left] = true;}
    	else if (angle == 270) {moveDirecs[down] = true;}
    	else {moveDirecs[down] = true; moveDirecs[right] = true;}
    	return moveDirecs;
    }
    private boolean canMove(boolean[] moveDirecs, boolean[] movementCapabilities)
    {
    	for (int i = 0; i < moveDirecs.length; i++)
    	{
    		if (moveDirecs[i] && !movementCapabilities[i])
    		{
    			return false;
    		}
    	}
    	return true;
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
		return Audio.playSound(walkSounds[which], walkVolume);
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
    protected static final int noStop = 2;
    protected static final int resetWalk = -1;
    protected void handleCombatException()
	{
		if (health == 0) { try { throw new Exception("health for combatChar was 0");} 
		catch (Exception e) {e.printStackTrace(); System.exit(0);}}
		if (maxHealth == 0) { try { throw new Exception("maxHealth for combatChar was 0");} 
		catch (Exception e) {e.printStackTrace(); System.exit(0);}}
		if (soundFXSwitch == 0) { try { throw new Exception("soundFXSwitch for combatChar was 0");} 
		catch (Exception e) {e.printStackTrace(); System.exit(0);}}
		if (walkAnimSwitch == 0) { try { throw new Exception("walkAnimSwitch for combatChar was 0");} 
		catch (Exception e) {e.printStackTrace(); System.exit(0);}}
		if (firstSound == 0) { try { throw new Exception("firstSound for combatChar was 0");} 
		catch (Exception e) {e.printStackTrace(); System.exit(0);}}
		if (walkSounds == null) { try { throw new Exception("walkSounds uninitialized for combatChar");} 
		catch (Exception e) {e.printStackTrace(); System.exit(0);}}
		if (anims == null) { try { throw new Exception("anims uninitialized for combatChar");} 
		catch (Exception e) {e.printStackTrace(); System.exit(0);}}
	}
    
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
	protected static final int healAnimInd = playerAnimInd + 20;
	
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
		
		
		loadedTex = new Texture[82];
		loadedTex[skelAnimInd + uW0] = new Texture("Mobs/Skeleton/IdleUp.png");
		loadedTex[skelAnimInd + uW1] = new Texture("Mobs/Skeleton/IdleUp.png");
		loadedTex[skelAnimInd + uW2] = new Texture("Mobs/Skeleton/IdleUp.png");
		loadedTex[skelAnimInd + uA] = new Texture("Mobs/Skeleton/IdleUp.png");
		loadedTex[skelAnimInd + uI] = new Texture("Mobs/Skeleton/IdleUp.png");
		loadedTex[skelAnimInd + rW0] = new Texture("Mobs/Skeleton/IdleRight.png");
		loadedTex[skelAnimInd + rW1] = new Texture("Mobs/Skeleton/IdleRight.png");
		loadedTex[skelAnimInd + rW2] = new Texture("Mobs/Skeleton/IdleRight.png");
		loadedTex[skelAnimInd + rA] = new Texture("Mobs/Skeleton/IdleRight.png");
		loadedTex[skelAnimInd + rI] = new Texture("Mobs/Skeleton/IdleRight.png");
		loadedTex[skelAnimInd + dW0] = new Texture("Mobs/Skeleton/IdleDown.png");
		loadedTex[skelAnimInd + dW1] = new Texture("Mobs/Skeleton/IdleDown.png");
		loadedTex[skelAnimInd + dW2] = new Texture("Mobs/Skeleton/IdleDown.png");
		loadedTex[skelAnimInd + dA] = new Texture("Mobs/Skeleton/IdleDown.png");
		loadedTex[skelAnimInd + dI] = new Texture("Mobs/Skeleton/IdleDown.png");
		loadedTex[skelAnimInd + lW0] = new Texture("Mobs/Skeleton/IdleLeft.png");
		loadedTex[skelAnimInd + lW1] = new Texture("Mobs/Skeleton/IdleLeft.png");
		loadedTex[skelAnimInd + lW2] = new Texture("Mobs/Skeleton/IdleLeft.png");
		loadedTex[skelAnimInd + lA] = new Texture("Mobs/Skeleton/IdleLeft.png");
		loadedTex[skelAnimInd + lI] = new Texture("Mobs/Skeleton/IdleLeft.png");
		
		
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
    	
    	loadedTex[healAnimInd + 0] = new Texture("Particles/heal0.png");
    	loadedTex[healAnimInd + 1] = new Texture("Particles/heal1.png");
    	loadedTex[healAnimInd + 2] = new Texture("Particles/heal2.png");
    	loadedTex[healAnimInd + 3] = new Texture("Particles/heal3.png");
    	loadedTex[healAnimInd + 4] = new Texture("Particles/heal4.png");
    	loadedTex[healAnimInd + 5] = new Texture("Particles/heal5.png");
    	loadedTex[healAnimInd + 6] = new Texture("Particles/heal6.png");
    	loadedTex[healAnimInd + 7] = new Texture("Particles/heal7.png");
    	loadedTex[healAnimInd + 8] = new Texture("Particles/heal8.png");
    	loadedTex[healAnimInd + 9] = new Texture("Particles/heal9.png");
    	loadedTex[healAnimInd + 10] = new Texture("Particles/heal10.png");
    	loadedTex[healAnimInd + 11] = new Texture("Particles/heal11.png");
    	loadedTex[healAnimInd + 12] = new Texture("Particles/heal12.png");
    	loadedTex[healAnimInd + 13] = new Texture("Particles/heal13.png");
    	loadedTex[healAnimInd + 14] = new Texture("Particles/heal14.png");
    	loadedTex[healAnimInd + 15] = new Texture("Particles/heal15.png");
    	loadedTex[healAnimInd + 16] = new Texture("Particles/heal16.png");
    	loadedTex[healAnimInd + 17] = new Texture("Particles/heal17.png");
    	loadedTex[healAnimInd + 18] = new Texture("Particles/heal18.png");
    	loadedTex[healAnimInd + 19] = new Texture("Particles/heal19.png");
    	loadedTex[healAnimInd + 20] = new Texture("Particles/heal20.png");
    	loadedTex[healAnimInd + 21] = new Texture("Particles/heal21.png");
    }
    

	
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
    
    private static final int initialHitVelocity = 5;
    private static final int pauseLength = 8;
    private static final int startParticles = -1;
    
    public void setDirec(int newDirec) {walkDirec = newDirec;}
    public void setDamageMultiplier(double newMultiplier) {damageMultiplier = newMultiplier;}
    public int getDirec() {return walkDirec;}
    public double getHealth() {return health;}
    public double getMaxHealth() {return maxHealth;}
    public double getMana() {return mana;}
    public double getMaxMana() {return maxMana;}
    public double getDamageMultiplier() {return damageMultiplier;}
    /**
     * sets health and accounts for overflow
     * @param newHealth
     */
    public void setHealth(double newHealth) {health = Math.min(newHealth, maxHealth);}
    public void setMaxHealth(double newMax) {maxHealth = newMax;}
    /**
     * sets mana and accounts for overflow
     * @param newMana
     */
    public void setMana(double newMana) {mana = Math.min(newMana, maxMana);}
    public void setMaxMana(double newMax) {maxMana = newMax;}
    
    public static final int heal = 2;
    public static final int powerUp = 3;
    
    public static final float[] poisonMult = new float[] {50f/255, 255f/255, 50f/255, 255f/255};
    public static final float[] frostMult = new float[] {50f/255, 50f/255, 255f/255, 255f/255};
    public static final float[] powerMult = new float[] {255f/255, 150f/255, 150f/255, 255f/255};
}
