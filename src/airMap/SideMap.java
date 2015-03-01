package airMap;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JPanel;

public class SideMap extends JPanel {
	private static final long serialVersionUID = 1L;
	private int direction;

	private String address;
	private String address2;
	private double startlat;
	private double startlong;
	private PathMap pathMap;
	private NavigationMap navigationMap;

	public SideMap() throws IOException {
		setPreferredSize(new Dimension(300, 600));
		// TODO try out boxlayout
		setLayout(new GridLayout(2, 1));
		//setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		//TODO change to center of map coordinates
		startlat = 45.0;
		startlong = 45.0;
		address = "USA";

		pathMap = new PathMap();
		add(pathMap);
	
		direction = 2;
		navigationMap = new NavigationMap(startlat, startlong);
		// send in speed of 0 when initially create that map
		updateMap(0, direction);
	
		add(navigationMap);
		repaint();
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void drawMap(Graphics g) {
		pathMap.drawMap(g);
		navigationMap.drawMap(g);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		pathMap.drawMap(g);
		navigationMap.drawMap(g);
		navigationMap.drawPlane(g);
	}

	public void newTrip(double startlat, double startlong, double endlat, double endlong) throws MalformedURLException {
		pathMap.updateMap(startlat, startlong, endlat, endlong);
	}

	public void updateMap(int speed, int direction) throws IOException {
		//FIXME repaint navigation map
		navigationMap.update(speed, direction);
	}
}