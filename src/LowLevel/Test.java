package LowLevel;

public class Test {
	public static void main(String[] args)
	{

		
		Point p1 = new Point(0, 0);
		Point p2 = new Point(1, 0);
		Point p3 = new Point(1, 3);
		Point p4 = new Point(0, 3);
		
		Point p5 = new Point(1, 0);
		Point p6 = new Point(2, 0);
		Point p7 = new Point(2, 2);
		Point p8 = new Point(1, 2);
		
		Point p9 = new Point(2, 0);
		Point p10 = new Point(3, 0);
		Point p11 = new Point(3, 3);
		Point p12 = new Point(2, 3);
		
		Line[] shape1 = Geometry.createLines(new Point[] {p1, p2, p3, p4});
		Line[] shape2 = Geometry.createLines(new Point[] {p5, p6, p7, p8});
		Line[] shape3 = Geometry.createLines(new Point[] {p9, p10, p11, p12});
		Line[] endShape = Geometry.addNewShape(shape1, shape2);
		endShape = Geometry.addNewShape(endShape, shape3);
		for (int i = 0; i < endShape.length; i++)
		{
			System.out.println("Line " + i + ": " + endShape[i].toString());
		}
	
		//Line l1 = new Line(new Point(0, 2), new Point(0, 0));
		//Line l2 = new Line(new Point(1, 4), new Point(1, 2));
		//System.out.println(l1.adjacent(l2));
	}
}
