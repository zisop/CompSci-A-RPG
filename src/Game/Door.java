package Game;

import Imported.Texture;

public class Door extends Displayable
{
	
    private int leadRoom;
    /**
     * Entry direction of the door
     * 0 Corresponds to door being to the north of player
     * 1 = to the east
     * 2 = to the south
     * 3 = to the west
     */
    private int entry;
    
    public Door(Texture img, double inX, double inY, double w, double l, int inEntry) {
        super(img, inX, inY, w, l);
        this.entry = inEntry;
    }
    //Sets the door's lead room to an int
    public void setLead(int newLead) {
        leadRoom = newLead;
    }
    
    public void show()
    {
    	System.out.println(Main.player.relPos(this));
    	if (Main.canInteract(this) && (Main.xInteraction(this) || Main.clickInteraction(this)) && Main.player.relPos(this) == entry)
		{
			Main.currRoom = leadRoom;
			Main.interactingChar = this;
			Main.initRoom(leadRoom);
		}
    	super.show();
    	
    }
}