package Game;

import Imported.Audio;
import Imported.Texture;
import LowLevel.Image;
import UI.ItemBag;

public class Chest extends Image{
	public static Texture[] chestTex = new Texture[4];
	private ItemBag chestBag;
	private boolean isOpen;
	private int openFrame;
	private int ID;

	public Chest(int inID, double x, double y, double width, double length, ItemBag inBag)
	{
		this(inID, x, y, width, length, width, length * 4 / 5, inBag);
	}
	public Chest(int inID, double x, double y, double width, double length, double charWidth, double charLength, ItemBag inBag)
	{
		super(chestTex[inID * 4], x, y, width, length, charWidth, charLength);
		chestBag = inBag;
		isOpen = false;
		openFrame = 0;
		ID = inID;
		setCollisionStatus(true);
		setProjInteraction(true);
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
		
		if (Main.xInteraction(this) || Main.clickInteraction(this))
		{
			//If I was closed, now I'm open.
			//If I was open, now I'm closed
			setState(!isOpen);
			if (isOpen)
			{
				//If I just opened the chest, the game should start interacting with me
				Main.interactingChar = this;
				Audio.playSound("Door/door", .3);
			}
			else 
			{
				//If I just closed the chest, the game should stop interacting with me
				Main.interactingChar = null;
				
			}
		}
		//Animation garbage
		int initFrame = openFrame;
		if (isOpen && openFrame != 3)
		{
			openFrame++;
			setImage(chestTex[ID * 4 + openFrame]);
		}
		if (!isOpen && openFrame != 0)
		{
			openFrame--;
			setImage(chestTex[ID * 4 + openFrame]);
		}
		if (openFrame != initFrame && (openFrame == 3 || openFrame == 0))
		{
			boolean initVisibility = chestBag.getVisibility();
			chestBag.setVisibility(isOpen);
			if (!isOpen && initVisibility != isOpen) {Audio.playSound("Inter/interface1", 1.3);}
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
