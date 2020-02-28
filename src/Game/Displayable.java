package Game;


import Imported.Texture;
import LowLevel.Image;

public class Displayable extends Image
{
    
    
    public Displayable(Texture img, double inX, double inY, double w, double l) {
        super(img, inX, inY, w, l);
    }
    
    public Displayable(Texture img, double inX, double inY, double w, double l, double hitW, double hitL) {
        super(img, inX, inY, w, l, hitW, hitL);
    }
    public Displayable(Texture img, double inX, double inY, double w, double l, double hitW, double hitL, double hitboxDown) {
        super(img, inX, inY, w, l, hitW, hitL, hitboxDown);
    }
    
    
    public void rotate(double addAng) {
        setAngle(getAngle() + addAng);
    }
}
