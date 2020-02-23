package Exchange;


import Game.Main;
import Game.NPC;
import Imported.Audio;
import LowLevel.Geometrical;
import LowLevel.Geometry;
import LowLevel.Positionable;
import LowLevel.Shape;
import UI.Item;
import UI.ItemBag;
import UI.ItemSlot;
import UI.TextBox;
import UI.UI;
import sun.java2d.opengl.OGLContext;

public class ShopKeeper extends NPC{
	private Geometrical menu;
	private Shape menuX;
	
	private Geometrical talkButton;
	private TextBox talkText;
	
	private Geometrical shopButton;
	private TextBox shopText;
	private Shape shopX;
	private Geometrical shopScreen;
	private Shop shop;
	
	
	private int optionState;
	public ShopKeeper(int ID, double x, double y, double w, double l, int inDia, double font, Shop inShop)
	{
		super(ID, x, y, w, l, inDia, font);
		shopScreen = new Geometrical();
		shop = inShop;
		shopScreen.addShape(shop);
		optionState = atMenu;
		
		//Create the menu inside and border
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
		
		//Create the new variable values for the shopbutton
		leftX += 5;
		rightX -= 5;
		offset = 2.5;
		botY = 45;
		topY = 70;
		//create the shopbutton
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
		
		xOffset = 18;
		Positionable selection = shop.getSelection();
		double cornerX = selection.getX() + selection.getWidth() / 2;
		double cornerY = selection.getY() + selection.getLength() / 2;
		shopX = new Shape(Shape.xButton, cornerX + xOffset / 2, cornerY + xOffset, xRadius, xRadius);
		shopScreen.addShape(shopX);
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
				optionState = atMenu;
				emptyItems();
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
			shopScreen.UIshow();
			if (UI.mouseHovering(shopX))
			{
				shopX.setRGB(100, 200, 200);
				if (UI.shouldInteract())
				{
					optionState = atMenu;
					emptyItems();
					Audio.playSound("NPC/Slime/slime5", .7);
				}
			}
			else 
			{
				shopX.setRGB(255, 255, 255);
			}
		}
	}
	public void emptyItems()
	{
		ItemBag inputBag = shop.getBox().getInput();
		ItemSlot[] inputSlots = inputBag.getSlots();
		for (int inputInd = 0; inputInd < inputSlots.length; inputInd++)
		{
			Item inputItem = inputSlots[inputInd].getItem();
			if (inputItem != null)
			{
				boolean shouldKeep = false;
				ItemSlot[] playerSlots = UI.playerBag.getSlots();
				for (int playerInd = 0; playerInd < UI.playerBag.getSlots().length; playerInd++)
				{
					ItemSlot playerSlot = playerSlots[playerInd];
					Item playerItem = playerSlot.getItem();
					if (playerItem == null)
					{
						UI.playerBag.addItem(inputItem, playerInd);
						shouldKeep = true;
						break;
					}
					else if (playerItem.getID() == inputItem.getID() && playerItem.getQuantity() < playerItem.getMax())
					{
						int toAdd = Math.min(inputItem.getQuantity() + playerItem.getQuantity(), 
								playerItem.getMax()) - playerItem.getQuantity();
						playerItem.setQuantity(playerItem.getQuantity() + toAdd);
						inputItem.setQuantity(toAdd - inputItem.getQuantity());
						if (inputItem.getQuantity() > 0)
						{
							inputInd--;
							continue;
						}
						else 
						{
							shouldKeep = true;
						}
						break;
					}
					
				}
				if (!shouldKeep) {inputItem.setSlot(Item.destroyItem);}
				inputBag.removeItem(inputInd);
			}
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
