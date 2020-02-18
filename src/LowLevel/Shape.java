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
    private float alpha;
    
    public Shape(int ID, double x, double y, double width, double length)
	{
		super(shapes[ID], x, y, width, length);
		red = 255;
		green = 255;
		blue = 255;
		alpha = 255;
	}
	public Shape(int ID, double x, double y, double width, double length, float r, float b, float g, float a)
	{
		super(shapes[ID], x, y, width, length);
		red = r;
		green = g;
		blue = b;
		alpha = a;
	}
	public void UIshow()
	{
		super.UIshow(red, blue, green, alpha);
	}
	public float getRed() {return red;}
	public float getBlue() {return blue;}
	public float getGreen() {return green;}
	public float getAlpha() {return alpha;}
	public void setRed(float newRed) {red = newRed;}
	public void setBlue(float newBlue) {blue = newBlue;}
	public void setGreen(float newGreen) {green = newGreen;}
	public void setAlpha(float newAlpha) {alpha = newAlpha;}
	public static int square = 0;
	public static int ellipse = 1;
	public static int xButton = 2;
}
