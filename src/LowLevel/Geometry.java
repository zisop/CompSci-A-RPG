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
