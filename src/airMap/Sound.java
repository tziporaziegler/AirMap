package airMap;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class Sound extends Thread{

		private AudioClip click;
		private int seconds;
		private String filename;
		private boolean loop;
		
	
		public Sound( int seconds, String filename, boolean loop){
			this.seconds=seconds;
			this.filename=filename;
			this.loop=loop;
			URL urlClick = getClass().getResource(filename);
			click = Applet.newAudioClip(urlClick);
			System.out.println("new "+ filename);
		}
		@Override
		public void run() {
			try {
				sleep(seconds);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		
			if(!loop){
			click.play();
			}
			else{
				click.loop();
			}
		
		}

		public boolean running() {
			return this.isAlive();
		}

		public void stopMusic() {
			click.stop();
		}
}
