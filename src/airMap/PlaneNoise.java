package airMap;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class PlaneNoise extends Thread {
	private AudioClip click;

	@Override
	public void run() {
		try {
			sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		URL urlClick = getClass().getResource("sound/airTraffic.wav");
		click = Applet.newAudioClip(urlClick);
		click.loop();
	}

	public boolean running() {
		return isAlive();
	}

	public void stopMusic() {
		click.stop();
	}

	public static void main(String args[]) {
		PlaneNoise music = new PlaneNoise();
		music.start();
	}
}