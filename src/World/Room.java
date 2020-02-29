package World;

import Game.Main;
import Game.Projectile;
import Imported.MergerSort;
import LowLevel.Geometry;
import LowLevel.Image;
import LowLevel.Line;
import LowLevel.Point;
import LowLevel.Positionable;
import Mobs.Mob;

public class Room extends Image{
	private Line[] outline;
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
		if (terrains.length > 0) {outline = Geometry.createLines(terrains[0].getShowBasis());}
		for (int i = 1; i < terrains.length; i++)
		{
			outline = Geometry.addNewShape(outline, Geometry.createLines(terrains[i].getShowBasis()));
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
	public boolean insideRoom(Point movementPoint)
	{
		for (int i = 0; i < terrains.length; i++)
		{
			if (terrains[i].insideTerrain(movementPoint))
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * Determines if walking a path will keep the mob inside the room<br>
	 * Does not test for collisions between other objects
	 * @param mob
	 * @param movementPoint
	 * @return path can be walked
	 */
	public boolean pathPossible(Mob mob, Point movementPoint)
	{
		double orX = mob.getX();
		double orY = mob.getY();
		Point p1;
		if (mob.startingHorizontal())
		{
			p1 = new Point(movementPoint.getX(), mob.getY());
		}
		else 
		{
			p1 = new Point(mob.getX(), movementPoint.getY());
		}
		
		if (!Geometry.shapeIntersection(outline, new Line(movementPoint, p1)) && !Geometry.shapeIntersection(outline, new Line(p1, mob)))
		{
			boolean collided = false;
			mob.setPos(movementPoint.getX(), movementPoint.getY());
			collided = Geometry.strictCollision(outline, Geometry.createLines(mob.getCollisionBasis()));
			mob.setPos(p1.getX(), p1.getY());
			collided = collided || Geometry.strictCollision(outline, Geometry.createLines(mob.getCollisionBasis()));
			mob.setPos(orX, orY);
			return !collided;
		}
		return false;
	}
}
