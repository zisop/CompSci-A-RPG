package World;

import java.util.ArrayList;

import LowLevel.Geometry;
import LowLevel.Image;
import LowLevel.Point;
import LowLevel.Positionable;

public class Terrain extends Image {
	private ArrayList<Tile> tiles;
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	private double tileWidth;
	public Terrain(Tile[] inTiles)
	{
		super(null, 0, 0, 1, 1, 1, 1);
		tiles = new ArrayList<Tile>();
		Tile startTile = inTiles[0];
		tileWidth = startTile.getWidth();
		tiles.add(startTile);
		int minXInd = 0;
		int maxXInd = 0;
		int minYInd = 0;
		int maxYInd = 0;
		for (int i = 1; i < inTiles.length; i++)
		{
			Tile currTile = inTiles[i];
			if (currTile.getX() < inTiles[minXInd].getX()) {minXInd = i;}
			if (currTile.getX() > inTiles[maxXInd].getX()) {maxXInd = i;}
			if (currTile.getY() < inTiles[minYInd].getY()) {minYInd = i;}
			if (currTile.getY() > inTiles[maxYInd].getY()) {maxYInd = i;}
			tiles.add(currTile);
		}
		minX = inTiles[minXInd].getX() - tileWidth / 2;
		maxX = inTiles[maxXInd].getX() + tileWidth / 2;
		minY = inTiles[minYInd].getY() - tileWidth / 2;
		maxY = inTiles[maxYInd].getY() + tileWidth / 2;
		super.setX((minX + maxX) / 2);
		super.setY((minY + maxY) / 2);
		setWidth(maxX - minX);
		setLength(maxY - minY);
	}
	public void show()
	{
		for (int i = 0; i < tiles.size(); i++)
		{
			Tile currTile = tiles.get(i);
			currTile.show();
		}
	}
	public void setX(double newX)
	{
		double xDiff = newX - getX();
		for (int i = 0; i < tiles.size(); i++)
		{
			tiles.get(i).setX(tiles.get(i).getX() + xDiff);
		}
		super.setX(newX);
		maxX += xDiff;
		minX += xDiff;
	}
	public void setY(double newY)
	{
		double yDiff = newY - getY();
		for (int i = 0; i < tiles.size(); i++)
		{
			tiles.get(i).setY(tiles.get(i).getY() + yDiff);
		}
		super.setY(newY);
		maxY += yDiff;
		minY += yDiff;
	}
	public void addTile(Tile tile)
	{
		double tileX = tile.getX();
		double tileY = tile.getY();
		if (tileX + tileWidth / 2 > maxX) {setMaxX(tileX + tileWidth / 2);}
		if (tileX - tileWidth / 2 < minX) {setMinX(tileX - tileWidth / 2);}
		if (tileY + tileWidth / 2 > maxY) {setMaxY(tileY + tileWidth / 2);}
		if (tileY - tileWidth / 2 < minY) {setMinY(tileY - tileWidth / 2);}
		tiles.add(tile);
	}
	public void addRow(int ID, int direc)
	{
		double xVal;
		double yVal;
		if (direc == up) {
			setMaxY(maxY + tileWidth);
			
			//get rid of rounding errors
			int numTiles = (int)((getWidth() + .00001) / tileWidth);
			xVal = minX + tileWidth / 2;
			yVal = maxY - tileWidth / 2;
			for (int i = 0; i < numTiles; i++)
			{
				tiles.add(new Tile(ID, xVal, xVal, tileWidth));
				xVal += tileWidth;
			}
		}
		
		else if (direc == right) {
			setMaxX(maxX + tileWidth);
			int numTiles = (int)((getLength() + .00001) / tileWidth);
			System.out.println(numTiles);
			xVal = maxX - tileWidth / 2;
			yVal = minY + tileWidth / 2;
			for (int i = 0; i < numTiles; i++)
			{
				tiles.add(new Tile(ID, xVal, yVal, tileWidth));
				yVal += tileWidth;
			}
		}
		
		else if (direc == down) {
			setMinY(minY - tileWidth);
			int numTiles = (int)((getWidth() + .00001) / tileWidth);
			xVal = minX + tileWidth / 2;
			yVal = minY + tileWidth / 2;
			for (int i = 0; i < numTiles; i++)
			{
				tiles.add(new Tile(ID, xVal, yVal, tileWidth));
				xVal += tileWidth;
			}
		}
		
		else if (direc == left) {
			setMinX(minX - tileWidth);
			int numTiles = (int)((getLength() + .00001) / tileWidth);
			xVal = minX + tileWidth / 2;
			yVal = minY + tileWidth / 2;
			for (int i = 0; i < numTiles; i++)
			{
				tiles.add(new Tile(ID, xVal, yVal, tileWidth));
				yVal += tileWidth;
			}
		}
		
	}
	
	
	private void setMaxX(double newX)
	{
		double xDiff = newX - maxX;
		super.setX(getX() + xDiff / 2);
		maxX = newX;
		super.setWidth(maxX - minX);
	}
	private void setMinX(double newX)
	{
		double xDiff = newX - minX;
		super.setX(getX() + xDiff / 2);
		minX = newX;
		super.setWidth(maxX - minX);
	}
	private void setMaxY(double newY)
	{
		double yDiff = newY - maxY;
		super.setY(getY() + yDiff / 2);
		maxY = newY;
		super.setLength(maxY - minY);
	}
	private void setMinY(double newY)
	{
		double yDiff = newY - minY;
		super.setY(getY() + yDiff / 2);
		minY = newY;
		super.setLength(maxY - minY);
	}
	public double getMinX() {return minX;}
	public double getMaxX() {return maxX;}
	public double getMinY() {return minY;}
	public double getMaxY() {return maxY;}
	
	public static Terrain createTerrain(int ID, double x, double y, int rows, int cols, double tileWidth)
	{
		Tile[] tiles = new Tile[rows * cols];
		double startX = x - tileWidth * cols / 2 + tileWidth / 2;
		double startY = y + tileWidth * rows / 2 - tileWidth / 2;
		double currX = startX;
		double currY = startY;
		for (int r = 0; r < rows; r++)
		{
			for (int c = 0; c < cols; c++)
			{
				
				tiles[c * rows + r] = new Tile(ID, currX, currY, tileWidth);
				currX += tileWidth;
			}
			currX = startX;
			currY -= tileWidth;
		}
		return new Terrain(tiles);
	}
	public boolean insideTerrain(Positionable character, Point movementPoint)
	{
		double originalX = character.getX();
		double originalY = character.getY();
		character.setPos(movementPoint.getX(), movementPoint.getY());
		//If the movementpoint puts the character in a collision with a terrain's boundary,
		//Then the character would be moving outside of the terrain visually
		//So we test no strict collision, and that the point is inside the terrain
		boolean wasInside = Geometry.insideShape(getCollisionBasis(), movementPoint);
		boolean lineIntersec = strictCollision(character);
		character.setPos(originalX, originalY);
		return wasInside && !lineIntersec;
	}
	private static int up = 0;
    private static int right = 1;
    private static int down = 2;
    private static int left = 3;
}
