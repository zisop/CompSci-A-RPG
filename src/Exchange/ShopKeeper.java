package Exchange;

import Game.Movable;
import Game.NPC;
import LowLevel.Geometrical;
import LowLevel.Geometry;
import LowLevel.Image;
import LowLevel.Shape;
import UI.TextBox;

public class ShopKeeper extends NPC{
	private Geometrical menu;
	private Geometrical shopButton;
	private Geometrical talkButton;
	private Image xButton;
	private Shop shop;
	private int optionState;
	public ShopKeeper(int ID, double x, double y, double w, double l, int inDia, double font)
	{
		super(ID, x, y, w, l, inDia, font);
		optionState = atMenu;
		menu = new Geometrical();
		double offset = 10;
		double leftX = -400;
		double rightX = 400;
		double topY = 200;
		double botY = -200;
		Shape mainRect = Geometry.createSquare(leftX, rightX, botY, topY, 230, 150, 230, 255);
		Shape rightRect = Geometry.createSquare(rightX, rightX + offset, botY, topY, 20, 230, 230, 255);
		Shape leftRect = Geometry.createSquare(leftX - offset, leftX, botY, topY, 20, 230, 230, 255);
		Shape topRect = Geometry.createSquare(leftX - offset, rightX + offset, topY, topY + offset, 20, 230, 230, 255);
		Shape botRect = Geometry.createSquare(leftX - offset, rightX + offset, botY, botY - offset, 20, 230, 230, 255);
		double radius = 30;
		Shape ULcircle = new Shape(Shape.ellipse, leftX, topY, radius, radius, 200, 200, 80, 255);
		Shape URcircle = new Shape(Shape.ellipse, leftX, botY, radius, radius, 200, 200, 80, 255);
		Shape DLcircle = new Shape(Shape.ellipse, rightX, topY, radius, radius, 200, 200, 80, 255);
		Shape DRcircle = new Shape(Shape.ellipse, rightX, botY, radius, radius, 200, 200, 80, 255);
		menu.addShape(mainRect);
		menu.addShape(rightRect);
		menu.addShape(leftRect);
		menu.addShape(topRect);
		menu.addShape(botRect);
		menu.addShape(ULcircle);
		menu.addShape(URcircle);
		menu.addShape(DLcircle);
		menu.addShape(DRcircle);
		
		//shopButton = new Geometrical();
		
		//new TextBox(20, "Shop", shopButton);
	}
	
	public void show()
	{
		//NPC's show function would mess up with text
		optionState = talking;
		super.show();
		//menu.UIshow();
	}
	
	//NPC's shouldInteract function, but only if the shopkeeper is in its talking state
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
