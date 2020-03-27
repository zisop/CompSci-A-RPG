package Combat;



import java.util.ArrayList;

import Game.Door;
import Game.Main;
import Imported.Audio;
import Imported.Texture;
import LowLevel.Geometry;
import LowLevel.Point;
import LowLevel.Positionable;
import UI.Item;
import UI.ItemBag;
import UI.ItemEffect;
import UI.ItemSlot;
import UI.SpellSlot;
import UI.TextBox;
import UI.UI;

public class Player extends CombatChar
{
	private static double xInteractionRadius = 20;
	private ArrayList<Projectile> orbit;
	
	private Point[] interactionPoints;
	private boolean casting;
	private AOE cast;
	
	private int level;
	private double exp;
	
	private double baseHealth;
	private double baseMana;
	private double baseHealthRegen;
	private double baseManaRegen;
	private double baseArmor;
	private double baseAttackMultiplier;
	private double itemAttackMultiplier;
	private double itemExpMultiplier;
	
	//xps for each level
	private static double[] levelXPs = {100, 150, 200};
	
    public Player(double inX, double inY) {
    	//showWidth = 70, showLength = 70, hitBoxWidth = 35, hitBoxLength = 10, hitBoxDown 25 pixels
        super(null, inX, inY, 70, 70);
        setHitWidth(35);
        setHitLength(10);
        hitBoxDown(25);
        Point[] collisionBasis = getCollisionBasis();
        Point p1 = new Point(collisionBasis[DL].getX() - xInteractionRadius, collisionBasis[DL].getY() - xInteractionRadius);
        Point p2 = new Point(collisionBasis[DR].getX() + xInteractionRadius, collisionBasis[DR].getY() - xInteractionRadius);
        Point p3 = new Point(collisionBasis[UR].getX() + xInteractionRadius, collisionBasis[UR].getY() + xInteractionRadius);
        Point p4 = new Point(collisionBasis[UL].getX() - xInteractionRadius, collisionBasis[UL].getY() + xInteractionRadius);
        interactionPoints = new Point[] {p1, p2, p3, p4};

        speed = baseSpeed;
        
        orbit = new ArrayList<Projectile>();
        
        setLevel(1);
        
        
        walkAnimSwitch = 6;
        soundFXSwitch = 20;
        walkVolume = .2;
        
        anims = getAnims(playerAnimInd, playerAnimInd + 19);
        setImage(anims[0]);
        walkSounds = getSounds(playerSoundInd, playerSoundInd + 0);
        firstSound = 6;
        setEnemyState(good);
        handleCombatException();
        casting = false;
        
        itemExpMultiplier = 1;
        itemProcessFrame = 0;
        exp = 0;
    }
    public void gainXP(double xp)
    {
    	if (level == getMaxLevel()) {return;}
    	exp += (xp * itemExpMultiplier);
    	double max = getXPMax();
    	if (exp >= max)
    	{
    		exp -= max;
    		setLevel(getLevel() + 1);
    		if (level == getMaxLevel()) {exp = max;}
    	}
    }
    private int itemProcessFrame;
    //process items every 15 frames
    private static int shouldProcess = 15;
    public void show()
    {
    	if (itemProcessFrame++ == shouldProcess) {processItems(); itemProcessFrame = 0;}
    	if (!Main.alreadyInteracting)
    	{
    		setMana(mana + manaRegen);
    		setHealth(health + healthRegen);
    		manageProjectiles();
    	}
    	showSpells();
    	manageSpells();
    	
    	super.show();
    }
    public void setSpell(int slot, int ID)
    {
    	UI.selectedSpells[slot].setSpell(ID);
    }
    
    /**
     * Shows all the player's visible spells
     * Adds them to the visProj arrayList so they will be displayed at the top of the screen
     */
    private void showSpells()
    {
    	for (int i = orbit.size() - 1; i >= 0; i--)
    	{
    		Projectile curr = orbit.get(i);
    		Main.allRooms[Main.currRoom].tempShow(curr);
    	}
    	if (casting) 
    	{
    		cast.setPos(Main.cursor.getX() + Main.player.getX(), Main.cursor.getY() + Main.player.getY());
    		Main.allRooms[Main.currRoom].tempShow(cast);
    		if (Main.leftClick && !Main.leftClickLastFrame)
    		{
    			cast.place();
    			updateCastState(false);
    			int ID = cast.getID();
    			switch(ID)
    			{
    				case AOE.damageCloud:
    					mana -= poisonCost;
    			}
    		}
    		else if (Main.rightClick && !Main.rightClickLastFrame)
    		{
    			updateCastState(false);
    		}
    	}
    }
    private void updateCastState(boolean flag)
    {
    	casting = flag;
    	Main.alreadyInteracting = flag;
    }
    /**
     * Casts a spell corresponding to the ID
     * @param ID
     */
    public void castSpell(int ID)
    {
    	switch(ID)
    	{
    		case Projectile.fireball:
    			if (mana >= fireBallCost)
    			{
    				double projSpeed = 10;
    				double mouseAngle = Main.cursorAngle();
    				int smallestInd = 0;
    				for (int i = 1; i < orbit.size(); i++)
    				{
    					double smallestDist = Math.abs(orbit.get(smallestInd).getOrbitAngle() - mouseAngle);
    					double currDist = Math.abs(orbit.get(i).getOrbitAngle() - mouseAngle);
    					if (currDist < smallestDist)
    					{
    						smallestInd = i;
    					}	
    				}
    				Projectile shot = orbit.get(smallestInd);
    				orbit.remove(smallestInd);
    				double xDiff = Main.cursor.getX() + getX() - shot.getX();
    				double yDiff = Main.cursor.getY() + getY() - shot.getY();
    				double hypoLen = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    				double shotAngle = Math.toDegrees(Math.acos(xDiff / hypoLen));
    				if (yDiff < 0) {shotAngle *= -1;}
    				shot.setAngle(shotAngle);
    				shot.setOrbit(false);
    				shot.setSpeed(projSpeed);
    				mana -= fireBallCost;
    				shot.setDamage(10 * itemAttackMultiplier);
    				shot.setHitLength(shot.getHitLength() * 3 / 5);
    				shot.setInvuln(8);
    				shot.setStun(8);
    				Audio.playSound("Batt/fireball", .48);
    				Main.allRooms[Main.currRoom].permaShow(shot);
    			}
    			return;
    		case AOE.damageCloud:
    			if (mana >= poisonCost)
    			{
    				cast = new AOE(AOE.damageCloud, this);
    				cast.setDamage(cast.getDamage() * itemAttackMultiplier);
    				updateCastState(true);
    			}
    			return;
    		case CombatChar.heal:
    			if (mana >= healCost && health < maxHealth)
    			{
    				double conversionRatio = .3;
    				setMana(getMana() - healCost);
    				Audio.playSound("Spells/blessing2", .3);
    				double healthIncrease = healCost * conversionRatio;
    				int numTicks = 20;
    				int tickFrame = 1;
    				double[] effectInfo = new double[3];
    				effectInfo[Effect.healDuration] = numTicks * tickFrame;
    				effectInfo[Effect.healTick] = healthIncrease / numTicks;
    				effectInfo[Effect.tickFrame] = tickFrame;
    				
    				Effect effect = new Effect(Effect.heal, effectInfo, this);
    				receiveEffect(effect);
    			}
    			return;
    		case CombatChar.powerUp:
    			if (mana >= powerUpCost)
    			{
    				double[] effectInfo = new double[2];
    				double multiplier = 1.3;
    				int duration = 210;
    				effectInfo[Effect.powerDuration] = duration;
    				effectInfo[Effect.powerMultiplier] = multiplier;
    				Effect effect = new Effect(Effect.powerUp, effectInfo, this);
    				receiveEffect(effect);
    				mana -= powerUpCost;
    			}
    			return;
    		case AOE.lightning:
    			if (mana >= lightningCost)
    			{
    				AOE lightning = new AOE(AOE.lightning, this);
    				double angle = Main.cursorAngle();
    				double radius = lightning.getLength() / 2 + getWidth() / 2;
    				lightning.setAngle(angle - 90);
    				angle = Math.toRadians(angle);
    				lightning.setPos(getX() + radius * Math.cos(angle), getY() + radius * Math.sin(angle));
    				lightning.place();
    				lightning.setDamage(lightning.getDamage() * itemAttackMultiplier);
    				mana -= lightningCost;
    			}
    			return;
    		case Projectile.door:
    			if (mana >= doorCost)
    			{
    				double mouseAngle = Main.cursorAngle();
    				mouseAngle = Math.toRadians(mouseAngle);
    				double radiusFromPlayer = 30;
    				double x = getX() + Math.cos(mouseAngle) * radiusFromPlayer;
    				double y = getY() + Math.sin(mouseAngle) * radiusFromPlayer;
    				Projectile shot = new Projectile(Projectile.door, x, y, Math.toDegrees(mouseAngle), this);
    				shot.setAngle(Main.random.nextDouble() * 360);
    				//Audio.playSound("Batt/fireball", .48);
    				Main.allRooms[Main.currRoom].permaShow(shot);
    				mana -= doorCost;
    			}
    			return;
    	}
    	try {
			throw new Exception("Spell ID " + ID + " didnt exist");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
    }
    public int getLevel() {return level;}
    public double getXP() {return exp;}
    public double getXPMax() {
    	if (level == getMaxLevel())
    	{
    		return levelXPs[level - 2];
    	}
    	return levelXPs[level - 1];
    }
    
    
    private void setLevel(int newLevel) {
    	level = newLevel;
    	switch (level) {
			case 1:
				//player stats for level 1
				baseHealth = 100;
		        baseMana = 100;
		        baseManaRegen = .5;
		        baseHealthRegen = .02;
		        baseAttackMultiplier = 1;
		        baseArmor = 0;
		        break;
			case 2:
				baseHealth = 110;
		        baseMana = 105;
		        baseManaRegen = .55;
		        baseHealthRegen = .03;
		        baseAttackMultiplier = 1.05;
		        baseArmor = 1;
		        break;
			case 3:
				baseHealth = 115;
		        baseMana = 110;
		        baseManaRegen = .6;
		        baseHealthRegen = .04;
		        baseAttackMultiplier = 1.1;
		        baseArmor = 3;
		        break;
			case 4:
				baseHealth = 120;
		        baseMana = 110;
		        baseManaRegen = .6;
		        baseHealthRegen = .04;
		        baseAttackMultiplier = 1.1;
		        baseArmor = 4;
		        break;
		    default:
		    	try {
					throw new Exception("Level: " + newLevel + " stats were not implemented for player");
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(0);
				}
		}
    	processItems();
    	health = maxHealth;
    	mana = maxMana;
    }
    
    private void processItems()
    {
    	ItemBag equipment = UI.armorBag;
    	ArrayList<Item> items = Item.extractItems(equipment.getSlots());
    	double[] totalEffects = new double[ItemEffect.numEffects];
    	for (Item item : items)
    	{
    		ItemEffect[] effects = item.getEffects();
    		for (ItemEffect effect : effects)
    		{
    			int ID = effect.getID();
    			totalEffects[ID] += effect.getInfo();
    		}
    	}
    	
    	
    	
    	maxHealth = baseHealth + totalEffects[ItemEffect.healthAdd];
    	maxMana = baseMana + totalEffects[ItemEffect.manaAdd];
    	setHealth(Math.min(maxHealth, health));
    	setMana(Math.min(maxMana, mana));
    	manaRegen = baseManaRegen + totalEffects[ItemEffect.manaRegenAdd];
    	armor = baseArmor + totalEffects[ItemEffect.armorAdd];
    	healthRegen = baseHealthRegen + totalEffects[ItemEffect.healthRegenAdd];
    	itemAttackMultiplier = baseAttackMultiplier + totalEffects[ItemEffect.damageMult];
    	itemExpMultiplier = 1 + totalEffects[ItemEffect.expAdd];
    	updateStatDisplay();
    }
    private void updateStatDisplay()
    {
    	TextBox[] boxes = UI.statText;
    	boxes[ItemEffect.armorAdd].setText("Armor: " + (int)(armor));
    	boxes[ItemEffect.healthAdd].setText("Health: " + (int)(maxHealth));
    	boxes[ItemEffect.manaAdd].setText("Mana: " + (int)(maxMana));
    	boxes[ItemEffect.manaRegenAdd].setText("MNregen: " + (int)(manaRegen * 10));
    	boxes[ItemEffect.healthRegenAdd].setText("hpregen: " + (int)(healthRegen * 10));
    	boxes[ItemEffect.expAdd].setText("EXP bonus: +" + (int)(itemExpMultiplier * 100 - 100) + "%");
    	boxes[ItemEffect.damageMult].setText("Damage: +" + (int)((itemAttackMultiplier * damageMultiplier) * 100 - 100) + "%");
    }
    
    
    public void setXP(double newXP) {exp = newXP;}
    public int getMaxLevel() {return levelXPs.length + 1;}

    /**
     * Determines where to place projectiles
     */
    private void manageProjectiles()
    {
    	int numProj = (int)(100 / fireBallCost);
    	int idealNum = (int)(mana / fireBallCost);
    	if (orbit.size() < idealNum)
    	{
    		double angle = 0;
    		if (orbit.size() > 0)
    		{
    			double[] angles = new double[orbit.size()];
    			for (int i = 0; i < angles.length; i++)
    			{
    				angles[i] = orbit.get(i).getOrbitAngle(); 
    			}
    			int minInd = 0;
    			for (int i = 1; i < angles.length; i++)
    			{
    				if (angles[i] < angles[minInd])
    				{
    					minInd = i;
    				}
    			}
    			angle = angles[minInd] + 360 / numProj;
    			for (int i = 0; i < angles.length; i++)
    			{
    				if (Math.abs(angle - angles[i]) <= .0001)
    				{
    					angle += 360 / numProj;
    				}
    			}
    		}
    		Projectile currProj = new Projectile(Projectile.fireball, 0, 0, Projectile.facingUp, this);
    		currProj.setOrbit(true);
    		currProj.setOrbitAngle(angle);
    		double orbitRadius = currProj.getOrbitRadius();
    		double mathAngle = Math.toRadians(angle);
			double xDist = orbitRadius * Math.cos(mathAngle);
			double yDist = orbitRadius * Math.sin(mathAngle);
			currProj.setPos(getX() + xDist, getY() + yDist);
    		
    		
    		orbit.add(currProj);
    		currProj.setDamage(.2 * itemAttackMultiplier);
    	}
    }
    /**
     * Handles spell cast inputs from the player
     */
    private void manageSpells()
    {
    	int[] bindedSpells = UI.getBindedSpells();
    	if (!Main.alreadyInteracting && !(UI.spellBagVisible && UI.mouseHovering(UI.spellBag)))
    	{
    		if (Main.one && !Main.oneLastFrame && bindedSpells[0] != SpellSlot.noSpell) {castSpell(bindedSpells[0]);}
    		else if (Main.two && !Main.twoLastFrame && bindedSpells[1] != SpellSlot.noSpell) {castSpell(bindedSpells[1]);}
    		else if (Main.three && !Main.threeLastFrame && bindedSpells[2] != SpellSlot.noSpell) {castSpell(bindedSpells[2]);}
    		else if (Main.four && !Main.fourLastFrame && bindedSpells[3] != SpellSlot.noSpell) {castSpell(bindedSpells[3]);}
    	}
    }
    
    
    
    /**
     * Determines whether the character was within player's X button pressing collision radius
     * @param otherChar
     * @return player can interact with the x button
     */
    public boolean interactionCollision(Positionable otherChar)
    {
    	Point[] otherBasis = otherChar.getCollisionBasis();
    	return Geometry.colliding(interactionPoints, otherBasis);
    }
    public void setWidth(double newWidth)
    {
    	super.setWidth(newWidth);
    	Point[] collisionBasis = getCollisionBasis();
        interactionPoints[DL].setX(collisionBasis[DL].getX() - xInteractionRadius);
        interactionPoints[UL].setX(collisionBasis[UL].getX() - xInteractionRadius);
        interactionPoints[UR].setX(collisionBasis[UR].getX() + xInteractionRadius);
        interactionPoints[DR].setX(collisionBasis[DR].getX() + xInteractionRadius);
    }
    public void setLength(double newLength)
    {
    	super.setLength(newLength);
    	Point[] collisionBasis = getCollisionBasis();
        interactionPoints[DL].setY(collisionBasis[DL].getY() - xInteractionRadius);
        interactionPoints[UL].setY(collisionBasis[UL].getY() - xInteractionRadius);
        interactionPoints[UR].setY(collisionBasis[UR].getY() + xInteractionRadius);
        interactionPoints[DR].setY(collisionBasis[DR].getY() + xInteractionRadius);
    }
    public void setX(double newX)
    {
    	double xDiff = newX - getX();
    	super.setX(newX);
    	for (int i = 0; i < interactionPoints.length; i++)
    	{
    		interactionPoints[i].setX(interactionPoints[i].getX() + xDiff);
    	}
    }
    public void setY(double newY)
    {
    	double yDiff = newY - getY();
    	super.setY(newY);
    	for (int i = 0; i < interactionPoints.length; i++)
    	{
    		interactionPoints[i].setY(interactionPoints[i].getY() + yDiff);
    	}
    }
    
    private static final double lightningCost = 30;
    private static final double fireBallCost = 25;
    private static final double poisonCost = 30;
    private static final double healCost = 40;
    private static final double powerUpCost = 50;
    private static final double doorCost = 60;
    public final static double baseSpeed = 8;
}
