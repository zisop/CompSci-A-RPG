package Exchange;


import Game.Main;
import Game.NPC;
import Imported.Audio;
import LowLevel.Geometrical;
import LowLevel.Geometry;
import LowLevel.Shape;
import UI.TextBox;
import UI.UI;

public class ShopKeeper extends NPC{
	private Geometrical menu;
	private Shape menuX;
	
	private Geometrical talkButton;
	private TextBox talkText;
	
	private Geometrical shopButton;
	private TextBox shopText;
	private Shape shopX;
	private Shop shop;
	
	
	private int optionState;
	public ShopKeeper(int ID, double x, double y, double w, double l, int inDia, double font, Shop inShop)
	{
		super(ID, x, y, w, l, inDia, font);
		shop = inShop;
		optionState = atMenu;
		
		menu = new Geometrical();
		double offset = 7;
		double leftX = -50;
		double rightX = 50;
		double topY = 75;
		double botY = -75;
		Shape mainRect = Geometry.createSquare(leftX, rightX, botY, topY, 50, 50, 50, 255);
		float r, g, b, a;
		r = 20;
		g = 230;
		b = 230;
		a = 255;
		Shape rightRect = Geometry.createSquare(rightX, rightX + offset, botY, topY, r, g, b, a);
		Shape leftRect = Geometry.createSquare(leftX - offset, leftX, botY, topY, r, g, b, a);
		Shape topRect = Geometry.createSquare(leftX - offset, rightX + offset, topY, topY + offset, r, g, b, a);
		Shape botRect = Geometry.createSquare(leftX - offset, rightX + offset, botY, botY - offset, r, g, b, a);
		double radius = 10;
		double circleOffset = 5;
		r = 200; g = 200; b = 80;
		Shape ULcircle = new Shape(Shape.ellipse, leftX - circleOffset, topY + circleOffset, radius, radius, r, g, b, a);
		Shape URcircle = new Shape(Shape.ellipse, rightX + circleOffset, topY + circleOffset, radius, radius, r, g, b, a);
		Shape DLcircle = new Shape(Shape.ellipse, leftX - circleOffset, botY - circleOffset, radius, radius, r, g, b, a);
		Shape DRcircle = new Shape(Shape.ellipse, rightX + circleOffset, botY - circleOffset, radius, radius, r, g, b, a);
		menu.addShape(mainRect);
		menu.addShape(rightRect);
		menu.addShape(leftRect);
		menu.addShape(topRect);
		menu.addShape(botRect);
		menu.addShape(ULcircle);
		menu.addShape(URcircle);
		menu.addShape(DLcircle);
		menu.addShape(DRcircle);
		menu.setAlpha(200);
		
		
		
		
		Geometrical textBorder = new Geometrical();
		
		
		leftX += 5;
		rightX -= 5;
		offset = 2.5;
		botY = 45;
		topY = 70;
		mainRect = Geometry.createSquare(leftX, rightX, botY, topY + 5, 0, 0, 0, 0);
		r = 100; g = 100; b = 220;
		rightRect = Geometry.createSquare(rightX, rightX + offset, botY, topY, r, g, b, a);
		leftRect = Geometry.createSquare(leftX - offset, leftX, botY, topY, r, g, b, a);
		topRect = Geometry.createSquare(leftX - offset, rightX + offset, topY, topY + offset, r, g, b, a);
		botRect = Geometry.createSquare(leftX - offset, rightX + offset, botY, botY - offset, r, g, b, a);
		textBorder.addShape(mainRect);
		textBorder.addShape(rightRect);
		textBorder.addShape(topRect);
		textBorder.addShape(botRect);
		textBorder.addShape(leftRect);
		
		double fontSize = 18;
		shopText = new TextBox(fontSize, "Shop", textBorder, optionA);
		shopText.setTextRGBA(optionR, optionG, optionB, optionA);
		
		shopButton = new Geometrical();
		shopButton.addShape(shopText);
		menu.addShape(shopButton);
		
		double xOffset = 25;
		double xRadius = 35;
		menuX = new Shape(Shape.xButton, rightX + xOffset / 2, topY + xOffset, xRadius, xRadius);
		menu.addShape(menuX);
		
		menu.setPos(400, -300);
		menu.setVisibility(false);
		
		
	}
	
	public void show()
	{
		//NPC's show function would mess up with text
		if ((Main.xInteraction(this) || Main.clickInteraction(this)) && !Main.alreadyInteracting)
		{
			Main.alreadyInteracting = true;
			Main.interactionEvent = true;
			Main.interactingChar = this;
			menu.setVisibility(true);
			Audio.playSound("NPC/Slime/slime4");
			
		}
		if (menu.isVisible()) {
			UI.visMenus.add(this);
		}
		super.show();
	}
	public void showMenu()
	{
		menu.UIshow();
		if (UI.mouseHovering(menuX))
		{
			menuX.setRGB(100, 200, 200);
			if (UI.shouldInteract())
			{
				Main.alreadyInteracting = false;
				Main.interactionEvent = true;
				Main.interactingChar = null;
				menu.setVisibility(false);
				Audio.playSound("NPC/Slime/slime5", .7);
			}
		}
		else 
		{
			menuX.setRGB(255, 255, 255);
		}
		if (UI.mouseHovering(shopButton))
		{
			if (UI.mouseInteraction(shopButton))
			{
				optionState = shopping;
			}
			shopButton.setAlpha(150);
			float frac = 1.2f;
			shopText.setTextRGBA(optionR * frac, optionG * frac, optionB * frac, optionA);
		}
		else 
		{
			if (optionState != shopping)
			{
				shopButton.setAlpha(255);
				shopText.setTextRGBA(optionR, optionG, optionB, optionA);
			}
		}
		if (optionState == shopping)
		{
			shop.UIshow();
		}
	}
	
	//NPC's shouldInteract function, but only if the shopkeeper is in its talking state
	public boolean shouldInteract()
	{
		return optionState == talking && super.shouldInteract();
	}
	public boolean showingText()
	{
		return optionState == talking && super.showingText();
	}
	
	public static int cowboyUp = 0;
	public static int cowboyDown = 1;
	public static int atMenu = 0;
	public static int talking = 1;
	public static int shopping = 2;
	
	public static float optionR = 200;
	public static float optionG = 120;
	public static float optionB = 200;
	public static float optionA = 200;
}
