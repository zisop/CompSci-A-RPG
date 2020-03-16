package UI;


import org.lwjgl.system.CallbackI.V;

import Combat.AOE;
import Combat.CombatChar;
import Combat.Projectile;
import Imported.Texture;
import LowLevel.Geometrical;
import LowLevel.Image;

public class SpellSlot extends Image{
	private int spellContained;
	private int slotType;
	private Image spellIcon;
	
	public SpellSlot(Geometrical display, double x, double y, double w, double l, int type)
	{
		super(null, x, y, w, l);
		slotType = type;
	}
	
	public void setSpell(int ID)
	{
		spellContained = ID;
		spellIcon = new Image(spellIcons[ID], getX(), getY(), getWidth(), getLength());
	}
	
	public void show()
	{
		if (spellIcon != null) {spellIcon.UIshow();}
	}
	
	public static final int acceptsSpells = 0;
	public static final int displaysSpells = 1;
	
	public static Texture[] spellIcons;
	public static void init()
	{
		spellIcons = new Texture[5];
		spellIcons[Projectile.fireball] = new Texture("SpellIcons/fireball.png");
		spellIcons[AOE.damageCloud] = new Texture("SpellIcons/eruption.png");
		spellIcons[AOE.lightning] = new Texture("SpellIcons/lightning.png");
		spellIcons[CombatChar.heal] = new Texture("SpellIcons/heal.png");
		spellIcons[CombatChar.powerUp] = new Texture("SpellIcons/powerUp.png");
	}
}
