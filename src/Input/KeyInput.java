package Input;

import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyInput extends GLFWKeyCallback
{
    public static boolean[] keys;
    
    static {
        KeyInput.keys = new boolean[65536];
    }
    
    public void invoke(final long window, final int key, final int scancode, final int action, final int mods) {
        KeyInput.keys[key] = (action != 0);
    }
}