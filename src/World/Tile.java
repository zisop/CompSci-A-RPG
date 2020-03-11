package World;

import Imported.Texture;
import LowLevel.Image;

public class Tile extends Image{
	
	private int ID;
	//tiles are squares
	public Tile(int inID, double x, double y, double w)
	{
		super(tileTex[inID], x, y, w, w);
		ID = inID;
	}
	
	
	public static Texture[] tileTex = new Texture[8];
	public static void initTex()
	{
		tileTex[Grass] = new Texture("Tiles/Grass.PNG");
		tileTex[GrassDirtR] = new Texture("Tiles/GrassDirtR.PNG");
		tileTex[GrassDirtL] = new Texture("Tiles/GrassDirtL.PNG");
		tileTex[GrassDirtBL] = new Texture("Tiles/GrassDirtBL.PNG");
		tileTex[GrassDirtBR] = new Texture("Tiles/GrassDirtBR.PNG");
		tileTex[GrassDirtUL] = new Texture("Tiles/GrassDirtUL.PNG");
		tileTex[GrassDirtUR] = new Texture("Tiles/GrassDirtUR.PNG");
		tileTex[Dirt] = new Texture("Tiles/Dirt.PNG");
	}

	public static final int Grass = 0;
	public static final int GrassDirtL = 1;
	public static final int GrassDirtR = 2;
	public static final int GrassDirtBL = 3;
	public static final int GrassDirtBR = 4;
	public static final int GrassDirtUL = 5;
	public static final int GrassDirtUR = 6;
	public static final int Dirt = 7;
}
