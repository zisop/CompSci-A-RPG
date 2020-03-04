package LowLevel;

import Imported.Texture;

public class Shape extends Image {
	public static Texture[] shapes = new Texture[3];
	public static void initShapes()
	{
		shapes[0] = new Texture("Shapes/square.jpg");
	    shapes[1] = new Texture("Shapes/circle.jpg");
	    shapes[2] = new Texture("Shapes/xButton.png");
	}
	
    
    private float red;
    private float blue;
    private float green;
    
    public Shape(int ID, double x, double y, double width, double length)
	{
		super(shapes[ID], x, y, width, length);
		red = 255;
		green = 255;
		blue = 255;
		setCollisionStatus(true);
	}
	public Shape(int ID, double x, double y, double width, double length, float r, float b, float g, float a)
	{
		super(shapes[ID], x, y, width, length);
		red = r;
		green = g;
		blue = b;
		setAlpha(a);
		setCollisionStatus(true);
	}
	public void UIshow()
	{
		super.UIshow(red, blue, green, getAlpha());
	}
	public void show()
	{
		super.show(red, blue, green, getAlpha());
	}
	public float getRed() {return red;}
	public float getBlue() {return blue;}
	public float getGreen() {return green;}
	public void setRed(float newRed) {red = newRed;}
	public void setBlue(float newBlue) {blue = newBlue;}
	public void setGreen(float newGreen) {green = newGreen;}
	public void setRGB(float newRed, float newBlue, float newGreen)
	{
		red = newRed;
		green = newGreen;
		blue = newBlue;
	}
	public static final int square = 0;
	public static final int ellipse = 1;
	public static final int xButton = 2;
}
