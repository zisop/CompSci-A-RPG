package LowLevel;

public class Line {
	private Point p1;
	private Point p2;
	private double m;
	private double b;
	private boolean isVertical;
	private double xMin;
	private double xMax;
	private double yMin;
	private double yMax;
	private Point yMinPoint;
	private Point yMaxPoint;
	private Point xMinPoint;
	private Point xMaxPoint;
	public Line(Point p1, Point p2)
	{
		this.p1 = p1;
		this.p2 = p2;
		if (p1.getX() == p2.getX())
		{
			isVertical = true;
		}
		else 
		{
			isVertical = false;
			m = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
			b = (p2.getY() - p2.getX() * m);
		}
		yMin = Math.min(p1.getY(), p2.getY());
		yMax = Math.max(p1.getY(), p2.getY());
		if (yMin == p1.getY()) {yMinPoint = p1; yMaxPoint = p2;} else {yMinPoint = p2; yMaxPoint = p1;}
		xMin = Math.min(p1.getX(), p2.getX());
		xMax = Math.max(p1.getX(), p2.getX());
		if (xMin == p1.getX()) {xMinPoint = p1; xMaxPoint = p2;} else {xMinPoint = p2; xMaxPoint = p1;}
	}
	public boolean overlapping(Line otherLine)
	{
		boolean bothVertical = isVertical() && otherLine.isVertical();
		if (bothVertical)
		{
			return (getXMin() == otherLine.getXMin() && getYMin() <= otherLine.getYMin() && otherLine.getYMin() <= getYMax() && 
					getYMin() <= otherLine.getYMax() && otherLine.getYMax() <= getYMax());
		}
		boolean sameLine = !isVertical() && !otherLine.isVertical() && (getSlope() == otherLine.getSlope() && getB() == otherLine.getB());
		if (sameLine)
		{
			return (getXMin() <= otherLine.getXMin() && otherLine.getXMin() <= getXMax())
					&& (getXMin() <= otherLine.getXMax() && otherLine.getXMax() <= getXMax());
		}
		return false;
	}
	public Line[] eraseOverlap(Line otherLine)
	{
		boolean bothVertical = isVertical() && otherLine.isVertical();
		Line line1;
		Line line2;
		if (bothVertical)
		{
			double overlapYMin = Math.max(getYMin(), otherLine.getYMin());
			double overlapYMax = Math.min(getYMax(), otherLine.getYMax());
			line1 = new Line(new Point(getXMin(), getYMin()), new Point(getXMin(), overlapYMin));
			line2 = new Line(new Point(getXMin(), overlapYMax), new Point(getXMin(), getYMax()));
		}
		else 
		{
			double overlapXMin = Math.max(getXMin(), otherLine.getXMin());
			double overlapXMax = Math.min(getXMax(), otherLine.getXMax());
			line1 = new Line(pointAt(getXMin()), pointAt(overlapXMin));
			line2 = new Line(pointAt(overlapXMax), pointAt(getXMax()));
		}
		Line[] lines = new Line[] {line1, line2};
		return lines;
	}
	public void combine(Line otherLine)
	{
		boolean bothVertical = isVertical() && otherLine.isVertical();
		if (bothVertical)
		{
			if (otherLine.getYMax() >= getYMax())
			{
				if (getP2().getY() == getYMax())
				{
					setP2(new Point(getP2().getX(), otherLine.getYMax()));
				}
				else 
				{
					setP1(new Point(getP1().getX(), otherLine.getYMax()));
				}
			}
			else 
			{
				if (getP2().getY() == getYMin())
				{
					setP2(new Point(getP2().getX(), otherLine.getYMin()));
				}
				else 
				{
					setP1(new Point(getP1().getX(), otherLine.getYMin()));
				}
			}
		}
		else
		{
			if (otherLine.getXMax() >= getXMax())
			{
				if (getP2().getX() == getXMax())
				{
					setP2(pointAt(otherLine.getXMax()));
				}
				else 
				{
					setP1(pointAt(otherLine.getXMax()));
				}
			}
			else 
			{
				if (getP2().getX() == getXMin())
				{
					setP2(pointAt(getXMin()));
				}
				else 
				{
					setP1(pointAt(getXMin()));
				}
			}
		}
	}
	public boolean adjacent(Line otherLine)
	{
		boolean bothVertical = isVertical() && otherLine.isVertical();
		if (bothVertical)
		{
			return getXMin() == otherLine.getXMin() && (getYMin() == otherLine.getYMax() || getYMax() == otherLine.getYMin());
		}
		boolean sameLine = !isVertical() && !otherLine.isVertical() && (getSlope() == otherLine.getSlope() && getB() == otherLine.getB());
		if (sameLine)
		{
			return (getXMin() == otherLine.getXMax() || getXMax() == otherLine.getXMin());
		}
		return false;
	}
	public void setP1(Point newPoint)
	{
		p1 = newPoint;
		if (p1.getX() == p2.getX())
		{
			isVertical = true;
		}
		else 
		{
			isVertical = false;
			m = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
			b = (p2.getY() - p2.getX() * m);
		}
		yMin = Math.min(p1.getY(), p2.getY());
		yMax = Math.max(p1.getY(), p2.getY());
		if (yMin == p1.getY()) {yMinPoint = p1; yMaxPoint = p2;} else {yMinPoint = p2; yMaxPoint = p1;}
		xMin = Math.min(p1.getX(), p2.getX());
		xMax = Math.max(p1.getX(), p2.getX());
		if (xMin == p1.getX()) {xMinPoint = p1; xMaxPoint = p2;} else {xMinPoint = p2; xMaxPoint = p1;}
	}
	public void setP2(Point newPoint)
	{
		p2 = newPoint;
		if (p1.getX() == p2.getX())
		{
			isVertical = true;
		}
		else 
		{
			isVertical = false;
			m = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
			b = (p2.getY() - p2.getX() * m);
		}
		yMin = Math.min(p1.getY(), p2.getY());
		yMax = Math.max(p1.getY(), p2.getY());
		if (yMin == p1.getY()) {yMinPoint = p1; yMaxPoint = p2;} else {yMinPoint = p2; yMaxPoint = p1;}
		xMin = Math.min(p1.getX(), p2.getX());
		xMax = Math.max(p1.getX(), p2.getX());
		if (xMin == p1.getX()) {xMinPoint = p1; xMaxPoint = p2;} else {xMinPoint = p2; xMaxPoint = p1;}
	}
	public Point pointAt(double xVal) {return new Point(xVal, m * xVal + b);}
	public boolean intersection(Line otherLine) {return Geometry.lineIntersection(getP1(), getP2(), otherLine.getP1(), otherLine.getP2());}
	public void delete() {setP1(new Point(0, 0)); setP2(new Point(0, 0));}
	public boolean isDeleted() {return p1.equals(p2);}
	public Point getP1() {return p1;}
	public Point getP2() {return p2;}
	public double getSlope() {return m;}
	public double getB() {return b;}
	public double getXMin() {return xMin;}
	public double getXMax() {return xMax;}
	public double getYMin() {return yMin;}
	public double getYMax() {return yMax;}
	public boolean isVertical() {return isVertical;}
	
	public String toString()
	{
		return p1.toString() + " -> " + p2.toString();
	}
}
