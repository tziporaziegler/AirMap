package airMap;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class LandingNoise extends Thread {
	private AudioClip click;
	
	@Override
	public void run() {
		URL urlClick = getClass().getResource("sound/landing.wav");
		click = Applet.newAudioClip(urlClick);
		click.play();
		try {
			sleep(7000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean running() {
		return this.isAlive();
	}

	public void stopMusic() {
		click.stop();
	}

	public static void main(String args[]) {
		new LandingNoise().start();
	}
}