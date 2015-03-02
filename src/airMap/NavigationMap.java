package airMap;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class NavigationMap extends JPanel {
	private static final long serialVersionUID = 1L;
	private int width;
	private int height;
	private Image planeImg;
	private Image mapImg;

	private String view;
	private JButton zoomout;
	private JButton zoomin;
	private int zoom;
	private double currentlat;
	private double currentlong;
	private Plane plane;

	private JMenuBar menu;
	@SuppressWarnings("unused")
	private String feature;
	private JMenu viewOptions;
	private JMenu featuresOptions;

	public NavigationMap(double startlat, double startlong) throws IOException {
		width = 300;
		height = 300;
		setPreferredSize(new Dimension(width, height));
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setLayout(new BorderLayout());
		currentlat = startlat;
		currentlong = startlong;

		view = "terrain";

		// create and add zoom buttons
		zoomout = new JButton("-");
		zoomin = new JButton("+");
		zoomout.addActionListener(zoomoutListen);
		zoomin.addActionListener(zoominListen);
		zoom = 7; // 0-21 disable + button is more

		featuresOptions = new JMenu("Features");
		menu = new JMenuBar();
		viewOptions = new JMenu("View");
		menu.add(zoomout);
		menu.add(zoomin);
		setUpMenu();
		add(menu, BorderLayout.NORTH);
		// TODO set plane location to start
		plane = new Plane(width / 2, height / 2);
		planeImg = ImageIO.read(getClass().getResource("pics/airplane.jpg"));

		// FIXME move to separate thread
		loadImg();
	}

	public void setUpMenu() {
		String[] viewNames = { "Satellite", "Roadmap", "Hybrid", "Terrain" };
		String[] featuresNames = { "Roads", "Landscape", "Transit", "transit.station.airports", "pio.school" };
		JCheckBoxMenuItem[] features = new JCheckBoxMenuItem[featuresNames.length];
		JMenuItem[] views = new JMenuItem[viewNames.length];
		featuresOptions.setToolTipText("Features to display");
		feature = "transit.station.airports";

		for (int i = 0; i < features.length; i++) {
			features[i] = new JCheckBoxMenuItem(featuresNames[i]);
			features[i].addActionListener(featuresView);
			featuresOptions.add(features[i]);
		}

		for (int i = 0; i < views.length; i++) {
			views[i] = new JMenuItem(viewNames[i]);
			views[i].setMnemonic(KeyEvent.VK_S);
			views[i].addActionListener(mapView);
			viewOptions.add(views[i]);
		}

		menu.add(viewOptions);
		menu.add(featuresOptions);
		// viewOptions.setSelectedIndex(2);
		viewOptions.setToolTipText("Map View");
		menu.add(viewOptions);

	}

	public void update(int speed, int direction) {
		movePlane(speed, direction);
	}

	public void movePlane(int speed, int direction) {
		switch (direction) {
			case 2: {
				plane.setY(plane.getY() + (speed / 69));
				break;
			}
			case 4: {
				plane.setX(plane.getX() - (speed / 69));
				break;
			}
			case 6: {
				plane.setX(plane.getX() + (speed / 69));
				break;
			}
			case 8: {
				plane.setY(plane.getY() - (speed / 69));
				break;
			}

		}
	}

	public void drawPlane(Graphics g) {
		g.drawImage(planeImg, plane.getX(), plane.getY(), 20, 20, null);
	}

	public void drawMap(Graphics g) {
		g.drawImage(mapImg, 0, 0, width, height, null);
	}

	public void newMap(double newLat, double newLog) throws MalformedURLException {
		currentlat = newLat;
		currentlong = newLog;
		loadImg();
	}

	public void paintComponent(Graphics g) {
		g.drawImage(mapImg, 0, 0, width, height, null);
		g.drawImage(planeImg, plane.getX(), plane.getY(), 20, 20, null);
	}

	public void loadImg() throws MalformedURLException {
		String zooms = "";
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
		URL url = new URL(adrhalf + airports + zooms+"&key=AIzaSyAirHEsA08agmW9uizDvXagTjWS3mRctPE");

		mapImg = new ImageIcon(url).getImage();
		// new ImgDownloadThread(url, new JLabel()).start();
	}

	public void updateFeature(String feature) throws MalformedURLException {
		this.feature = feature.toLowerCase();
		loadImg();
	}

	public void updateView(String view) throws MalformedURLException {
		this.view = view.toLowerCase();
		loadImg();
	}

	ActionListener zoominListen = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent event) {
			zoom++;
			if (zoom == 21) {
				zoomin.setEnabled(false);
			}
			if (!zoomout.isEnabled()) {
				zoomout.setEnabled(true);
			}

			try {
				loadImg();
			}
			catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	};
	ActionListener zoomoutListen = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent event) {
			zoom--;
			if (zoom == 1) {
				zoomout.setEnabled(false);
			}
			if (!zoomin.isEnabled()) {
				zoomin.setEnabled(true);
			}
			try {
				loadImg();
			}
			catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	};
	ActionListener featuresView = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
			String feature = (String) item.getText();
			try {
				updateFeature(feature);

			}
			catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
		}
	};

	ActionListener mapView = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem item = (JMenuItem) e.getSource();
			String view = (String) item.getText();
			try {
				updateView(view);
			}
			catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
		}
	};
}
