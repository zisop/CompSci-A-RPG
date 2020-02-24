package Exchange;

import Game.Main;
import Imported.Audio;
import LowLevel.Geometrical;
import LowLevel.Image;
import LowLevel.Shape;
import UI.Item;
import UI.ItemBag;
import UI.ItemSlot;
import UI.UI;

public class ExchangeSelection extends Image{
	private ItemExchange[] exchanges;
	private ExchangeDisplay[] displays;
	private ItemExchange selectedExchange;
	private ItemSlot selectedSlot;
	private ExchangeDisplay selectedDisplay;
	private ItemSlot[] slots;
	private ItemBag storeBag;
	private CraftingBox box;
	private Geometrical outline;
	public ExchangeSelection(ItemExchange[] inExchanges, CraftingBox inBox, double yOffset)
	{
		super(null, inBox.getX(), inBox.getY() + yOffset, 0, 0);
		
		box = inBox;
		int rows = 6;
		int cols = 3;
		ItemExchange[] newExch = new ItemExchange[rows * cols];
		for (int i = 0; i < inExchanges.length; i++)
		{
			newExch[i] = inExchanges[i];
		}
		exchanges = newExch;
		storeBag = new ItemBag(getX(), getY(), 60, 60, rows, cols);
		slots = storeBag.getSlots();
		super.setWidth(storeBag.getWidth());
		super.setLength(storeBag.getLength());
		for (int i = 0; i < slots.length; i++)
		{
			slots[i].setType(Item.noInteraction);
		}
		displays = new ExchangeDisplay[rows * cols];
		for (int i = 0; i < exchanges.length; i++)
		{
			if (exchanges[i] != null) {
				displays[i] = new ExchangeDisplay(inBox, exchanges[i]);
				storeBag.addItem(new Item(displays[i].getOutputID()), i);
			}
			
		}
		
		//Grab slots[0] to access its width and length values
		//This creates a yellow outline selection box
		selectedSlot = slots[0];
		double offset = 5;
		double rightInX = selectedSlot.getX() + selectedSlot.getWidth() / 2;
		double rightOutX = rightInX + offset;
		double leftInX = selectedSlot.getX() - selectedSlot.getWidth() / 2;
		double leftOutX = leftInX - offset;
		double upInY = selectedSlot.getY() + selectedSlot.getLength() / 2;
		double upOutY = upInY + offset;
		double downInY = selectedSlot.getY() - selectedSlot.getLength() / 2;
		double downOutY = downInY - offset;
		
		//Outline, for when a shop item is clicked
		outline = new Geometrical();
		//Yellow
		float red = 210, green = 230, blue = 0, alpha = 255;
		
		//Lines
		Shape rightLine = new Shape(0, (rightInX + rightOutX) / 2, selectedSlot.getY(), offset, selectedSlot.getLength(), red, green, blue, alpha);
		Shape upLine = new Shape(0, selectedSlot.getX(), (upInY + upOutY) / 2, selectedSlot.getWidth(), offset, red, green, blue, alpha);
		Shape leftLine = new Shape(0, (leftInX + leftOutX) / 2, selectedSlot.getY(), offset, selectedSlot.getLength(), red, green, blue, alpha);
		Shape downLine = new Shape(0, selectedSlot.getX(), (downInY + downOutY) / 2, selectedSlot.getWidth(), offset, red, green, blue, alpha);
		
		//Corners
		Shape boxUR = new Shape(0, (rightInX + rightOutX) / 2, (upInY + upOutY) / 2, offset, offset, red, green, blue, alpha);
		Shape boxUL = new Shape(0, (leftInX + leftOutX) / 2, (upInY + upOutY) / 2, offset, offset, red, green, blue, alpha);
		Shape boxDR = new Shape(0, (rightInX + rightOutX) / 2, (downInY + downOutY) / 2, offset, offset, red, green, blue, alpha);
		Shape boxDL = new Shape(0, (leftInX + leftOutX) / 2, (downInY + downOutY) / 2, offset, offset, red, green, blue, alpha);
		
		//this is the mainRect that controls position
		outline.addShape(new Shape(0, selectedSlot.getX(), selectedSlot.getY(), 0, 0));
		outline.addShape(rightLine);
		outline.addShape(upLine);
		outline.addShape(leftLine);
		outline.addShape(downLine);
		outline.addShape(boxUR);
		outline.addShape(boxUL);
		outline.addShape(boxDR);
		outline.addShape(boxDL);
		outline.UIshow();
		selectedSlot = null;

	}
	public ItemBag getStoreBag()
	{
		return storeBag;
	}
	public ItemExchange getSelectedExchange()
	{
		return selectedExchange;
	}
	public void UIshow()
	{
		storeBag.UIshow();
		boolean wasHovering = false;
		for (int i = 0; i < slots.length; i++)
		{
			ItemSlot currSlot = slots[i];
			boolean onCurr = UI.mouseHovering(currSlot);
			if (onCurr)
			{
				
				//If i'm hovering a slot, i'm going to display its exchange
				if (currSlot.getItem() != null) {
					box.setDisplay(displays[i]);
					wasHovering = true;
				}
				//If i click the slot, let me select it
				if (UI.shouldInteract()) {
					selectedSlot = slots[i];
					selectedDisplay = displays[i];
					selectedExchange = exchanges[i];
					box.getOutput().removeItem(0);
					if (currSlot.getItem() != null)
					{
						Audio.playSound("Inv/coin3");
					}
				}
				break;
			}
			
		}
		//Whatever is hovered will be displayed with first priority, then whatever is selected with second priority
		if (!wasHovering) {box.setDisplay(selectedDisplay);}
		if (selectedSlot != null)
		{
			Item selectedItem = selectedSlot.getItem();
			if (selectedItem != null)
			{
				//move the outline to the selectedSlot
				//then show it
				outline.setPos(selectedSlot.getX(), selectedSlot.getY());
				outline.UIshow();
			}
		}
	}
	public void setPos(double newX, double newY)
	{
		System.out.println("ExchangeSelection setPos not implemented");
	}
	public void setX(double newX)
	{
		System.out.println("ExchangeSelection setX not implemented");
	}
	public void setY(double newY)
	{
		System.out.println("ExchangeSelection setY not implemented");
	}
}
