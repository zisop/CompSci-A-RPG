package Game;


import Imported.Texture;
import LowLevel.Geometrical;
import LowLevel.Shape;
import UI.TextDisplay;
import sun.java2d.opengl.OGLContext;

public class NPC extends Movable{
	//Put all raw dialogue for all NPCs in rawDialogue String[][][]
	//Each NPC has one String[][] of dialogue
	//Each String[][] of dialogue has a String[] for each questState
	public static String[][][] rawDialogue = 
	{
		//NPC0
		{
			//QuestInd = 0
			{",;!()@&#%$0123456789abcdefghijklmnopqrstuvwxyz:?/", "yourmom", "isgaylol"}
		},
		//NPC1
		{
			//QuestInd = 0
			{"all men are pigs", "rise up gamers"}
		}
	};
	public static char[][][][] allDialogue;
	public static int textFrame = 0;
	//All NPC dialogue is displayed in the textbox
	public static Geometrical textBox;
	
	
	public static int cowboy = 0;
	
	
	private char[][][] dialogue;
	private double fontSize;
	private int currText;
	private int frameNum;
	private Texture[] anims;
	public NPC(int ID, double inX, double inY, double w, double l, int inDia, double font) {
        super(null, inX, inY, w, l);
        dialogue = allDialogue[inDia];
        currText = notYetSpeaking;
        fontSize = font;
        setProjInteraction(false);
        frameNum = 0;
        if (ID == cowboy)
        {
        	anims = getAnims(cowboyStart, cowboyEnd);
        }
        setAnim(0);
    }
    
    public NPC(int ID, double inX, double inY, double w, double l, double hitW, double hitL, int inDia, double font) {
        super(null, inX, inY, w, l, hitW, hitL);
        dialogue = allDialogue[inDia];
        currText = notYetSpeaking;
        fontSize = font;
        setProjInteraction(false);
        frameNum = 0;
        if (ID == cowboy)
        {
        	anims = getAnims(cowboyStart, cowboyEnd);
        }
        setAnim(0);
    }
    
    public void setAnim(int ID)
    {
    	setImage(anims[ID]);
    }
    
    public int getCurr()
    {
    	return currText;
    }
    public void updateTextState()
    {
    	boolean skipped = false;
    	if (currText != dialogue[Main.questState].length - 1)
    	{
    		if (currText != notYetSpeaking && frameNum < dialogue[Main.questState][currText].length)
    		{
    			frameNum = dialogue[Main.questState][currText].length;
    			skipped = true;
    		}
    		else
    		{
    			currText += 1;
			}
    		Main.alreadyInteracting = true;
    	}
    	else 
    	{
    		if (Main.alreadyInteracting && frameNum < dialogue[Main.questState][currText].length)
    		{
    			frameNum = dialogue[Main.questState][currText].length;
    			skipped = true;
    		}
    		else
    		{
    			Main.alreadyInteracting = false;
    			currText = notYetSpeaking;
    		}
		}
    	if (!skipped)
    	{
    		frameNum = 0;
    	}
    }

    //Displays text from an NPC's dialogue given a font size
    
    public void showText()
    {
    	TextDisplay.showText(textBox, dialogue[Main.questState][currText], fontSize, frameNum);
    	//we never overflow bois (doesnt matter unless this hits 2 million LOL)
    	if (frameNum < dialogue[Main.questState][currText].length)
    	{
    		frameNum++;
    	}
    }
    
    public void show()
    {
    	//updatetextstate will show text or take away the text depending on npc information
    	//System.out.println(Main.interact);
    	if (shouldInteract())
    	{
    		updateTextState();
   			if (Main.alreadyInteracting)
   			{
   				Main.interactingChar = this;
   			}
   			else
   			{
   				Main.interactingChar = null;
			}
   			Main.xEvent = true;
    	}
    	super.show();
    }
    public boolean shouldInteract()
    {
    	
    	return Main.xInteraction(this) || Main.clickInteraction(this);
    }
    
    
    public static Texture[] NPCtex = new Texture[2];
    
    public static int cowboyStart = 0;
    public static int cowboyEnd = 1;
    public static void initTex()
    {
    	NPCtex[0] = new Texture("NPCs/Cowboy/IdleUp.PNG");
    	NPCtex[1] = new Texture("NPCs/Cowboy/IdleDown.PNG");
    }
    public static Texture[] getAnims(int start, int end)
    {
    	Texture[] textures = new Texture[end - start + 1];
    	for (int i = 0; i < textures.length; i++)
    	{
    		textures[i] = NPCtex[start + i]; 
    	}
    	return textures;
    }
    
    //Initializes char[][][] allDialogue
    public static void initText()
    {
    	//Do some art that makes the textbox fancy
    	Shape mainRect = new Shape(0, 0, -Main.length / 2 + 50, Main.width - 100, 75, 165, 214, 176, 255);
    	double radius = 12;
    	double x = mainRect.getX();
    	double y = mainRect.getY();
    	double width = mainRect.getWidth();
    	double length = mainRect.getLength();
    	
    	Shape rect1 = new Shape(0, x - (width + radius) / 2, y, radius / 2, length, 232, 235, 150, 255);
    	Shape rect2 = new Shape(0, x + (width + radius) / 2, y, radius / 2, length, 232, 235, 150, 255);
    	Shape rect3 = new Shape(0, x, y - (length + radius) / 2, width, radius / 2, 232, 235, 150, 255);
    	Shape rect4 = new Shape(0, x, y + (length + radius) / 2, width, radius / 2, 232, 235, 150, 255);
    	Shape ellipse1 = new Shape(1, x - (width) / 2, y - (length) / 2, radius, radius, 232, 235, 150, 255);
    	Shape ellipse2 = new Shape(1, x + (width) / 2, y - (length) / 2, radius, radius, 232, 235, 150, 255);
    	Shape ellipse3 = new Shape(1, x - (width) / 2, y + (length) / 2, radius, radius, 232, 235, 150, 255);
    	Shape ellipse4 = new Shape(1, x + (width) / 2, y + (length) / 2, radius, radius, 232, 235, 150, 255);
    	textBox = new Geometrical();
    	textBox.addShape(mainRect);
    	textBox.addShape(rect1);
    	textBox.addShape(rect2);
    	textBox.addShape(rect3);
    	textBox.addShape(rect4);
    	textBox.addShape(ellipse1);
    	textBox.addShape(ellipse2);
    	textBox.addShape(ellipse3);
    	textBox.addShape(ellipse4);

    	
    	
    	allDialogue = new char[rawDialogue.length][][][];
    	for (int charInd = 0; charInd < rawDialogue.length; charInd++)
    	{
    		String[][] currChar = rawDialogue[charInd];
    		allDialogue[charInd] = new char[currChar.length][][]; 
    		for (int questInd = 0; questInd < currChar.length; questInd++)
    		{
    			String[] currQuest = rawDialogue[charInd][questInd];
    			allDialogue[charInd][questInd] = new char[currQuest.length][];
    			for (int textInd = 0; textInd < currQuest.length; textInd++)
    			{
    				String currText = currQuest[textInd];
    				allDialogue[charInd][questInd][textInd] = new char[currText.length()];
    				for (int i = 0; i < currText.length(); i++)
    				{
    					allDialogue[charInd][questInd][textInd][i] = currText.charAt(i); 
    				}
    			}
    		}
    	}
    	
    }
    public static int notYetSpeaking = -1;
}
