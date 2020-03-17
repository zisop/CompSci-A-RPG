package UI;


import Game.Main;
import Imported.Texture;
import LowLevel.Geometrical;
import LowLevel.Image;
import LowLevel.Shape;

public class Item extends Image{
	public static Texture[] itemTextures = new Texture[6];
	
	
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
	public Item(int inID)
	{
		super(itemTextures[inID], 0, 0, 0, 0);
		onMouse = false;
		ID = inID;
		setItemType();
		myToolTip = ToolTip.defaultTip(ToolTip.rawItemTips[ID], this);
		Geometrical display = new Geometrical();
		display.addShape(new Shape(Shape.square, 0, 0, 15, 10, 100, 100, 100, 0));
		quantity = 1;
		quantityDisplay = new TextBox(10, "", display, 255);
		
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
	
	private void setItemType()
	{
		if (ID >= 0 && ID <= 2)
		{
			ItemType = wandType;
		}
		
		if (ID >= resourceInd && ID <= resourceInd + 2)
		{
			ItemType = resourceType;
		}
		maxQuantity = findMax(ID);
		if (ID == ruby || ID == emerald)
		{
			xOffset = -4;
			yOffset = 4;
		}
	}
	public static int findMax(int ID)
	{
		if (ID >= wandInd && ID <= wandInd + 2)
		{
			return 4;
		}
		if (ID >= resourceInd && ID <= resourceInd + 2)
		{
			return 64;
		}
		System.out.println("Item ID doesnt exist");
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
	public static void initItems()
	{
		itemTextures[wand0] = new Texture("Items/Wands/Wand1.png");
		itemTextures[wand1] = new Texture("Items/Wands/Wand2.png");
		itemTextures[wand2] = new Texture("Items/Wands/Wand3.png");
		
		itemTextures[emerald] = new Texture("Items/Resources/Emerald.png");
		itemTextures[ruby] = new Texture("Items/Resources/Ruby.png");
		itemTextures[sapphire] = new Texture("Items/Resources/Sapphire.png");
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
	
	private static int wandInd = 0;
	public static int wand0 = wandInd + 0;
	public static int wand1 = wandInd + 1;
	public static int wand2 = wandInd + 2;
	
	private static int resourceInd = 3;
	public static int emerald = resourceInd + 0;
	public static int ruby = resourceInd + 1;
	public static int sapphire = resourceInd + 2;
	
	public static int acceptAll = -1;
	public static int acceptNone = -2;
	public static int noInteraction = -3;
	public static int wandType = 0;
	public static int ringType = 1;
	public static int bookType = 2;
	public static int helmType = 3;
	public static int resourceType = 4;
	
	public static int destroyItem = -1;
}
