package UI;

import LowLevel.Positionable;
import LowLevel.Shape;
import Game.Main;
import LowLevel.Geometrical;
import UI.UI;

public class ItemBag extends Positionable {
	public static boolean holdingItem = false;
	public static ItemBag heldBag;
	public static Item heldItem;
	private Geometrical bagDisplay;
	private boolean isShowing;
	private ItemSlot[] itemSlots;
	public ItemBag(double x, double y, double itemW, double itemL, int r, int c)
	{
		super(x, y, itemW * r, itemL * c);
		itemSlots = new ItemSlot[r * c];

		isShowing = false;
		UI.allBags.add(this);
		bagDisplay = new Geometrical();
		bagDisplay.addShape(new Shape(0, getX(), getY(), getWidth(), getLength()));
		for (int i = 0; i < c + 1; i++)
		{
			bagDisplay.addShape(new Shape(0, getX(), (getY() + getLength() / 2) - (itemL * i), getWidth(), 5, 200, 200, 200, 255));
		}
		for (int i = 0; i < r + 1; i++)
		{
			bagDisplay.addShape(new Shape(0, (getX() + getWidth() / 2) - (itemW * i), getY(), 5, getLength(), 200, 200, 200, 255));
		}
		for (int currR = 0; currR < r; currR++)
		{
			for (int currC = 0; currC < c; currC++)
			{
				double currX = x - (r / 2.0 * itemW) + itemW / 2 + (currR * itemW);
				double currY = y - (c / 2.0 * itemL) + itemL / 2 + (currC * itemL);
				itemSlots[currR * c + currC] = new ItemSlot(currX, currY, itemW - 5, itemL - 5);
			}
		}
	}
	public ItemSlot[] getSlots()
	{
		return itemSlots;
	}
	public ItemBag(Geometrical display, ItemSlot[] slots)
	{
		super(display.getX(), display.getY(), display.getWidth(), display.getLength());
		itemSlots = slots;
		bagDisplay = display;
		UI.allBags.add(this);
		isShowing = false;
	}
	public boolean isShowing()
	{
		return isShowing;
	}
	public boolean getVisibility()
	{
		return isShowing;
	}
	public void setVisibility(boolean visibility)
	{
		if (!visibility)
		{
			if (holdingItem && heldItem.getBag() == this && heldItem.getSlot() != Item.destroyItem)
			{
				Item temp = getItem(heldItem.getSlot());
				if (temp != null)
				{
					temp.setQuantity(heldItem.getQuantity() + temp.getQuantity());
				}
				else
				{
					addItem(heldItem, heldItem.getSlot());	
				}
				heldItem.unStick();
			}
		}
		isShowing = visibility;
		
	}
	public void addItem(Item item, int slot)
	{
		ItemSlot currSlot = itemSlots[slot];
		currSlot.setItem(item);
		item.setWidth(currSlot.getWidth() - 7);
		item.setLength(currSlot.getLength() - 6);
		item.setPos(currSlot.getX(), currSlot.getY());
		item.setSlot(slot);
		item.setBag(this);
	}
	public void removeItem(int slot)
	{
		itemSlots[slot].setItem(null); 
	}

	public void UIshow()
	{
		bagDisplay.UIshow();
		//If this just checks whether the mouse button is currently held down, then
		//You get a lot of repetition, so we check that the mouse was not down last frame
		boolean validLeft = Main.leftClick && !Main.leftClickLastFrame;
		boolean validRight = Main.rightClick && !Main.rightClickLastFrame;
		for (int slot = 0; slot < itemSlots.length; slot++)
		{
			boolean onCurr = mouseOnItem(slot);
			ItemSlot currSlot = itemSlots[slot];
			Item currItem = currSlot.getItem();
			boolean itemHere = currItem != null;
			//Slot type -3 does not accept any clicks
			validLeft = currSlot.getType() != Item.noInteraction && validLeft;
			validRight = currSlot.getType() != Item.noInteraction && validRight;
			if (itemHere) {
				if (onCurr)
				{
					if (validLeft)
					{ 
						if (!holdingItem)
						{
							currItem.stick();
							removeItem(slot);
							currSlot.playPick();
						}
						else
						{
							//Swap out the mouse's held item with the item in the slot
							//If the type is acceptable to the slot
							if (currSlot.acceptableType(heldItem.getType()))
							{
							
								Item temp = heldItem;
								if (temp.getID() == currItem.getID())
								{
									int newQ = temp.getQuantity() + currItem.getQuantity();
									if (newQ <= temp.getMax())
									{
										currItem.setQuantity(newQ);
										heldItem.unStick();
										currSlot.playPut();
									}
									else
									{
										currItem.setQuantity(currItem.getMax());
										heldItem.setQuantity(newQ - currItem.getMax());
										currSlot.playPut();
									}
								}
								else 
								{
									heldItem.unStick();
									currItem.stick();
									addItem(temp, slot);
									currSlot.playPut();
									
								}
							}
						}
					}
					else if (validRight) {
						if (holdingItem && currSlot.acceptableType(heldItem.getType()))
						{
							if (heldItem.getID() == currItem.getID())
							{
								if (currItem.getQuantity() != currItem.getMax())
								{
									//if they're the same items, put down one item from the held items
									currItem.setQuantity(currItem.getQuantity() + 1);
									heldItem.setQuantity(heldItem.getQuantity() - 1);
									currSlot.playPut();
								}
								
							}
							else
							{
								//if they're different items, this just acts as a swap
								Item temp = heldItem;
								heldItem.unStick();
								currItem.stick();
								removeItem(slot);
								addItem(temp, slot);
								currSlot.playPut();
							}
						}
						else
						{
							//listen you dickhead this is literally the minecraft item system
							//picks up half the items
							Item newItem = Item.createCopy(currItem);
							newItem.stick();
							currItem.setQuantity(currItem.getQuantity() / 2);
							newItem.setQuantity(newItem.getQuantity() - currItem.getQuantity());
							currSlot.playPick();
						}
					}
				}
				if (currItem.getQuantity() == 0) {removeItem(slot);}
				else {UI.visItems.add(currItem);}
			}
			else 
			{
				//If we just clicked and we had an item and the slot accepts that type,
				//Put the item in
				if (onCurr && holdingItem && currSlot.acceptableType(heldItem.getType())) {
					if (validLeft)
					{
						addItem(heldItem, slot);
						heldItem.unStick();
						currSlot.playPut();
					}
					else if (validRight) {
						//if we right clicked actually, then put down one item
						Item newItem = Item.createCopy(heldItem);
						heldItem.setQuantity(heldItem.getQuantity() - 1);
						newItem.setQuantity(1);
						addItem(newItem, slot);
						if (heldItem.getQuantity() == 0)
						{
							heldItem.unStick();
						}
						currSlot.playPut();
					}
				}
			}
		}
	}
	//Returns if the mouse is hovering an item slot
	//Note: mouseHovering from Main cannot be used due to empty item slots
	public boolean mouseOnItem(int slot)
	{
		ItemSlot currSlot = itemSlots[slot];
		double mouseX = Main.cursor.getX();
		double mouseY = Main.cursor.getY();
		double startX = currSlot.getX() - currSlot.getWidth() / 2;
		double startY = currSlot.getY() - currSlot.getLength() / 2;
		double endX = startX + currSlot.getWidth();
		double endY = startY + currSlot.getLength();
		if (startX < mouseX && mouseX < endX)
		{
			if (startY < mouseY && mouseY < endY)
			{
				return true;
			}
		}
		return false;
	}
	public Item getItem(int slot)
	{
		return itemSlots[slot].getItem();
	}
	public void setPos(double newX, double newY)
	{
		double xDiff = newX - getX();
		double yDiff = newY - getY();
		super.setPos(newX, newY);
		bagDisplay.setPos(newX, newY);
		for (int i = 0; i < itemSlots.length; i++)
		{
			ItemSlot currSlot = itemSlots[i];
			currSlot.setPos(currSlot.getX() + xDiff, currSlot.getY() + yDiff);
		}
	}
	public void setX(double newX)
	{
		double xDiff = newX - getX();
		super.setX(newX);
		bagDisplay.setX(newX);
		for (int i = 0; i < itemSlots.length; i++)
		{
			ItemSlot currSlot = itemSlots[i];
			currSlot.setX(currSlot.getX() + xDiff);
		}
	}
	public void setY(double newY)
	{
		double yDiff = newY - getY();
		super.setY(newY);
		bagDisplay.setY(newY);
		for (int i = 0; i < itemSlots.length; i++)
		{
			ItemSlot currSlot = itemSlots[i];
			currSlot.setY(currSlot.getY() + yDiff);
		}
	}
	public boolean itemIn(int slot)
	{
		return itemSlots[slot].getItem() != null;
	}
}
