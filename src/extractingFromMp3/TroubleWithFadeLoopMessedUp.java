package extractingFromMp3;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.ArrayList;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class TroubleWithFadeLoopMessedUp extends Applet {

	private static final File file = new File("res/test.wav");
	private static final String path = "res/sox-14.4.1/sox";
	private static final String pathOfOriginalFile = "res/test.wav";
	private static final String pathOfNewFile = "res/Extracted_Files/test";
	private ArrayList<String> paths = new ArrayList<String>();

	private static final String action = "trim";
	private int startTime = 0;
	private int count = 1;
	private int x = 1;

	public int numberOfMinutesInAudio() throws UnsupportedAudioFileException,
			IOException {
		
		System.out.println(file.getCanonicalFile());
		
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
		AudioFormat format = audioInputStream.getFormat();

		long frames = audioInputStream.getFrameLength();
		double durationInSeconds = (frames + 0.0) / format.getFrameRate();
		double timeInMinutes = durationInSeconds / 60;
		int timeInMinutesForLoop = (int) (timeInMinutes + 1);
		System.out.println("time " + timeInMinutesForLoop);
		return timeInMinutesForLoop;
	}

	public void extractAudioIntoFolder(int timeInMinutesForLoop)
			throws IOException {

		// Do process in memory (use buffers) -> write to disk

		for (int i = 0; i < timeInMinutesForLoop; i++) {
			String newPath = pathOfNewFile + x + ".wav";

			ProcessBuilder pb = new ProcessBuilder(path, pathOfOriginalFile,
					newPath, action, startTime + "", "5", "fade", "h", "0.5", "0", "0.5");

			paths.add(newPath);

			Process p = pb.start();

			startTime += 60;
			count++;
			x++;
		}

	}

	//CHECK
	public void extractAndPackage() throws UnsupportedAudioFileException, IOException {
		AudioInputStream clip1 = null;
		//!!!! print out what is in paths
		for (String path1 : paths)
		{
			System.out.println("PATH NAME " + path1);
			
		    if(clip1 == null)
		    {
		        clip1 = AudioSystem.getAudioInputStream(new File(path1));
		        System.out.println("I'm about to leave if clip1 is null loop");
		        continue;
		    }
		    AudioInputStream clip2 = AudioSystem.getAudioInputStream(new File(path1));
		    AudioInputStream appendedFiles = new AudioInputStream(
		            new SequenceInputStream(clip1, clip2),     
		            clip1.getFormat(), 
		            clip1.getFrameLength() + clip2.getFrameLength());
		    System.out.println("I don't understand what just happened");
		    clip1 = appendedFiles;
		}
			AudioSystem.write(clip1,AudioFileFormat.Type.WAVE,new File("res/exported.wav"));
	}
	//reading before extracting!
	
	public static void main(String[] args)
			throws UnsupportedAudioFileException, IOException {
		
		TroubleWithFadeLoopMessedUp cextractingFromMP3_2 = new TroubleWithFadeLoopMessedUp();

		int timeInMinutesForLoop = cextractingFromMP3_2.numberOfMinutesInAudio();
		
		cextractingFromMP3_2.extractAudioIntoFolder(timeInMinutesForLoop);
		System.out.println("I've extracted");
		cextractingFromMP3_2.extractAndPackage();
		System.out.println("I've removed beep");
	}
}
