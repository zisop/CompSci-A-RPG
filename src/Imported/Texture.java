package Imported;

import java.nio.ByteBuffer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import javax.imageio.ImageIO;

public class Texture
{
    private int id;
    private int width;
    private int length;
    //Initializes a texture given a Image name
    public Texture(String file) {
        try {
        	BufferedImage bi = ImageIO.read(getClass().getClassLoader().getResource("res/" + file));
            width = bi.getWidth();
            length = bi.getHeight();
            int[] rawPix = new int[width * length * 4];
            rawPix = bi.getRGB(0, 0, width, length, null, 0, width);
            ByteBuffer pixels = BufferUtils.createByteBuffer(width * length * 4);
            for (int r = 0; r < width; ++r) {
                for (int c = 0; c < length; ++c) {
                    final int pixel = rawPix[r * length + c];
                    pixels.put((byte)(pixel >> 16 & 0xFF));
                    pixels.put((byte)(pixel >> 8 & 0xFF));
                    pixels.put((byte)(pixel & 0xFF));
                    pixels.put((byte)(pixel >> 24 & 0xFF));
                }
            }
            pixels.flip();
            GL11.glBindTexture(3553, id = GL11.glGenTextures());
            GL11.glTexParameterf(3553, 10241, 9728.0f);
            GL11.glTexParameterf(3553, 10240, 9728.0f);
            GL11.glTexImage2D(3553, 0, 6408, width, length, 0, 6408, 5121, pixels);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Binds the texture to the screen
    public void bind() {
        GL11.glBindTexture(3553, id);
    }
}