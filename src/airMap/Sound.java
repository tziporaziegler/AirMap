package airMap;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

public class Sound extends Thread {
	private AudioClip click;
	private int seconds;
	private boolean loop;
	private CountDownLatch latch;

	public Sound(CountDownLatch latch, int seconds, String filename, boolean loop) {
		this.seconds = seconds;
		this.loop = loop;
		this.latch = latch;
		URL urlClick = getClass().getResource(filename);
		click = Applet.newAudioClip(urlClick);
	}

	@Override
	public void run() {
		if (loop) {
			click.loop();
		}
		else {
			click.play();
		}

		try {
			sleep(seconds);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		latch.countDown();
	}

	public void stopMusic() {
		click.stop();
	}
}
