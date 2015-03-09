package airMap;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JMenuBar;

public class NavigationMap extends Map {
	private static final long serialVersionUID = 1L;
	private double currentlat;
	private double currentlong;

	private Plane plane;

	private JMenuBar menu;
	@SuppressWarnings("unused")
	private String feature;
	private MenuZoom zoomPanel;
	private double diffBuffer;

	public NavigationMap(double startlat, double startlong) throws IOException {
		width = 300;
		height = 272;
		setPreferredSize(new Dimension(width, height));
		setLayout(new BorderLayout());
		currentlat = startlat;
		currentlong = startlong;

		// set up menu bar
		menu = new JMenuBar();
		view = "terrain";
		menu.add(new MenuView(this));
		feature = "transit.station.airports";
		menu.add(new MenuFeatures(this));
		menu.add(Box.createHorizontalStrut(95));
		zoomPanel = new MenuZoom(this, 5);
		menu.add(zoomPanel);
		add(menu, BorderLayout.NORTH);

		plane = new Plane(width / 2, height/ 2-10);

		diffBuffer = 0;

		img = ImageIO.read(getClass().getResource("pics/navigationMap.png"));
	}

	public void update(int speed, int direction, double currentlat, double currentlong) throws MalformedURLException {
		movePlane(speed, direction, currentlat, currentlong);
	}

	public void movePlane(int speed, int direction, double currentlat, double currentlong) throws MalformedURLException {
		double pixelPerLong = (width * (Math.pow(2, (zoomPanel.zoom - 1))) / 360);

		int difference;
		//System.out.println(count++);
		double diff = ((speed / 69.0) * pixelPerLong);
		if (diffBuffer != 0) {
			diff += diffBuffer;
			diffBuffer = 0;
		}
		if (diff % 1 != 0) {
			diffBuffer = diff % 1;
			difference = (int) diff;
		}
		else {
			difference = (int) diff;
		}

		switch (direction) {
			case 2: {
				plane.changeY(difference);
				break;
			}
			case 4: {
				plane.changeX(-difference);
				break;
			}
			case 6: {
				plane.changeX(difference);
				break;
			}
			case 8: {
				plane.changeY(-difference);
				break;
			}
		}

		int x = plane.getX();
		int y = plane.getY();

		if (x <= 0 || x >= width || y <= 0 || y >= height) {
			this.currentlat = currentlat;
			this.currentlong = currentlong;
			
			plane.reset();
			loadImg();
		}
	}

	public void setDegree(int direction) {
		plane.setDegree(direction);
	}

	public void newMap(double newLat, double newLog) throws MalformedURLException {
		currentlat = newLat;
		currentlong = newLog;
		plane.reset();
		loadImg();
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, width, height, null);
		plane.paintComponent(g);
	}

	public void loadImg() throws MalformedURLException {
		String zooms = "";
		int zoom = zoomPanel.zoom;
		if (zoom != 0) {
			zooms = "&zoom=" + zoom;
		}

		String adrhalf = "https://maps.googleapis.com/maps/api/staticmap?center=" + currentlat + "," + currentlong
				+ "&size=" + width + "x" + height + "&maptype=" + view;

		String airports = "&markers=size:mid%7Ccolor:green%7C" + "atl+airport" + "%7C" + "anc+airport" + "%7C"
				+ "aus+airport" + "%7C" + "bwi+airport" + "%7C" + "bos+airport" + "%7C" + "clt+airport" + "%7C"
				+ "mdw+airport" + "%7C" + "ord+airport" + "%7C" + "cvg+airport" + "%7C" + "cle+airport" + "%7C"
				+ "cmh+airport" + "%7C" + "dfw+airport" + "%7C" + "den+airport" + "%7C" + "dtw+airport" + "%7C"
				+ "fll+airport" + "%7C" + "rsw+airport" + "%7C" + "bdl+airport" + "%7C" + "hnl+airport" + "%7C"
				+ "iah+airport" + "%7C" + "hou+airport" + "%7C" + "ind+airport" + "%7C" + "mci+airport" + "%7C"
				+ "las+airport" + "%7C" + "lax+airport" + "%7C" + "mem+airport" + "%7C" + "mia+airport" + "%7C"
				+ "msp+airport" + "%7C" + "bna+airport" + "%7C" + "msy+airport" + "%7C" + "jfk+airport" + "%7C"
				+ "ont+airport" + "%7C" + "lga+airport" + "%7C" + "ewr+airport" + "%7C" + "oak+airport" + "%7C"
				+ "mco+airport" + "%7C" + "phl+airport" + "%7C" + "phx+airport" + "%7C" + "pit+airport" + "%7C"
				+ "pdx+airport" + "%7C" + "rdu+airport" + "%7C" + "smf+airport" + "%7C" + "slc+airport" + "%7C"
				+ "sat+airport" + "%7C" + "san+airport" + "%7C" + "sfo+airport" + "%7C" + "sjc+airport" + "%7C"
				+ "sna+airport" + "%7C" + "sea+airport" + "%7C" + "stl+airport" + "%7C" + "tpa+airport" + "%7C"
				+ "iad+airport" + "%7C" + "dca+airport" + "%7C";

		// URL url = new URL(adrhalf + airports);
		URL url = new URL(adrhalf + airports + zooms + "&key=AIzaSyAirHEsA08agmW9uizDvXagTjWS3mRctPE");

		new ImgDownloadThread(url, this).start();
	}

	public void updateFeature(String feature) throws MalformedURLException {
		this.feature = feature.toLowerCase();
		loadImg();
	}

	public void updateView(String view) throws MalformedURLException {
		this.view = view.toLowerCase();
		loadImg();
	}
}
