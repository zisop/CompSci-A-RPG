package UI;

public class ItemEffect {
	
	private int ID;
	private double information;
	public ItemEffect(int ID, double information)
	{
		this.ID = ID;
		this.information = information;
	}
	public double getInfo() {return information;}
	public int getID() {return ID;}
	
	public static final int damageMult = 0;
	public static final int healthAdd = 1;
	public static final int manaAdd = 2;
	public static final int manaRegenAdd = 3;
	public static final int healthRegenAdd = 4;
	public static final int armorAdd = 5;
	
	
	//total quantity of effect types
	public static final int numEffects = 6;
}
