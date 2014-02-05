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

public class BeepSeparator2 extends Applet {

	private static final File file = new File("../res/test.wav");
	private static final String path = "../res/sox-14.4.1/sox";
	private static final String pathOfOriginalFile = "../res/test.wav";
	private static final String pathOfNewFile = "../res/Extracted_Files/test";
	private ArrayList<String> paths = new ArrayList<String>();

	private static final String action = "trim";
	private int startTime = 0;
	private int count = 1;
	private int x = 1;

	public int numberOfMinutesInAudio() throws UnsupportedAudioFileException,
			IOException {
		AudioInputStream audioInputStream = AudioSystem
				.getAudioInputStream(file);
		AudioFormat format = audioInputStream.getFormat();

		long frames = audioInputStream.getFrameLength();
		double durationInSeconds = (frames + 0.0) / format.getFrameRate();
		double timeInMinutes = durationInSeconds / 60;
		int timeInMinutesForLoop = (int) (timeInMinutes + 1);
		return timeInMinutesForLoop;
	}

	public void extractAudioIntoFolder(int timeInMinutesForLoop)
			throws IOException {

		// Do process in memory (use buffers) -> write to disk

		for (int i = 0; i < timeInMinutesForLoop; i++) {
			String newPath = pathOfNewFile + x + ".wav";

			ProcessBuilder pb = new ProcessBuilder(path, pathOfOriginalFile,
					newPath, action, startTime + "", "5");

			paths.add(newPath);

			Process p = pb.start();

			startTime += 60;
			count++;
			x++;
		}

	}

	public void addBeep() throws UnsupportedAudioFileException, IOException {

		AudioInputStream clip1 = null;
		for (String path1 : paths) {
			
			AudioInputStream beep = AudioSystem.getAudioInputStream(new File("../res/beep.wav"));
			
			if (clip1 == null) {
				clip1 = AudioSystem.getAudioInputStream(new File(path1));
				AudioInputStream appended = new AudioInputStream(
						new SequenceInputStream(clip1, beep),
						clip1.getFormat(), clip1.getFrameLength()
								+ beep.getFrameLength());
				clip1 = appended;
				continue;
			}
			AudioInputStream clip2 = AudioSystem.getAudioInputStream(new File(path1));

			AudioInputStream appendedFiles = new AudioInputStream(
					new SequenceInputStream(clip2, beep), clip2.getFormat(),
					clip2.getFrameLength() + beep.getFrameLength());

			AudioInputStream all = new AudioInputStream(
					new SequenceInputStream(clip1, appendedFiles),
					clip1.getFormat(), clip1.getFrameLength()
							+ appendedFiles.getFrameLength());

			clip1 = all;
		}
		AudioSystem.write(clip1,AudioFileFormat.Type.WAVE,new File("../res/exported.wav"));

	}

	public static void main(String[] args)
			throws UnsupportedAudioFileException, IOException {
		
		BeepSeparator2 beepSeparator2 = new BeepSeparator2();

		int timeInMinutesForLoop = beepSeparator2.numberOfMinutesInAudio();
		
		beepSeparator2.extractAudioIntoFolder(timeInMinutesForLoop);
		beepSeparator2.addBeep();
		beepSeparator2.init();

	}

	public void init() {
		//System.out.println(System.getProperty("user.dir"));
		Button b = new Button("Play");
		b.setVisible(true);
		b.setSize(20, 20);

		b.addMouseListener(new PlayButton());
		add(b);
	}

	class PlayButton implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			AudioClip soundFile2 = getAudioClip(
					getDocumentBase(),
					"../res/exported.wav");
		
			soundFile2.play();
			System.out.println("BOOM!");
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

}
