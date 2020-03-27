package UI;

import java.util.ArrayList;

import Combat.AOE;
import Combat.CombatChar;
import Combat.Projectile;
import Exchange.ShopKeeper;
import Game.Main;
import Game.NPC;
import LowLevel.Geometrical;
import LowLevel.Geometry;
import LowLevel.Image;
import LowLevel.Point;
import LowLevel.Positionable;
import LowLevel.Shape;

public class UI {
	public static ArrayList<ItemBag> allBags = new ArrayList<ItemBag>();
    public static ItemBag playerBag;
    public static ItemBag armorBag;
    public static Geometrical statBars;
    public static Geometrical statDisplay;
    public static boolean statsShowing;
    public static ArrayList<ToolTip> visTips = new ArrayList<ToolTip>();
    public static ArrayList<Item> visItems = new ArrayList<Item>();
    public static ArrayList<ShopKeeper> interactingKeepers = new ArrayList<ShopKeeper>();
    public static ArrayList<NPC> talkingNPCs = new ArrayList<NPC>();
    //Can't be done in the ItemBag or Chest class because they dont store player's itemBag
    //Shows all visible ItemBags
    public static void showUI()
    {
    	if (statsShowing)
    	{
    		showStats();
    	}
    	showNPCText();
    	showMenus();
    	showBags();
    }
    public static void init()
    {
    	//Inits the entire UI
    	initInventory();
    	initStats();
    	initSpellBag();
    	initStatDisplay();
    }
    public static int[] getBindedSpells()
    {
    	int[] bindedSpells = new int[selectedSpells.length];
    	for (int i = 0; i < bindedSpells.length; i++)
    	{
    		bindedSpells[i] = selectedSpells[i].getSpellID();
    	}
    	return bindedSpells;
    }
    
    /**
     * determines if mouse hovers over UI object
     * @param obj
     * @return hovered == true 
     */
    public static boolean mouseHovering(Positionable obj)
    {
    	Point[] objPoints = obj.getShowBasis();
    	Point cursorPoint = new Point(Main.cursor.getX(), Main.cursor.getY());
    	return Geometry.insideShape(objPoints, cursorPoint);
    }
    /**
     * Determines if an object in UI was clicked
     * @param obj
     * @return clicked == true
     */
    public static boolean mouseInteraction(Positionable obj)
    {
    	return shouldInteract() && mouseHovering(obj);
    }
    /**
     * Determines if the player left clicked on this frame
     * @return leftClick && !leftClickLastFrame
     */
    public static boolean shouldInteract()
    {
    	return Main.leftClick && !Main.leftClickLastFrame;
    }
    
    private static Geometrical createSlot(String name)
    {
    	Geometrical boxDisplay = new Geometrical();
    	Shape outBox = Geometry.createRect(-2, 27, -2, 27, 150, 150, 150, 255);
    	Shape inBox = Geometry.createRect(0, 25, 0, 25, 3, 162, 162, 255);
    	boxDisplay.addShape(outBox);
    	boxDisplay.addShape(inBox);
    	
    	double xPos = 0;
    	switch (name) {
			case "Helmet":
				xPos = 14;
				break;
			case "Ring":
				xPos = 24;
				break;
			case "Tome":
				xPos = 21;
				break;
			case "Wand":
				xPos = 21;
				break;
			default:
				try {
					throw new Exception("Item type " + name + " wasn't implemented");
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(0);
				}
		}
    	Geometrical helmTextPos = new Geometrical();
    	helmTextPos.addShape(new Shape(0, xPos, 37, 60, 10, 0, 0, 0, 0));
    	TextBox itemText = new TextBox(8, name, helmTextPos, 255);
    	Geometrical slotDisplay = new Geometrical();
    	slotDisplay.addShape(boxDisplay);
    	slotDisplay.addShape(itemText);
    	
    	return slotDisplay;
    }
    public static Geometrical spellBag;
    public static SpellSlot[] selectedSpells;
    public static boolean spellBagVisible = false;
    private static void initSpellBag()
    {
    	selectedSpells = new SpellSlot[4];
    	spellBag = new Geometrical();
    	double width = 40;
    	double length = 40;
    	double distance = 55;
    	double offset = 12;
    	Shape outerRect = Geometry.createRect(
    	//x1, x2
    	-offset - width / 2, offset + width / 2 + distance * 3,
    	//y1, y2
    	offset + length / 2 + distance, -(offset + length / 2 + distance * 1.5),
    	//color
    	100, 100, 100, 255);
    	offset = 8;
    	Shape innerRect = Geometry.createRect(
    	//x1, x2
    	-offset - width / 2, offset + width / 2 + distance * 3,
    	//y1, y2
    	offset + length / 2 + distance, -(offset + length / 2 + distance * 1.5),
    	//Color
    	150, 150, 150, 255);
    	
    	spellBag.addShape(outerRect);
    	spellBag.addShape(innerRect);
    	SpellSlot fireSlot = new SpellSlot(distance * 0, distance * -.5, width, length, SpellSlot.displaysSpells);
    	fireSlot.setSpell(Projectile.fireball);
    	
    	SpellSlot lightningSlot = new SpellSlot(distance * 1, distance * -.5, width, length, SpellSlot.displaysSpells);
    	lightningSlot.setSpell(AOE.lightning);
    	
    	SpellSlot damageCloud = new SpellSlot(distance * 2, distance * -.5, width, length, SpellSlot.displaysSpells);
    	damageCloud.setSpell(AOE.damageCloud);
    	
    	SpellSlot healSlot = new SpellSlot(distance * 3, distance * -.5, width, length, SpellSlot.displaysSpells);
    	healSlot.setSpell(CombatChar.heal);
    	
    	SpellSlot powerSlot = new SpellSlot(distance * 0, distance * -1.5, width, length, SpellSlot.displaysSpells);
    	powerSlot.setSpell(CombatChar.powerUp);
    	
    	SpellSlot doorSlot = new SpellSlot(distance * 1, distance * -1.5, width, length, SpellSlot.displaysSpells);
    	doorSlot.setSpell(Projectile.door);
    	
    	for (int i = 0; i < selectedSpells.length; i++)
    	{
    		selectedSpells[i] = new SpellSlot(distance * i, distance, width, length, SpellSlot.acceptsSpells);
    		spellBag.addShape(selectedSpells[i]);
    	}
    	
    	
    	spellBag.addShape(lightningSlot);
    	spellBag.addShape(fireSlot);
    	spellBag.addShape(damageCloud);
    	spellBag.addShape(healSlot);
    	spellBag.addShape(powerSlot);
    	spellBag.addShape(doorSlot);
    	spellBag.setPos(-360, 220);
    }
    public static TextBox[] statText;
    private static void initStatDisplay()
    {
    	statDisplay = new Geometrical();
    	double offset = 3;
    	double width = spellBag.getWidth() - offset * 2;
    	double length = 140;
    	Shape mainRect = Geometry.createRect(-offset, width + offset, -offset, length + offset, 100, 100, 100, 255);
    	Shape innerRect = Geometry.createRect(0, width, 0, length, 52, 107, 235, 255);
    	statDisplay.addShape(mainRect);
    	statDisplay.addShape(innerRect);
    	
    	statText = new TextBox[ItemEffect.numEffects];
    	double fontSize = 10;
    	Image textRect = Geometry.createRect(0, width, length - fontSize, length, 0, 0, 0, 0);
    	float r = 24; float g = 53; float b = 22;
    	
    	statText[ItemEffect.healthAdd] = new TextBox(fontSize, "Health: ", textRect, 255);
    	statText[ItemEffect.healthAdd].setTextRGBA(r, g, b, 255);
    	statDisplay.addShape(statText[ItemEffect.healthAdd]);
    	offset = fontSize + 10;
    	textRect = Image.createCopy(textRect);
    	textRect.setY(textRect.getY() - offset);
    	statText[ItemEffect.manaAdd] = new TextBox(fontSize, "Mana: ", textRect, 255);
    	statDisplay.addShape(statText[ItemEffect.manaAdd]);
    	statText[ItemEffect.manaAdd].setTextRGBA(r, g, b, 255);
    	textRect = Image.createCopy(textRect);
    	textRect.setY(textRect.getY() - offset);
    	statText[ItemEffect.healthRegenAdd] = new TextBox(fontSize, "HPRegen: ", textRect, 255);
    	statDisplay.addShape(statText[ItemEffect.healthRegenAdd]);
    	statText[ItemEffect.healthRegenAdd].setTextRGBA(r, g, b, 255);
    	textRect = Image.createCopy(textRect);
    	textRect.setY(textRect.getY() - offset);
    	statText[ItemEffect.manaRegenAdd] = new TextBox(fontSize, "MNRegen: ", textRect, 255);
    	statDisplay.addShape(statText[ItemEffect.manaRegenAdd]);
    	statText[ItemEffect.manaRegenAdd].setTextRGBA(r, g, b, 255);
    	textRect = Image.createCopy(textRect);
    	textRect.setY(textRect.getY() - offset);
    	statText[ItemEffect.armorAdd] = new TextBox(fontSize, "Armor: ", textRect, 255);
    	statDisplay.addShape(statText[ItemEffect.armorAdd]);
    	statText[ItemEffect.armorAdd].setTextRGBA(r, g, b, 255);
    	textRect = Image.createCopy(textRect);
    	textRect.setY(textRect.getY() - offset);
    	statText[ItemEffect.damageMult] = new TextBox(fontSize, "Damage: ", textRect, 255);
    	statDisplay.addShape(statText[ItemEffect.damageMult]);
    	statText[ItemEffect.damageMult].setTextRGBA(r, g, b, 255);
    	textRect = Image.createCopy(textRect);
    	textRect.setY(textRect.getY() - offset);
    	statText[ItemEffect.expAdd] = new TextBox(fontSize, "EXP Bonus: ", textRect, 255);
    	statDisplay.addShape(statText[ItemEffect.expAdd]);
    	statText[ItemEffect.expAdd].setTextRGBA(r, g, b, 255);
    	
    	offset = 10;
    	statDisplay.setPos(spellBag.getX(), spellBag.getY() - spellBag.getLength() / 2 - length / 2 - offset);
    }
    
    private static void initInventory()
    {
    	
    	//Player bag display
    	playerBag = new ItemBag(-395, 235, 40, 40, 4, 4);
    	Geometrical armor = new Geometrical();
    	
    	Shape armorMainRect = Geometry.createRect(-2, 152, -2, 177, 100, 100, 100, 255);
    	Shape armorInRect = Geometry.createRect(0, 150, 0, 175, 161, 101, 9, 255);

    	Geometrical wandSlotDisplay = createSlot("Wand");
    	ItemSlot wandSlot = new ItemSlot(wandSlotDisplay.getX(), wandSlotDisplay.getY(), 25, 25, Item.wandType);
    	Geometrical ringSlotDisplay = createSlot("Ring");
    	ItemSlot ringSlot = new ItemSlot(ringSlotDisplay.getX(), ringSlotDisplay.getY(), 25, 25, Item.ringType);
    	Geometrical bookSlotDisplay = createSlot("Tome");
    	ItemSlot bookSlot = new ItemSlot(bookSlotDisplay.getX(), bookSlotDisplay.getY(), 25, 25, Item.bookType);
    	Geometrical helmSlotDisplay = createSlot("Helmet");
    	ItemSlot helmSlot = new ItemSlot(helmSlotDisplay.getX(), helmSlotDisplay.getY(), 25, 25, Item.helmType);
    	
    	
    	helmSlotDisplay.setPos(75, 140);
    	helmSlot.setPos(helmSlotDisplay.getX(), helmSlotDisplay.getY());
    	
    	wandSlotDisplay.setPos(125, 100);
    	wandSlot.setPos(wandSlotDisplay.getX(), wandSlotDisplay.getY());
    	
    	ringSlotDisplay.setPos(125, 50);
    	ringSlot.setPos(ringSlotDisplay.getX(), ringSlotDisplay.getY());
    	
    	bookSlotDisplay.setPos(25, 100);
    	bookSlot.setPos(bookSlotDisplay.getX(), bookSlotDisplay.getY());
    	
    	armor.addShape(armorMainRect);
    	armor.addShape(armorInRect);
    	armor.addShape(helmSlotDisplay);
    	armor.addShape(ringSlotDisplay);
    	armor.addShape(bookSlotDisplay);
    	armor.addShape(wandSlotDisplay);
    	
    	ItemSlot[] slots = {wandSlot, ringSlot, bookSlot, helmSlot};
    	
    	armorBag = new ItemBag(armor, slots);
    	armorBag.setPos(-395, 55);
    }
    
    public static final int maxHP = 3;
    public static final int maxMN = 4;
    public static final int HP = 5;
    public static final int MN = 6;
    public static final int maxXP = 8;
    public static final int XP = 9;
    
    private static void initStats()
    {
    	//Stats Display
    	statBars = new Geometrical();
    	statsShowing = true;
    	Shape mainRect = Geometry.createRect(-2, 402, -2, 52, 0, 0, 0, 255);
    	Shape innerRect = Geometry.createRect(0, 400, 0, 50, 100, 100, 100, 255);
    	Shape healthRect = Geometry.createRect(54, 396, 27, 48, 150, 0, 0, 255);
    	Shape manaRect = Geometry.createRect(54, 396, 2, 23, 0, 0, 150, 255);
    	Shape charSlot = Geometry.createRect(4, 46, 4, 46, 255, 255, 255, 255);
    	Shape healthBar = Geometry.createRect(54, 396, 27, 48, 255, 0, 0, 255);
    	Shape manaBar = Geometry.createRect(54, 396, 2, 23, 0, 0, 255, 255);
    	
    	Shape xpBorder = Geometry.createRect(3, 252, -15, -4, 100, 100, 100, 255);
    	Shape xpBar = Geometry.createRect(5, 250, -14, -5, 200, 200, 20, 255);
    	Shape xpRect = Geometry.createRect(5, 250, -14, -5, 130, 130, 15, 255);
    	//Index 0
    	statBars.addShape(mainRect);
    	statBars.addShape(innerRect);
    	statBars.addShape(charSlot);
    	//MaxHealth = ind3, MaxMana = ind4
    	statBars.addShape(healthRect);
    	statBars.addShape(manaRect);
    	//healthBar = index 5, manaBar = index 6
    	statBars.addShape(healthBar);
    	statBars.addShape(manaBar);
    	
    	//xpRect = index 8, xpBar = index 9
    	statBars.addShape(xpBorder);
    	statBars.addShape(xpRect);
    	statBars.addShape(xpBar);
    	statBars.setPos(-275, 365);
    }
    private static void showNPCText()
    {
    	talkingNPCs.forEach((npc) -> npc.showText());
    	talkingNPCs.clear();
    }
    private static void showBags()
    {
    	if (Main.r && !Main.rLastFrame)
    	{
    		spellBagVisible = !spellBagVisible;
    		if (spellBagVisible && playerBag.getVisibility()) {playerBag.setVisibility(false); armorBag.setVisibility(false);}
    	}
    	if (Main.e && !Main.eLastFrame)
        {
        	playerBag.setVisibility(!playerBag.getVisibility());
        	armorBag.setVisibility(playerBag.getVisibility());
        	if (playerBag.getVisibility() && spellBagVisible) {spellBagVisible = false;}
        }
    	if (spellBagVisible)
    	{
    		spellBag.UIshow();
    		statDisplay.UIshow();
    	}
    	for (int i = 0; i < allBags.size(); i++)
    	{
    		ItemBag currBag = allBags.get(i);
    		if (currBag.isShowing())
    		{
    			currBag.UIshow();
    		}
    	}
    	visItems.forEach((item) -> item.UIshow());
    	visItems.clear();
    	visTips.forEach((tip) -> tip.UIshow());
    	visTips.clear();
    	
    	if (ItemBag.holdingItem)
    	{
    		if (ItemBag.heldItem.getQuantity() == 0) {ItemBag.heldItem.unStick();}
    		else {ItemBag.heldItem.UIshow();}
    	}
    }
    private static void showMenus()
    {
    	interactingKeepers.forEach((keeper) -> keeper.showMenu());
    	interactingKeepers.clear();
    }
    private static void showStats()
    {
   
    	Image healthBar = statBars.getShape(HP);
		Image manaBar = statBars.getShape(MN);
		Image maxHealth = statBars.getShape(maxHP);
		Image maxMana = statBars.getShape(maxMN);
		Image expBar = statBars.getShape(XP);
		Image expRect = statBars.getShape(maxXP);
	    
		double HP = Main.player.getHealth();
		double maxHP = Main.player.getMaxHealth();
		double MN = Main.player.getMana();
		double maxMN = Main.player.getMaxMana();
		double exp = Main.player.getXP();
		double maxXP = Main.player.getXPMax();
	    
		double HPfrac1 = healthBar.getWidth() / maxHealth.getWidth();
		double HPfrac2 = Math.max(0, HP / maxHP);
		double HPxDiff = (HPfrac2 - HPfrac1) / 2 * maxHealth.getWidth();
		
		double XPfrac1 = expBar.getWidth() / expRect.getWidth();
		double XPfrac2 = Math.max(0, exp / maxXP);
		double XPxDiff = (XPfrac2 - XPfrac1) / 2 * expRect.getWidth();
	    
	
		double MNfrac1 = manaBar.getWidth() / maxMana.getWidth();
		double MNfrac2 = Math.max(0, MN / maxMN);
		double MNxDiff = (MNfrac2 - MNfrac1) / 2 * maxMana.getWidth();
	    
	    
		healthBar.setWidth(maxHealth.getWidth() * HPfrac2);
		healthBar.setX(healthBar.getX() + HPxDiff);
		manaBar.setWidth(maxMana.getWidth() * MNfrac2);
		manaBar.setX(manaBar.getX() + MNxDiff);
		expBar.setWidth(expRect.getWidth() * XPfrac2);
		expBar.setX(expBar.getX() + XPxDiff);
		
		statBars.UIshow();
		TextBox hoveredDisplay;
		Geometrical temp;
		Image copy;
		String displayText;
		double fontSize;
		if (mouseHovering(maxHealth))
		{
			fontSize = 12;
			temp = new Geometrical();
			copy = Image.createCopy(maxHealth);
			copy.setAlpha(0);
			temp.addShape(copy);
			displayText = (int)HP + " / " + (int)maxHP;
			hoveredDisplay = new TextBox(fontSize, displayText, temp);
			hoveredDisplay.setTextRGBA(40, 140, 140, 255);
			hoveredDisplay.UIshow();
		}
		else if (mouseHovering(maxMana))
		{
			fontSize = 12;
			temp = new Geometrical();
			copy = Image.createCopy(maxMana);
			copy.setAlpha(0);
			temp.addShape(copy);
			displayText = (int)MN + " / " + (int)maxMN;
			hoveredDisplay = new TextBox(fontSize, displayText, temp);
			hoveredDisplay.setTextRGBA(140, 140, 40, 255);
			hoveredDisplay.UIshow();
		}
		else if (mouseHovering(expRect))
		{
			fontSize = 8;
			temp = new Geometrical();
			copy = Image.createCopy(expRect);
			copy.setAlpha(0);
			copy.setPos(copy.getX() + 5, copy.getY() + 3);
			temp.addShape(copy);
			displayText = (int)exp + " / " + (int)maxXP;
			hoveredDisplay = new TextBox(fontSize, displayText, temp);
			hoveredDisplay.setTextRGBA(20, 20, 20, 255);
			hoveredDisplay.UIshow();
		}
	    
    	
    }
    
}
