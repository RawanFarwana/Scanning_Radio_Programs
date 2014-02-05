package extractingFromMp3;

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

public class extraction_packaging {
	
	public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException {
		try {
			 
			File file = new File("/Users/rawanfarwana/Documents/workspace/Test/bin/extractingFromMp3/test.wav");
			
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
			AudioFormat format = audioInputStream.getFormat();
		
			long frames = audioInputStream.getFrameLength();	
			double durationInSeconds = (frames + 0.0) / format.getFrameRate();
			double timeInMinutes = durationInSeconds / 60;
			int timeInMinutesForLoop = (int)(timeInMinutes + 1); 
					
			String path = "/Users/rawanfarwana/Documents/workspace/Test/bin/extractingFromMp3/sox-14.4.1/sox";
			String pathOfOriginalFile = "/Users/rawanfarwana/Documents/workspace/Test/bin/extractingFromMp3/test.wav";
			String pathOfNewFile = "/Users/rawanfarwana/Documents/workspace/Test/bin/extractingFromMp3/Extracted_Files/test";

			String action = "trim";  
			int startTime = 0; 
			int count = 1;
			int x = 1;
			
			ArrayList<String> paths = new ArrayList<String>();
			
			for(int i = 0; i < timeInMinutesForLoop; i++) {
				String newPath = pathOfNewFile+x+".wav";
				
				ProcessBuilder pb = new ProcessBuilder(path, pathOfOriginalFile, newPath , action , startTime+ "", "5");

				paths.add(newPath);
				
				pb.directory(new File("bin/"));
				
				Process p = pb.start();
				
				startTime += 60; 
				count++;
				x++;
			}
			
			AudioInputStream clip1 = null;
			for (String path1 : paths)
			{
			    if(clip1 == null)
			    {
			        clip1 = AudioSystem.getAudioInputStream(new File(path1));
			        continue;
			    }
			    AudioInputStream clip2 = AudioSystem.getAudioInputStream(new File(path1));
			    AudioInputStream appendedFiles = new AudioInputStream(
			            new SequenceInputStream(clip1, clip2),     
			            clip1.getFormat(), 
			            clip1.getFrameLength() + clip2.getFrameLength());
			    clip1 = appendedFiles;
			}
			AudioSystem.write(clip1, AudioFileFormat.Type.WAVE, new File("/Users/rawanfarwana/Documents/workspace/Test/bin/extractingFromMp3/exported.wav"));
		
		} catch (IOException e)
		{
			System.out.println("I've caught an exception");
			throw new RuntimeException(e);
		}
	}
}