package airMap;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JComponent;

public class RainComponent extends JComponent {
	private static final long serialVersionUID = 1L;

	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		generateRain(g);
	}

	public void generateRain(Graphics g) {
		g.setColor(Color.BLACK);
		Random random = new Random();
		int count = 250;
		int x;
		int y;
		while (count > 0) {
			x = random.nextInt(200);
			y = random.nextInt(200);
			g.fillRect(x, y, 1, 3);
			count--;
		}
	}
}
