package UI;




import Combat.AOE;
import Combat.CombatChar;
import Combat.Projectile;
import Game.Main;
import Imported.Audio;
import Imported.Texture;
import LowLevel.Geometrical;
import LowLevel.Geometry;
import LowLevel.Image;
import LowLevel.Shape;

public class SpellSlot extends Image{
	private int spellContained;
	private int slotType;
	private boolean colorChanged;
	private Image spellIcon;
	private Geometrical spellDisplay;
	private ToolTip spellTip;
	
	public SpellSlot(Geometrical display, double x, double y, double w, double l, int type)
	{
		super(null, x, y, w, l);
		slotType = type;
		colorChanged = false;
		spellContained = noSpell;
		spellDisplay = display;
		spellDisplay.setPos(getX(), getY());
	}
	public SpellSlot(double x, double y, double w, double l, int type)
	{
		this(createGenericDisplay(w, l), x, y, w, l, type);
	}
	public int getSpellID() {return spellContained;}
	
	public void setSpell(int ID)
	{
		spellContained = ID;
		if (ID == noSpell) {spellIcon = null; return;}
		spellIcon = new Image(spellIcons[ID], getX(), getY(), getWidth(), getLength());
		spellTip = ToolTip.defaultTip(spellTips[ID], this);
	}
	
	public void UIshow()
	{
		spellDisplay.UIshow();
		if (spellIcon != null) {
			spellIcon.UIshow();
			boolean hovering = UI.mouseHovering(this);
			if (hovering) {
				UI.visTips.add(spellTip);
				if (!colorChanged) {
					spellIcon.setRGBA(getRed() * 100f/255, getGreen() * 100f/255, getBlue() * 100f/255, getAlpha() * 255f/255);
					colorChanged = true;
				}
				if (slotType == displaysSpells)
				{
					if (Main.one && !Main.oneLastFrame) {
						Main.player.setSpell(0, spellContained); 
						Audio.playSound("Spells/enchant", 2);
					}
					else if (Main.two && !Main.twoLastFrame) {
						Main.player.setSpell(1, spellContained); 
						Audio.playSound("Spells/enchant", 2);
					}
					else if (Main.three && !Main.threeLastFrame) {
						Main.player.setSpell(2, spellContained); 
						Audio.playSound("Spells/enchant", 2);
					}
					else if (Main.four && !Main.fourLastFrame) {
						Main.player.setSpell(3, spellContained); 
						Audio.playSound("Spells/enchant", 2);
					}
				}
				else if (slotType == acceptsSpells && Main.leftClick && !Main.leftClickLastFrame) {
					setSpell(noSpell);
					Audio.playSound("Spells/magicfail", 4);
				}
			}
			else if (colorChanged) {
				spellIcon.setRGBA(getRed() * 255f/100, getGreen() * 255f/100, getBlue() * 255f/100, getAlpha() * 255f/100);
				colorChanged = false;
			}
		}
	}
	public void setX(double newX)
	{
		double xDiff = newX - getX();
		if (spellIcon != null) {spellIcon.setX(spellIcon.getX() + xDiff);}
		spellDisplay.setX(spellDisplay.getX() + xDiff);
		super.setX(newX);
	}
	public void setY(double newY)
	{
		double yDiff = newY - getY();
		if (spellIcon != null) {spellIcon.setY(spellIcon.getY() + yDiff);}
		spellDisplay.setY(spellDisplay.getY() + yDiff);
		super.setY(newY);
	}
	
	public static Geometrical createGenericDisplay(double width, double length)
	{
		Geometrical display = new Geometrical();
		double offset = 4;
		
		Shape inner = Geometry.createRect(0, width, 0, length, 0x6A, 0x37, 0x39, 0xFF);
		Shape outer = Geometry.createRect(-offset, width + offset, -offset, length + offset, 0x5A, 0x27, 0x29, 0xFF);
		display.addShape(outer);
		display.addShape(inner);
		//Display's mainrect will be the inner square
		display.setMain(1);
		return display;
	}
	public static String[] spellTips = {
			"A basic fire`ball``We think it's`the cause of`climate change",
			"An area of`effect cloud``of magical`essence",
			"Will heal you``  as long as`your brain is`extremely`large",
			"Multiplies yourspell`damage (allowsfor stacking)",
			"A lightning`spell that will blow`enemies away,``literally."
	};
	
	public static final int acceptsSpells = 0;
	public static final int displaysSpells = 1;
	public static final int noSpell = -1;
	
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
