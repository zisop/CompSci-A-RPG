package LowLevel;

import Imported.Texture;

public class Animation {
	private Texture[] animTex;
	public int length;
	private int switchFrame;
	private int currFrame;
	private int animInd;
	private boolean shouldRepeat;
	private Image owner;
	public Animation(Texture[] tex, int switchFrame, Image owner)
	{
		this.owner = owner;
		this.switchFrame = switchFrame;
		this.currFrame = 0;
		animInd = 0;
		animTex = tex;
		length = animTex.length;
		shouldRepeat = false;
	}
	public void setSwitch(int switchFrame) {this.switchFrame = switchFrame;}
	public void reset() {currFrame = 0; animInd = 0;}
	public void setRepeat(boolean flag) {shouldRepeat = flag;}
	public void update()
	{
		if (++currFrame % switchFrame == 0)
		{
			if (++animInd == length) {
				if (shouldRepeat) {animInd--;}
				else {animInd = 0;}
			}
		}
		owner.setImage(animTex[animInd]);
	}
}
