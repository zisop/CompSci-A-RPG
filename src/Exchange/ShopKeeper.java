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
		
		createMenu();
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
			UI.interactingKeepers.add(this);
		}
		super.show();
	}
	public void showMenu()
	{
		if (optionState != talking)
		{
			menu.UIshow();
		}
		if (UI.mouseHovering(menuX))
		{
			menuX.setRGB(100, 200, 200);
			if (UI.shouldInteract())
			{
				closeMenu();
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
				Audio.playSound("NPC/Slime/slime4");
			}
			shopButton.setAlpha(150);
			float frac = 1.2f;
			shopText.setTextRGBA(optionR * frac, optionG * frac, optionB * frac, optionA);
		}
		else if (optionState != shopping)
		{
			shopButton.setAlpha(255);
			shopText.setTextRGBA(optionR, optionG, optionB, optionA);
		}
		if (UI.mouseHovering(talkButton))
		{
			if (UI.mouseInteraction(talkButton))
			{
				optionState = talking;
				setCurr(0);
				Audio.playSound("NPC/Slime/slime4");
			}
			talkButton.setAlpha(150);
			float frac = 1.2f;
			talkText.setTextRGBA(optionR * frac, optionG * frac, optionB * frac, optionA);
		}
		else if (optionState != talking)
		{
			talkButton.setAlpha(255);
			talkText.setTextRGBA(optionR, optionG, optionB, optionA);
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
	public void updateTextState()
    {
		int currText = getCurr();
		char[][][] dialogue = getDialogue();
		int frameNum = getFrameNum();
    	boolean skipped = false;
    	if (currText != dialogue[Main.questState].length - 1)
    	{
    		if (currText != notYetSpeaking && frameNum < dialogue[Main.questState][currText].length)
    		{
    			setFrameNum(dialogue[Main.questState][currText].length);
    			skipped = true;
    		}
    		else
    		{
    			setCurr(getCurr() + 1);
			}
    		Main.alreadyInteracting = true;
    	}
    	else 
    	{
    		if (Main.alreadyInteracting && frameNum < dialogue[Main.questState][currText].length)
    		{
    			setFrameNum(dialogue[Main.questState][currText].length);
    			skipped = true;
    		}
    		else
    		{
    			setCurr(notYetSpeaking);
    			optionState = atMenu;
    		}
		}
    	if (!skipped)
    	{
    		setFrameNum(0);
    	}
    }
	
	private void emptyItems()
	{
		//it empties the crafting box's input items don't fucking ask
		//emptied items are sent to the player's bag or deleted if the player's bag is full
		//this code is either unreadable because i'm horrible at code or because dealing with the exceptions is supposed to suck
		ItemBag inputBag = shop.getBox().getInput();
		ItemSlot[] inputSlots = inputBag.getSlots();
		for (int inputInd = 0; inputInd < inputSlots.length; inputInd++)
		{
			Item inputItem = inputSlots[inputInd].getItem();
			if (inputItem != null)
			{
				boolean shouldDelete = true;
				ItemSlot[] playerSlots = UI.playerBag.getSlots();
				for (int playerInd = 0; playerInd < playerSlots.length; playerInd++)
				{
					ItemSlot playerSlot = playerSlots[playerInd];
					Item playerItem = playerSlot.getItem();
					if (playerItem == null)
					{
						UI.playerBag.addItem(inputItem, playerInd);
						shouldDelete = false;
						break;
					}
					else if (playerItem.getID() == inputItem.getID() && playerItem.getQuantity() < playerItem.getMax())
					{
						int toAdd = Math.min(inputItem.getQuantity() + playerItem.getQuantity(), 
								playerItem.getMax()) - playerItem.getQuantity();
						playerItem.setQuantity(playerItem.getQuantity() + toAdd);
						inputItem.setQuantity(inputItem.getQuantity() - toAdd);
						if (inputItem.getQuantity() > 0)
						{
							playerInd++;
							while (playerInd < playerSlots.length)
							{
								playerSlot = playerSlots[playerInd];
								playerItem = playerSlot.getItem();
								if (playerItem == null)
								{
									UI.playerBag.addItem(inputItem, playerInd);
									shouldDelete = false;
									break;
								}
								else if (playerItem.getID() == inputItem.getID() && playerItem.getQuantity() < playerItem.getMax())
								{
									toAdd = Math.min(inputItem.getQuantity() + playerItem.getQuantity(), 
											playerItem.getMax()) - playerItem.getQuantity();
									playerItem.setQuantity(playerItem.getQuantity() + toAdd);
									inputItem.setQuantity(inputItem.getQuantity() - toAdd);
									shouldDelete = false;
									break;
								}
								playerInd++;
							}
							break;
						}
						else 
						{
							shouldDelete = false;
						}
						break;
					}
				}
				if (shouldDelete) {inputItem.setSlot(Item.destroyItem);}
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
	
	private void closeMenu()
	{
		Main.alreadyInteracting = false;
		Main.interactionEvent = true;
		Main.interactingChar = null;
		menu.setVisibility(false);
		optionState = atMenu;
		emptyItems();
		Audio.playSound("NPC/Slime/slime5", .7);
	}
	
	private void createMenu()
	{
		//Create the menu inside and border
		menu = new Geometrical();
		double offset = 7;
		double leftX = -50;
		double rightX = 50;
		double topY = 75;
		double botY = -75;
		Shape mainRect = Geometry.createRect(leftX, rightX, botY, topY, 50, 50, 50, 255);
		float r, g, b, a;
		r = 20;
		g = 230;
		b = 230;
		a = 255;
		Shape rightRect = Geometry.createRect(rightX, rightX + offset, botY, topY, r, g, b, a);
		Shape leftRect = Geometry.createRect(leftX - offset, leftX, botY, topY, r, g, b, a);
		Shape topRect = Geometry.createRect(leftX - offset, rightX + offset, topY, topY + offset, r, g, b, a);
		Shape botRect = Geometry.createRect(leftX - offset, rightX + offset, botY, botY - offset, r, g, b, a);
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
		mainRect = Geometry.createRect(leftX, rightX, botY, topY + 5, 0, 0, 0, 0);
		r = 100; g = 100; b = 220;
		rightRect = Geometry.createRect(rightX, rightX + offset, botY, topY, r, g, b, a);
		leftRect = Geometry.createRect(leftX - offset, leftX, botY, topY, r, g, b, a);
		topRect = Geometry.createRect(leftX - offset, rightX + offset, topY, topY + offset, r, g, b, a);
		botRect = Geometry.createRect(leftX - offset, rightX + offset, botY, botY - offset, r, g, b, a);
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
				
		textBorder = new Geometrical();
				
		//Create the new variable values for the shopbutton
		botY = 10;
		topY = 35;
		//create the shopbutton
		mainRect = Geometry.createRect(leftX, rightX, botY, topY + 5, 0, 0, 0, 0);
		r = 100; g = 100; b = 220;
		rightRect = Geometry.createRect(rightX, rightX + offset, botY, topY, r, g, b, a);
		leftRect = Geometry.createRect(leftX - offset, leftX, botY, topY, r, g, b, a);
		topRect = Geometry.createRect(leftX - offset, rightX + offset, topY, topY + offset, r, g, b, a);
		botRect = Geometry.createRect(leftX - offset, rightX + offset, botY, botY - offset, r, g, b, a);
		textBorder.addShape(mainRect);
		textBorder.addShape(rightRect);
		textBorder.addShape(topRect);
		textBorder.addShape(botRect);
		textBorder.addShape(leftRect);
				
		fontSize = 18;
		talkText = new TextBox(fontSize, "Talk", textBorder, optionA);
		talkText.setTextRGBA(optionR, optionG, optionB, optionA);
				
		talkButton = new Geometrical();
		talkButton.addShape(talkText);
		menu.addShape(talkButton);
				
		botY = 45;
		topY = 70;
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
}
