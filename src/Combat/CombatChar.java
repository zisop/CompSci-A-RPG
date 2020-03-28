package Combat;


import java.util.ArrayList;

import Imported.Audio;
import Imported.Texture;
import LowLevel.Animation;
import LowLevel.Geometry;
import LowLevel.Image;
import LowLevel.Point;

public class CombatChar extends Movable{
	private Point[] projectileBasis;
	protected boolean isDead;
	
	private Animation particleAnim;
	private ArrayList<Effect> currentEffects;
	
	private Image particleEffect;
	private int particleFrames;
	private int particleSwitch;
	private boolean idling;
	
	protected double manaRegen;
	protected double mana;
	protected double health;
	protected double healthRegen;
	protected double maxHealth;
	protected double maxMana;
	protected double armor;
	
	protected int hitStunFrames;
	protected int invulnerabilityLength;
	protected int stunLength;
	protected double initialVelocity;
	protected double hitAngle;
	
	protected int soundFXFrame;
	protected int walkDirec;
	protected int walkAnimSwitch;
	protected int soundFXSwitch;
	protected int firstSound;
	protected double walkVolume;
	
	protected double damageMultiplier;
	
	protected String[] walkSounds;
	
	private Animation currAnim;
	protected Animation[] anims;
	
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
        isDead = false;
        walkDirec = down;
        idling = true;
		soundFXFrame = 0;
    }
	public void printStats()
	{
		System.out.print("Armor: " + armor + "\nDamage: " + damageMultiplier + "\nmaxHealth: " + maxHealth
		+ "\nmaxMana: " + maxMana + "\nhealthRegen: " + healthRegen + "\nmanaRegen: " + manaRegen);
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
    		if (soundFXFrame == soundFXSwitch || soundFXFrame == firstSound)
    		{
    			playWalkSound();
    			soundFXFrame = firstSound + 1;
    		}
    		else {soundFXFrame++;}
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
    			double fromAngle = information[Effect.damageFromAngle];
    			int invulnLen = (int)information[Effect.damageInvulnFrames];
    			int stunLen = (int)information[Effect.damageStunFrames];
    			double damage = information[Effect.damageDamage];
    			hitAngle = fromAngle + 180;
    			invulnerabilityLength = invulnLen;
    			stunLength = stunLen;
    			hitStunFrames = invulnerabilityLength + stunLength;
    			initialVelocity = information[Effect.damageInitialVelocity];
    			setHealth(getHealth() - Math.max(damage - armor, 0));
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
    			setDamageMultiplier(damageMultiplier * effect.getInformation()[Effect.powerMultiplier]);
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
    					double numTicks = information[Effect.poisonDuration] / information[Effect.tickFrame];
    					double damage = Math.max(information[Effect.poisonTick] - (armor / numTicks), 0);
    					setHealth(getHealth() - damage);
    				}
    				break;
    			case Effect.heal:
    				if (frame % (int)information[Effect.tickFrame] == 0)
    				{
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
    	boolean showParticles = false;
    	if (!isDead)
    	{
    		if (hitStunFrames > 0)
    		{
    			if (hitStunFrames - invulnerabilityLength > 0)
    			{
    				boolean[] moveDirecs = findDirecs(hitAngle);
    				double velocity = initialVelocity * ((hitStunFrames - invulnerabilityLength) / (double)(stunLength));
    				updateMovement(velocity);
    				boolean[] movement = getMovement();
    				if (canMove(moveDirecs, movement))
    				{
    					double angle = Math.toRadians(hitAngle);
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
    		receiveStuckEffects();
    		if (particleFrames != 0) {
    			particleAnim.update();
    			particleEffect.setPos(getX(), getY());
    			particleFrames--;
    			showParticles = true;
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
    		currAnim.update();
    	}
    	super.show();
    	if (showParticles) {particleEffect.show();}
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
			
			particleSwitch = 2;
			particleEffect = new Image(null, 0, 0, getWidth(), getLength());
			
			particleAnim = getAnims(healAnimInd, healAnimInd, particleSwitch, particleEffect)[0];
			particleEffect.setAlpha(100);
			particleFrames = (particleAnim.length - 1) * particleSwitch;
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
		soundFXFrame = 0;
		idling = true;
		switch (walkDirec)
		{
			case up: setAnim(uI); break;
			case right: setAnim(rI); break;
			case down: setAnim(dI); break;
			case left: setAnim(lI); break;
		}
	}
    protected static final int shortStop = 0;
    protected static final int longStop = 1;
    protected static final int noStop = 2;
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
	}
    
    protected static Animation[] getAnims(int start, int end, int switchFrame, Image owner)
	{
		Animation[] anims = new Animation[end - start + 1];
		for (int i = 0; i < anims.length; i++)
		{
			Texture[] tex = loadedTex[start + i];
			Animation anim = new Animation(tex, switchFrame, owner);
			anims[i] = anim;
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
    public static Texture[][] loadedTex;
    
	protected static final int skelAnimInd = 0;
	protected static final int slimeAnimInd = skelAnimInd + 12;
	protected static final int playerAnimInd = slimeAnimInd + 12;
	protected static final int healAnimInd = playerAnimInd + 12;
	protected static final int duckAnimInd = healAnimInd + 1;
	protected static final int archerAnimInd = duckAnimInd + 12;
	
	protected static final int skelSoundInd = 0;
	protected static final int slimeSoundInd = 2;
	protected static final int playerSoundInd = 12;
	protected static final int duckSoundInd = 13;
	
    public static void init ()
    {
    	loadedSounds = new String[14];
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
		loadedSounds[playerSoundInd] = "Move/Steps/foot2";
		loadedSounds[duckSoundInd] = "Move/Steps/foot1";
		
		//49 animations
		loadedTex = new Texture[61][];
		Texture[] uWalk = loadedTex[uW + skelAnimInd] = new Texture[4];
		Texture[] uIdle = loadedTex[uI + skelAnimInd] = new Texture[1];
		Texture[] uAtk = loadedTex[uA + skelAnimInd] = new Texture[1];
		
		Texture[] rWalk = loadedTex[rW + skelAnimInd] = new Texture[4];
		Texture[] rAtk = loadedTex[rA + skelAnimInd] = new Texture[1];
		Texture[] rIdle = loadedTex[rI + skelAnimInd] = new Texture[1];
		
		Texture[] dWalk = loadedTex[dW + skelAnimInd] = new Texture[4];
		Texture[] dAtk = loadedTex[dA + skelAnimInd] = new Texture[1];
		Texture[] dIdle = loadedTex[dI + skelAnimInd] = new Texture[1];
		
		Texture[] lWalk = loadedTex[lW + skelAnimInd] = new Texture[4];
		Texture[] lAtk = loadedTex[lA + skelAnimInd] = new Texture[1];
		Texture[] lIdle = loadedTex[lI + skelAnimInd] = new Texture[1];
		
		uWalk[0] = new Texture("Mobs/Skeleton/Animations/walkUp/up0.png");
		uWalk[1] = new Texture("Mobs/Skeleton/Animations/walkUp/up1.png");
		uWalk[2] = new Texture("Mobs/Skeleton/Animations/walkUp/up2.png");
		uWalk[3] = uWalk[1];
		uAtk[0] = new Texture("Mobs/Skeleton/Idle/IdleUp.png");
		uIdle[0] = new Texture("Mobs/Skeleton/Idle/IdleUp.png");
		
		rWalk[0] = new Texture("Mobs/Skeleton/Animations/walkRight/right0.png");
		rWalk[1] = new Texture("Mobs/Skeleton/Animations/walkRight/right1.png");
		rWalk[2] = new Texture("Mobs/Skeleton/Animations/walkRight/right2.png");
		rWalk[3] = rWalk[1];
		rIdle[0] = new Texture("Mobs/Skeleton/Idle/IdleRight.png");
		rAtk[0] = new Texture("Mobs/Skeleton/Idle/IdleRight.png");
		
		dWalk[0] = new Texture("Mobs/Skeleton/Animations/walkDown/down0.png");
		dWalk[1] = new Texture("Mobs/Skeleton/Animations/walkDown/down1.png");
		dWalk[2] = new Texture("Mobs/Skeleton/Animations/walkDown/down2.png");
		dWalk[3] = dWalk[1];
		dAtk[0] = new Texture("Mobs/Skeleton/Idle/IdleDown.png");
		dIdle[0] = new Texture("Mobs/Skeleton/Idle/IdleDown.png");
		
		lWalk[0] = new Texture("Mobs/Skeleton/Animations/walkLeft/left0.png");
		lWalk[1] = new Texture("Mobs/Skeleton/Animations/walkLeft/left1.png");
		lWalk[2] = new Texture("Mobs/Skeleton/Animations/walkLeft/left2.png");
		lWalk[3] = lWalk[1];
		lAtk[0] = new Texture("Mobs/Skeleton/Idle/IdleLeft.png");
		lIdle[0] = new Texture("Mobs/Skeleton/Idle/IdleLeft.png");
		
		uWalk = loadedTex[uW + duckAnimInd] = new Texture[4];
		uIdle = loadedTex[uI + duckAnimInd] = new Texture[1];
		uAtk = loadedTex[uA + duckAnimInd] = new Texture[1];
		
		rWalk = loadedTex[rW + duckAnimInd] = new Texture[4];
		rAtk = loadedTex[rA + duckAnimInd] = new Texture[1];
		rIdle = loadedTex[rI + duckAnimInd] = new Texture[1];
		
		dWalk = loadedTex[dW + duckAnimInd] = new Texture[4];
		dAtk = loadedTex[dA + duckAnimInd] = new Texture[1];
		dIdle = loadedTex[dI + duckAnimInd] = new Texture[1];
		
		lWalk = loadedTex[lW + duckAnimInd] = new Texture[4];
		lAtk = loadedTex[lA + duckAnimInd] = new Texture[1];
		lIdle = loadedTex[lI + duckAnimInd] = new Texture[1];
		
		uWalk[0] = new Texture("Mobs/Duck/anims/up0.png");
		uWalk[1] = new Texture("Mobs/Duck/anims/up1.png");
		uWalk[2] = new Texture("Mobs/Duck/anims/up2.png");
		uWalk[3] = uWalk[1];
		uAtk[0] = uWalk[1];
		uIdle[0] = uWalk[1];
		
		rWalk[0] = new Texture("Mobs/Duck/anims/right0.png");
		rWalk[1] = new Texture("Mobs/Duck/anims/right1.png");
		rWalk[2] = new Texture("Mobs/Duck/anims/right2.png");
		rWalk[3] = rWalk[1];
		rAtk[0] = rWalk[1];
		rIdle[0] = rWalk[1];
		
		lWalk[0] = new Texture("Mobs/Duck/anims/left0.png");
		lWalk[1] = new Texture("Mobs/Duck/anims/left1.png");
		lWalk[2] = new Texture("Mobs/Duck/anims/left2.png");
		lWalk[3] = lWalk[1];
		lAtk[0] = lWalk[1];
		lIdle[0] = lWalk[1];
		
		dWalk[0] = new Texture("Mobs/Duck/anims/down0.png");
		dWalk[1] = new Texture("Mobs/Duck/anims/down1.png");
		dWalk[2] = new Texture("Mobs/Duck/anims/down2.png");
		dWalk[3] = dWalk[1];
		dAtk[0] = dWalk[1];
		dIdle[0] = dWalk[1];
		
		uWalk = loadedTex[uW + slimeAnimInd] = new Texture[4];
		uIdle = loadedTex[uI + slimeAnimInd] = new Texture[1];
		uAtk = loadedTex[uA + slimeAnimInd] = new Texture[1];
		
		rWalk = loadedTex[rW + slimeAnimInd] = new Texture[4];
		rAtk = loadedTex[rA + slimeAnimInd] = new Texture[1];
		rIdle = loadedTex[rI + slimeAnimInd] = new Texture[1];
		
		dWalk = loadedTex[dW + slimeAnimInd] = new Texture[4];
		dAtk = loadedTex[dA + slimeAnimInd] = new Texture[1];
		dIdle = loadedTex[dI + slimeAnimInd] = new Texture[1];
		
		lWalk = loadedTex[lW + slimeAnimInd] = new Texture[4];
		lAtk = loadedTex[lA + slimeAnimInd] = new Texture[1];
		lIdle = loadedTex[lI + slimeAnimInd] = new Texture[1];
		
		uWalk[0] = new Texture("Mobs/Slime/IdleUp.png");
		uWalk[1] = uWalk[0];
		uWalk[2] = uWalk[0];
		uWalk[3] = uWalk[0];
		uIdle[0] = uWalk[0];
		uAtk[0] = uWalk[0];
		
		rWalk[0] = new Texture("Mobs/Slime/IdleRight.png");
		rWalk[1] = rWalk[0];
		rWalk[2] = rWalk[0];
		rWalk[3] = rWalk[0];
		rIdle[0] = rWalk[0];
		rAtk[0] = rWalk[0];
		
		dWalk[0] = new Texture("Mobs/Slime/IdleDown.png");
		dWalk[1] = dWalk[0];
		dWalk[2] = dWalk[0];
		dWalk[3] = dWalk[0];
		dIdle[0] = dWalk[0];
		dAtk[0] = dWalk[0];
		
		lWalk[0] = new Texture("Mobs/Slime/IdleLeft.png");
		lWalk[1] = lWalk[0];
		lWalk[2] = lWalk[0];
		lIdle[0] = lWalk[0];
		lAtk[0] = lWalk[0];
		
		uWalk = loadedTex[uW + playerAnimInd] = new Texture[4];
		uIdle = loadedTex[uI + playerAnimInd] = new Texture[1];
		uAtk = loadedTex[uA + playerAnimInd] = new Texture[1];
		
		rWalk = loadedTex[rW + playerAnimInd] = new Texture[4];
		rAtk = loadedTex[rA + playerAnimInd] = new Texture[1];
		rIdle = loadedTex[rI + playerAnimInd] = new Texture[1];
		
		dWalk = loadedTex[dW + playerAnimInd] = new Texture[4];
		dAtk = loadedTex[dA + playerAnimInd] = new Texture[1];
		dIdle = loadedTex[dI + playerAnimInd] = new Texture[1];
		
		lWalk = loadedTex[lW + playerAnimInd] = new Texture[4];
		lAtk = loadedTex[lA + playerAnimInd] = new Texture[1];
		lIdle = loadedTex[lI + playerAnimInd] = new Texture[1];
		
		uWalk[0] = new Texture("WalkAnim/WalkUp/Up01.PNG");
		uWalk[1] = new Texture("WalkAnim/WalkUp/Up02.PNG");
		uWalk[2] = new Texture("WalkAnim/WalkUp/Up03.PNG");
		uWalk[3] = uWalk[1];
    	uIdle[0] = uWalk[1];
    	uAtk[0] = uWalk[1];
    	
    	rWalk[0] = new Texture("WalkAnim/WalkRight/Right01.PNG");
    	rWalk[1] = new Texture("WalkAnim/WalkRight/Right02.PNG");
    	rWalk[2] = new Texture("WalkAnim/WalkRight/Right03.PNG");
    	rWalk[3] = rWalk[1];
    	rIdle[0] = rWalk[1];
    	rAtk[0] = rWalk[1];
    	
    	dWalk[0] = new Texture("WalkAnim/WalkDown/Down01.PNG");
    	dWalk[1] = new Texture("WalkAnim/WalkDown/Down02.PNG");
    	dWalk[2] = new Texture("WalkAnim/WalkDown/Down03.PNG");
    	dWalk[3] = dWalk[1];
    	dIdle[0] = dWalk[1];
    	dAtk[0] = dWalk[1];
    	
    	lWalk[0] = new Texture("WalkAnim/WalkLeft/Left01.PNG");
    	lWalk[1] = new Texture("WalkAnim/WalkLeft/Left02.PNG");
    	lWalk[2] = new Texture("WalkAnim/WalkLeft/Left03.PNG");
    	lWalk[3] = lWalk[1];
    	lIdle[0] = lWalk[1];
    	lAtk[0] = lWalk[1];
    	
    	uWalk = loadedTex[uW + archerAnimInd] = new Texture[4];
    	uAtk = loadedTex[uA + archerAnimInd] = new Texture[5];
		uIdle = loadedTex[uI + archerAnimInd] = new Texture[1];
		
		rWalk = loadedTex[rW + archerAnimInd] = new Texture[4];
		rAtk = loadedTex[rA + archerAnimInd] = new Texture[5];
		rIdle = loadedTex[rI + archerAnimInd] = new Texture[1];
		
		dWalk = loadedTex[dW + archerAnimInd] = new Texture[4];
		dAtk = loadedTex[dA + archerAnimInd] = new Texture[5];
		dIdle = loadedTex[dI + archerAnimInd] = new Texture[1];
		
		lWalk = loadedTex[lW + archerAnimInd] = new Texture[4];
		lAtk = loadedTex[lA + archerAnimInd] = new Texture[5];
		lIdle = loadedTex[lI + archerAnimInd] = new Texture[1];
		
		uWalk[0] = new Texture("Mobs/Archer/up/up0.PNG");
		uWalk[1] = new Texture("Mobs/Archer/up/up1.PNG");
		uWalk[2] = new Texture("Mobs/Archer/up/up2.PNG");
		uWalk[3] = uWalk[1];
    	uIdle[0] = new Texture("Mobs/Archer/up/idle.PNG");
    	uAtk[0] = new Texture("Mobs/Archer/up/attack0.PNG");
    	uAtk[1] = new Texture("Mobs/Archer/up/attack1.PNG");
    	uAtk[2] = new Texture("Mobs/Archer/up/attack2.PNG");
    	uAtk[3] = new Texture("Mobs/Archer/up/attack3.PNG");
    	uAtk[4] = new Texture("Mobs/Archer/up/attack4.PNG");
    	
    	rWalk[0] = new Texture("Mobs/Archer/right/right0.PNG");
    	rWalk[1] = new Texture("Mobs/Archer/right/right1.PNG");
    	rWalk[2] = new Texture("Mobs/Archer/right/right2.PNG");
    	rWalk[3] = rWalk[1];
    	rIdle[0] = new Texture("Mobs/Archer/right/idle.PNG");
    	rAtk[0] = new Texture("Mobs/Archer/right/attack0.PNG");
    	rAtk[1] = new Texture("Mobs/Archer/right/attack1.PNG");
    	rAtk[2] = new Texture("Mobs/Archer/right/attack2.PNG");
    	rAtk[3] = new Texture("Mobs/Archer/right/attack3.PNG");
    	rAtk[4] = new Texture("Mobs/Archer/right/attack4.PNG");

    	dWalk[0] = new Texture("Mobs/Archer/down/down0.PNG");
    	dWalk[1] = new Texture("Mobs/Archer/down/down1.PNG");
    	dWalk[2] = new Texture("Mobs/Archer/down/down2.PNG");
    	dWalk[3] = dWalk[1];
		dIdle[0] = new Texture("Mobs/Archer/down/idle.PNG");
    	dAtk[0] = new Texture("Mobs/Archer/down/attack0.PNG");
    	dAtk[1] = new Texture("Mobs/Archer/down/attack1.PNG");
    	dAtk[2] = new Texture("Mobs/Archer/down/attack2.PNG");
    	dAtk[3] = new Texture("Mobs/Archer/down/attack3.PNG");
    	dAtk[4] = new Texture("Mobs/Archer/down/attack4.PNG");

    	lWalk[0] = new Texture("Mobs/Archer/left/left0.PNG");
		lWalk[1] = new Texture("Mobs/Archer/left/left1.PNG");
		lWalk[2] = new Texture("Mobs/Archer/left/left2.PNG");
		lWalk[3] = lWalk[1];
    	lIdle[0] = new Texture("Mobs/Archer/left/idle.PNG");
    	lAtk[0] = new Texture("Mobs/Archer/left/attack0.PNG");
    	lAtk[1] = new Texture("Mobs/Archer/left/attack1.PNG");
    	lAtk[2] = new Texture("Mobs/Archer/left/attack2.PNG");
    	lAtk[3] = new Texture("Mobs/Archer/left/attack3.PNG");
    	lAtk[4] = new Texture("Mobs/Archer/left/attack4.PNG");
    	
    	Texture[] heal = loadedTex[healAnimInd] = new Texture[22];
    	heal[0] = new Texture("Particles/heal0.png");
    	heal[1] = new Texture("Particles/heal1.png");
    	heal[2] = new Texture("Particles/heal2.png");
    	heal[3] = new Texture("Particles/heal3.png");
    	heal[4] = new Texture("Particles/heal4.png");
    	heal[5] = new Texture("Particles/heal5.png");
    	heal[6] = new Texture("Particles/heal6.png");
    	heal[7] = new Texture("Particles/heal7.png");
    	heal[8] = new Texture("Particles/heal8.png");
    	heal[9] = new Texture("Particles/heal9.png");
    	heal[10] = new Texture("Particles/heal10.png");
    	heal[11] = new Texture("Particles/heal11.png");
    	heal[12] = new Texture("Particles/heal12.png");
    	heal[13] = new Texture("Particles/heal13.png");
    	heal[14] = new Texture("Particles/heal14.png");
    	heal[15] = new Texture("Particles/heal15.png");
    	heal[16] = new Texture("Particles/heal16.png");
    	heal[17] = new Texture("Particles/heal17.png");
    	heal[18] = new Texture("Particles/heal18.png");
    	heal[19] = new Texture("Particles/heal19.png");
    	heal[20] = new Texture("Particles/heal20.png");
    	heal[21] = new Texture("Particles/heal21.png");
    }
    

	
	//uW0 = up walk 0
	//uI = up idle
	//uA = up attack
	public static final int uW = 0;
	public static final int uI = 1;
	public static final int uA = 2;
	
	public static final int rW = 3;
	public static final int rI = 4;
	public static final int rA = 5;
	
	public static final int dW = 6;
	public static final int dI = 7;
	public static final int dA = 8;
	
	public static final int lW = 9;
	public static final int lI = 10;
	public static final int lA = 11;
    
    private static final int pauseLength = 8;
    
    protected void setAnim(int ind)
    {
    	currAnim = anims[ind];
    	currAnim.reset();
    }
    /**
     * Sets the animation toward a new direction, walking
     * @param newDirec
     */
    public void walkInDirec(int newDirec)
    {
    	if (walkDirec != newDirec || idling)
    	{
    		switch (newDirec) {
				case up: setAnim(uW); break;
				case right: setAnim(rW); break;
				case down: setAnim(dW); break;
				case left: setAnim(lW); break;
			}
    		setDirec(newDirec);
    		idling = false;
    	}
    }
    public void setDirec(int newDirec) {
    	walkDirec = newDirec;
    }
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
