package airMap;

import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class AirMap {

	public static void main(String[] args) {
		try {
			/*try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}
			catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}*/
			new GameLoopThread(new World()).start();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}