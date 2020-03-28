package LowLevel;

import Imported.Texture;

public class Animation {
	private Texture[] animTex;
	public int length;
	private int switchFrame;
	private int currFrame;
	private int animInd;
	private Image owner;
	public Animation(Texture[] tex, int switchFrame, Image owner)
	{
		this.owner = owner;
		this.switchFrame = switchFrame;
		this.currFrame = 0;
		animInd = 0;
		animTex = tex;
		length = animTex.length;
	}
	public void reset() {currFrame = 0; animInd = 0;}
	public void update()
	{
		if (++currFrame % switchFrame == 0)
		{
			if (++animInd == animTex.length) {animInd = 0;}
		}
		owner.setImage(animTex[animInd]);
	}
}
