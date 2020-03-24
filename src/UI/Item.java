package UI;


import java.util.ArrayList;

import Game.Main;
import Imported.Texture;
import LowLevel.Geometrical;
import LowLevel.Image;
import LowLevel.Shape;

public class Item extends Image{
	private boolean onMouse;
	private int slot;
	private ItemBag myBag;
	private ToolTip myToolTip;
	private int ID;
	private int quantity;
	private int maxQuantity;
	//ItemType 0 = wands, ItemType 1 = ring, ItemType 2 = tome, ItemType 3 = helmet
	private int ItemType;
	private TextBox quantityDisplay;
	private double xOffset;
	private double yOffset;
	private ItemEffect[] effects;
	public Item(int inID)
	{
		super(itemTextures[inID], 0, 0, 0, 0);
		onMouse = false;
		ID = inID;
		setEffects();
		setItemType();
		setTooltip();
		Geometrical display = new Geometrical();
		display.addShape(new Shape(Shape.square, 0, 0, 15, 10, 100, 100, 100, 0));
		quantity = 1;
		quantityDisplay = new TextBox(10, "", display, 255);
		
	}
	
	public static ArrayList<Item> extractItems(ItemSlot[] slots)
	{
		ArrayList<Item> items = new ArrayList<Item>();
		for (int i = 0; i < slots.length; i++)
		{
			if (slots[i].getItem() != null)
			{
				items.add(slots[i].getItem());
			}
		}
		return items;
	}
	
	public void setQuantity(int inQuantity)
	{
		quantity = inQuantity;
		if (quantity == 1 || quantity == 0)
		{
			quantityDisplay.setText("");
		}
		else 
		{
			quantityDisplay.setText(((Integer)(quantity)).toString());
		}
		if (quantity > 9)
		{
			quantityDisplay.setWidth(30);
		}
		else {
			quantityDisplay.setWidth(15);
		}
	}
	public int getQuantity()
	{
		return quantity;
	}
	public ItemEffect[] getEffects() 
	{
		if (effects == null)
		{
			try {throw new Exception("Tried to extract the effects of item: " + ID + " but they didn't exist");} 
			catch (Exception e) {e.printStackTrace(); System.exit(0);}
		}
		return effects;
	}
	private void setTooltip()
	{
		String temp = ToolTip.rawItemTips[ID];
		if (effects.length != 0) 
		{
			temp += "``";
			for (int i = 0; i < effects.length - 1; i++)
			{
				temp += effects[i] + "`";
			}
			temp += effects[effects.length - 1];
		}
		myToolTip = ToolTip.defaultTip(temp, this);
	}
	private void setEffects()
	{
		switch (ID) {
			case wand0:
				effects = new ItemEffect[2];
				effects[0] = new ItemEffect(ItemEffect.damageMult, .1);
				effects[1] = new ItemEffect(ItemEffect.manaAdd, 20);
				break;
			case wand1:
				effects = new ItemEffect[2];
				effects[0] = new ItemEffect(ItemEffect.damageMult, .15);
				effects[1] = new ItemEffect(ItemEffect.manaAdd, 25);
				break;
			case wand2:
				effects = new ItemEffect[2];
				effects[0] = new ItemEffect(ItemEffect.damageMult, .3);
				effects[1] = new ItemEffect(ItemEffect.manaAdd, 10);
				break;
			case wand3:
				effects = new ItemEffect[2];
				effects[0] = new ItemEffect(ItemEffect.damageMult, .3);
				effects[1] = new ItemEffect(ItemEffect.manaAdd, 30);
				break;
			case wand4:
				effects = new ItemEffect[3];
				effects[0] = new ItemEffect(ItemEffect.damageMult, .8);
				effects[1] = new ItemEffect(ItemEffect.manaAdd, -10);
				effects[3] = new ItemEffect(ItemEffect.manaRegenAdd, -.2);
				break;
				
				
			case helm0:
				effects = new ItemEffect[2];
				effects[0] = new ItemEffect(ItemEffect.armorAdd, 1);
				effects[1] = new ItemEffect(ItemEffect.healthAdd, 20);
				break;
			case helm1:
				effects = new ItemEffect[2];
				effects[0] = new ItemEffect(ItemEffect.armorAdd, 3);
				effects[1] = new ItemEffect(ItemEffect.healthAdd, 30);
				break;
			case helm2:
				effects = new ItemEffect[2];
				effects[0] = new ItemEffect(ItemEffect.armorAdd, 4);
				effects[1] = new ItemEffect(ItemEffect.healthAdd, 10);
				break;
			case helm3:
				effects = new ItemEffect[2];
				effects[0] = new ItemEffect(ItemEffect.armorAdd, 12);
				effects[1] = new ItemEffect(ItemEffect.healthAdd, -10);
				break;
			case helm4:
				effects = new ItemEffect[2];
				effects[0] = new ItemEffect(ItemEffect.armorAdd, 12);
				effects[1] = new ItemEffect(ItemEffect.healthAdd, 30);
				break;
			
			case ring0:
				effects = new ItemEffect[2];
				effects[0] = new ItemEffect(ItemEffect.manaAdd, 5);
				effects[1] = new ItemEffect(ItemEffect.manaRegenAdd, .1);
				break;
			case ring1:
				effects = new ItemEffect[2];
				effects[0] = new ItemEffect(ItemEffect.manaAdd, 10);
				effects[1] = new ItemEffect(ItemEffect.manaRegenAdd, .1);
				break;
			case ring2:
				effects = new ItemEffect[2];
				effects[0] = new ItemEffect(ItemEffect.manaAdd, 15);
				effects[1] = new ItemEffect(ItemEffect.manaRegenAdd, .1);
				break;
			case ring3:
				effects = new ItemEffect[2];
				effects[0] = new ItemEffect(ItemEffect.manaAdd, 15);
				effects[1] = new ItemEffect(ItemEffect.manaRegenAdd, .2);
				break;
			case ring4:
				effects = new ItemEffect[2];
				effects[0] = new ItemEffect(ItemEffect.manaAdd, 20);
				effects[1] = new ItemEffect(ItemEffect.manaRegenAdd, .2);
				break;
			case ring5:
				effects = new ItemEffect[2];
				effects[0] = new ItemEffect(ItemEffect.manaAdd, 15);
				effects[1] = new ItemEffect(ItemEffect.manaRegenAdd, .25);
				break;
				
			case tome0:
				effects = new ItemEffect[1];
				effects[0] = new ItemEffect(ItemEffect.expAdd, .05);
				break;
			case tome1:
				effects = new ItemEffect[1];
				effects[0] = new ItemEffect(ItemEffect.expAdd, .1);
				break;
			case tome2:
				effects = new ItemEffect[1];
				effects[0] = new ItemEffect(ItemEffect.expAdd, .15);
				break;
			case tome3:
				effects = new ItemEffect[1];
				effects[0] = new ItemEffect(ItemEffect.expAdd, .2);
				break;
			case tome4:
				effects = new ItemEffect[1];
				effects[0] = new ItemEffect(ItemEffect.expAdd, .25);
				break;
			case tome5:
				effects = new ItemEffect[1];
				effects[0] = new ItemEffect(ItemEffect.expAdd, .3);
				break;
				
			default:
				effects = new ItemEffect[] {};
		}
	}
	
	
	private void setItemType()
	{
		if (ID >= wandInd + 0 && ID <= wandEnd) {ItemType = wandType;}
		else if (ID >= resourceInd && ID <= resourceEnd) {ItemType = resourceType;}
		else if (ID >= helmInd && ID <= helmEnd) {ItemType = helmType;}
		else if (ID >= ringInd && ID <= ringEnd) {ItemType = ringType;}
		else if (ID >= potionInd && ID <= potionEnd) {ItemType = potionType;}
		else if (ID >= tomeInd && ID <= tomeEnd) {ItemType = bookType;}
		maxQuantity = findMax(ID);
		if (ID == ruby || ID == emerald) {xOffset = -4; yOffset = 4;}
	}
	public static int findMax(int ID)
	{
		if (ID >= wandInd && ID <= wandEnd) {return 1;}
		if (ID >= resourceInd && ID <= resourceEnd) {return 64;}
		if (ID >= helmInd && ID <= helmEnd) {return 1;}
		if (ID >= ringInd && ID <= ringEnd) {return 1;}
		if (ID >= tomeInd && ID <= tomeEnd) {return 1;}
		if (ID >= potionInd && ID <= potionEnd) {return 16;}
		try {
			throw new Exception("Item ID: " + ID + " had no max implemented");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return -1;
	}
	public int getMax()
	{
		return maxQuantity;
	}
	public void setMax(int newMax)
	{
		maxQuantity = newMax;
	}
	public int getType()
	{
		return ItemType;
	}
	public int getID()
	{
		return ID;
	}
	public void UIshow()
	{
		if (onMouse)
		{
			setX(Main.cursor.getX());
			setY(Main.cursor.getY());
		}
		setPos(getX() + xOffset, getY() + yOffset);
		super.UIshow();
		setPos(getX() - xOffset, getY() - yOffset);
		quantityDisplay.setAlpha(getAlpha());
		quantityDisplay.UIshow();
		if (!onMouse)
		{
			if (!ItemBag.holdingItem && UI.mouseHovering(this))
			{
				UI.visTips.add(myToolTip);
			}
		}
	}
	public void setX(double newX)
	{
		double xDiff = newX - getX();
		super.setX(newX);
		quantityDisplay.setX(quantityDisplay.getX() + xDiff);
		
	}
	public void setY(double newY)
	{
		double yDiff = newY - getY();
		super.setY(newY);
		quantityDisplay.setY(quantityDisplay.getY() + yDiff);
		
	}
	public void setWidth(double newWidth)
	{
		super.setWidth(newWidth);
		quantityDisplay.setX(getX() + newWidth / 3);
	}
	public void setLength(double newLength)
	{
		super.setLength(newLength);
		quantityDisplay.setY(getY() - newLength / 3);
	}
	public TextBox getQuantityDisplay()
	{
		return quantityDisplay;
	}

	public void setSlot(int inSlot)
	{
		slot = inSlot;
	}
	public int getSlot()
	{
		return slot;
	}
	public void stick()
	{
		onMouse = true;
		ItemBag.holdingItem = true;
		ItemBag.heldBag = myBag;
		ItemBag.heldItem = this;
	}
	public void unStick()
	{
		
		onMouse = false;
		ItemBag.holdingItem = false;
		
	}
	public ItemBag getBag()
	{
		return myBag;
	}
	public void setBag(ItemBag newBag)
	{
		myBag = newBag;
	}
	public boolean getStuck()
	{
		return onMouse;
	}
	public static Texture[] itemTextures;
	public static void initItems()
	{
		itemTextures = new Texture[tomeEnd + 1];
		itemTextures[wand0] = new Texture("Items/Wands/Wand1.png");
		itemTextures[wand1] = new Texture("Items/Wands/Wand2.png");
		itemTextures[wand2] = new Texture("Items/Wands/Wand3.png");
		itemTextures[wand3] = new Texture("Items/Wands/Wand4.png");
		itemTextures[wand4] = new Texture("Items/Wands/Wand5.png");
		
		itemTextures[helm0] = new Texture("Items/Helmets/helmet0.png");
		itemTextures[helm1] = new Texture("Items/Helmets/helmet1.png");
		itemTextures[helm2] = new Texture("Items/Helmets/helmet2.png");
		itemTextures[helm3] = new Texture("Items/Helmets/helmet3.png");
		itemTextures[helm4] = new Texture("Items/Helmets/helmet4.png");

		itemTextures[tome0] = new Texture("Items/Tomes/tome0.png");
		itemTextures[tome1] = new Texture("Items/Tomes/tome1.png");
		itemTextures[tome2] = new Texture("Items/Tomes/tome2.png");
		itemTextures[tome3] = new Texture("Items/Tomes/tome3.png");
		itemTextures[tome4] = new Texture("Items/Tomes/tome4.png");
		itemTextures[tome5] = new Texture("Items/Tomes/tome5.png");
		
		itemTextures[ring0] = new Texture("Items/Rings/ring0.png");
		itemTextures[ring1] = new Texture("Items/Rings/ring1.png");
		itemTextures[ring2] = new Texture("Items/Rings/ring2.png");
		itemTextures[ring3] = new Texture("Items/Rings/ring3.png");
		itemTextures[ring4] = new Texture("Items/Rings/ring4.png");
		itemTextures[ring5] = new Texture("Items/Rings/ring5.png");
		
		itemTextures[healthPot] = new Texture("Items/Potions/Health.png");
		itemTextures[manaPot] = new Texture("Items/Potions/Mana.png");
		
		itemTextures[emerald] = new Texture("Items/Resources/Emerald.png");
		itemTextures[ruby] = new Texture("Items/Resources/Ruby.png");
		itemTextures[sapphire] = new Texture("Items/Resources/Sapphire.png");
		itemTextures[gold] = new Texture("Items/Resources/Gold.png");
		itemTextures[amethyst] = new Texture("Items/Resources/Amethyst.png");
	}
	public static Item createCopy(Item item)
	{
		Item copy = new Item(item.getID());
		copy.setWidth(item.getWidth());
		copy.setLength(item.getLength());
		copy.setPos(item.getX(), item.getY());
		copy.setQuantity(item.getQuantity());
		copy.setBag(item.getBag());
		copy.setSlot(item.getSlot());
		return copy;
	}
	public ToolTip getToolTip()
	{
		return myToolTip;
	}
	public void setAlpha(float newAlpha)
	{
		super.setAlpha(newAlpha);
		quantityDisplay.setTextAlpha(newAlpha);
	}
	
	private static final int wandInd = 0;
	public static final int wand0 = wandInd + 0;
	public static final int wand1 = wandInd + 1;
	public static final int wand2 = wandInd + 2;
	public static final int wand3 = wandInd + 3;
	public static final int wand4 = wandInd + 4;
	private static final int wandEnd = wand4;
	
	private static final int resourceInd = wandEnd + 1;
	public static final int emerald = resourceInd + 0;
	public static final int ruby = resourceInd + 1;
	public static final int sapphire = resourceInd + 2;
	public static final int amethyst = resourceInd + 3;
	public static final int gold = resourceInd + 4;
	private static final int resourceEnd = gold;
	
	private static final int helmInd = resourceEnd + 1;
	public static final int helm0 = helmInd + 0;
	public static final int helm1 = helmInd + 1;
	public static final int helm2 = helmInd + 2;
	public static final int helm3 = helmInd + 3;
	public static final int helm4 = helmInd + 4;
	private static final int helmEnd = helm4;
	
	private static final int ringInd = helmEnd + 1;
	public static final int ring0 = ringInd;
	public static final int ring1 = ringInd + 1;
	public static final int ring2 = ringInd + 2;
	public static final int ring3 = ringInd + 3;
	public static final int ring4 = ringInd + 4;
	public static final int ring5 = ringInd + 5;
	private static final int ringEnd = ring5;
	
	private static final int potionInd = ringEnd + 1;
	public static final int healthPot = potionInd;
	public static final int manaPot = potionInd + 1;
	private static final int potionEnd = manaPot;
	
	private static final int tomeInd = potionEnd + 1;
	public static final int tome0 = tomeInd;
	public static final int tome1 = tomeInd + 1;
	public static final int tome2 = tomeInd + 2;
	public static final int tome3 = tomeInd + 3;
	public static final int tome4 = tomeInd + 4;
	public static final int tome5 = tomeInd + 5;
	private static final int tomeEnd = tome5;
	
	public static final int acceptAll = -1;
	public static final int acceptNone = -2;
	public static final int noInteraction = -3;
	public static final int wandType = 0;
	public static final int ringType = 1;
	public static final int bookType = 2;
	public static final int helmType = 3;
	public static final int resourceType = 4;
	public static final int potionType = 5;
	
	public static int destroyItem = -1;
}
