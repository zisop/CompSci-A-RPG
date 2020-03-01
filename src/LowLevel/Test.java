package LowLevel;

public class Test {
	public static void main(String[] args)
	{

		
		Point p1 = new Point(0, 0);
		Point p2 = new Point(3, 0);
		Point p3 = new Point(3, 3);
		Point p4 = new Point(0, 3);
		Line[] shape1 = Geometry.createLines(new Point[] {p1, p2, p3, p4});
		Point p5 = new Point(1, 3);
		Point p6 = new Point(2, 3);
		Point p7 = new Point(2, 5);
		Point p8 = new Point(1, 5);
		//Line[] shape2 = Geometry.createLines(new Point[] {p5, p6, p7, p8});
		
		Line line = new Line(new Point(-1, 2), new Point(2, 2));
		System.out.println(Geometry.shapeIntersection(shape1, line));
	
		//Line l1 = new Line(new Point(0, 2), new Point(0, 0));
		//Line l2 = new Line(new Point(1, 4), new Point(1, 2));
		//System.out.println(l1.adjacent(l2));
	}
}
