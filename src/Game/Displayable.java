package Game;


import Imported.Texture;
import LowLevel.Image;

public class Displayable extends Image
{
    private double charWidth;
    private double charLength;
    
    
    public Displayable(Texture img, double inX, double inY, double w, double l) {
        super(img, inX, inY, w, l);
        charWidth = w;
        charLength = l;
    }
    
    public Displayable(Texture img, double inX, double inY, double w, double l, double charW, double charL) {
        super(img, inX, inY, w, l);
        charWidth = charW;
        charLength = charL;
    }
    
    
    public void rotate(double addAng) {
        setAngle(getAngle() + addAng);
    }
    
    public double getCharWidth() {
        return charWidth;
    }
    
    public double getCharLength() {
        return charLength;
    }
}
