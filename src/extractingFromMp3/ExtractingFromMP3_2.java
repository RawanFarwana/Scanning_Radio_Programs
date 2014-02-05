package extractingFromMp3;

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

public class ExtractingFromMP3_2 {

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
		System.out.println("Duration of audio file in seconds: " + durationInSeconds);
		
		double timeInMinutes = durationInSeconds / 60;
		System.out.println("Duration of audio file in minutes: " + timeInMinutes);
		
		int isLastMinuteIncomplete = (durationInSeconds - (int) durationInSeconds > 0.000001) ? 1 : 0;
		int timeInMinutesForLoop = (int) (timeInMinutes + isLastMinuteIncomplete);
		System.out.println("time " + timeInMinutesForLoop);
		
		return timeInMinutesForLoop;
	}

	public void extractAudioIntoFolder(int timeInMinutesForLoop)
			throws IOException {

		// Do process in memory (use buffers) -> write to disk

		// For i = 0 to i is less than 28 
		for (int i = 0; i < timeInMinutesForLoop; i++) {
			
			// res/Extracted_Files/test1.wav 
			
			String newPath = pathOfNewFile + i + ".wav";

			// Get process builder to call this sox command from terminal 
			// Extracts first 5 seconds of every minute 
			ProcessBuilder pb = new ProcessBuilder(path, pathOfOriginalFile, newPath, action, startTime + "", "5");

			System.out.println("Paths in array: " + paths.toString());
			
			paths.add(newPath);
			
			System.out.println("Paths in array after new Path has been added: " + paths.toString());

			// Start process
			Process p = pb.start();

			// Increment start time of extraction to next minute along
			startTime += 60;
		}

	}

	public void addBeep() throws UnsupportedAudioFileException, IOException {
		// Get the first path from the ArrayList 
		AudioInputStream clip = AudioSystem.getAudioInputStream(new File(paths.get(0)));
		
		// For all the strings (paths) in the ArrayList
		for (int i = 1; i < paths.size(); i++) {			
			System.out.println("PATH NAME " + paths.get(i));
		
			// Get current clip
			AudioInputStream currentClip = AudioSystem.getAudioInputStream(new File(paths.get(i)));
			
			// Append beep to the current clip
			AudioInputStream beep = AudioSystem.getAudioInputStream(new File("res/beep.wav"));
			clip = new AudioInputStream(new SequenceInputStream(clip, beep), clip.getFormat(), clip.getFrameLength() + beep.getFrameLength());
			
			// Append next clip to the curent clip
			clip = new AudioInputStream(new SequenceInputStream(clip, currentClip), clip.getFormat(), clip.getFrameLength() + currentClip.getFrameLength());
		}
		
		String nameOfFile = "exported_" + (paths.size() - 1);
		AudioSystem.write(clip, AudioFileFormat.Type.WAVE, new File("res/" + nameOfFile + ".wav"));
	}

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
		
		ExtractingFromMP3_2 extractingFromMP3_2 = new ExtractingFromMP3_2();

		int timeInMinutesForLoop = extractingFromMP3_2.numberOfMinutesInAudio();
		
		extractingFromMP3_2.extractAudioIntoFolder(timeInMinutesForLoop);
		System.out.println("I've extracted");
		extractingFromMP3_2.addBeep();
		System.out.println("I've added beep");
	}
}
