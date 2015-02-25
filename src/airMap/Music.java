package airMap;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class Music extends Thread {

	private AudioClip click;

	@Override
	public void run() {

		URL urlClick = getClass().getResource("flight.wav");
		click = Applet.newAudioClip(urlClick);
		click.play();

	}

	public static void main(String args[]) {
		Music music = new Music();
		music.start();
	}
}
