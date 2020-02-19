package Game;

import Imported.Texture;
import UI.ItemBag;

public class Chest extends Displayable{
	public static Texture[] chestTex = new Texture[4];
	private ItemBag chestBag;
	private boolean isOpen;
	private int openFrame;
	private int whichChest;

	public Chest(int inChest, double x, double y, double width, double length, ItemBag inBag)
	{
		super(chestTex[inChest * 4], x, y, width, length, width, length * 4/5);
		chestBag = inBag;
		isOpen = false;
		openFrame = 0;
		whichChest = inChest;
	}
	public Chest(int inChest, double x, double y, double width, double length, double charWidth, double charLength, ItemBag inBag)
	{
		super(chestTex[inChest * 4], x, y, width, length, width, length * 4/5);
		chestBag = inBag;
		isOpen = false;
		openFrame = 0;
		whichChest = inChest;
	}
	public void setState(boolean state)
	{
		isOpen = state;
		Main.alreadyInteracting = state;
	}
	public boolean getState()
	{
		return isOpen;
	}

	public void show()
	{
		//Basically, the game checks that this is the object it's supposed to be interacting with
		//The player should not be interacting with two things at once
		
		if (Main.xInteraction(this, 20) || Main.clickInteraction(this))
		{
			//If I was closed, now I'm open.
			//If I was open, now I'm closed
			setState(!isOpen);
			if (isOpen)
			{
				//If I just opened the chest, the game should start interacting with me
				Main.interactingChar = this;
			}
			else 
			{
				//If I just closed the chest, the game should stop interacting with me
				Main.interactingChar = null;
			}
			//If we just interacted with this chest, the game needs to know not to interact with anybody else
			//This is performed via an xEvent boolean
			Main.xEvent = true;
		}
		//Animation garbage
		if (isOpen && openFrame != 3)
		{
			openFrame++;
			setImage(chestTex[whichChest * 4 + openFrame]);
		}
		if (!isOpen && openFrame != 0)
		{
			openFrame--;
			setImage(chestTex[whichChest * 4 + openFrame]);
		}
		if (openFrame == 3 || openFrame == 0)
		{
			chestBag.setVisibility(isOpen);
		}
		
		super.show();
	}
	public static void initChests()
	{
		//Inits the chest textures
		chestTex[0] = new Texture("Chests/RedChest/RedChest0.png");
		chestTex[1] = new Texture("Chests/RedChest/RedChest1.png");
		chestTex[2] = new Texture("Chests/RedChest/RedChest2.png");
		chestTex[3] = new Texture("Chests/RedChest/RedChest3.png");
	}
}