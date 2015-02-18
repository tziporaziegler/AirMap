package airMap;

import java.awt.Color;

import javax.swing.JFrame;

public class Snow extends JFrame {
	private static final long serialVersionUID = 1L;

	public Snow() {
		setSize(200, 200);
		setBackground(Color.GRAY);
		SnowComponent snow = new SnowComponent();

		add(snow);
	}

	public static void main(String agrs[]) {
		Snow snow = new Snow();
		snow.setVisible(true);

		Thread t = new Thread() {

			public void run() {
				while (true) {

					snow.repaint();
					try {
						Thread.sleep(500);
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
