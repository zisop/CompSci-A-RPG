package UI;

import LowLevel.Geometrical;
import LowLevel.Image;

public class ToolTip extends Image {
	//In tooltips, ` will act as \n, meaning it indicates the start of a new line
	//I did this because im way too lazy to implement regular expressions stop being mean to me
	public static String[] rawTips = {
			//Wands
			"this``is the boi`ok", "yee", "dab", 
			//Resources
			"An egregious`emerald", "A riveting`ruby", "A shiny`sapphire"
	};
	public static char[][] allTips = new char[rawTips.length][];
	private Geometrical textBox;
	private char[] tip;
	private double fontSize;
	private Item item;
	public ToolTip(Item inItem, double width, double font, int inID, Geometrical inBox)
	{
		super(null, 0, 0, width, 0);
		item = inItem;
		textBox = new Geometrical();
		tip = allTips[inID];
		fontSize = font;
		setLength(inBox.getLength());
		textBox = inBox;
		
	}
	public static void initTips()
	{
		for (int strInd = 0; strInd < rawTips.length; strInd++)
		{
			String currStr = rawTips[strInd];
			
			allTips[strInd] = new char[currStr.length()]; 
			char[] currChars = allTips[strInd];
			for (int charInd = 0; charInd < currStr.length(); charInd++)
			{
				
				currChars[charInd] = currStr.charAt(charInd); 
			}
		}
	}
	
	public void updatePos()
	{
		super.setPos(item.getX() + getWidth() / 2, item.getY() - getLength() / 2);
		textBox.setPos(item.getX() + getWidth() / 2, item.getY() - getLength() / 2);
	}

	public void UIshow()
	{
		updatePos();
		TextDisplay.showText(textBox, tip, fontSize);
	}
}
