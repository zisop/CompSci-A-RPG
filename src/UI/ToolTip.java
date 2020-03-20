package UI;

import LowLevel.Geometrical;
import LowLevel.Image;
import LowLevel.Positionable;
import LowLevel.Shape;

public class ToolTip extends Image {
	//In tooltips, ` will act as \n, meaning it indicates the start of a new line
	//I did this because im way too lazy to implement regular expressions stop being mean to me
	public static String[] rawItemTips = {
			//Wands
			"this``is the boi`ok", "yee", "dab", 
			//Resources
			"An egregious`emerald", "A riveting`ruby", "A shiny`sapphire"
	};
	public static char[][] itemTips = new char[rawItemTips.length][];
	private TextBox textBox;
	private Image owner;
	public ToolTip(Image inOwner, double font, int inID, Geometrical inBox)
	{
		this(inOwner, font, rawItemTips[inID], inBox);
	}
	public ToolTip(Image inOwner, double font, String text, Geometrical inBox)
	{
		super(null, 0, 0, 0, 0);
		owner = inOwner;
		super.setWidth(inBox.getWidth());
		super.setLength(inBox.getLength());
		textBox = new TextBox(font, text, inBox);
	}
	public static void initTips()
	{
		for (int strInd = 0; strInd < rawItemTips.length; strInd++)
		{
			String currStr = rawItemTips[strInd];
			itemTips[strInd] = TextDisplay.toChars(currStr);
		}
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
			if (currX >= width - fontSize / 2 || tip[i] == '`')
			{
				yLen += fontSize;
				currX = fontSize / 2;
			}
			else
			{
				currX += fontSize;
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
