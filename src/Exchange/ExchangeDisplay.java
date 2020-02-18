package Exchange;


import LowLevel.Image;
import UI.Item;
import UI.ItemBag;


public class ExchangeDisplay extends Image{
	private ItemExchange exchange;
	private CraftingBox craftBox;
	private int[] inputIDs;
	private int[] inputQuantities;
	private int outputID;
	private ItemBag inputBag;
	private ItemBag outputBag;
	private Item[] itemList;
	private Item outputItem;
	public ExchangeDisplay(CraftingBox box, ItemExchange inExchange)
	{
		super(null, box.getX(), box.getY(), box.getWidth(), box.getLength());
		exchange = inExchange;
		craftBox = box;
		inputBag = craftBox.getInput();
		outputBag = craftBox.getOutput();
		inputIDs = exchange.getDesiredIDs();
		inputQuantities = exchange.getDesiredQuantities();
		
		
		itemList = new Item[inputIDs.length];
		for (int i = 0; i < inputIDs.length; i++)
		{
			Item currItem = new Item(inputIDs[i]);
			currItem.setQuantity(inputQuantities[i]);
			inputBag.addItem(currItem, i);
			inputBag.removeItem(i);
			currItem.setAlpha(100);
			itemList[i] = currItem;
		}
		
		outputID = inExchange.getOutput();
		outputItem = new Item(outputID);
		outputBag.addItem(outputItem, 0);
		
		outputBag.removeItem(0);
		outputItem.setAlpha(100);
		
		
	}
	public void UIshow()
	{
		for (int i = 0; i < itemList.length; i++)
		{
			Item curr = itemList[i];
			if (curr != null && !inputBag.itemIn(i))
			{
				curr.UIshow();
			}
		}
		if (!outputBag.itemIn(0)) {outputItem.UIshow();}
	}
	public int getOutputID()
	{
		return outputID;
	}
}
