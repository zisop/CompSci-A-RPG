package Exchange;

import UI.Item;
import UI.ItemBag;
import UI.ItemSlot;
import UI.UI;

public class ItemExchange {
	private int[] desiredIDs;
	private int[] desiredQuantities;
	private int craftedID;
	public ItemExchange(int[] inQuantities, int[] inIDs, int inCrafted)
	{
		desiredQuantities = inQuantities;
		desiredIDs = inIDs;
		craftedID = inCrafted;
	}
	public int possibleExchanges(ItemBag offer)
	{
		ItemSlot[] slots = offer.getSlots();
		int[] quantities = extractDesired(slots);
		int[] craftableQuantities = new int[quantities.length];
		for (int i = 0; i < quantities.length; i++)
		{
			craftableQuantities[i] = quantities[i] / desiredQuantities[i];  
		}
		int minimum = craftableQuantities[0];
		for (int i = 1; i < craftableQuantities.length; i++)
		{
			minimum = Math.min(minimum, craftableQuantities[i]);
		}
		minimum = Math.min(minimum, Item.findMax(craftedID));
		return minimum;
	}
	//Removes all items that should be removed given an itembag and a quantity to purchase
	public void removeItems(ItemBag offer, int craftedQ)
	{
		ItemSlot[] slots = offer.getSlots();
		int[] toRemove = new int[desiredQuantities.length];
		int[] removedQs = new int[slots.length];
		for (int i = 0; i < toRemove.length; i++)
		{
			toRemove[i] = desiredQuantities[i] * craftedQ; 
		}
		for (int slotInd = 0; slotInd < slots.length; slotInd++)
		{
			Item currItem = slots[slotInd].getItem();
			if (currItem != null)
			{
				for (int idInd = 0; idInd < desiredIDs.length; idInd++)
				{
					if (desiredIDs[idInd] == currItem.getID())
					{
						removedQs[slotInd] = Math.min(toRemove[idInd], currItem.getQuantity());
						toRemove[idInd] -= removedQs[slotInd];
						break;
					}
				}
			}
		}
		for (int i = 0; i < slots.length; i++)
		{
			if (removedQs[i] > 0)
			{
				ItemSlot currSlot = slots[i];
				Item currItem = currSlot.getItem();
				currItem.setQuantity(currItem.getQuantity() - removedQs[i]);
			}
		}
	}
	//Figures out what items are actually relevant to this itemExchange
	//Looks at a list of slots
	private int[] extractDesired(ItemSlot[] slots)
	{
		int[] desirables = new int[desiredIDs.length];
		for (int slotInd = 0; slotInd < slots.length; slotInd++)
		{
			Item currItem = slots[slotInd].getItem();
			if (currItem != null)
			{
				for (int desiredInd = 0; desiredInd < desiredIDs.length; desiredInd++)
				{
					if (currItem.getID() == desiredIDs[desiredInd])
					{
						desirables[desiredInd] += currItem.getQuantity();
						break;
					}
				}
			}
		}
		return desirables;
	}
	public int[] getDesiredIDs() {return desiredIDs;}
	public int[] getDesiredQuantities() {return desiredQuantities;}
	public int getOutput() {return craftedID;}
	public Item createItem() {return new Item(craftedID);}
}
