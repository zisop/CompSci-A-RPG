package Game;


import Combat.Movable;
import Imported.Audio;
import Imported.Texture;
import LowLevel.Image;
import LowLevel.Point;
public class Door extends Image
{
    private int leadRoom;
    /**
     * Entry direction of the door
     * 0 Corresponds to player being to the north of door
     * 1 = to the east
     * 2 = to the south
     * 3 = to the west
     */
    private int entry;
    private int ID;
    private Point exitPoint;
    private String openSound;
    private Texture[] selfAnims;
    private boolean opening;
    private int openFrame;
    private int maxOpenFrame;
    private int animInd;
    private double volume;
    
    public Door(int inID, double inX, double inY, int inEntry) {
        super(null, inX, inY, 0, 0);
        entry = inEntry;
        ID = inID;
        openFrame = 0;
        animInd = 0;
        opening = false;
        setCollisionStatus(true);
        double wid = 75;
        double hitWid = 0;
        double len = 130;
        double hitLen = 0;
        setWidth(wid);
        setLength(len);
        switch (ID) {
			case woodWindowless:
				switch (inEntry) {
					case Movable.down:
					case Movable.up:
						selfAnims = getAnims(wood0_SideAnimInd, wood0_SideAnimInd + 3);
						hitLen = len * 2 / 7;
						hitWid = wid * 3.5 / 5;
						setHitWidth(hitWid);
				        setHitLength(hitLen);
						hitBoxDown(30);
						hitBoxRight(13);
						
						break;
					case Movable.right:
					case Movable.left:
						selfAnims = getAnims(wood0_UpAnimInd, wood0_UpAnimInd + 3);
						hitWid = wid * 1.8 / 7;
						hitLen = len * 3 / 5;
						setHitWidth(hitWid);
				        setHitLength(hitLen);
						hitBoxDown(25);
						break;
				}
				volume = .3;
				openSound = sounds[woodSoundInd];
				break;
		}
        
        setImage(selfAnims[0]);
        maxOpenFrame = animSwitch * (selfAnims.length);
        if (openSound == null || selfAnims == null)
        {
        	try {throw new Exception("Door ID: " + ID + " entryWay: " + entry + " didn't exist");} 
			catch (Exception e) {e.printStackTrace(); System.exit(0);}
        }
    }
    public void setExit(double x, double y)
    {
    	exitPoint = new Point(x, y);
    }
    //Sets the door's lead room to an int
    public void setLead(int newLead) {
        leadRoom = newLead;
    }
    
    public void show()
    {
    	if (Main.canInteract(this) && (Main.xInteraction(this) || Main.clickInteraction(this)) && relPos(Main.player) == entry)
		{
			Main.interactingChar = this;
			Main.alreadyInteracting = true;
			Main.exitPoint = exitPoint;
			opening = true;
			Audio.playSound(openSound, volume);
		}
    	super.show();
    	
    	if (opening)
    	{
    		if (openFrame % animSwitch == 0)
    		{
    			setImage(selfAnims[animInd]);
    			animInd++;
    		}
    		openFrame++;
    		if (openFrame == maxOpenFrame)
    		{
    			Main.toInit.add(leadRoom);
    			Main.interactingChar = null;
    			Main.alreadyInteracting = false;
    			opening = false;
    			openFrame = 0;
    			setImage(selfAnims[animInd = 0]);
    		}
    	}
    }
    private static Texture[] getAnims(int startInd, int endInd)
    {
    	Texture[] anims = new Texture[endInd - startInd + 1];
    	for (int i = 0; i < anims.length; i++)
    	{
    		anims[i] = Door.anims[startInd + i];
    	}
    	return anims;
    }
    
    private static int animSwitch = 4;
    
    public static Texture[] anims;
    public static String[] sounds;
    private static final int wood0_UpAnimInd = 0;
    private static final int wood0_SideAnimInd = 4;
    private static final int woodSoundInd = 0;
    public static void initTex()
    {
    	anims = new Texture[8];
    	anims[wood0_UpAnimInd + 0] = new Texture("Door/wood0/anim0.png");
    	anims[wood0_UpAnimInd + 1] = new Texture("Door/wood0/anim1.png");
    	anims[wood0_UpAnimInd + 2] = new Texture("Door/wood0/anim2.png");
    	anims[wood0_UpAnimInd + 3] = new Texture("Door/wood0/anim3.png");
    	anims[wood0_SideAnimInd + 0] = anims[wood0_UpAnimInd + 3];
    	anims[wood0_SideAnimInd + 1] = anims[wood0_UpAnimInd + 2];
    	anims[wood0_SideAnimInd + 2] = anims[wood0_UpAnimInd + 1];
    	anims[wood0_SideAnimInd + 3] = anims[wood0_UpAnimInd + 0];
    	
    	sounds = new String[1];
    	sounds[woodSoundInd] = "Door/door";
    }
    
    public static final int woodWindowless = 0;
}