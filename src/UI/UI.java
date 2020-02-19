package UI;

import java.util.ArrayList;

import Game.Main;
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
    //Can't be done in the ItemBag or Chest class because they dont store player's itemBag
    //Shows all visible ItemBags
    public static void showUI()
    {
    	if (statsShowing)
    	{
    		showStats();
    	}
    	showBags();
    	
    }
    public static void showBags()
    {
    	if (Main.e && !Main.eLastFrame)
        {
        	playerBag.setVisibility(!UI.playerBag.getVisibility());
        	armorBag.setVisibility(playerBag.getVisibility());
        }
    	for (int i = 0; i < allBags.size(); i++)
    	{
    		if (allBags.get(i).isShowing())
    		{
    			allBags.get(i).UIshow();
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
    public static void showStats()
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
    public static void init()
    {
    	//Inits the entire UI
    	initInventory();
    	initStats();
    }
    public static Geometrical createSlot(String name)
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
    
    public static void initInventory()
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
    	armorBag.setPos(-200, 235);
    }
    public static void initStats()
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
  //Will determine if the mouse is hovering over a positionable in the UI
    public static boolean mouseHovering(Positionable obj)
    {
    	double objLeft = obj.getX() - obj.getCharWidth() / 2;
    	double objRight = obj.getX() + obj.getCharWidth() / 2;
    	double objTop = obj.getY() + obj.getCharLength() / 2;
    	double objBot = obj.getY() - obj.getCharLength() / 2;
    	Point p1 = new Point(objLeft, objBot);
    	Point p2 = new Point(objRight, objBot);
    	Point p3 = new Point(objRight, objTop);
    	Point p4 = new Point(objLeft, objTop);
    	Point[] objPoints = new Point[] {p1, p2, p3, p4};

    	Point cursorPoint = new Point(Main.cursor.getX(), Main.cursor.getY());
    	return Geometry.insideShape(objPoints, cursorPoint);
    }
}