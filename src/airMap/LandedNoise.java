package airMap;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class LandedNoise extends Thread {
	private AudioClip click;

	@Override
	public void run() {
		URL urlClick = getClass().getResource("sound/landed.wav");
		click = Applet.newAudioClip(urlClick);
		click.play();
	}

	public boolean running() {
		return isAlive();
	}

	public void stopMusic() {
		click.stop();
	}

	public static void main(String args[]) {
		new LandedNoise().start();
	}
}