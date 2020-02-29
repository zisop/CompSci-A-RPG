package LowLevel;

public class Point implements Comparable<Point>{
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
	public int compareTo(Point other)
	{
		if (getX() > other.getX())
		{
			return 1;
		}
		else if (getX() < other.getX())
		{
			return -1;
		}
		if (getY() > other.getY())
		{
			return 1;
		}
		else if (getY() < other.getY())
		{
			return -1;
		}
		return 0;
	}
	public boolean equals(Point other)
	{
		return compareTo(other) == 0;
	}
}
