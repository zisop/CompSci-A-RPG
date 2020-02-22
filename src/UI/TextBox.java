package UI;

import LowLevel.Geometrical;
import LowLevel.Image;

public class TextBox extends Image{
	private char[] text;
	private Geometrical textBox;
	private double fontSize;
	public TextBox(double inSize, char[] myText, Geometrical display)
	{
		super(null, display.getX(), display.getY(), display.getWidth(), display.getLength());
		text = myText;
		textBox = display;
		fontSize = inSize;
		super.setAlpha((textBox.getMain().getAlpha()));
	}
	public TextBox(double inSize, String myText, Geometrical display)
	{
		super(null, display.getX(), display.getY(), display.getWidth(), display.getLength());
		char[] toChars = new char[myText.length()];
		for (int i = 0; i < toChars.length; i++)
		{
			toChars[i]= myText.charAt(i); 
		}
		text = toChars;
		textBox = display;
		fontSize = inSize;
		super.setAlpha((textBox.getMain().getAlpha()));
	}
	public TextBox(double inSize, char[] myText, Geometrical display, float inAlpha)
	{
		super(null, display.getX(), display.getY(), display.getWidth(), display.getLength());
		text = myText;
		textBox = display;
		fontSize = inSize;
		super.setAlpha(inAlpha);
	}
	public TextBox(double inSize, String myText, Geometrical display, float inAlpha)
	{
		super(null, display.getX(), display.getY(), display.getWidth(), display.getLength());
		char[] toChars = new char[myText.length()];
		for (int i = 0; i < toChars.length; i++)
		{
			toChars[i]= myText.charAt(i); 
		}
		text = toChars;
		textBox = display;
		fontSize = inSize;
		super.setAlpha(inAlpha);
	}
	public void setText(String newText)
	{
		char[] newChars = new char[newText.length()];
		for (int i = 0; i < newChars.length; i++)
		{
			newChars[i] = newText.charAt(i); 
		}
		text = newChars;
	}
	public double getFontSize()
	{
		return fontSize;
	}
	public void setFontSize(double newSize)
	{
		fontSize = newSize;
	}
	public void setX(double newX)
	{
		textBox.setX(newX);
		super.setX(newX);
	}
	public void setY(double newY)
	{
		textBox.setY(newY);
		super.setY(newY);
	}
	public void setWidth(double newWidth)
	{
		textBox.setWidth(newWidth);
		super.setWidth(newWidth);
	}
	public void setLength(double newLength)
	{
		textBox.setLength(newLength);
		super.setLength(newLength);
	}
	public void UIshow()
	{
		TextDisplay.showText(textBox, text, fontSize, getAlpha());
	}
	public void setAlpha(float newAlpha)
	{
		textBox.setAlpha(newAlpha);
		super.setAlpha(newAlpha);
	}
}
