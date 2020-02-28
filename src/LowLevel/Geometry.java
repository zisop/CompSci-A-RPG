package LowLevel;

public class Geometry
{
    public static Point rotatePoint(Point point, double angle) {
    	double x = point.getX();
    	double y = point.getY();
        angle = Math.toRadians(angle);
        final double hypoLen = Math.sqrt(x * x + y * y);
        double currAngle = Math.acos(x / hypoLen);
        if (y < 0.0) {
            currAngle = 6.283185307179586 - currAngle;
        }
        final double newAngle = currAngle + angle;
        return new Point(Math.cos(newAngle) * hypoLen, Math.sin(newAngle) * hypoLen);
    }
    public static Shape createSquare(double x1, double x2, double y1, double y2, float r, float g, float b, float a)
    {
    	return new Shape(0, x1 + (x2 - x1) / 2, y1 + (y2 - y1) / 2, x2 - x1, y2 - y1, r, g, b, a);
    }
    public static boolean onLeft(Point l1, Point l2, Point point)
    {
    	boolean checkBelow = true;
    	if (l2.getX() > l1.getX())
    	{
    		checkBelow = false;
    	}
    	
    	//slope (y = mx + b)
    	double m = (l2.getY() - l1.getY()) / (l2.getX() - l1.getX());
    	//constant b
    	double b = l1.getY() - l1.getX() * m;
    	//if it should be below, check that y <= mx + b
    	//small error of .00001 to adjust for double rounding
    	if (checkBelow) {return point.getY() <= m * point.getX() + b + .00001;}
    	//if it should be above, check that y >= mx + b
    	//small error of .00001 to adjust for double rounding
    	else {return point.getY() >= m * point.getX() + b - .00001;}
    }
    //Checks for purely horizontal or vertical intersection
    public static boolean vertIntersec(Point p1_1, Point p1_2, Point p2_1, Point p2_2)
    {
    	//line1 is vertical
    	if (p1_1.getX() == p1_2.getX())
    	{
    		double minYL1 = Math.min(p1_1.getY(), p1_2.getY());
			double maxYL1 = Math.max(p1_1.getY(), p1_2.getY());
			double minYL2 = Math.min(p2_1.getY(), p2_2.getY());
			double maxYL2 = Math.max(p2_1.getY(), p2_2.getY());
			//line2 is also vertical
    		if (p2_1.getX() == p2_2.getX())
    		{
    			return ((minYL1 <= minYL2 && maxYL1 >= minYL2) || (minYL2 <= minYL1 && maxYL2 >= minYL1)) && p1_1.getX() == p2_1.getX();
    		}
    		double m2 = (p2_2.getY() - p2_1.getY()) / (p2_2.getX() - p2_1.getX());
    		double b2 = p2_2.getY() - m2 * p2_2.getX();
    		double intersecY = p1_1.getX() * m2 + b2;
    		double intersecX = (p1_1.getY() - b2) / m2;
    		
    		double minXL2 = Math.min(p2_1.getX(), p2_2.getX());
    		double maxXL2 = Math.max(p2_1.getX(), p2_2.getX());
    		
    		boolean yBounded = intersecY >= minYL1 - .00001 && intersecY <= maxYL1 + .00001;
    		boolean xBounded = intersecX >= minXL2 - .00001 && intersecX <= maxXL2 + .00001;
    		
    		return yBounded && xBounded;
    	}
    	//line2 is vertical
    	if (p2_1.getX() == p2_2.getX())
    	{
    		double minYL1 = Math.min(p1_1.getY(), p1_2.getY());
			double maxYL1 = Math.max(p1_1.getY(), p1_2.getY());
			
    		double m1 = (p1_2.getY() - p1_1.getY()) / (p1_2.getX() - p1_1.getX());
    		double b1 = p1_2.getY() - m1 * p1_2.getX();
    		double intersecY = p2_1.getX() * m1 + b1;
    		double intersecX = (p2_1.getY() - b1) / m1;
    		
    		double minXL1 = Math.min(p1_1.getX(), p1_2.getX());
    		double maxXL1 = Math.max(p1_1.getX(), p1_2.getX());
    		
    		boolean yBounded = intersecY >= minYL1 - .00001 && intersecY <= maxYL1 + .00001;
    		boolean xBounded = intersecX >= minXL1 - .00001 && intersecX <= maxXL1 + .00001;
    		
    		return yBounded && xBounded;
    	}
    	return false;
    }
    public static boolean lineIntersection(Point p1_1, Point p1_2, Point p2_1, Point p2_2)
    {
    	if (vertIntersec(p1_1, p1_2, p2_1, p2_2))
    	{
    		return true;
    	}
    	double m1 = (p1_2.getY() - p1_1.getY()) / (p1_2.getX() - p1_1.getX());
    	double m2 = (p2_2.getY() - p2_1.getY()) / (p2_2.getX() - p2_1.getX());
    	double b1 = p1_2.getY() - m1 * p1_2.getX();
    	double b2 = p2_2.getY() - m2 * p2_2.getX();
    	//y = m1x + b1
    	//y = m2x + b2
    	//m1x - m2x + b1 - b2 = 0
    	//x(m1 - m2) = b2 - b1
    	//x = (b2 - b1) / (m1 - m2)
    	double intersecX = (b2 - b1) / (m1 - m2);
    	boolean insideFirst = (p1_1.getX() - .0001 <= intersecX && intersecX <= p1_2.getX() + .0001) 
    			|| (p1_2.getX() - .0001 <= intersecX && intersecX <= p1_1.getX() + .0001);
    	boolean insideSecond = (p2_1.getX() - .0001 <= intersecX && intersecX <= p2_2.getX() + .0001) 
    			|| (p2_2.getX() - .0001 <= intersecX && intersecX <= p2_1.getX() + .0001);
    	return insideFirst && insideSecond;
    }
    public static boolean onLeft(Point l1, Point l2, Point point, boolean checkBelow)
    {
    	//slope (y = mx + b)
    	double m = (l2.getY() - l1.getY()) / (l2.getX() - l1.getX());
    	//constant b
    	double b = l1.getY() + l1.getX() * m;
    	
    	//if it should be below, check that y <= mx + b
    	if (checkBelow) {return point.getY() <= m * point.getX() + b;}
    	//if it should be above, check that y >= mx + b
    	else {return point.getY() >= m * point.getX() + b;}
    }
    public static boolean insideShape(Point[] shape, Point point)
    {
    	
    	double pointX = point.getX();
    	
    	int curr = shape.length - 1;
    	Point currPoint = shape[curr];
    	Point nextPoint = shape[0];
    	double currX = currPoint.getX();
    	double currY = currPoint.getY();
    	double nextX = nextPoint.getX();
    	double nextY = nextPoint.getY();
    	boolean stillInside = false;
    	
    	if (currX == nextX) {
    		if (nextY < currY) {stillInside = pointX >= nextX;}
    		else {stillInside = pointX <= nextX;}
    	} else if (currY == nextY) {
			if (nextX < currX) {stillInside = onLeft(currPoint, nextPoint, point, true);}
			else {stillInside = onLeft(currPoint, nextPoint, point, false);}
		} else {
			stillInside = onLeft(currPoint, nextPoint, point);
		}
    	curr = 0;
    	while (stillInside && curr != shape.length - 1)
    	{
    		currPoint = shape[curr];
        	nextPoint = shape[curr + 1];
        	currX = currPoint.getX();
        	currY = currPoint.getY();
        	nextX = nextPoint.getX();
        	nextY = nextPoint.getY();
        	
        	if (currX == nextX) {
        		if (nextY < currY) {stillInside = pointX >= nextX;}
        		else {stillInside = pointX <= nextX;}

        		
        	} else if (currY == nextY) {
    			if (nextX < currX) {stillInside = onLeft(currPoint, nextPoint, point, true);}
    			else {stillInside = onLeft(currPoint, nextPoint, point, false);}

    			
    		} else {
    			stillInside = onLeft(currPoint, nextPoint, point);

    			
    		}
        	curr++;
    	}
    	return stillInside;
    	
    }
    //Checks collision between concave polygons
    public static boolean colliding(Point[] shape1, Point[] shape2)
    {
    	for (int i = 0; i < shape2.length; i++)
    	{
    		if (insideShape(shape1, shape2[i]))
    		{
    			return true;
    		}
    	}
    	for (int i = 0; i < shape1.length; i++)
    	{
    		if (insideShape(shape2, shape1[i]))
    		{
    			return true;
    		}
    	}
    	return false;
    }
}
