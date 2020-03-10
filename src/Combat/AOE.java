package Combat;

import java.util.ArrayList;

import Game.Main;
import Imported.Audio;
import Imported.Texture;
import LowLevel.Image;
import LowLevel.Positionable;
import World.Room;

public class AOE extends Image{
	private CombatChar owner;
	private double damage;
	private boolean placed;
	private int animNum;
	private int animSwitch;
	private int existenceFrames;
	private int maxFrames;
	private int stunFrames;
	private int invulnFrames;
	private int ID;
	private Texture[] anims;
	private Texture[] startAnims;
	private Texture[] activeAnims;
	private String[] sounds;
	public AOE(int inID, CombatChar inOwner)
	{
		super(null, 0, 0, 0, 0);
		animNum = -1;
		ID = inID;
		switch (ID) {
			case poison:
				setWidth(150);
				setLength(150);
				invulnFrames = 32;
				stunFrames = 16;
				damage = 5;
				sounds = getSounds(poisonSoundInd, poisonSoundInd + 0);
				startAnims = getAnims(poisonStartInd, poisonStartInd + 14);
				activeAnims = getAnims(poisonStartInd, poisonStartInd + 27);
				anims = startAnims;
				animSwitch = 2;
				maxFrames = animSwitch * (activeAnims.length * 5 - 1);
				existenceFrames = 0;
				break;
		}
		owner = inOwner;
	}
	public void show()
	{
		if (!placed)
		{
			if (existenceFrames % animSwitch == 0) {
				animNum++;
				if (animNum == anims.length) {animNum = 0;}
				setImage(anims[animNum]);
			}
			existenceFrames++;
			super.show();
		}
		else 
		{
			if (!Main.alreadyInteracting) {
				damageAffected();
				if (existenceFrames % animSwitch == 0) {
					animNum++;
					if (animNum == anims.length) {animNum = 0;}
					setImage(anims[animNum]);
				}
				existenceFrames++;
			}
			super.show();
		}
		
	}
	public void place()
	{
		placed = true;
		animNum = 0;
		anims = activeAnims;
		existenceFrames = 0;
		Main.allRooms[Main.currRoom].permaShow(this);
		int which = (int)(Math.random() * sounds.length);
		Audio.playSound(sounds[which], .2);
	}
	public boolean isEnded()
	{
		return existenceFrames == maxFrames;
	}
	private void damageAffected()
	{
		Room currRoom = Main.allRooms[Main.currRoom];
		ArrayList<Image> chars = currRoom.getImages();
		for (int i = 0; i < chars.size(); i++)
		{
			Image curr = chars.get(i);
			if (owner.isEnemy(curr))
			{
				CombatChar toAffect = (CombatChar)(curr);
				if (toAffect.canBeAttacked() && collision(toAffect))
				{
					toAffect.receiveHit(damage, toAffect.angleTo(this), stunFrames, invulnFrames);
				}
			}
		}
	}
	public int getID() {return ID;}
	
	public static Texture[] getAnims(int start, int end)
	{
		Texture[] anims = new Texture[end - start + 1];
		for (int i = 0; i < anims.length; i++)
		{
			anims[i] = allAnims[i + start];
		}
		return anims;
	}
	public static String[] getSounds(int start, int end)
	{
		String[] sounds = new String[end - start + 1];
		for (int i = 0; i < sounds.length; i++)
		{
			sounds[i] = allSounds[i + start];
		}
		return sounds;
	}
	public static Texture[] allAnims;
	public static String[] allSounds;
	public static void init()
	{
		allAnims = new Texture[28];
		allAnims[poisonStartInd + 0] = new Texture("Projectiles/Poison/startUp0.png");
		allAnims[poisonStartInd + 1] = new Texture("Projectiles/Poison/startUp1.png");
		allAnims[poisonStartInd + 2] = new Texture("Projectiles/Poison/startUp2.png");
		allAnims[poisonStartInd + 3] = new Texture("Projectiles/Poison/startUp3.png");
		allAnims[poisonStartInd + 4] = new Texture("Projectiles/Poison/startUp4.png");
		allAnims[poisonStartInd + 5] = new Texture("Projectiles/Poison/startUp5.png");
		allAnims[poisonStartInd + 6] = new Texture("Projectiles/Poison/startUp6.png");
		allAnims[poisonStartInd + 7] = new Texture("Projectiles/Poison/startUp7.png");
		allAnims[poisonStartInd + 8] = new Texture("Projectiles/Poison/startUp8.png");
		allAnims[poisonStartInd + 9] = new Texture("Projectiles/Poison/startUp9.png");
		allAnims[poisonStartInd + 10] = new Texture("Projectiles/Poison/startUp10.png");
		allAnims[poisonStartInd + 11] = new Texture("Projectiles/Poison/startUp11.png");
		allAnims[poisonStartInd + 12] = new Texture("Projectiles/Poison/startUp12.png");
		allAnims[poisonStartInd + 13] = new Texture("Projectiles/Poison/startUp13.png");
		allAnims[poisonStartInd + 14] = new Texture("Projectiles/Poison/startUp14.png");
		allAnims[poisonActiveInd + 0] = new Texture("Projectiles/Poison/active0.png");
		allAnims[poisonActiveInd + 1] = new Texture("Projectiles/Poison/active1.png");
		allAnims[poisonActiveInd + 2] = new Texture("Projectiles/Poison/active2.png");
		allAnims[poisonActiveInd + 3] = new Texture("Projectiles/Poison/active3.png");
		allAnims[poisonActiveInd + 4] = new Texture("Projectiles/Poison/active4.png");
		allAnims[poisonActiveInd + 5] = new Texture("Projectiles/Poison/active5.png");
		allAnims[poisonActiveInd + 6] = new Texture("Projectiles/Poison/active6.png");
		allAnims[poisonActiveInd + 7] = new Texture("Projectiles/Poison/active7.png");
		allAnims[poisonActiveInd + 8] = new Texture("Projectiles/Poison/active8.png");
		allAnims[poisonActiveInd + 9] = new Texture("Projectiles/Poison/active9.png");
		allAnims[poisonActiveInd + 10] = new Texture("Projectiles/Poison/active10.png");
		allAnims[poisonActiveInd + 11] = new Texture("Projectiles/Poison/active11.png");
		allAnims[poisonActiveInd + 12] = new Texture("Projectiles/Poison/active12.png");
		
		allSounds = new String[1];
		allSounds[poisonSoundInd + 0] = "Batt/energy";
	}
	private static int poisonSoundInd = 0;
	private static int poisonStartInd = 0;
	private static int poisonActiveInd = poisonStartInd + 15;

	public static final int poison = 1;
}
