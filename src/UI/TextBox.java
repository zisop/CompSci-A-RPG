package UI;

import LowLevel.Geometrical;
import LowLevel.Image;

public class TextBox extends Image{
	private char[] text;
	private Geometrical textBox;
	private double fontSize;
	private float textRed;
	private float textGreen;
	private float textBlue;
	private float textAlpha;
	public TextBox(double inSize, char[] myText, Geometrical display)
	{
		super(null, display.getX(), display.getY(), display.getWidth(), display.getLength());
		text = myText;
		textBox = display;
		fontSize = inSize;
		textRed = 0;
		textBlue = 0;
		textGreen = 0;
		super.setAlpha((textBox.getMain().getAlpha()));
		textAlpha = getAlpha();
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
		textRed = 0;
		textBlue = 0;
		textGreen = 0;
		super.setAlpha((textBox.getMain().getAlpha()));
		textAlpha = getAlpha();
	}
	public TextBox(double inSize, char[] myText, Geometrical display, float inAlpha)
	{
		super(null, display.getX(), display.getY(), display.getWidth(), display.getLength());
		text = myText;
		textBox = display;
		fontSize = inSize;
		textRed = 0;
		textBlue = 0;
		textGreen = 0;
		super.setAlpha(inAlpha);
		textAlpha = inAlpha;
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
		textRed = 0;
		textBlue = 0;
		textGreen = 0;
		super.setAlpha(inAlpha);
		textAlpha = inAlpha;
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
	public void setTextRed(float newRed) {textRed = newRed;}
	public void setTextGreen(float newGreen) {textGreen = newGreen;}
	public void setTextBlue(float newBlue) {textBlue = newBlue;}
	public void setTextAlpha(float newAlpha) {textAlpha = newAlpha;}
	public float getTextRed() {return textRed;}
	public float getTextGreen() {return textGreen;}
	public float getTextBlue() {return textBlue;}
	public float getTextAlpha() {return textAlpha;}
	public void setTextRGBA(float newRed, float newGreen, float newBlue, float newAlpha)
	{
		setTextRed(newRed);
		setTextBlue(newBlue);
		setTextAlpha(newAlpha);
		setTextGreen(newGreen);
	}
	public void UIshow()
	{
		TextDisplay.showText(textBox, text, fontSize, text.length, textAlpha, textRed, textGreen, textBlue);
	}
	public void setAlpha(float newAlpha)
	{
		textBox.setAlpha(newAlpha);
		super.setAlpha(newAlpha);
	}
}
