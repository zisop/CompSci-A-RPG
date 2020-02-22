package LowLevel;

public class Test {
	public static void main(String[] args)
	{

		Point p1 = new Point(-50, -50);
		Point p2 = new Point(50, -50);
		Point p3 = new Point(50, 50);
		Point p4 = new Point(-50, 50);
		Point[] shape = new Point[] {p1, p2, p3, p4};
		Point point = new Point(25, -51);
		boolean inside = Geometry.insideShape(shape, point);
		System.out.println("inside: " + inside);

	}
}
