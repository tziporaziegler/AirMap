package airMap;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class PlaneNoise extends Thread {
	private AudioClip click;
	private boolean first;
	public PlaneNoise(boolean first){
		this.first=first;
	}

	@Override
	public void run() {
		if(first){
		try {
			sleep(10000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
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
		new PlaneNoise(false).start();
	}
}