package Combat;

import java.util.Random;

import Game.Main;
import UI.Item;
import UI.ItemBag;

public class ItemDrop {
	private int[] IDs;
	private double[] probabilities;
	private int[] quantities;
	private int numRolls;
	public ItemDrop(int[] inIDs, double[] inProbabilities, int[] inQuantities, int inRolls)
	{
		IDs = inIDs;
		probabilities = inProbabilities;
		quantities = inQuantities;
		numRolls = inRolls;
	}
	
	public ItemBag generateItems()
	{
		Item[] items = new Item[numRolls];
		int numItems = 0;
		Random random = Main.random;
		for (int i = 0; i < numRolls; i++)
		{
			for (int currID = 0; currID < IDs.length; currID++)
			{
				if (random.nextDouble() <= probabilities[currID])
				{
					int q = random.nextInt(quantities[currID]);
					if (q > 0)
					{
						Item item = new Item(IDs[currID]);
						item.setQuantity(q);
						items[i] = item;
						numItems++;
					}
					break;
				}
			}
		}
		ItemBag bag = new ItemBag(0, 250, 40, 40, Math.max(1, numItems), 1);
		int itemsAdded = 0;
		for (int i = 0; i < items.length; i++)
		{
			Item curr = items[i];
			if (curr != null)
			{
				bag.addItem(curr, itemsAdded++);
			}
		}
		return bag;
	}
}
