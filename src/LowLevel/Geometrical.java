package LowLevel;

import java.util.ArrayList;

public class Geometrical extends Image{
	private ArrayList<Image> shapes;
	public Geometrical()
	{
		super(null, 0, 0, 0, 0);
		shapes = new ArrayList<Image>();
	}
	public void UIshow()
	{
		for (int i = 0; i < shapes.size(); i++)
		{
			shapes.get(i).UIshow();
		}
	}
	public void show()
	{
		for (int i = 0; i < shapes.size(); i++)
		{
			shapes.get(i).show();
		}
	}
	public void setAlpha(float newAlpha)
	{
		float alphaFrac = newAlpha / getAlpha();
		for (int i = 0; i < shapes.size(); i++)
		{
			shapes.get(i).setAlpha(shapes.get(i).getAlpha() * alphaFrac);
		}
		super.setAlpha(newAlpha);
	}
	
	public void addShape(Image shape)
	{
		shapes.add(shape);
		if (shapes.size() == 1)
		{
			super.setX(shape.getX());
			super.setY(shape.getY());
			super.setWidth(shape.getWidth());
			super.setLength(shape.getLength());
		}
	}
	public Image getShape(int i)
	{
		return shapes.get(i);
	}
	public void setPos(double newX, double newY)
	{
		double xDiff = newX - getX();
		double yDiff = newY - getY();
		super.setPos(newX, newY);
		for (int i = 0; i < shapes.size(); i++)
		{
			Image currShape = shapes.get(i);
			double currX = currShape.getX();
			double currY = currShape.getY();
			currShape.setPos(currX + xDiff, currY + yDiff);
		}
	}
	public void setX(double newX)
	{
		double xDiff = newX - getX();
		super.setX(newX);
		for (int i = 0; i < shapes.size(); i++)
		{
			Image currShape = shapes.get(i);
			double currX = currShape.getX();
			currShape.setX(currX + xDiff);
		}
	}
	public void setY(double newY)
	{
		double yDiff = newY - getY();
		super.setY(newY);
		for (int i = 0; i < shapes.size(); i++)
		{
			Image currShape = shapes.get(i);
			double currY = currShape.getY();
			currShape.setY(currY + yDiff);
		}
	}
	public void setWidth(double newWidth)
	{
		double widthFrac = newWidth / getWidth();
		super.setWidth(newWidth);
		for (int i = 0; i < shapes.size(); i++)
		{
			Image currShape = shapes.get(i);
			double currWidth = currShape.getWidth();
			currShape.setWidth(currWidth * widthFrac);
		}
	}
	public void setLength(double newLength)
	{
		double lengthFrac = newLength / getWidth();
		super.setLength(newLength);
		for (int i = 0; i < shapes.size(); i++)
		{
			Image currShape = shapes.get(i);
			double currLength = currShape.getWidth();
			currShape.setWidth(currLength * lengthFrac);
		}
	}
	public Shape getMain()
	{
		return (Shape)shapes.get(0);
	}
}
