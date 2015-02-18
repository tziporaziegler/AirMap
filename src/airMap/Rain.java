package airMap;

import java.awt.Color;
import javax.swing.JFrame;

public class Rain extends JFrame {
	private static final long serialVersionUID = 1L;

	public Rain() {
		setSize(200, 200);
		setBackground(Color.GRAY);
		RainComponent rain = new RainComponent();

		add(rain);

	}

	public static void main(String agrs[]) {
		Rain rain = new Rain();
		rain.setVisible(true);

		Thread t = new Thread() {

			public void run() {
				while (true) {

					rain.repaint();
					try {
						Thread.sleep(10);
					}
					catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		t.start();

	}

}
