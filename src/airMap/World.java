package airMap;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class World extends JFrame implements KeyListener {
	private static final long serialVersionUID = 1L;

	// three panels
	private SideMap sideMap;
	private WeatherCont weather;
	private CenterMap centerMap;

	// menu
	private JMenu viewOptions;
	private JMenuBar menu;
	private JMenu featuresOptions;
	private JButton go;
	private TextField location;
	private TextField destination;

	// current address
	private String address;
	private double currentLat;
	private double currentLong;

	// destination address
	private String address2;
	private double endLat;
	private double endLong;

	// plane controls
	private int direction;
	private int speed;

	public World() throws IOException {
		setLayout(new BorderLayout());
		setSize(1000, 600);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("AirMap");
		
		currentLat = 40.633785;
		currentLong = -73.779277;
		
		// create window icon (only visible on mac when minimize window)
		setIconImage(ImageIO.read(getClass().getResource("pics/airplane.jpg")));

		addKeyListener(this);
		setFocusable(true);

		setUpMenu();
		// don't want setJMenuBar(menu); because by default it adds it to north
		add(menu, BorderLayout.SOUTH);

		direction = 4;
		speed = 69;

		// create the three panels and set up their location on the screen
		centerMap = new CenterMap(currentLat, currentLong);
		add(centerMap, BorderLayout.CENTER);

		sideMap = new SideMap(currentLat, currentLong, direction);
		add(sideMap, BorderLayout.WEST);

		weather = new WeatherCont();
		add(weather, BorderLayout.EAST);

		setVisible(true);
	}

	public void setUpMenu() {
		menu = new JMenuBar();
		featuresOptions = new JMenu("Options1");
		viewOptions = new JMenu("Options2");

		String[] viewNames = { "A", "B", "C", "D" };
		String[] featuresNames = { "A", "B", "C", "D", "E" };
		JCheckBoxMenuItem[] features = new JCheckBoxMenuItem[5];
		JMenuItem[] views = new JMenuItem[4];
		featuresOptions.setToolTipText("Features to display");

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

		go = new JButton("Go!");
		go.addActionListener(click);
		location = new TextField("Departure", this);
		destination = new TextField("Destination", this);
		menu.add(location);
		menu.add(destination);
		menu.add(go);
	}

	public void setAddress(String address, String address2) throws UnsupportedEncodingException {
		if (address.equals("Departure")) {
			address = null;
		}
		if (address2.equals("Destination")) {
			address2 = null;
		}

		this.address = URLEncoder.encode(address, "UTF-8");
		new AddressThread(this, this.address, 1).start();
		this.address2 = URLEncoder.encode(address2, "UTF-8");
		new AddressThread(this, this.address2, 2).start();
	}

	public void setCurrLatLog(double lat, double log) throws MalformedURLException {
		currentLat = lat;
		currentLong = log;
		centerMap.updateMap(0, 0, currentLat, currentLong);
	}

	public void setEndLatLog(double lat, double log) throws IOException {
		endLat = lat;
		endLong = log;
		// TODO change to lat and log instead of address
		weather.updateAll(address, address2);
		sideMap.newTrip(currentLat, currentLong, endLat, endLong);
		centerMap.updateMap(0, 0, currentLat, currentLong);
	}

	public void gobutton() throws IOException {
		setAddress(location.getText(), destination.getText());
		location.reset();
		destination.reset();
	}

	ActionListener click = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				gobutton();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	// FIXME unused
	ActionListener featuresView = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
			String feature = (String) item.getText();
			feature.toLowerCase();
		}
	};

	// FIXME unused
	ActionListener mapView = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem item = (JMenuItem) e.getSource();
			String view = (String) item.getText();
			view.toLowerCase();
		}
	};

	public void update() throws IOException {
		double difference = speed / 69.0;
		switch (direction) {
			case 8: {
				currentLat += difference;
				break;
			}
			case 2: {
				currentLat -= difference;
				break;
			}
			case 4: {
				currentLong -= difference;
				break;
			}
			case 6: {
				currentLong += difference;
				break;
			}
		}
		centerMap.updateMap(direction, difference, currentLat, currentLong);
		// weather.updateCurrent(currentLat, currentLong);
		sideMap.updateMap(speed, direction);
	}

	public void setDirection(int direction) {
		this.direction = direction;
		sideMap.setDirection(direction);
	}

	public int getDirection() {
		return direction;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_8:
				setDirection(8);
			break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_2:
				setDirection(2);
			break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_4:
				setDirection(4);
			break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_6:
				setDirection(6);
			break;
			case KeyEvent.VK_P:
			// loop.togglePause();
			break;
			case KeyEvent.VK_Q:
				System.exit(0);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}