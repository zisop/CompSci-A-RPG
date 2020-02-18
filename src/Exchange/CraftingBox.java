package Exchange;

import LowLevel.Geometrical;
import LowLevel.Point;
import LowLevel.Polygon;
import LowLevel.Positionable;
import LowLevel.Shape;
import UI.Item;
import UI.ItemBag;

public class CraftingBox extends Positionable{
	
	public static double inputX = -200;
	public static double outputX = 200;
	public static double y = -200;
	private ItemBag input;
	private ItemBag output;
	private Geometrical arrow;
	private ExchangeDisplay exchangeDisplay;
	public CraftingBox()
	{
		super((inputX + outputX) / 2, y, 0, 0);
		input = new ItemBag(inputX, y, 70, 70, 3, 3);
		output = new ItemBag(outputX, y, 70, 70, 1, 1);
		output.getSlots()[0].setType(Item.noInteraction);
		double arrowLeftX = inputX + 120;
		double arrowRightX = outputX - 50;
		double middle = (arrowRightX - arrowLeftX) * 2/3 + arrowLeftX;
		Shape square = new Shape(0, (middle - arrowLeftX) / 2 + arrowLeftX, y, middle - arrowLeftX, 50, 150, 150, 150, 255);
		Point p1 = new Point(middle, y + 50);
		Point p2 = new Point(middle, y - 50);
		Point p3 = new Point(arrowRightX, y);
		Point[] trianglePoints = new Point[] {p1, p2, p3};
		Polygon triangle = new Polygon(trianglePoints, 150, 150, 150, 255);
		
		arrow = new Geometrical();
		arrow.addShape(square);
		arrow.addShape(triangle);
		
		//Arbitrary geometrical numbers; if you look at this and wonder, "where'd that constant come from?"
		//it came from me wanting to shift something by like 2 pixels so it looks better ok leave me alone
		arrowLeftX = inputX + 125;
		arrowRightX = outputX - 55;
		middle = (arrowRightX - arrowLeftX) * 2/3 + arrowLeftX;
		square = new Shape(0, (middle - arrowLeftX) / 2 + arrowLeftX + 2.5, y, middle - arrowLeftX + 5, 40, 155, 126, 75, 255);
		p1 = new Point(middle + 5, y + 42.5);
		p2 = new Point(middle + 5, y - 42.5);
		p3 = new Point(arrowRightX - 5, y);
		trianglePoints = new Point[] {p1, p2, p3};
		triangle = new Polygon(trianglePoints, 155, 126, 75, 255);
		arrow.addShape(triangle);
		arrow.addShape(square);

		
	}
	public void setPos(double newX, double newY)
	{
		double xDiff = getX() - newX;
		double yDiff = getY() - newY;
		setPos(newX, newY);
		input.setPos(input.getX() + xDiff, input.getY() + yDiff);
		output.setPos(output.getX() + xDiff, output.getY() + yDiff);
	}
	public void setX(double newX)
	{
		double xDiff = getX() - newX;
		setX(newX);
		input.setX(input.getX() + xDiff);
		output.setX(output.getX() + xDiff);
	}
	public void setY(double newY)
	{
		double yDiff = getY() - newY;
		setY(newY);
		input.setY(input.getY() + yDiff);
		output.setY(output.getY() + yDiff);
	}
	public ItemBag getOutput()
	{
		return output;
	}
	public ItemBag getInput()
	{
		return input;
	}
	public void UIshow()
	{
		input.UIshow();
		output.UIshow();
		arrow.UIshow();
		if (exchangeDisplay != null) {exchangeDisplay.UIshow();}
	}
	public void setDisplay(ExchangeDisplay display)
	{
		exchangeDisplay = display;
	}
}
