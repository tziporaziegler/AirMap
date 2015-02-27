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
import javax.swing.BoxLayout;
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
	private Plane plane;
	private int direction;
	private int zoom;
	private String view;
	private String address;
	private String address2;
	
	private JPanel center;
	private PathMap pathMap;
	private NavigationMap navigationMap;

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

		setPreferredSize(new Dimension(250, 600));
		setLayout(new BorderLayout());
		setBorder(new BevelBorder(BevelBorder.LOWERED));

		// create and add zoom buttons
		zoomout = new JButton("-");
		zoomin = new JButton("+");
		zoomout.addActionListener(zoomoutListen);
		zoomin.addActionListener(zoominListen);

		zoom = 2; // 0-21 disable + button is more
		view = "terrain";
		address = "USA";
		updateMap(address, address);
		// create menu bar
		featuresOptions = new JMenu("Features");
		menu = new JMenuBar();
		viewOptions = new JMenu("View");
		setUpMenu();
		add(menu, BorderLayout.NORTH);
		
		//panels
		center=new JPanel();
	
		add(center,BorderLayout.CENTER);
		navigationMap=new NavigationMap();
		pathMap=new PathMap();
		center.add(pathMap);
		plane=new Plane(200,200);
		direction=2;
		
	}
	public void movePlane(int directions, int speed){
		switch(direction){
		case 2:{
			plane.setY(plane.getY()+(speed/100));
			break;
		}
		case 4:{
			plane.setX(plane.getX()-(speed/100));
			break;
		}
		case 6:{
			plane.setX(plane.getX()+(speed/100));
			break;
		}
		case 8:{
			plane.setY(plane.getY()-(speed/100));
			break;
		}
		}
	}
	public void setDirection(int direction){
		this.direction=direction;
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
			g.drawRect(20,20,20,20);
			pathMap.drawMap(g);
			//g.drawImage(img, 0, 0, 300, 600, null);
			//g.drawImage(ImageIO.read(getClass().getResource("pics/airplane.jpg")), plane.getX(), plane.getY(), 20, 20, null);
		
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
		menu.add(zoomout);
		menu.add(zoomin);
	}

	public void loadImg() throws MalformedURLException {
		String zooms = "";
		if (zoom != 0) {
			zooms = "&zoom=" + zoom;
		}

		String adrhalf = "https://maps.googleapis.com/maps/api/staticmap?size=300x600&path=color:0x0000ff|weight:5|"
				+ address + "|" + address2 + "&maptype=" + view + "&markers=size:mid%7Ccolor:red%7C" + address2 + "%7C"
				+ address;

		// TODO add markers - view should only focus on red markers
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

		// URL url = new URL(adrhalf + airports + zooms);
		URL url = new URL(adrhalf + zooms);
		// FIXME should be in separate thread
		
		img = new ImageIcon(url).getImage();
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
