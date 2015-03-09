package airMap;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class Cockpit extends Thread {
	private AudioClip click;

	@Override
	public void run() {
		URL urlClick = getClass().getResource("sound/seat.wav");
		click = Applet.newAudioClip(urlClick);
		click.play();
	}

	public boolean running() {
		return this.isAlive();
	}

	public void stopMusic() {
		click.stop();
	}

	public static void main(String args[]) {
		new Cockpit().start();
	}
}