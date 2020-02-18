package Game;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;


import Exchange.ItemExchange;
import Exchange.Shop;
import Imported.MergerSort;
import Imported.Texture;
import Input.CursorInput;
import Input.KeyInput;
import LowLevel.Geometry;
import LowLevel.Image;
import LowLevel.Point;


import static org.lwjgl.opengl.GL11.*;
import LowLevel.Shape;
import UI.Item;
import UI.ItemBag;
import UI.TextDisplay;
import UI.UI;
import World.TileCreation;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

public class Main
{
	public static int questState = 0;
    public static int width;
    public static int length;
    public static int currRoom;
    public static long window;
    public static GLFWVidMode videoMode;
    public static GLFWKeyCallback events;
    public static Displayable[][] allRooms = new Displayable[3][];
    public static ArrayList<Integer> toInit;
    public static boolean[] initted = new boolean[allRooms.length];
    public static Player player;
    public static String currDialogue;
    
    public static boolean playerIn = false;
    public static CursorInput cursor;
    
    
    public static boolean[] movement;
    public static int moveDirecLastFrame;
    public static boolean xEvent;
    public static boolean alreadyInteracting;
    public static Displayable interactingChar;
    
    
    
    public static boolean x = false;
    public static boolean leftClick = false;
    public static boolean one = false;
    public static boolean e = false;
    public static boolean rightClick = false;
    
    public static boolean eLastFrame = false;
    public static boolean leftClickLastFrame = false;
    public static boolean xLastFrame = false;
    public static boolean oneLastFrame = false;
    public static boolean rightClickLastFrame = false;
    
    
    public static void main(String[] args) throws InterruptedException {
        init();
        glClearColor(1, 0, 1, 1);
        Texture tex = new Texture("IdleAnim/IdleDown.PNG");
        player = new Player(tex, 0, 0, 70, 70, 35, 55);
        initRoom(currRoom = 0);
        
        

        
        
        Item wand = new Item(Item.wand0);
        Item ruby = new Item(Item.ruby);
        ruby.setQuantity(40);
        Item wand2 = new Item(Item.wand1);
        
        UI.playerBag.addItem(wand, 8);
        UI.playerBag.addItem(wand2, 0);
        UI.playerBag.addItem(ruby, 3);
        
        int[] inIDs = new int[] {Item.ruby};
        int[] inQuantities = new int[] {6};
        ItemExchange exchange = new ItemExchange(inQuantities, inIDs, 0);
        Shop shop = new Shop(new ItemExchange[] {exchange});
        shop.setVisibility(true);
        
        Image[] tiles = TileCreation.createGrid(TileCreation.Grass, 200, 200);
        
        while (!glfwWindowShouldClose(window)) {
        	double startTime = System.currentTimeMillis();
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT);
            
            leftClick = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == 1;
            rightClick = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_2) == 1;
            x = KeyInput.keys[GLFW_KEY_X];
            e = KeyInput.keys[GLFW_KEY_E];
            one = KeyInput.keys[GLFW_KEY_1];
            
            for(int i = 0; i < toInit.size(); i++)
            {
            	initRoom(toInit.get(i));
            }
            
            
            //movement = {ability to move north, east, south, west}
            boolean[] movement = player.getMovement();
            for (int i = 0; i < toInit.size(); ++i) {
                initRoom(toInit.get(i));
            }
            toInit.clear();
            
            boolean walked = false;
            boolean[] keysPressed = new boolean[4];
            //Stores W A S D presses in keysPressed
            // 0 = W, 1 = D, 2 = S, 3 = A
            keysPressed[0] = KeyInput.keys[GLFW_KEY_W];
            keysPressed[1] = KeyInput.keys[GLFW_KEY_D];
            keysPressed[2] = KeyInput.keys[GLFW_KEY_S];
            keysPressed[3] = KeyInput.keys[GLFW_KEY_A];
            //Makes player walk according to KeyPresses
            int moveDirec = -1;
            if (!alreadyInteracting) {
                if (keysPressed[0] && movement[0] && (moveDirecLastFrame == 0 || moveDirecLastFrame == -1)) {
                    moveDirec = 0;
                    walked = true;
                } 
                else if (keysPressed[1] && movement[1] && (moveDirecLastFrame == 1 || moveDirecLastFrame == -1)) {
                    moveDirec = 1;
                    walked = true;
                } 
                else if (keysPressed[2] && movement[2] && (moveDirecLastFrame == 2 || moveDirecLastFrame == -1)) {
                    moveDirec = 2;
                    walked = true;
                } 
                else if (keysPressed[3] && movement[3] && (moveDirecLastFrame == 3 || moveDirecLastFrame == -1)) {
                    moveDirec = 3;
                    walked = true;
                }
            }
            if (moveDirec != -1) {
            	player.move(moveDirec);
            }
            //If player didn't walk, then he stopped walking :)
            if (!walked) {player.stopWalk();}
            TileCreation.showTiles(tiles);
            showVisibles();
            if (shop.getVisibility()) {shop.UIshow();}
            
            UI.showUI();
            
            
            
            glfwSwapBuffers(window);
            
            //30FPS
            double wait = 1000/30.0 - (System.currentTimeMillis() - startTime);
            if (wait > 0) {Thread.sleep((long)wait);}
            
            leftClickLastFrame = leftClick;
            xLastFrame = x;
            eLastFrame = e;
            moveDirecLastFrame = moveDirec;
            oneLastFrame = one;
            rightClickLastFrame = rightClick;
            
            xEvent = false;
        }
    }
    public static double cursorAngle()
    {
    	double curseX = cursor.getX();
    	double curseY = cursor.getY();
    	double hypoLen = Math.sqrt((curseX * curseX) + (curseY * curseY));
    	double angle;
    	if (curseY >= 0)
    	{
    		
    		angle = Math.toDegrees(Math.acos(curseX / hypoLen));
    	}
    	else 
    	{
			angle = 360 -Math.toDegrees(Math.acos(curseX / hypoLen));
		}
    	return angle;
    }
    //Figures out whether a character can interact, or if something else is already interacting
    public static boolean canInteract(Displayable obj)
    {
    	return !xEvent && (interactingChar == null || interactingChar == obj);
    }
    public static boolean xInteraction(Displayable obj, double interactionRadius)
    {
    	return x && !xLastFrame && player.collision(obj, interactionRadius) && Main.canInteract(obj);
    }
    //Will determine if a click interaction has been made on an object
    public static boolean clickInteraction(Displayable obj)
    {
    	boolean validClick = leftClick && !leftClickLastFrame;
    	boolean inRadius = player.collision(obj, 40);
    	double objLeft = obj.getX() - obj.getCharWidth() / 2;
    	double objRight = obj.getX() + obj.getCharWidth() / 2;
    	double objTop = obj.getY() + obj.getCharLength() / 2;
    	double objBot = obj.getY() - obj.getCharLength() / 2;
    	Point p1 = new Point(objLeft, objBot);
    	Point p2 = new Point(objRight, objBot);
    	Point p3 = new Point(objRight, objTop);
    	Point p4 = new Point(objLeft, objTop);
    	Point[] objPoints = new Point[] {p1, p2, p3, p4};
    	Point cursorPoint = new Point(cursor.getX() + player.getX(), cursor.getY() + player.getY());

    	return validClick && inRadius && Geometry.insideShape(objPoints, cursorPoint);
    }
    
  //Shows all visible Displayables
    public static void showVisibles() {
    	if (!playerIn)
    	{
    		Displayable[] newVisibles = new Displayable[allRooms[currRoom].length + 1];
    		for (int i = 0; i < allRooms[currRoom].length; i++)
    		{
    			newVisibles[i] = allRooms[currRoom][i];
    		}
    		newVisibles[newVisibles.length - 1] = player;
    		newVisibles = MergerSort.mergeSort(newVisibles);
            allRooms[currRoom] = newVisibles;
            playerIn = true;
    	}
    	else 
    	{
    		allRooms[currRoom] = MergerSort.mergeSort(allRooms[currRoom]);
		}
        
        for (int i = 0; i < allRooms[currRoom].length; ++i) {
           	((Image) allRooms[currRoom][i]).show();
        }
        if (interactingChar instanceof NPC)
        {
        	((NPC)interactingChar).showText();
        }
        Projectile.showVisProjectiles();
    }
    
    
    //Initializes a room given a roomNumber, or doesn't if already initialized
    public static void initRoom(int roomNum) {
        if (!Main.initted[roomNum]) {
            if (roomNum == 0) {
                initRoom0();
            }
            if (roomNum == 1) {
                initRoom1();
            }
            if (roomNum == 2) {
                initRoom2();
            }
            playerIn = false;
        }
        if (roomNum == 0) {
            player.setPos(0.0, -150.0);
        }
        if (roomNum == 1) {
            player.setPos(0.0, 150.0);
        }
        if (roomNum == 2) {
            player.setPos(0.0, 150.0);
        }
        interactingChar = null;
    }
    
    public static void initRoom0() {
        Displayable[] room = new Displayable[6];
        NPC[] npcs = new NPC[1];
        Door door1 = new Door(Shape.shapes[0], 0, -300, 50, 100, 2);
        Door door2 = new Door(Shape.shapes[0], 200, -100, 100, 50, 1);
        room[0] = door1;
        room[1] = door2;
        door1.setLead(1);
        door2.setLead(2);
        room[2] = new Displayable(Shape.shapes[0], -200, 200, 150, 150);
        room[3] = new Displayable(Shape.shapes[0], 200, 200, 150, 150);
        room[4] = new NPC(Player.loadedTex[5], 200, -50, 40, 40, 0, 15);
        ItemBag bag = new ItemBag(0, 300, 40, 40, 2, 2);
        Item wand = new Item(2);
        bag.addItem(wand, 1);
        room[5] = new Chest(0, 200, 50, 50, 50, 50, 40, bag);
        npcs[0] = (NPC)room[4];
        allRooms[0] = room;
        initted[0] = true;
    }
    
    public static void initRoom1() {
        Displayable[] room = new Displayable[4];
        Door theDoor = new Door(Shape.shapes[0], 0, 300, 50, 100, 0);
        theDoor.setLead(0);
        room[0] = theDoor;
        room[1] = new Displayable(Shape.shapes[0], -200, -200, 150, 150);
        room[2] = new Displayable(Shape.shapes[0], 200, -200, 150, 150);
        room[3] = new NPC(Player.loadedTex[7], 100, 300, 40, 40, 1, 15);
        allRooms[1] = room;
        initted[1] = true;
    }
    
    public static void initRoom2() {
    	
        Displayable[] room = new Displayable[3];
        Door theDoor = new Door(Shape.shapes[0], 0, 300, 50, 100, 0);
        theDoor.setLead(0);
        room[0] = theDoor;
        room[1] = new Displayable(Shape.shapes[0], -100, -200, 150, 150);
        room[2] = new Displayable(Shape.shapes[0], 100, -200, 150, 150);
        allRooms[2] = room;
        initted[2] = true;
    }
    
    //Initializes the game
    public static void init() {
        events = new KeyInput();
        toInit = new ArrayList<Integer>();
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
        
        Player.initTex();
        Item.initItems();
        Chest.initChests();
        Projectile.initProj();
        Shape.initShapes();
        UI.init();
        TextDisplay.initText();
        TileCreation.initTex();
        glEnable(3553);
        glEnable(3042);
        glBlendFunc(770, 771);
        glfwSetKeyCallback(window, events);
    }
}
