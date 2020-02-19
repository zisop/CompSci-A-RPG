package Exchange;

import Game.NPC;
import LowLevel.Geometrical;

public class ShopKeeper extends NPC{
	private Geometrical menu;
	private Shop shop;
	private int optionState;
	public ShopKeeper(int ID, double x, double y, double w, double l, int inDia, double font)
	{
		super(ID, x, y, w, l, inDia, font);
		optionState = atMenu;
	}
	
	public boolean shouldInteract()
	{
		return optionState == talking && super.shouldInteract();
	}
	
	public static int cowboyUp = 0;
	public static int cowboyDown = 1;
	public static int atMenu = 0;
	public static int talking = 1;
	public static int shopping = 2;
}
