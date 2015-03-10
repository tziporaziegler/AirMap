package airMap;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

public class Sound extends Thread{

		private AudioClip click;
		private int seconds;
		private boolean loop;
		private CountDownLatch latch;
		
	
		public Sound(CountDownLatch latch, int seconds, String filename, boolean loop){
			this.seconds=seconds;
			this.loop=loop;
			this.latch=latch;
			URL urlClick = getClass().getResource(filename);
			click = Applet.newAudioClip(urlClick);
		}
		@Override
		public void run() {
			
		
			if(!loop){
			click.play();
			}
			else{
				click.loop();
			}
			try {
				sleep(seconds);
			}
			catch (InterruptedException e) {
				
			}
		latch.countDown();
		System.out.println("finished");
		}

		public boolean running() {
			return this.isAlive();
		}

		public void stopMusic() {
			click.stop();
		}
}
