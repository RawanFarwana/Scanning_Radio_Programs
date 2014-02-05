package extractingFromMp3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import javax.sound.sampled.UnsupportedAudioFileException;

public class extractionOrg {

	public static void main(String[] args) throws UnsupportedAudioFileException {
		try {
			ProcessBuilder pb = new ProcessBuilder("extractingFromMp3/sox-14.4.1/sox", "extractingFromMp3/test.wav", "extractingFromMp3/test1.wav", "trim", "0", "5");

			// sets the home directory of my process
			pb.directory(new File("bin/"));
			
			System.out.println(pb.command());
			System.out.println(pb.directory());

			Process p = pb.start();

			System.out.println("started process");

			File file2 = new File("log.txt");
			try {
				PrintStream printStream = new PrintStream(file2);
				System.setOut(printStream);
				System.setErr(printStream);
				pb.redirectErrorStream(true);
				System.out.println("This is redirected output");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			System.out.println("I've caught an exception");
			throw new RuntimeException(e);
		}
	}
}