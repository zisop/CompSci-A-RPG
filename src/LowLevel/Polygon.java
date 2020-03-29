package LowLevel;

import org.lwjgl.opengl.GL11;
import LowLevel.Image;

import Game.Main;
import Imported.Texture;

public class Polygon extends Image{
	
	private Texture image;
	public Polygon(Point[] points)
	{
		super(null, 0, 0, 0, 0);
		image = Shape.shapes[0];
		double maxX = points[0].getX();
		double minX = maxX;
		double maxY = points[0].getY();
		double minY = maxY;
		for (int i = 1; i < points.length; i++)
		{
			if (points[i].getX() > maxX)
			{
				maxX = points[i].getX();
			}
			else if (points[i].getX() < minX) {
				minX = points[i].getX();
			}
			if (points[i].getY() > maxY)
			{
				maxY = points[i].getY();
			}
			else if (points[i].getY() < minY) {
				minY = points[i].getY();
			}
		}
		super.setX((maxX + minX) / 2);
		super.setY((minY + maxY) / 2);
		super.setWidth(maxX - minX);
		super.setLength(maxY - minY);
		setShowBasis(points);
		Point[] collBas = new Point[points.length];
		for (int i = 0; i < points.length; i++)
		{
			collBas[i] = new Point(points[i].getX(), points[i].getY()); 
		}
		setCollisionBasis(collBas);
		setCollisionStatus(true);
	}
	public Polygon(Point[] points, float red, float green, float blue, float alpha)
	{
		super(null, 0, 0, 0, 0);
		image = Shape.shapes[0];
		double maxX = points[0].getX();
		double minX = maxX;
		double maxY = points[0].getY();
		double minY = maxY;
		for (int i = 1; i < points.length; i++)
		{
			if (points[i].getX() > maxX)
			{
				maxX = points[i].getX();
			}
			else if (points[i].getX() < minX) {
				minX = points[i].getX();
			}
			if (points[i].getY() > maxY)
			{
				maxY = points[i].getY();
			}
			else if (points[i].getY() < minY) {
				minY = points[i].getY();
			}
		}
		super.setX((maxX + minX) / 2);
		super.setY((minY + maxY) / 2);
		super.setWidth(maxX - minX);
		super.setLength(maxY - minY);
		setShowBasis(points);
		Point[] collBas = new Point[points.length];
		for (int i = 0; i < points.length; i++)
		{
			collBas[i] = new Point(points[i].getX(), points[i].getY()); 
		}
		setCollisionBasis(collBas);
		setRGBA(red, green, blue, alpha);
		setCollisionStatus(true);
	}
	public void setWidth(double newWidth)
	{
		Point[] showBasis = getShowBasis();
		Point[] collBasis = getCollisionBasis();
		double widthFrac = newWidth / getWidth();
		for (int i = 0; i < showBasis.length; i++)
		{
			Point currPoint = showBasis[i];
			double xDiff = currPoint.getX() - getX();
			currPoint.setX(currPoint.getX() + xDiff * widthFrac);
		}
		for (int i = 0; i < collBasis.length; i++)
		{
			Point currPoint = collBasis[i];
			double xDiff = currPoint.getX() - getX();
			currPoint.setX(currPoint.getX() + xDiff * widthFrac);
		}
		super.setWidth(newWidth);
	}
	public void setLength(double newLength)
	{
		Point[] showBasis = getShowBasis();
		Point[] collBasis = getCollisionBasis();
		double lengthFrac = newLength / getLength();
		for (int i = 0; i < showBasis.length; i++)
		{
			Point currPoint = showBasis[i];
			double yDiff = currPoint.getY() - getY();
			currPoint.setX(currPoint.getY() + yDiff * lengthFrac);
		}
		for (int i = 0; i < collBasis.length; i++)
		{
			Point currPoint = collBasis[i];
			double yDiff = currPoint.getY() - getY();
			currPoint.setX(currPoint.getY() + yDiff * lengthFrac);
		}
		super.setWidth(newLength);
	}
	//displays the image + rotations if those end up being useful
    public void UIshow() {
    	Point[] showBasis = getShowBasis();
        Point[] pointCords = new Point[showBasis.length];
        for (int i = 0; i < pointCords.length; i++)
        {
        	Point adjustedPoint = new Point(showBasis[i].getX() - getX(), showBasis[i].getY() - getY());
        	pointCords[i] = Geometry.rotatePoint(adjustedPoint, getAngle()); 
        }
        
        image.bind();
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glColor4f(getRed() / 255, getGreen() / 255, getBlue() / 255, getAlpha() / 255);
        for (int i = 0; i < pointCords.length; i++)
        {
        	float currX = (float)((pointCords[i].getX() + getX()) * 2.0f / monWid);
        	float currY = (float)((pointCords[i].getY() + getY()) * 2.0f / monLen);
        	GL11.glVertex2f(currX, currY);
        }
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnd();
    }
    
    public void show()
    {
    	Point[] showBasis = getShowBasis();
        Point[] pointCords = new Point[showBasis.length];
        for (int i = 0; i < pointCords.length; i++)
        {
        	Point adjustedPoint = new Point(showBasis[i].getX() - getX(), showBasis[i].getY() - getY());
        	pointCords[i] = Geometry.rotatePoint(adjustedPoint, getAngle()); 
        }
        
        image.bind();
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glColor4f(getRed() / 255, getGreen() / 255, getBlue() / 255, getAlpha() / 255);
        for (int i = 0; i < pointCords.length; i++)
        {
        	float currX = (float)((pointCords[i].getX() + getX() - Main.player.getX()) * 2.0f / monWid);
        	float currY = (float)((pointCords[i].getY() + getY() - Main.player.getY()) * 2.0f / monLen);
        	GL11.glVertex2f(currX, currY);
        }
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnd();
    }
    public int relPos(Positionable otherChar)
    {
    	System.out.println("relPos unimplemented for class Polygon");
    	return 0;
    }
    private static int monWid = Main.width;
	private static int monLen = Main.length;
}
