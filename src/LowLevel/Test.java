package LowLevel;

public class Test {
	public static void main(String[] args)
	{

		Point p1 = new Point(0, 0);
		Point p2 = new Point(1, 0);
		Point p3 = new Point(1, 1);
		Point p4 = new Point(0, 1);
		Point[] square1 = new Point[] {p1, p2, p3, p4};
		
		Point p5 = new Point(.25, .25);
		Point p6 = new Point(.75, .25);
		Point p7 = new Point(.75, .75);
		Point p8 = new Point(.25, .75);
		Point[] square2 = new Point[] {p5, p6, p7, p8};
		boolean inside = Geometry.colliding(square2, square1);
		System.out.println("inside: " + inside);

	}
}
