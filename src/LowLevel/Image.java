package LowLevel;
import org.lwjgl.opengl.GL11;

import Game.Main;
import Imported.Texture;

public class Image extends Positionable
{
    private Texture image;
    private float alpha;
    private double hitboxDown;
    private boolean collides;
    
    public static boolean shouldRotate = true;
    public static boolean shouldNotRotate = false;
    
    public Image(Texture img, double inX, double inY, double w, double l) {
        super(inX, inY, w, l);
        image = img;
        alpha = 255;
        hitboxDown = 0;
        collides = false;
    }
    public Image(Texture img, double inX, double inY, double w, double l, double hitW, double hitL) {
        super(inX, inY, w, l, hitW, hitL);
        image = img;
        alpha = 255;
        hitboxDown = 0;
        collides = false;
    }
    public Image(Texture img, double inX, double inY, double w, double l, double hitW, double hitL, double hbDown) {
        super(inX, inY, w, l, hitW, hitL, hbDown);
        image = img;
        alpha = 255;
        collides = false;
    }
    public int relPos(Positionable otherChar) {return relPos(otherChar, hitboxDown);}
    public void setAlpha(float newAlpha) {alpha = newAlpha;}
    public float getAlpha() {return alpha;}
    public void setImage(Texture newImg) {image = newImg;}
    public boolean collides() {return collides;}
    public void setCollisionStatus(boolean newStatus) {collides = newStatus;}
    //displays the image + rotations if those end up being useful
    public void show(boolean shouldRotate) {
    	double screenX = getX() - Main.player.getX();
    	double screenY = getY() - Main.player.getY();
    	if ((Math.abs(screenX) < monWid) && (Math.abs(screenY) < monLen))
    	{
    		
        	if (shouldRotate)
        	{
        		//Shows assuming that rotations are enabled for the image
        		Point[] pointCords = {new Point(-getWidth() / 2.0, -getLength() / 2.0), 
                        new Point(-getWidth() / 2.0, getLength() / 2.0), 
                        new Point(getWidth() / 2.0f, getLength() / 2.0), 
                        new Point(getWidth() / 2.0f, -getLength() / 2.0)};
        		pointCords = new Point[]{Geometry.rotatePoint(pointCords[0], getAngle()), 
        				                Geometry.rotatePoint(pointCords[1], this.getAngle()), 
        				                Geometry.rotatePoint(pointCords[2], this.getAngle()), 
        				                Geometry.rotatePoint(pointCords[3], this.getAngle())};
        		image.bind();
                GL11.glBegin(7);
                GL11.glTexCoord2f(0.0f, 1.0f);
                GL11.glVertex2f(((float)(pointCords[0].getX() + screenX) * 2.0f / monWid), ((float)(pointCords[0].getY() + screenY) * 2.0f / monLen));
                GL11.glTexCoord2f(0.0f, 0.0f);
                GL11.glVertex2f(((float)(pointCords[1].getX() + screenX) * 2.0f / monWid), ((float)(pointCords[1].getY() + screenY) * 2.0f / monLen));
                GL11.glTexCoord2f(1.0f, 0.0f);
                GL11.glVertex2f(((float)(pointCords[2].getX() + screenX) * 2.0f / monWid), ((float)(pointCords[2].getY() + screenY) * 2.0f / monLen));
                GL11.glTexCoord2f(1.0f, 1.0f);
                GL11.glVertex2f(((float)(pointCords[3].getX() + screenX) * 2.0f / monWid), ((float)(pointCords[3].getY() + screenY) * 2.0f / monLen));
                GL11.glEnd();
        	}
        	else {
        		//Takes less time than show if shouldRotate; time optimization
        		image.bind();
                GL11.glBegin(7);
                GL11.glTexCoord2f(0.0f, 1.0f);
                GL11.glVertex2f(((float)(screenX - getWidth() / 2) * 2.0f / monWid), ((float)(screenY - getLength() / 2) * 2.0f / monLen));
                GL11.glTexCoord2f(0.0f, 0.0f);
                GL11.glVertex2f(((float)(screenX - getWidth() / 2) * 2.0f / monWid), ((float)(screenY + getLength() / 2) * 2.0f / monLen));
                GL11.glTexCoord2f(1.0f, 0.0f);
                GL11.glVertex2f(((float)(screenX + getWidth() / 2) * 2.0f / monWid), ((float)(screenY + getLength() / 2) * 2.0f / monLen));
                GL11.glTexCoord2f(1.0f, 1.0f);
                GL11.glVertex2f(((float)(screenX + getWidth() / 2) * 2.0f / monWid), ((float)(screenY - getLength() / 2) * 2.0f / monLen));
                GL11.glEnd();
        	}
            
    	}
    	
        
        
    }
    public void show() {
    	show(shouldNotRotate);
    }
    public void UIshow(float r, float g, float b, float a) {
        double[] xVals = { -getWidth() / 2.0, -getWidth() / 2.0, getWidth() / 2.0f, getWidth() / 2.0f };
        double[] yVals = { -getLength() / 2.0, getLength() / 2.0, getLength() / 2.0, -getLength() / 2.0 };
        Point[] pointCords = { Geometry.rotatePoint(new Point(xVals[0], yVals[0]), getAngle()), 
        		Geometry.rotatePoint(new Point(xVals[1], yVals[1]), this.getAngle()), 
        		Geometry.rotatePoint(new Point(xVals[2], yVals[2]), this.getAngle()), 
        		Geometry.rotatePoint(new Point(xVals[3], yVals[3]), this.getAngle())};
        
        image.bind();
        GL11.glBegin(7);
        GL11.glColor4f(r / 255, g / 255, b / 255, a / 255);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2f(((float)pointCords[0].getX() + (float)getX()) * 2.0f / monWid, ((float)pointCords[0].getY() + (float)getY()) * 2.0f / monLen);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(((float)pointCords[1].getX() + (float)getX()) * 2.0f / monWid, ((float)pointCords[1].getY() + (float)getY()) * 2.0f / monLen);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2f(((float)pointCords[2].getX() + (float)getX()) * 2.0f / monWid, ((float)pointCords[2].getY() + (float)getY()) * 2.0f / monLen);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2f(((float)pointCords[3].getX() + (float)getX()) * 2.0f / monWid, ((float)pointCords[3].getY() + (float)getY()) * 2.0f / monLen);
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnd();
    }
    public void UIshow() {
    	float r = 255, g = 255, b = 255;
        UIshow(r, g, b, alpha);
    }
    private static int monLen = Main.length;
    private static int monWid = Main.width;
}
