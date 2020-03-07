package World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Combat.Mob;
import Combat.Projectile;
import Game.Main;
import Imported.MergerSort;
import LowLevel.Geometry;
import LowLevel.Image;
import LowLevel.Line;
import LowLevel.Point;
import LowLevel.Positionable;

public class Room extends Image{
	private Line[] outline;
	private Terrain[] terrains;
	private ArrayList<Image> images;
	private ArrayList<Image> toRemove;
	//Terrain should all be rectangles; if you need to make complex polygons, split them into multiple rectangles
	//Do not try to make diagonals out of squares you fucking idiots
	public Room(ArrayList<Image> inImages, Terrain[] inTerrains)
	{
		super(null, 0, 0, 0, 0);
		terrains = inTerrains;
		images = inImages;
		images.add(Main.player);
		if (terrains.length > 0) {outline = Geometry.createLines(terrains[0].getShowBasis());}
		for (int i = 1; i < terrains.length; i++)
		{
			outline = Geometry.addNewShape(outline, Geometry.createLines(terrains[i].getShowBasis()));
		}
		toRemove = new ArrayList<Image>();
	}
	public void show()
	{
		for (int i = 0; i < terrains.length; i++)
		{
			terrains[i].show();
		}
		Image[] toArray = images.toArray(new Image[images.size()]);
		toArray = MergerSort.mergeSort(toArray);
		for (int i = 0; i < toArray.length; i++)
		{
			toArray[i].show();
		}
		Projectile.showVisProjectiles();
		
		List<Image> temp = Arrays.asList(toArray);
		images = new ArrayList<Image>(temp);
		for (int i = toRemove.size() - 1; i >= 0; i--)
		{
			images.remove(images.indexOf(toRemove.get(i)));
			toRemove.remove(i);
		}
	}
	public ArrayList<Image> getImages()
	{
		return images;
	}
	public Terrain[] getTerrain()
	{
		return terrains;
	}
	public boolean strictCollision(Positionable otherChar)
	{
		return Geometry.strictCollision(outline, Geometry.createLines(otherChar.getCollisionBasis()));
	}
	/**
	 * not implemented you fuck<br>
	 * use strictCollision
	 */
	public boolean collision(Positionable otherChar)
	{
		try {
			throw new Exception("Do not use collision; use strictCollision, for Room.java");
		} catch (Exception e) {
			e.printStackTrace();
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
	public void removeChar(Image character)
	{
		toRemove.add(character);
	}
	public Line[] getOutline()
	{
		return outline;
	}
}
