package World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Combat.AOE;
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
	private ArrayList<Projectile> orbitProj;
	private ArrayList<Projectile> shotProj;
	private ArrayList<Image> toAdd;
	private ArrayList<AOE> uncasted;
	private ArrayList<AOE> casted;
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
		orbitProj = new ArrayList<Projectile>();
		shotProj = new ArrayList<Projectile>();
		uncasted = new ArrayList<AOE>();
		casted = new ArrayList<AOE>();
		toAdd = new ArrayList<Image>();
	}
	public void show()
	{
		toAdd.forEach((img) -> images.add(img));
		toAdd.clear();
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
		
		for (int i = casted.size() - 1; i >= 0; i--)
		{
			AOE curr = casted.get(i);
			curr.show();
			if (curr.isEnded())
			{
				casted.remove(i);
			}
		}
		
		for (int i = shotProj.size() - 1; i >= 0; i--)
		{
			Projectile curr = shotProj.get(i);
			curr.show();
			if (curr.isEnded())
			{
				shotProj.remove(i);
			}
		}
		orbitProj.forEach((orbitProj) -> orbitProj.show());
		orbitProj.clear();
		
		uncasted.forEach((aoe) -> aoe.show());
		uncasted.clear();
		
		
		
		
		
		
		List<Image> temp = Arrays.asList(toArray);
		images = new ArrayList<Image>(temp);
		for (int i = toRemove.size() - 1; i >= 0; i--)
		{
			images.remove(images.indexOf(toRemove.get(i)));
			toRemove.remove(i);
		}
	}
	public void clear()
	{
		casted.clear();
		shotProj.clear();
	}
	public void tempShow(AOE aoe) {uncasted.add(aoe);}
	public void permaShow(AOE aoe) {casted.add(aoe);}
	public void tempShow(Projectile proj) {orbitProj.add(proj);}
	public void permaShow(Projectile proj) {shotProj.add(proj);}
	
	public ArrayList<Image> getImages() {return images;}
	public ArrayList<Projectile> getShotProj() {return shotProj;}
	public Terrain[] getTerrain() {return terrains;}
	public boolean strictCollision(Positionable otherChar) 
	{
		return Geometry.strictCollision(outline, Geometry.createLines(otherChar.getCollisionBasis()));
	}
	public boolean collision(Positionable otherChar)
	{
		for (int i = 0; i < terrains.length; i++)
		{
			if (terrains[i].collision(otherChar))
			{
				return true;
			}
		}
		return false;
	}
	public boolean imageCollision(Positionable otherChar)
	{
		for (int i = 0; i < images.size(); i++)
		{
			Image curr = images.get(i);
			if (curr.collides() && otherChar.collision(curr))
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
	public void addChar(Image character)
	{
		toAdd.add(character);
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
