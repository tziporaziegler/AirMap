package airMap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class InstructionsComponent extends JComponent {
	private static final long serialVersionUID = 1L;
	private BufferedImage img;

	public InstructionsComponent() throws IOException {
		img = ImageIO.read(getClass().getResource("pics/inst4.jpg"));
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		g.setColor(Color.WHITE);

		// use variables to maintain consistent spacing
		int i = 15; // initial line location
		int j = 18; // line spacing
		int k = 30; // para spacing

		g.drawString("WELCOME TO AIRMAP 1.0", 280, i);

		g.drawString("Press the play button to begin flying.", 10, i += k - 4);
		g.drawString("You may pause and resume the game any time during the flight.", 10, i += j);
		g.drawString(
				"Enter the name of your departure and destination sites (this can be an address or any airport name). Then press go!",
				10, i += j);
		g.drawString("Navigate the plane from the departure site to your destination.", 10, i += j);
		g.drawString(
				"The plane's initial speed is set to 0. Like any plane, it will not start flying until the speed is increased.",
				10, i += k);
		g.drawString("The only thing that will move at speed 0 are the animated control dials!", 10, i += j);
		g.drawString("Use the top control panel to keep track of the current speed and lat/log.", 10, i += j);
		g.drawString("The plane will automatically land if you reach within range of the destination.", 10, i += j);
		g.drawString("After landing, you can either enter a new destination, or just fly and explore.", 10, i += j);
		g.drawString(
				"If you enter a new departure site and destination mid flight, the plane will relocate to the beginning of the new route.",
				10, i += j);

		g.drawString("Warning: Aliens have invaded the north and south pole. Any trespassers will be zapped!!!", 10,
				i += k);

		g.drawString("Controls:", 10, i += k);
		g.drawString("four arrow keys                     control the plane direction (or numbers/number pad)", 40,
				i += j);
		g.drawString("\u2190", 140, i);
		g.drawString("\u2191", 157, i - 10);
		g.drawString("\u2192", 170, i);
		g.drawString("\u2193", 157, i + 5);
		g.drawString("-/+ on keyboard increase and decrease speed", 40, i += j);

		g.drawString("check box toogles mute", 40, i += j);

		g.drawString("Additional Features:", 10, i += k);
		g.drawString("20 different zoom levels (-/+ buttons)", 40, i += j);
		g.drawString("view and features drop down menus allow map customization", 40, i += j);
		g.drawString("(these features can be accesses through memonomics - features options currently do nothing!)",
				60, i += j);
		g.drawString("Look for tooltips if you ever get stuck, or just click on the ? button.", 40, i += j);

		g.drawString("Upcoming in AirMap2.0:", 10, i += k);
		g.drawString("street view", 40, i += j);
		g.drawString("features menu that updates map", 40, i += j);
		g.drawString("airport names on markers", 40, i += j);
		g.drawString("ablitity to choose destination by clicking on an airport marker", 40, i += j);
		g.drawString("watch the weather (snow/rain) come alive on actual map", 40, i += j);
		g.drawString("crashing notification", 40, i += j);
	}
}
