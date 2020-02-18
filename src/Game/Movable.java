package Game;

import Imported.Texture;

public class Movable extends Displayable{
	public Displayable lastCollision;
	public Movable(Texture img, double inX, double inY, double w, double l) {
        super(img, inX, inY, w, l);
    }
    
    public Movable(Texture img, double inX, double inY, double w, double l, double charW, double charL) {
        super(img, inX, inY, w, l, charW, charL);
    }
    public void move(int direc, double dist) {
        if (direc == 0)
        {
            setY(getY() + dist);
            return;
        }
        if (direc == 1)
        {
            setX(getX() + dist);
            return;
        }
        if (direc == 2)
        {
            setY(getY() - dist);
            return;
        }
        if (direc == 3)
        {
            setX(getX() - dist);
        }
    }
    
}
