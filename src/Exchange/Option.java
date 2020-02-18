package Exchange;

import Game.Main;
import LowLevel.Shape;
import UI.UI;

public abstract class Option {
	protected boolean visibility;
	protected Shape xButton;
	public Option()
	{
		visibility = false;
	}
	protected abstract void createX();
	protected void updateX()
	{
		if (Main.leftClick && !Main.leftClickLastFrame && UI.mouseHovering(xButton))
		{
			visibility = false;
		}
	}
}
