package World;

import Game.Main;
import Game.Projectile;
import Imported.MergerSort;
import LowLevel.Image;

public class Room extends Image{
	private Terrain[] terrains;
	private Image[] images;
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
}
