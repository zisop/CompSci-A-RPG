package Input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

import Game.Main;

public class CursorInput extends GLFWCursorPosCallback{
	
	private double xPos;
	private double yPos;
	public void invoke(long window, double x, double y)
	{
		xPos = x - Main.width / 2;
		yPos = Main.length / 2 - y;
	}
	public double getX()
	{
		return xPos;
	}
	public double getY()
	{
		return yPos;
	}

}
