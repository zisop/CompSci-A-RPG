package World;

import Imported.Texture;
import LowLevel.Image;

public class TileCreation {
	public static Texture[] tileTex = new Texture[7];
	
	public static void initTex()
	{
		tileTex[Grass] = new Texture("Tiles/Grass.PNG");
		tileTex[DirtGrass] = new Texture("Tiles/DirtGrass.PNG");
		tileTex[GrassDirt] = new Texture("Tiles/GrassDirt.PNG");
		tileTex[GrassDirtBL] = new Texture("Tiles/GrassDirtBL.PNG");
		tileTex[GrassDirtBR] = new Texture("Tiles/GrassDirtBR.PNG");
		tileTex[GrassDirtUL] = new Texture("Tiles/GrassDirtUL.PNG");
		tileTex[GrassDirtUR] = new Texture("Tiles/GrassDirtUR.PNG");
	}
	public static Image[] createGrid(int tile, int rows, int cols)
	{
		double tileWidth = 50;
		double tileLength = 50;
		Image[] grid = new Image[rows * cols];
		for (int r = 0; r < rows; r++)
		{
			for (int c = 0; c < cols; c++)
			{
				grid[r * cols + c] = new Image(tileTex[tile], tileWidth * c, tileLength * r, tileWidth, tileLength);
			}
		}
		return grid;
	}
	public static void showTiles(Image[] tiles)
	{
		for (Image tile : tiles)
		{
			tile.show();
		}
	}
	public static int Grass = 0;
	public static int DirtGrass = 1;
	public static int GrassDirt = 2;
	public static int GrassDirtBL = 3;
	public static int GrassDirtBR = 4;
	public static int GrassDirtUL = 5;
	public static int GrassDirtUR = 6;
}
