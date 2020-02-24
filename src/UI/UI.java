package UI;

import java.util.ArrayList;

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
    public static Geometrical playerStats;
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
    }
    
    //Will determine if the mouse is hovering over a positionable in the UI
    public static boolean mouseHovering(Positionable obj)
    {
    	Point[] objPoints = obj.getShowBasis();
    	Point cursorPoint = new Point(Main.cursor.getX(), Main.cursor.getY());
    	return Geometry.insideShape(objPoints, cursorPoint);
    }
    
    public static boolean mouseInteraction(Positionable obj)
    {
    	return shouldInteract() && mouseHovering(obj);
    }
    
    public static boolean shouldInteract()
    {
    	return !Main.interactionEvent && Main.leftClick && !Main.leftClickLastFrame;
    }
    
    private static Geometrical createSlot(String name)
    {
    	Geometrical boxDisplay = new Geometrical();
    	Shape outBox = Geometry.createSquare(-2, 27, -2, 27, 150, 150, 150, 255);
    	Shape inBox = Geometry.createSquare(0, 25, 0, 25, 3, 162, 162, 255);
    	boxDisplay.addShape(outBox);
    	boxDisplay.addShape(inBox);
    	
    	double xPos = 0;
    	if (name.equals("Helmet"))
    	{
    		xPos = 14;
    	}
    	if (name.equals("Ring"))
    	{
    		xPos = 24;
    	}
    	if (name.equals("Tome"))
    	{
    		xPos = 21;
    	}
    	if (name.equals("Wand"))
    	{
    		xPos = 21;
    	}
    	Geometrical helmTextPos = new Geometrical();
    	helmTextPos.addShape(new Shape(0, xPos, 37, 60, 10, 0, 0, 0, 0));
    	TextBox itemText = new TextBox(8, name, helmTextPos, 255);
    	Geometrical slotDisplay = new Geometrical();
    	slotDisplay.addShape(boxDisplay);
    	slotDisplay.addShape(itemText);
    	
    	return slotDisplay;
    }
    
    private static void initInventory()
    {
    	
    	//Player bag display
    	playerBag = new ItemBag(-395, 245, 40, 40, 4, 4);
    	Geometrical armor = new Geometrical();
    	
    	Shape armorMainRect = Geometry.createSquare(-2, 152, -2, 177, 100, 100, 100, 255);
    	Shape armorInRect = Geometry.createSquare(0, 150, 0, 175, 161, 101, 9, 255);

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
    	armorBag.setPos(-395, 65);
    }
    private static void initStats()
    {
    	//Stats Display
    	playerStats = new Geometrical();
    	statsShowing = true;
    	Shape mainRect = Geometry.createSquare(-2, 402, -2, 52, 0, 0, 0, 255);
    	Shape innerRect = Geometry.createSquare(0, 400, 0, 50, 100, 100, 100, 255);
    	Shape healthRect = Geometry.createSquare(54, 396, 27, 48, 150, 0, 0, 255);
    	Shape manaRect = Geometry.createSquare(54, 396, 2, 23, 0, 0, 150, 255);
    	Shape charSlot = Geometry.createSquare(4, 46, 4, 46, 255, 255, 255, 255);
    	Shape healthBar = Geometry.createSquare(54, 396, 27, 48, 255, 0, 0, 255);
    	Shape manaBar = Geometry.createSquare(54, 396, 2, 23, 0, 0, 255, 255);
    	//Index 0
    	playerStats.addShape(mainRect);
    	playerStats.addShape(innerRect);
    	playerStats.addShape(charSlot);
    	//MaxHealth = ind3, MaxMana = ind4
    	playerStats.addShape(healthRect);
    	playerStats.addShape(manaRect);
    	//healthBar = index 5, manaBar = index 6
    	playerStats.addShape(healthBar);
    	playerStats.addShape(manaBar);
    	playerStats.setPos(-275, 365);
    }
    private static void showNPCText()
    {
    	talkingNPCs.forEach((npc) -> npc.showText());
    	talkingNPCs.clear();
    }

    private static void showBags()
    {
    	if (Main.e && !Main.eLastFrame)
        {
        	playerBag.setVisibility(!UI.playerBag.getVisibility());
        	armorBag.setVisibility(playerBag.getVisibility());
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
   
    	Image healthBar = playerStats.getShape(5);
		Image manaBar = playerStats.getShape(6);
		Image maxHealth = playerStats.getShape(3);
		Image maxMana = playerStats.getShape(4);
	    
		double HP = Main.player.getHealth();
		double maxHP = Main.player.getMaxHealth();
		double MN = Main.player.getMana();
		double maxMN = Main.player.getMaxMana();
	    
		double HPfrac1 = healthBar.getWidth() / maxHealth.getWidth();
		double HPfrac2 = Math.max(0, HP / maxHP);
		double HPxDiff = (HPfrac2 - HPfrac1) / 2 * maxHealth.getWidth();
	    
	
		double MNfrac1 = manaBar.getWidth() / maxMana.getWidth();
		double MNfrac2 = Math.max(0, MN / maxMN);
		double MNxDiff = (MNfrac2 - MNfrac1) / 2 * maxMana.getWidth();
	    
	    
		healthBar.setWidth(maxHealth.getWidth() * HPfrac2);
		healthBar.setX(healthBar.getX() + HPxDiff);
		manaBar.setWidth(maxMana.getWidth() * MNfrac2);
		manaBar.setX(manaBar.getX() + MNxDiff);
	    
    	playerStats.UIshow();
    }
}
