package LowLevel;

public class Test {
	public static void main(String[] args)
	{

		Point p1 = new Point(0, 0);
		Point p2 = new Point(0, 50);
		Point p3 = new Point(1, 50);
		Point p4 = new Point(1, 0);
		
		System.out.println("Intersection: " + Geometry.vertIntersec(p1, p2, p3, p4));

	}
}
