package Imported;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;

// To play sound using Clip, the process need to be alive.
// Hence, we use a Swing application.
public class Audio extends JFrame {

	
	public static double baseVolume = .8;
	private static double volume = baseVolume;
	private Clip clip;
	public Audio(String file){

		try {
			// Open an audio input stream.
			URL url = this.getClass().getClassLoader().getResource(file + ".wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			// Get a sound clip resource.
			clip = AudioSystem.getClip();
			// Open audio clip and load samples from the audio input stream.
			clip.open(audioIn);
			FloatControl gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
			float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
			gainControl.setValue(dB);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		volume = baseVolume;
   }
   public void end()
   {
	   clip.setMicrosecondPosition(clip.getMicrosecondLength());
   }
   //Milliseconds
   public void setTime(int time)
   {
	   clip.setMicrosecondPosition(time * 1000);
   }
   public static Audio playSound(String file)
   {
		return new Audio(file);
   }
   public static Audio playSound(String file, double volumeMultiplier)
   {
	   volume *= volumeMultiplier;
	   return new Audio(file);
   }
}