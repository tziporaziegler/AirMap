package animatedWeather;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JComponent;

public class SnowComponent extends JComponent {
	private static final long serialVersionUID = 1L;

	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		generateSnow(g);
	}

	public void generateSnow(Graphics g) {
		g.setColor(Color.WHITE);
		Random random = new Random();
		int count = 250;
		int x;
		int y;
		int d;
		while (count > 0) {
			x = random.nextInt(200);
			y = random.nextInt(200);
			d = random.nextInt(4);
			g.fillOval(x, y, d, d);
			count--;
		}
	}
}