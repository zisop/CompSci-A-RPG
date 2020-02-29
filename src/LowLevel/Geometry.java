package LowLevel;



public class Geometry
{
	/**
	 * rotates a point about (0, 0) by angle,
	 * assumes center is exactly (0, 0)
	 * @param point
	 * @param angle
	 * @return rotated point
	 */
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
    /**
     * Creates a rectangle between two diagonal points
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     * @param r
     * @param g
     * @param b
     * @param a
     * @return new rectangle
     */
    public static Shape createRect(double x1, double x2, double y1, double y2, float r, float g, float b, float a)
    {
    	return new Shape(0, x1 + (x2 - x1) / 2, y1 + (y2 - y1) / 2, x2 - x1, y2 - y1, r, g, b, a);
    }
    private static boolean onLeft(Point l1, Point l2, Point point)
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
    private static boolean vertIntersec(Point p1_1, Point p1_2, Point p2_1, Point p2_2)
    {
    	
    	double minYL1 = Math.min(p1_1.getY(), p1_2.getY());
		double maxYL1 = Math.max(p1_1.getY(), p1_2.getY());
		double minYL2 = Math.min(p2_1.getY(), p2_2.getY());
		double maxYL2 = Math.max(p2_1.getY(), p2_2.getY());
    	//line1 is vertical
    	if (p1_1.getX() == p1_2.getX())
    	{
			//line2 is also vertical
    		if (p2_1.getX() == p2_2.getX())
    		{
    			
    			return ((minYL1 <= minYL2 && maxYL1 >= minYL2) || (minYL2 <= minYL1 && maxYL2 >= minYL1)) && p1_1.getX() == p2_1.getX();
    		}
    		double minXL2 = Math.min(p2_1.getX(), p2_2.getX());
    		double maxXL2 = Math.max(p2_1.getX(), p2_2.getX());
    		
    		double m2 = (p2_2.getY() - p2_1.getY()) / (p2_2.getX() - p2_1.getX());
    		double b2 = p2_2.getY() - m2 * p2_2.getX();
    		double intersecY = p1_1.getX() * m2 + b2;
    		double intersecX = p1_1.getX();
    		
    		boolean y1Bounded = intersecY >= minYL1 - .00001 && intersecY <= maxYL1 + .00001;
    		if (y1Bounded)
    		{
    			boolean y2Bounded = intersecY >= minYL2 - .00001 && intersecY <= maxYL2 + .00001;
    			if (y2Bounded)
    			{
    				boolean xBounded = intersecX >= minXL2 - .00001 && intersecX <= maxXL2 + .00001;
    				return xBounded;
    			}
    		}
    		return false;
    	}
    	//line2 is vertical
    	if (p2_1.getX() == p2_2.getX())
    	{	
			double minXL1 = Math.min(p1_1.getX(), p1_2.getX());
    		double maxXL1 = Math.max(p1_1.getX(), p1_2.getX());
			
    		double m1 = (p1_2.getY() - p1_1.getY()) / (p1_2.getX() - p1_1.getX());
    		double b1 = p1_2.getY() - m1 * p1_2.getX();
    		double intersecY = p2_1.getX() * m1 + b1;
    		double intersecX = p2_1.getX();
    		
    		boolean y1Bounded = intersecY >= minYL1 - .00001 && intersecY <= maxYL1 + .00001;
    		if (y1Bounded)
    		{
    			boolean y2Bounded = intersecY >= minYL2 - .00001 && intersecY <= maxYL2 + .00001;
    			if (y2Bounded)
    			{
    				boolean xBounded = intersecX >= minXL1 - .00001 && intersecX <= maxXL1 + .00001;
    				return xBounded;
    			}
    		}
    		return false;
    	}
    	return false;
    }
    /**
     * determines if P1_1 -> P1_2 intersects P2_1 -> P2_2
     * @param p1_1
     * @param p1_2
     * @param p2_1
     * @param p2_2
     * @return intersection == true
     */
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
    	if (insideFirst)
    	{
    		boolean insideSecond = (p2_1.getX() - .0001 <= intersecX && intersecX <= p2_2.getX() + .0001) 
    			|| (p2_2.getX() - .0001 <= intersecX && intersecX <= p2_1.getX() + .0001);
    		return insideSecond;
    	}
    	return false;
    }
    /**
     * determines if two lines intersect
     * @param l1
     * @param l2
     * @return intersection == true
     */
    public static boolean lineIntersection(Line l1, Line l2)
    {
    	return lineIntersection(l1.getP1(), l1.getP2(), l2.getP1(), l2.getP2());
    }
    private static boolean onLeft(Point l1, Point l2, Point point, boolean checkBelow)
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
    /**
     * Determines if a point is inside a shape
     * Assumes shapes have points organized in clockwise order
     * @param shape
     * @param point
     * @return pointInside == true
     */
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
    /**
     * Determines if two shapes overlap
     * Assumes clockwise arrangement of points
     * @param shape1
     * @param shape2
     * @return overlap == true
     */
    public static boolean colliding(Point[] shape1, Point[] shape2)
    {
    	return strictCollision(shape1, shape2) || 
    			insideShape(shape1, shape2[0]) || insideShape(shape2, shape1[0]);
    }
    /**
     * Determines if two lists of points intersect at the lines
     * Assumes that points go clockwise around a shape
     * @param shape1
     * @param shape2
     * @return intersection == true
     */
    public static boolean strictCollision(Point[] shape1, Point[] shape2)
    {
    	Point p1_1;
		Point p1_2;
		Point p2_1;
		Point p2_2;
    	for (int s1I = 0; s1I < shape1.length; s1I++)
    	{
    		if (s1I == 0) {
				p1_1 = shape1[shape1.length - 1];
				p1_2 = shape1[0];
			}
    		else {
    			p1_1 = shape1[s1I - 1];
    			p1_2 = shape1[s1I];
    		}
    		for (int s2I = 0; s2I < shape2.length; s2I++)
    		{
    			
    			if (s2I == 0) {
    				p2_1 = shape2[shape2.length - 1];
    				p2_2 = shape2[0];
    			}
        		else {
        			p2_1 = shape2[s2I - 1];
        			p2_2 = shape2[s2I];
        		}
        		if (lineIntersection(p1_1, p1_2, p2_1, p2_2))
        		{
        			return true;
        		}
    		}
    	}
    	return false;
    }
    /**
     * Determines if two sets of lines intersect
     * @param shape1
     * @param shape2
     * @return intersection == true
     */
    public static boolean strictCollision(Line[] shape1, Line[] shape2)
    {
    	for (int s1I = 0; s1I < shape1.length; s1I++)
    	{
    		for (int s2I = 0; s2I < shape2.length; s2I++)
    		{
    			if (lineIntersection(shape1[s1I], shape2[s2I]))
    			{
    				return true;
    			}
    		}
    	}
    	return false;
    }
    /**
     * determines if a line intersects a list of lines
     * @param shape
     * @param test
     * @return intersection == true
     */
    public static boolean shapeIntersection(Line[] shape, Line test)
    {
    	for (int i = 0; i < shape.length; i++)
    	{
    		if (lineIntersection(shape[i], test))
    		{
    			return true;
    		}
    	}
    	return false;
    }
    /**
     * DONT FUCKING ASK ME HOW THIS WORKS
     * Make sure that your shapes are intersecting each other tangentially :)
     * @param mainShape
     * @param newShape
     * @return Line array with new shape (lines are unordered)
     */
    public static Line[] addNewShape(Line[] mainShape, Line[] newShape)
    {
    	Line[] allLines = new Line[mainShape.length + newShape.length];
    	int count = 0;
    	for (int i = 0; i < mainShape.length; i++)
    	{
    		allLines[count] = mainShape[i];
    		count++;
    	}
    	for (int i = 0; i < newShape.length; i++)
    	{
    		allLines[count] = newShape[i];
    		count++;
    	}
    	Line[] newLines = new Line[allLines.length];
    	count = 0;
    	for (int mInd = 0; mInd < mainShape.length; mInd++)
    	{
    		Line currMain = mainShape[mInd];
    		for (int nInd = 0; nInd < newShape.length; nInd++)
    		{
    			Line currNew = newShape[nInd];
    			if (currMain.overlapping(currNew))
    			{
    				Line[] toAdd = currMain.eraseOverlap(currNew);
    				newLines[count] = toAdd[0];
    				count++;
    				newLines[count] = toAdd[1];
    				count++;
    				currNew.delete();
    				currMain.delete();
    				break;
    			}
    			else if (currMain.adjacent(currNew))
    			{
    				currMain.combine(currNew);
    				newLines[count] = currMain;
    				currNew.delete();
    				count++;
    				break;
    			}
    		}
    		if (!currMain.isDeleted())
    		{
    			newLines[count] = currMain;
    			count++;
    		}
    	}
    	for (int i = 0; i < newShape.length; i++)
    	{
    		if (!newShape[i].isDeleted())
    		{
    			newLines[count] = newShape[i];
    			count++;
    		}
    	}
    	Line[] endLines = new Line[count];
    	for (int i = 0; i < newLines.length; i++)
    	{
    		endLines[i] = newLines[i];
    	}
    	return endLines;
    }
    /**
     * turns points into a list of lines
     * @param points
     * @return 
     */
    public static Line[] createLines(Point[] points)
    {
    	Line[] lines = new Line[points.length];
    	for (int i = 0; i < points.length; i++)
    	{
    		if (i == 0)
    		{
    			lines[i] = new Line(points[points.length - 1], points[0]);
    		}
    		else 
    		{
				lines[i] = new Line(points[i - 1], points[i]);
			}
    	}
    	return lines;
    }
    /**
     * turns lines into a list of points,
     * not yet implemented
     * @param lines
     * @return clockwise ordered points
     */
    public static Point[] createPoints(Line[] lines)
    {
    	
    	return new Point[] {};
    }
}
