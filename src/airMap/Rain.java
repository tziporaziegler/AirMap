package airMap;

import java.awt.Color;
import javax.swing.JFrame;

public class Rain extends JFrame {
	private static final long serialVersionUID = 1L;

	public Rain() {
		setSize(200, 200);
		setBackground(Color.GRAY);
		add(new RainComponent());
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
						e.printStackTrace();
					}
				}
			}
		};
		
		t.start();
	}
}