package LowLevel;
import org.lwjgl.opengl.GL11;

import Game.Main;
import Imported.Texture;

public class Image extends Positionable
{
    private Texture image;
    private float alpha;
    private boolean collides;
    private int enemyState;
    
    public static boolean shouldRotate = true;
    public static boolean shouldNotRotate = false;
    
    public Image(Texture img, double inX, double inY, double w, double l) {
        super(inX, inY, w, l);
        image = img;
        alpha = 255;
        collides = false;
        enemyState = neutral;
    }
    public Image(Texture img, double inX, double inY, double w, double l, double hitW, double hitL) {
        super(inX, inY, w, l, hitW, hitL);
        image = img;
        alpha = 255;
        collides = false;
        enemyState = neutral;
    }
    public Image(Texture img, double inX, double inY, double w, double l, double hitW, double hitL, double hbDown) {
        super(inX, inY, w, l, hitW, hitL, hbDown);
        image = img;
        alpha = 255;
        collides = false;
        enemyState = neutral;
    }
    public boolean isEnemy(Image otherChar)
    {
    	if (enemyState() == good && otherChar.enemyState() == bad) {return true;}
    	if (enemyState() == bad && otherChar.enemyState() == good) {return true;}
    	return false;
    }
    public void setEnemyState(int newState) {enemyState = newState;}
    public int enemyState() {return enemyState;}
    public void setAlpha(float newAlpha) {alpha = newAlpha;}
    public float getAlpha() {return alpha;}
    public void setImage(Texture newImg) {image = newImg;}
    public boolean collides() {return collides;}
    public void setCollisionStatus(boolean newStatus) {collides = newStatus;}
    //displays the image + rotations if those end up being useful
    public void show(float r, float g, float b, float a) {
    	double screenX = getX() - Main.player.getX();
    	double screenY = getY() - Main.player.getY();
    	if ((Math.abs(screenX) < monWid) && (Math.abs(screenY) < monLen))
    	{
    		GL11.glColor4f(r * colorMultiplier / 255, g * colorMultiplier / 255, b * colorMultiplier / 255, a / 255);
    		Point[] pointCords;
        	if (shouldRotate())
        	{
        		//Shows assuming that rotations are enabled for the image
        		//DL, UL, UR, DR
        		pointCords = Geometry.rotatePoints(getShowBasis(), this, getAngle());
        		image.bind();
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glTexCoord2f(0.0f, 1.0f);
                GL11.glVertex2f(((float)(pointCords[DL].getX() - Main.player.getX()) * 2.0f / monWid), ((float)(pointCords[DL].getY() - Main.player.getY()) * 2.0f / monLen));
                GL11.glTexCoord2f(0.0f, 0.0f);
                GL11.glVertex2f(((float)(pointCords[UL].getX() - Main.player.getX()) * 2.0f / monWid), ((float)(pointCords[UL].getY() - Main.player.getY()) * 2.0f / monLen));
                GL11.glTexCoord2f(1.0f, 0.0f);
                GL11.glVertex2f(((float)(pointCords[UR].getX() - Main.player.getX()) * 2.0f / monWid), ((float)(pointCords[UR].getY() - Main.player.getY()) * 2.0f / monLen));
                GL11.glTexCoord2f(1.0f, 1.0f);
                GL11.glVertex2f(((float)(pointCords[DR].getX() - Main.player.getX()) * 2.0f / monWid), ((float)(pointCords[DR].getY() - Main.player.getY()) * 2.0f / monLen));
                GL11.glEnd();
        	}
        	else {
        		//Takes less time than show if shouldRotate; time optimization
        		pointCords = getShowBasis();
        		image.bind();
        		GL11.glBegin(GL11.GL_QUADS);
                GL11.glTexCoord2f(0.0f, 1.0f);
                GL11.glVertex2f(((float)(pointCords[DL].getX() - Main.player.getX()) * 2.0f / monWid), ((float)(pointCords[DL].getY() - Main.player.getY()) * 2.0f / monLen));
                GL11.glTexCoord2f(0.0f, 0.0f);
                GL11.glVertex2f(((float)(pointCords[UL].getX() - Main.player.getX()) * 2.0f / monWid), ((float)(pointCords[UL].getY() - Main.player.getY()) * 2.0f / monLen));
                GL11.glTexCoord2f(1.0f, 0.0f);
                GL11.glVertex2f(((float)(pointCords[UR].getX() - Main.player.getX()) * 2.0f / monWid), ((float)(pointCords[UR].getY() - Main.player.getY()) * 2.0f / monLen));
                GL11.glTexCoord2f(1.0f, 1.0f);
                GL11.glVertex2f(((float)(pointCords[DR].getX() - Main.player.getX()) * 2.0f / monWid), ((float)(pointCords[DR].getY() - Main.player.getY()) * 2.0f / monLen));
                GL11.glEnd();
        	}
        	GL11.glColor4f(1, 1, 1, 1);
            
    	}
    	
        
        
    }
    public void show() {
    	show(255, 255, 255, alpha);
    }
    public void UIshow(float r, float g, float b, float a) {
        Point[] pointCords = Geometry.rotatePoints(getShowBasis(), this, getAngle());
        
        image.bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor4f(r / 255, g / 255, b / 255, a / 255);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2f(((float)pointCords[DL].getX()) * 2.0f / monWid, ((float)pointCords[DL].getY()) * 2.0f / monLen);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(((float)pointCords[UL].getX()) * 2.0f / monWid, ((float)pointCords[UL].getY()) * 2.0f / monLen);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2f(((float)pointCords[UR].getX()) * 2.0f / monWid, ((float)pointCords[UR].getY()) * 2.0f / monLen);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2f(((float)pointCords[DR].getX()) * 2.0f / monWid, ((float)pointCords[DR].getY()) * 2.0f / monLen);
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnd();
    }
    public void UIshow() {
    	float r = 255, g = 255, b = 255;
        UIshow(r, g, b, alpha);
    }
    public static void init(int width, int length)
    {
    	monWid = width;
    	monLen = length;
    }
    public static int bad = 0;
    public static int good = 1;
    public static int neutral = 2;
    public static float colorMultiplier = 1;
    private static int monLen;
    private static int monWid;
}
