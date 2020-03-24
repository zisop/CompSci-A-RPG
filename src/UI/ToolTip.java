package UI;

import LowLevel.CharTex;
import LowLevel.Geometrical;
import LowLevel.Image;
import LowLevel.Shape;

public class ToolTip extends Image {
	//In tooltips, ` will act as \n, meaning it indicates the start of a new line
	//I did this because im way too lazy to implement regular expressions stop being mean to me
	public static String[] rawItemTips = {
			//Wands
			"A sad stick", 
			"A weak wand", 
			"The wand of a novice wizard",
			"There's a blackmarket for`these wands`among mana`addicts",
			"The devs didn'tmean to leave`this one in",
			//Resources
			"An egregious`emerald", 
			"A riveting`ruby", 
			"A shiny`sapphire",
			"An avant-garde amethyst",
			"Some great gold",
			//Helmets
			"A basic`protective`helmet",
			"Some old`prince's gear",
			"The lost relic of an ancient war",
			"It suckles yourhealth...`to help you",
			"The helmet of an Aztecan`emperor",
			//Rings
			"Worn by King`George III",
			"Has the power`of love",
			"It's made of`brass so you`can trust it",
			"Iron rings justlike minecraft",
			"Crafted by`some famous`blacksmith",
			"Almost has to`be more decor`than utility",
			//Potions
			"The result of a horrible alchemical experiment",
			"A scientist was found dead in a lab next to this",
			//Tomes
			"Increases PP`size",
			"Washed ashore`from some`ocean",
			"Dug up in`Atlantis",
			"Some map fromthe library ledto this one",
			"Found on a`wrecked pirateship",
			"Found next to`a dead peasant "
	};
	private TextBox textBox;
	private Image owner;
	public ToolTip(Image inOwner, double font, String text, Geometrical inBox)
	{
		super(null, 0, 0, 0, 0);
		owner = inOwner;
		super.setWidth(inBox.getWidth());
		super.setLength(inBox.getLength());
		textBox = new TextBox(font, text, inBox);
	}
	
	public void updatePos()
	{
		double offset = 3;
		super.setPos(owner.getX() + getWidth() / 2 + offset, owner.getY() - getLength() / 2 - offset);
		textBox.setPos(owner.getX() + getWidth() / 2 + offset, owner.getY() - getLength() / 2 - offset);
	}
	public void show()
	{
		try {
			throw new Exception("show() not implemented for Tooltip");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void UIshow()
	{
		updatePos();
		textBox.UIshow();
	}
	public static ToolTip defaultTip(String text, Image owner)
	{
		double fontSize = 10;
		double width = 150;
		double yLen = fontSize * 2;
		double currX = fontSize / 2;
		char[] tip = TextDisplay.toChars(text);
		for (int i = 0; i < tip.length; i++)
		{
			char curr = tip[i];
			CharTex temp = TextDisplay.getLetter(curr);
			if (currX >= width - fontSize || curr == '`')
			{
				yLen += fontSize;
				currX = fontSize / 2;
			}
			else
			{
				currX += fontSize * temp.getSpace();
			}
		}
		Geometrical textBox = new Geometrical();
		Shape mainRect = new Shape(0, 0, 0, width, yLen, 100, 255, 255, 200);
		Shape rect1 = new Shape(0, 0, 0, width - fontSize + 3, yLen - fontSize + 3, 0, 100, 255, 200);
		textBox.addShape(mainRect);
		textBox.addShape(rect1);
		ToolTip tooltip = new ToolTip(owner, 10, text, textBox);
		return tooltip;
	}
}
