package airMap;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JPanel;

public class SideMap extends JPanel {
	private static final long serialVersionUID = 1L;
	private int direction;
	private final int width;
	private final int height;
	private final double startlat;
	private final double startlong;
	private final PathMap pathMap;
	private final NavigationMap navigationMap;

	public SideMap(double currentLat, double currentLong, int direction) throws IOException {
		width = 300;
		height = 300;
		setPreferredSize(new Dimension(width, height));
		// TODO try out boxlayout
		setLayout(new GridLayout(2, 1));
		// setBorder(new BevelBorder(BevelBorder.LOWERED));

		// TODO change to center of map coordinates
		startlat = currentLat;
		startlong = currentLong;

		pathMap = new PathMap();
		add(pathMap);

		this.direction = direction;
		navigationMap = new NavigationMap(startlat, startlong);
		navigationMap.setDegree(direction);
		// send in speed of 0 when initially create that map
		updateMap(69, direction, currentLat, currentLong);

		add(navigationMap);
	}

	public void setDirection(int direction) {
		this.direction = direction;
		navigationMap.setDegree(direction);
	}

	public void newTrip(double startlat, double startlong, double endlat, double endlong) throws MalformedURLException {
		pathMap.updateMap(startlat, startlong, endlat, endlong);
		navigationMap.newMap(startlat, startlong);
	}

	public void updateMap(int speed, int direction, double currentLat, double currentLong) throws IOException {
		// FIXME repaint navigation map
		navigationMap.update(speed, this.direction, currentLat, currentLong);
	}
}