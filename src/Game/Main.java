package Game;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;


import Combat.MeleeMob;
import Combat.Mob;
import Combat.Movable;
import Combat.AOE;
import Combat.CombatChar;
import Combat.Player;
import Combat.Projectile;
import Combat.SpawnPoint;
import Exchange.ItemExchange;
import Exchange.Shop;
import Exchange.ShopKeeper;
import Imported.Texture;
import Input.CursorInput;
import Input.KeyInput;
import LowLevel.Geometry;
import LowLevel.Image;
import LowLevel.Point;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.Random;

import LowLevel.Shape;
import UI.Item;
import UI.ItemBag;
import UI.SpellSlot;
import UI.TextDisplay;
import UI.UI;
import World.Room;
import World.Terrain;
import World.Tile;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

public class Main
{
	public static Random random;
	public static int questState = 0;
    public static int width;
    public static int length;
    public static int currRoom;
    public static long window;
    public static GLFWVidMode videoMode;
    public static GLFWKeyCallback events;
    public static Room[] allRooms = new Room[3];
    public static boolean[] initted = new boolean[allRooms.length];
    public static ArrayList<Integer> toInit = new ArrayList<Integer>();
    public static Player player;
    public static String currDialogue;
    
    public static CursorInput cursor;
    
    
    public static int moveDirecLastFrame;
    public static boolean interactionEvent;
    public static boolean alreadyInteracting;
    public static Image interactingChar;
    public static final int FPS = 30;
    
    
    
    
    
    public static boolean leftClick = false;
    public static boolean one = false;
    public static boolean two = false;
    public static boolean three = false;
    public static boolean four = false;
    public static boolean e = false;
    public static boolean x = false;
    public static boolean r = false;
    public static boolean rightClick = false;
   
    public static boolean eLastFrame = false;
    public static boolean rLastFrame = false;
    public static boolean leftClickLastFrame = false;
    public static boolean xLastFrame = false;
    public static boolean oneLastFrame = false;
    public static boolean twoLastFrame = false;
    public static boolean threeLastFrame = false;
    public static boolean fourLastFrame = false;
    public static boolean rightClickLastFrame = false;
    
    
    public static void main(String[] args) throws InterruptedException {
        init();
        Texture tex = new Texture("IdleAnim/IdleDown.PNG");
        player = new Player(0, 0);
        initRoom(currRoom = 0);
        
        

        
        
        Item wand = new Item(Item.wand0);
        Item ruby = new Item(Item.ruby);
        Item ruby2 = new Item(Item.ruby);
        ruby2.setQuantity(64);
        ruby.setQuantity(64);
        Item wand2 = new Item(Item.wand1);
        
        UI.playerBag.addItem(wand, 8);
        UI.playerBag.addItem(wand2, 0);
        UI.playerBag.addItem(ruby, 3);
        UI.playerBag.addItem(ruby2, 4);
        
        
        
        
        while (!glfwWindowShouldClose(window)) {
        	double startTime = System.currentTimeMillis();
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT);
            
            toInit.forEach((room) -> initRoom(room));
            toInit.clear();
            
            leftClick = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == GLFW_TRUE;
            rightClick = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_2) == GLFW_TRUE;
            x = KeyInput.keys[GLFW_KEY_X];
            e = KeyInput.keys[GLFW_KEY_E];
            one = KeyInput.keys[GLFW_KEY_1];
            two = KeyInput.keys[GLFW_KEY_2];
            three = KeyInput.keys[GLFW_KEY_3];
            four = KeyInput.keys[GLFW_KEY_4];
            r = KeyInput.keys[GLFW_KEY_R];
            
            player.updateMovement();
            boolean[] movement = player.getMovement();
            boolean[] keysPressed = new boolean[4];
            //Stores W A S D presses in keysPressed
            // 0 = W, 1 = D, 2 = S, 3 = A
            keysPressed[CombatChar.up] = KeyInput.keys[GLFW_KEY_W];
            keysPressed[CombatChar.right] = KeyInput.keys[GLFW_KEY_D];
            keysPressed[CombatChar.down] = KeyInput.keys[GLFW_KEY_S];
            keysPressed[CombatChar.left] = KeyInput.keys[GLFW_KEY_A];
            //Makes player walk according to KeyPresses
            int moveDirec = notMoving;
            if (!alreadyInteracting) {
                if (keysPressed[CombatChar.up] && movement[CombatChar.up] && (moveDirecLastFrame == CombatChar.up || moveDirecLastFrame == notMoving)) {
                    moveDirec = CombatChar.up;
                } 
                else if (keysPressed[CombatChar.right] && movement[CombatChar.right] && (moveDirecLastFrame == CombatChar.right || moveDirecLastFrame == notMoving)) {
                    moveDirec = CombatChar.right;
                } 
                else if (keysPressed[CombatChar.down] && movement[CombatChar.down] && (moveDirecLastFrame == CombatChar.down || moveDirecLastFrame == notMoving)) {
                    moveDirec = CombatChar.down;
                } 
                else if (keysPressed[CombatChar.left] && movement[CombatChar.left] && (moveDirecLastFrame == CombatChar.left || moveDirecLastFrame == notMoving)) {
                    moveDirec = CombatChar.left;
                }
            }
            
            if (moveDirec != notMoving) {player.setDirec(moveDirec); player.move();}
            
            else {player.stopWalk();}
            if (alreadyInteracting) {Image.colorMultiplier = .7f;}
            else {Image.colorMultiplier = 1;}
            showVisibles();
            player.printStats();

            UI.showUI();
            
            glfwSwapBuffers(window);
            
            //30FPS
            double wait = 1000./FPS - (System.currentTimeMillis() - startTime);
            if (wait > 0) {Thread.sleep((long)wait);}
            
            leftClickLastFrame = leftClick;
            xLastFrame = x;
            eLastFrame = e;
            moveDirecLastFrame = moveDirec;
            oneLastFrame = one;
            twoLastFrame = two;
            threeLastFrame = three;
            fourLastFrame = four;
            rightClickLastFrame = rightClick;
            rLastFrame = r;
            interactionEvent = false;
        }
    }
    public static double cursorAngle()
    {
    	double curseX = cursor.getX();
    	double curseY = cursor.getY();
    	double hypoLen = Math.sqrt((curseX * curseX) + (curseY * curseY));
    	double angle;
    	if (curseY >= 0) {angle = Math.toDegrees(Math.acos(curseX / hypoLen));}
    	else {angle = 360 - Math.toDegrees(Math.acos(curseX / hypoLen));}
    	return angle;
    }
    //Figures out whether a character can interact, or if something else is already interacting
    public static boolean canInteract(Image obj)
    {
    	return !interactionEvent && (interactingChar == null || interactingChar == obj);
    }
    public static boolean xInteraction(Image obj)
    {
    	return x && !xLastFrame && player.xCollision(obj) && canInteract(obj);
    }
    /**
     * Returns a click interaction with a non UI character
     * @param obj
     * @return clicked and within distance of player
     */
    public static boolean clickInteraction(Image obj)
    {
    	boolean validClick = leftClick && !leftClickLastFrame;
    	return validClick && player.clickCollision(obj) &&
    	Geometry.insideShape(obj.getShowBasis(), new Point(cursor.getX() + player.getX(), cursor.getY() + player.getY()));
    }
    
  //Shows all visible Displayables
    public static void showVisibles() {
        allRooms[currRoom].show();
    }
    
    public static Point exitPoint = new Point(0, 0);
    //Initializes a room given a roomNumber, or doesn't if already initialized
    public static void initRoom(int roomNum) {
    	if (allRooms[currRoom] != null) {allRooms[currRoom].clear();}
    	currRoom = roomNum;
        if (!initted[roomNum]) {
            switch (roomNum) {
				case 0:
					initRoom0();
					break;
				case 1:
					initRoom1();
					break;
				case 2:
					initRoom2();
					break;
				default:
					try {throw new Exception("Room ID: " + roomNum + " didn't exist");}
					catch (Exception e) {e.printStackTrace(); System.exit(0);}
			
			}
        }
        player.setPos(exitPoint.getX(), exitPoint.getY());
        interactingChar = null;
    }
    public static void initRoom0() {
        ArrayList<Image> room = new ArrayList<Image>();
        Door door1 = new Door(Door.woodWindowless, 0, -300, Movable.up);
        Door door2 = new Door(Door.woodWindowless, 200, -100, Movable.left);
        
        door1.setExit(0, 200);
        door2.setExit(0, 200);
        
        
        room.add(door1);
        room.add(door2);
        door1.setLead(1);
        door2.setLead(2);
        
        int[] inIDs = new int[] {Item.ruby};
        int[] inQuantities = new int[] {6};
        ItemExchange exchange = new ItemExchange(inQuantities, inIDs, 0);
        Shop shop = new Shop(new ItemExchange[] {exchange});
        ShopKeeper npc = new ShopKeeper(NPC.cowboy, 200, -50, 40, 40, 0, 13, shop);
        
        npc.setAnim(ShopKeeper.cowboyUp);
        room.add(npc);
        ItemBag bag = new ItemBag(0, 300, 40, 40, 2, 2);
        Item wand = new Item(2);
        bag.addItem(wand, 1);
        room.add(new Chest(0, 200, 50, 50, 50, 50, 40, bag));
        
        Terrain test = Terrain.createTerrain(Tile.Dirt, 0, 0, 10, 10);
        test.addRow(Tile.GrassDirtBR, CombatChar.down);
        
        int[] mobIDs = new int[] {Mob.skeleton, Mob.slime};
        //SpawnPoint spawnPoint = new SpawnPoint(-20, 20, mobIDs);
        //room.add(spawnPoint);
        
        Room newRoom = new Room(room, new Terrain[] {test});
        allRooms[0] = newRoom;
        initted[0] = true;
    }
    
    public static void initRoom1() {
        ArrayList<Image> images = new ArrayList<Image>();
        Door theDoor = new Door(Door.woodWindowless, 0, 300, Movable.down);
        theDoor.setLead(0);
        theDoor.setExit(0, -100);
        
        images.add(theDoor);
        
        NPC npc = new NPC(0, 100, 300, 40, 40, 1, 13);
        images.add(npc);
        allRooms[1] = new Room(images, new Terrain[] {});
        initted[1] = true;
    }
    
    public static void initRoom2() {
    	
        ArrayList<Image> images = new ArrayList<Image>();
        Door theDoor = new Door(Door.woodWindowless, 0, 300, Movable.down);
        theDoor.setLead(0);
        theDoor.setExit(0, -100);
        images.add(theDoor);
        allRooms[2] = new Room(images, new Terrain[] {});
        initted[2] = true;
    }
    
    //Initializes the game
    public static void init() {
    	random = new Random();
        events = new KeyInput();
        width = 1000;
        length = 800;
        glfwInit();
        window = glfwCreateWindow(width, length, "game", 0L, 0L);
        glfwSetCursorPosCallback(window, cursor = new CursorInput());
        videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - length) / 2);
        glfwShowWindow(window);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        
        Image.init(width, length);
        Item.initItems();
        Chest.initChests();
        Projectile.initProj();
        Shape.initShapes();
        SpellSlot.init();
        TextDisplay.initText();
        Door.initTex();
        Tile.initTex();
        CombatChar.init();
        NPC.initTex();
        AOE.init();
        
        UI.init();
        glEnable(3553);
        glEnable(3042);
        glBlendFunc(770, 771);
        glfwSetKeyCallback(window, events);
    }
    
    private static final int notMoving = -1;
}
