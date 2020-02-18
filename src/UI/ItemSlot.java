package UI;

import Imported.Audio;
import LowLevel.Positionable;

public class ItemSlot extends Positionable{
	private Item item;
	private int slotType;
	private String putSound;
	private String pickSound;
	public ItemSlot(double x, double y, double w, double l, int whichType)
	{
		super(x, y, w, l);
		slotType = whichType;
		putSound = "Inv/chainmail1";
		pickSound = "Inv/cloth";
		if (slotType == Item.wandType)
		{
			putSound = "Inter/interface1";
		}
	}
	public ItemSlot(double x, double y, double w, double l)
	{
		super(x, y, w, l);
		slotType = -1;
		putSound = "Inv/chainmail1";
		pickSound = "Inv/cloth";
	}
	public void playPut()
	{
		Audio.playSound(putSound);
	}
	public void playPick()
	{
		Audio.playSound(pickSound);
	}
	public void setItem(Item inItem)
	{
		item = inItem;
		
	}
	public Item getItem()
	{
		return item;
	}
	public boolean acceptableType(int itemType)
	{
		if (slotType == Item.acceptAll)
		{
			return true;
		}
		if (slotType == Item.acceptNone)
		{
			return false;
		}
		return slotType == itemType;
	}
	public void setType(int newType)
	{
		slotType = newType;
	}
	public int getType()
	{
		return slotType;
	}
}
