package LowLevel;

import org.lwjgl.opengl.GL11;
import LowLevel.Image;

import Game.Main;
import Imported.Texture;

public class Polygon extends Image{
	
	private Point[] basis;
	private Texture image;
	private float monWid;
	private float monLen;
	private float r, g, b, a;
	public Polygon(Point[] points)
	{
		super(null, 0, 0, 0, 0);
		image = Shape.shapes[0];
		monWid = Main.width;
		monLen = Main.length;
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
		basis = points;
		r = 255; g = 255; b = 255; a = 255;
	}
	public Polygon(Point[] points, float red, float green, float blue, float alpha)
	{
		super(null, 0, 0, 0, 0);
		image = Shape.shapes[0];
		monWid = Main.width;
		monLen = Main.length;
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
		basis = points;
		r = red; g = green; b = blue; a = alpha;
	}
	public void setWidth(double newWidth)
	{
		double widthFrac = newWidth / getWidth();
		for (int i = 0; i < basis.length; i++)
		{
			Point currPoint = basis[i];
			double xDiff = currPoint.getX() - getX();
			currPoint.setX(currPoint.getX() + xDiff * widthFrac);
		}
		super.setWidth(newWidth);
	}
	public void setLength(double newLength)
	{
		double lengthFrac = newLength / getLength();
		for (int i = 0; i < basis.length; i++)
		{
			Point currPoint = basis[i];
			double yDiff = currPoint.getY() - getY();
			currPoint.setX(currPoint.getY() + yDiff * lengthFrac);
		}
		super.setWidth(newLength);
	}
	public void setX(double newX)
	{
		double xDiff = newX - getX();
		for (int i = 0; i < basis.length; i++)
		{
			basis[i].setX(basis[i].getX() + xDiff);
		}
		super.setX(newX);
	}
	public void setY(double newY)
	{
		double yDiff = newY - getY();
		for (int i = 0; i < basis.length; i++)
		{
			basis[i].setY(basis[i].getY() + yDiff);
		}
		super.setY(newY);
	}
	public void setPos(double newX, double newY)
	{
		double xDiff = newX - getX();
		double yDiff = newY - getY();
		for (int i = 0; i < basis.length; i++)
		{
			basis[i].setPos(basis[i].getX() + xDiff, basis[i].getY() + yDiff);
		}
		super.setPos(newX, newY);
	}
	//displays the image + rotations if those end up being useful
    public void UIshow() {
        Point[] pointCords = new Point[basis.length];
        for (int i = 0; i < pointCords.length; i++)
        {
        	Point adjustedPoint = new Point(basis[i].getX() - getX(), basis[i].getY() - getY());
        	pointCords[i] = Geometry.rotatePoint(adjustedPoint, getAngle()); 
        }
        
        image.bind();
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glColor4f(r / 255, g / 255, b / 255, a / 255);
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
    	System.out.println("not implemented");
    }
}
