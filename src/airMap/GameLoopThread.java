package airMap;

import java.io.IOException;

public class GameLoopThread extends Thread {
	private World world;

	public GameLoopThread(World world) {
		this.world = world;

	}

	@Override
	public void run() {
		try {
			while (true) {
				
				world.update();
				world.repaint();
				sleep(1000);

			}
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}

}