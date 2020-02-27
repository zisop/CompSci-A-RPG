package World;

import Game.Main;
import Game.Projectile;
import Imported.MergerSort;
import LowLevel.Image;
import LowLevel.Point;
import LowLevel.Positionable;

public class Room extends Image{
	private Terrain[] terrains;
	private Image[] images;
	//Terrain should all be rectangles; if you need to make complex polygons, split them into multiple rectangles
	//Do not try to make diagonals out of squares you fucking idiots
	public Room(Image[] inImages, Terrain[] inTerrains)
	{
		super(null, 0, 0, 0, 0);
		terrains = inTerrains;
		images = new Image[inImages.length + 1];
		for (int i = 0; i < inImages.length; i++)
		{
			images[i] = inImages[i]; 
		}
		images[inImages.length] = Main.player;
	}
	public void show()
	{
		for (int i = 0; i < terrains.length; i++)
		{
			terrains[i].show();
		}
		images = MergerSort.mergeSort(images);
		for (int i = 0; i < images.length; i++)
		{
			images[i].show();
		}
		Projectile.showVisProjectiles();
	}
	public Image[] getImages()
	{
		return images;
	}
	public Terrain[] getTerrain()
	{
		return terrains;
	}
	public boolean insideRoom(Positionable character, Point movementPoint)
	{
		for (int i = 0; i < terrains.length; i++)
		{
			if (terrains[i].insideTerrain(character, movementPoint))
			{
				return true;
			}
		}
		return false;
	}
}
