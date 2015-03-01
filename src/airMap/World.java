package airMap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

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
	private JTextField location;
	private JTextField destination;

	// current address
	private String address;

	// destination address
	private String address2;

	// plane controls
	private int direction;
	private int speed;

	private double currentLat;
	private double currentLog;
	private double endLat;
	private double endLog;

	public World() throws IOException {
		setLayout(new BorderLayout());
		setSize(1000, 600);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("AirMap");

		// create window icon (only visible on mac when minimize window)
		setIconImage(ImageIO.read(getClass().getResource("pics/airplane.jpg")));

		addKeyListener(this);
		setFocusable(true);

		setUpMenu();
		// don't want setJMenuBar(menu); because by default it adds it to north
		add(menu, BorderLayout.SOUTH);

		direction = 2;
		speed = 69;

		// create the three panels and set up their location on the screen
		address = "USA";
		centerMap = new CenterMap(address);
		add(centerMap, BorderLayout.CENTER);

		sideMap = new SideMap();
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

		location = new JTextField("Departure");
		location.setColumns(20);
		location.setSelectedTextColor(Color.BLUE);
		destination = new JTextField("Destination");
		destination.setSelectedTextColor(Color.BLUE);
		destination.setColumns(20);
		location.addFocusListener(focus);
		destination.addFocusListener(focus);
		go = new JButton("Go!");
		go.addActionListener(click);

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
	
	public void setCurrLatLog(double lat, double log) {
		currentLat = lat;
		currentLog = log;
	}
	
	public void setEndLatLog(double lat, double log) {
		endLat = lat;
		endLog = log;
	}

	ActionListener click = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				setAddress(location.getText(), destination.getText());
				location.setText("Departure");
				destination.setText("Destination");
				weather.updateAll(address, address2);
				centerMap.updateMap(address);
				sideMap.newTrip(currentLat, currentLog, endLat, endLog);
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

	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		sideMap.paintComponent(g);
	}

	public void update() throws IOException {
		// TODO dotn want to update unless the destination changes or zoom
		// in/out
		// sideMap.updateMap(address, address2);
		// TODO send in instead lat, log / address of plane
		// centerMap.updateMap(address);
		// currWeather.update(address2);
		// centerMap.updateMap(direction,speed);
		sideMap.updateMap(speed, direction);
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
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	public void setDirection(int direction) {
		this.direction = direction;
		sideMap.setDirection(direction);

	}

	public int getDirection() {
		return direction;
	}

	FocusListener focus = new FocusListener() {
		@Override
		public void focusGained(FocusEvent e) {
			JTextField f = (JTextField) e.getSource();
			f.selectAll();
		}

		@Override
		public void focusLost(FocusEvent e) {

		}

	};
}