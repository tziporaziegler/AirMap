package airMap;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class DingNoise extends Thread {
	private AudioClip click;

	@Override
	public void run() {
		URL urlClick = getClass().getResource("sound/ding.wav");
		click = Applet.newAudioClip(urlClick);
		click.play();
		try {
			sleep(2000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean running() {
		return isAlive();
	}

	public void stopMusic() {
		click.stop();
	}

	public static void main(String args[]) {
		new DingNoise().start();
	}
}
