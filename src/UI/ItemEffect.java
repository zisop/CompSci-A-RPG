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
	public String toString()
	{
		String afterSign = "";
		char sign;
		if (information < 0) {sign = '-';} else {sign = '+';}
		double temp = Math.abs(information);
		switch (ID) {
			case damageMult:
				afterSign = Math.round(temp * 100) + "% damage";
				break;
			case healthAdd:
				afterSign = Math.round(temp) + " health";
				break;
			case manaAdd:
				afterSign = Math.round(temp) + " mana";
				break;
			case healthRegenAdd:
				afterSign = Math.round(temp * 10) + " hpregen";
				break;
			case manaRegenAdd:
				afterSign = Math.round(temp * 10) + " mnregen";
				break;
			case armorAdd:
				afterSign = Math.round(temp) + " armor";
				break;
			case expAdd:
				afterSign = Math.round(temp * 100) + "% exp";
		}
		return sign + afterSign;
	}
	
	public static final int damageMult = 0;
	public static final int healthAdd = 1;
	public static final int manaAdd = 2;
	public static final int manaRegenAdd = 3;
	public static final int healthRegenAdd = 4;
	public static final int armorAdd = 5;
	public static final int expAdd = 6;
	
	
	//total quantity of effect types
	public static final int numEffects = 7;
}
