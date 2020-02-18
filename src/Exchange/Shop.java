package Exchange;

import Game.Main;
import Imported.Audio;
import LowLevel.Shape;
import UI.Item;
import UI.ItemBag;
import UI.UI;

public class Shop extends Option{
	private ExchangeSelection selection;
	private CraftingBox box;
	private ItemBag input;
	private ItemBag output;
	public Shop(ItemExchange[] inExchanges)
	{
		double yOffset = 400;
		box = new CraftingBox();
		selection = new ExchangeSelection(inExchanges, box, yOffset);
		input = box.getInput();
		output = box.getOutput();
		visibility = false;
		createX();
	}
	public void UIshow()
	{
		updateX();
		manageExchanges();
		box.UIshow();
		selection.UIshow();
		xButton.UIshow();
	}
	protected void createX()
	{
		xButton = new Shape(Shape.xButton, 
				selection.getX() + selection.getWidth() / 2 + 50,
				selection.getY() + selection.getLength() / 2 + 50, 100, 100);
	}
	
	
	//Buys whatever the player asks to buy if it's supposed to be buying it
	private void manageExchanges()
	{
		ItemExchange selectedExchange = selection.getSelectedExchange();
		
		//You can't buy an item unless you select the item that you're buying
		if (selectedExchange != null)
		{
			//Calls some stuff from itemExchange to figure out how to craft items
			int craftable = selectedExchange.possibleExchanges(input);
			if ((output.getItem(0) == null && craftable != 0) || (output.getItem(0) != null && craftable != output.getItem(0).getQuantity()))
			{
				Item craftedItem = selectedExchange.createItem();
				craftedItem.setQuantity(craftable);
				output.addItem(craftedItem, 0);
			}
			Item outputItem = output.getItem(0);
			//These are all the checks to make sure that we're SUPPOSED to be buying an item
			//You should not be able to buy an item if you're already holding one or if there isnt an item to buy or if you didnt click
			if (Main.leftClick && !Main.leftClickLastFrame && UI.mouseHovering(output.getSlots()[0]) && craftable > 0 && !ItemBag.holdingItem)
			{
				selectedExchange.removeItems(input, craftable);
				
				outputItem.setBag(UI.playerBag);
				boolean slotSet = false;
				ItemBag.heldBag = UI.playerBag;
				outputItem.stick();
				for (int i = 0; i < UI.playerBag.getSlots().length; i++)
				{
					if (UI.playerBag.getSlots()[i].getItem() != null)
					{
						outputItem.setSlot(i);
						slotSet = true;
						break;
					}
				}
				if (!slotSet) {outputItem.setSlot(Item.destroyItem);}
				output.removeItem(0);
				if (Math.random() < .5) {Audio.playSound("Inv/coin");}
				else {Audio.playSound("Inv/coin2");}
			}
		}
	}
	
	public void setVisibility(boolean newVisibility)
	{
		visibility = newVisibility;
	}
	public boolean getVisibility()
	{
		return visibility;
	}
}
