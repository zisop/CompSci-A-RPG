package World;

import java.util.ArrayList;
import World.Terrain;
import LowLevel.Image;

public class RoomCreation {
	
	public static ArrayList<Terrain> parseRoom(String rawText)
	{
		ArrayList<String> objects = toStrings(rawText);
		Room parsedRoom = null;
		
		ArrayList<Terrain> terrains = new ArrayList<Terrain>();
		ArrayList<Image> chars = new ArrayList<Image>();
		for (int i = 0; i < objects.size(); i++)
		{
			
			//Should look like: "0 <- this is the ID (would mean tile array), {other information}" 
			String currStr = objects.get(i);
			int endInd = currStr.indexOf(" ");
			int typeID = Integer.parseInt(currStr.substring(0, endInd));
			currStr = currStr.substring(endInd + 1);
			switch (typeID) {
				case terr:
					double[] information = new double[5];
					boolean lastVal = false;
					for (int j = 0; j < information.length; j++)
					{
						endInd = currStr.indexOf(" ");
						if (endInd == -1) {endInd = currStr.length(); lastVal = true;}
						double info = Double.parseDouble(currStr.substring(0, endInd));
						if (!lastVal) {currStr = currStr.substring(endInd + 1);}
						information[j] = info;
					}
					Terrain obj = Terrain.createTerrain((int)information[0], information[1], 
					information[2], (int)information[3], (int)information[4]);
					terrains.add(obj);
					break;
				case door:
					
					break;

				default:
					try {throw new Exception("couldn't read ID: " + typeID);}
					catch (Exception e) {e.printStackTrace(); System.exit(0);}
			}
			
		}
		
		return terrains;
	}
	public static ArrayList<String> toStrings(String rawText)
	{
		ArrayList<String> strs = new ArrayList<String>();
		int openInd = 0;
		int closeInd = 0;
		while (true)
		{
			openInd = rawText.indexOf("{");
			closeInd = rawText.indexOf("}");
			strs.add(rawText.substring(openInd + 1, closeInd));
			if (rawText.length() == closeInd + 1)
			{
				break;
			}
			else
			{
				rawText = rawText.substring(closeInd + 1);
			}
		}
		return strs;
	}
	public static void printArray(double[] arr)
	{
		for (int i = 0; i < arr.length - 1; i++)
		{
			System.out.print(arr[i] + ", ");
		}
		System.out.println(arr[arr.length - 1]);
	}
	
	public static void main(String[] args)
	{
		//Terrain goal = Terrain.createTerrain(Tile.Dirt, 0, 0, 5, 5);
		String str = "{0 7 0 0 5 5}";
		ArrayList<Terrain> terrs = parseRoom(str);
		System.out.println(terrs);
	}
	
	public static final int terr = 0;
	public static final int door = 1;
}
