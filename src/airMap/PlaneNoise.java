package airMap;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class PlaneNoise extends Thread {
	private AudioClip click;

	@Override
	public void run() {

		URL urlClick = getClass().getResource("sound/flight.wav");
		click = Applet.newAudioClip(urlClick);

		click.loop();

	}

	public boolean running() {
		return this.isAlive();
	}

	public void stopMusic() {
		click.stop();
	}

	public static void main(String args[]) {
		PlaneNoise music = new PlaneNoise();
		music.start();
	}
}