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
    
    public Shape(int ID, double x, double y, double width, double length)
	{
		super(shapes[ID], x, y, width, length);
		setCollisionStatus(true);
	}
	public Shape(int ID, double x, double y, double width, double length, float r, float g, float b, float a)
	{
		super(shapes[ID], x, y, width, length);
		setRed(r);
		setGreen(g);
		setBlue(b);
		setAlpha(a);
		setCollisionStatus(true);
	}
	
	public static final int square = 0;
	public static final int ellipse = 1;
	public static final int xButton = 2;
}
