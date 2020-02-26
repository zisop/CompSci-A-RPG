package World;

import java.util.ArrayList;

import LowLevel.Image;

public class Terrain extends Image {
	private ArrayList<Tile> tiles;
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	public Terrain(Tile[] inTiles)
	{
		super(null, 0, 0, 0, 0);
		tiles = new ArrayList<Tile>();
		tiles.add(inTiles[0]);
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
		minX = inTiles[minXInd].getX();
		maxX = inTiles[maxXInd].getX();
		minY = inTiles[minYInd].getY();
		maxY = inTiles[maxYInd].getY();
		setX((minX + maxX) / 2);
		setY((minY + maxY) / 2);
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
		if (tile.getX() > maxX) {setMaxX(tileX);}
		if (tile.getX() < minX) {setMinX(tileX);}
		if (tile.getY() > maxY) {setMaxY(tileY);}
		if (tile.getY() < minY) {setMinY(tileY);}
		tiles.add(tile);
	}
	public void setMaxX(double newX)
	{
		double xDiff = newX - maxX;
		setX(getX() + xDiff / 2);
		maxX = newX;
		setWidth(maxX - minX);
	}
	public void setMinX(double newX)
	{
		double xDiff = newX - minX;
		setX(getX() + xDiff / 2);
		minX = newX;
		setWidth(maxX - minX);
	}
	public void setMaxY(double newY)
	{
		double yDiff = newY - maxY;
		setY(getY() + yDiff / 2);
		maxY = newY;
		setLength(maxY - minY);
	}
	public void setMinY(double newY)
	{
		double yDiff = newY - minY;
		setY(getY() + yDiff / 2);
		minY = newY;
		setLength(maxY - minY);
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
}
