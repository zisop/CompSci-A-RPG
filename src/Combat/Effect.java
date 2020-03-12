package Combat;

public class Effect {
	private int ID;
	private double[] effect;
	private int liveFrames;
	
	public Effect(int inID, double[] information, CombatChar owner)
	{
		ID = inID;
		effect = information;
		
		int desiredLength = 0;
		switch (ID) {
		case frost:
			desiredLength = 2;
			liveFrames = (int)information[frostDuration];
			break;
		case damage:
			effect[hitDamage] *= owner.getDamageMultiplier();
			desiredLength = 4;
			break;
		
		case heal:
			liveFrames = (int)information[healDuration];
			desiredLength = 3;
			break;
		case poison:
			desiredLength = 3;
			liveFrames = (int)information[poisonDuration];
			effect[poisonTick] *= owner.getDamageMultiplier();
			break;
		case powerUp:
			desiredLength = 2;
			liveFrames = (int)information[powerDuration];
			break;
		}
		if (information.length != desiredLength)
		{
			try {
				throw new Exception("Information inputted for spell " + ID + " was not valid");
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
	
	public int getType() {return ID;}
	public double[] getInformation() {return effect;}
	public int getFrame() {return liveFrames;}
	public void updateFrame() {liveFrames--;}
	public boolean isEnded() {return liveFrames == 0;}
	
	//Frost is the effect type
	public static final int frost = 0;
	public static final int speedMultipier = 0;
	public static final int frostDuration = 1;
	
	//Damage is the effect type
	public static final int damage = 1;
	public static final int hitDamage = 0;
	public static final int invulnFrames = 1;
	public static final int stunFrames = 2;
	public static final int fromAngle = 3;
	
	//Poison and heal are the effect types
	public static final int poison = 2;
	public static final int heal = 3;
	public static final int healTick = 0;
	public static final int poisonTick = 0;
	public static final int tickFrame = 1;
	public static final int poisonDuration = 2;
	public static final int healDuration = 2;
	
	public static final int powerUp = 4;
	public static final int powerMultiplier = 0;
	public static final int powerDuration = 1;
	
	
	
}
