package UI;

import LowLevel.Geometrical;
import LowLevel.Image;

public class SpellBag extends Image{
	public SpellBag(Geometrical display, SpellSlot[] slots)
	{
		super(null, display.getX(), display.getY(), display.getWidth(), display.getLength());
	}
}
