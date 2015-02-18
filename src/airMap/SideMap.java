package airMap;

import java.awt.BorderLayout;
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

public class SideMap extends JPanel {
	private static final long serialVersionUID = 1L;
	private Image img;
	private int zoom;
	private String view;
	private String address;
	private String address2;

	// menu
	// TODO make feature menu work
	@SuppressWarnings("unused")
	private String feature;
	private JMenu viewOptions;
	private JMenuBar menu;
	private JMenu featuresOptions;
	private JButton zoomout;
	private JButton zoomin;

	public SideMap() throws IOException {
		setSize(300, 600);
		setLayout(new BorderLayout());
		setBorder(new BevelBorder(BevelBorder.LOWERED));

		// create and add zoom buttons
		zoomout = new JButton("-");
		zoomin = new JButton("+");
		zoomout.addActionListener(zoomoutListen);
		zoomin.addActionListener(zoominListen);

		zoom = 2; // 0-21 disable + button is more
		address = "USA";
		updateMap(address, address);
		// create menu bar
		featuresOptions = new JMenu("Features");
		menu = new JMenuBar();
		viewOptions = new JMenu("View");
		setUpMenu();
		add(menu, BorderLayout.NORTH);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			g.drawImage(img, 0, 0, 300, 600, null);
			g.drawImage(ImageIO.read(getClass().getResource("pics/airplane.jpg")), 150, 300, 20, 20, null);	
		}
		catch (IOException e) {
			e.printStackTrace();
		}
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
		view = "Hybrid";
		menu.add(viewOptions);
		menu.add(zoomout);
		menu.add(zoomin);
	}

	public void loadImg() throws MalformedURLException {
		String zooms = "";
		if (zoom != 0) {
			zooms = "&zoom=" + zoom;
		}
		URL url = new URL("https://maps.googleapis.com/maps/api/staticmap?size=300x600&path=color:0x0000ff|weight:5|"
				+ address + "|" + address2 + "&maptype=" + view + "&markers=size:mid%7Ccolor:red%7C" + address2 + "%7C"
				+ address + zooms);
		//FIXME should be in separate thread
		img = new ImageIcon(url).getImage();
		//new ImgDownloadThread(url, new JLabel()).start();
	}

	public void updateFeature(String feature) throws MalformedURLException {
		this.feature = feature.toLowerCase();
		loadImg();
	}

	public void updateView(String view) throws MalformedURLException {
		this.view = view.toLowerCase();
		loadImg();
	}

	public void updateMap(String address, String address2) throws IOException {
		this.address = address;
		this.address2 = address2;
		zoom = 0;
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
