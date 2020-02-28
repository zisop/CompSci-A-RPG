package LowLevel;

public class Point {
	private double x;
	private double y;
	public Point(double inX, double inY)
	{
		x = inX;
		y = inY;
	}
	public void setX(double newX)
	{
		x = newX;
	}
	public void setY(double newY)
	{
		y = newY;
	}
	public double getX()
	{
		return x;
	}
	public double getY()
	{
		return y;
	}
	public void setPos(double newX, double newY)
	{
		x = newX;
		y = newY;
	}
	public String toString()
	{
		return "(" + getX() + ", " + getY() + ")";
	}
}
