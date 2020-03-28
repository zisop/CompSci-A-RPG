package LowLevel;

import Imported.Texture;

public class CharTex {
	Texture tex;
	double spacing;
	double sizing;
	public CharTex(Texture inTex, double inSpace, double inSize)
	{
		tex = inTex;
		spacing = inSpace;
		sizing = inSize;
	}
	public CharTex(Texture inTex, double inSpace) {this(inTex, inSpace, 1);}
	public CharTex(Texture inTex) {this(inTex, 1, 1);}
	public Texture getTex() {return tex;}
	public double getSpace() {return spacing;}
	public void setSpace(double newSpace) {spacing = newSpace;}
	public double getSize() {return sizing;}
}
