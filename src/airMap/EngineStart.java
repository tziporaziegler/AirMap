package airMap;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class EngineStart extends Thread {
	private AudioClip click;

	@Override
	public void run() {
		URL urlClick = getClass().getResource("EngineStart.wav");
		click = Applet.newAudioClip(urlClick);
		click.play();
	}

	public static void main(String args[]) {
		EngineStart engine = new EngineStart();
		engine.start();
	}
}