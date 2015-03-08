package airMap;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

public class World extends JFrame {
	private static final long serialVersionUID = 1L;

	protected WorldMenuBar menu;

	private final static int UP = 8;
	private final static int DOWN = 2;
	private final static int RIGHT = 6;
	private final static int LEFT = 4;

	// three panels
	private final SideMap sideMap;
	private final WeatherCont weather;
	private final CenterMap centerMap;

	// current address
	private double currentLat;
	private double currentLog;

	// plane controls
	private int direction;
	private int speed;

	private boolean sound;
	private boolean paused;
	private Cockpit cockpit;
	private PlaneNoise noise;

	public World() throws IOException {
		setLayout(new BorderLayout());
		setSize(1000, 600);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("AirMap");

		currentLat = 40.633785;
		currentLog = -73.779277;

		// create window icon (only visible on mac when minimize window)
		setIconImage(ImageIO.read(getClass().getResource("pics/airplane.jpg")));
		setFocusable(true);

		menu = new WorldMenuBar(this);
		// don't want setJMenuBar(menu); because by default it adds it to north
		add(menu, BorderLayout.SOUTH);

		direction = LEFT;
		speed = 69;
		sound = true;

		paused = true;

		// create the three panels and set up their location on the screen
		centerMap = new CenterMap(currentLat, currentLog);
		add(centerMap, BorderLayout.CENTER);
		sideMap = new SideMap(currentLat, currentLog, direction);
		add(sideMap, BorderLayout.WEST);
		weather = new WeatherCont(currentLat, currentLog);
		add(weather, BorderLayout.EAST);
		setUpKeyBindings();

		setVisible(true);
		cockpit = new Cockpit();
		cockpit.start();
		noise = new PlaneNoise();
		noise.start();
	}

	public void updateLatLog(double curLat, double curLog, double endLat, double endLog) throws IOException {
		currentLat = curLat;
		currentLog = curLog;
		weather.updateAll(currentLat, currentLog, endLat, endLog);
		sideMap.newTrip(currentLat, currentLog, endLat, endLog);
		centerMap.updateMap(0, 0, currentLat, currentLog);
	}

	public void setUpKeyBindings() {
		InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = getRootPane().getActionMap();
		// inputMap.put(KeyStroke.getKeyStroke("P"), "togglePause");
		inputMap.put(KeyStroke.getKeyStroke("8"), "directionUp");
		inputMap.put(KeyStroke.getKeyStroke("2"), "directionDown");
		inputMap.put(KeyStroke.getKeyStroke("4"), "directionLeft");
		inputMap.put(KeyStroke.getKeyStroke("6"), "directionRight");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8, 0), "directionUp");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, 0), "directionDown");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0), "directionLeft");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, 0), "directionRight");
		inputMap.put(KeyStroke.getKeyStroke("UP"), "directionUp");
		inputMap.put(KeyStroke.getKeyStroke("DOWN"), "directionDown");
		inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "directionRight");
		inputMap.put(KeyStroke.getKeyStroke("LEFT"), "directionLeft");
		actionMap.put("directionUp", new Direction(UP));
		actionMap.put("directionDown", new Direction(DOWN));
		actionMap.put("directionRight", new Direction(RIGHT));
		actionMap.put("directionLeft", new Direction(LEFT));
		// actionMap.put("togglePause", new PauseAction());
	}

	public void togglePlay() {
		if (paused) {
			paused = false;
		}
		else {
			paused = true;
		}
		menu.togglePauseText();
	}

	public void update() throws IOException {
		if (!paused) {
			double difference = speed / 69.0;
			switch (direction) {
				case UP: {
					currentLat += difference;
					break;
				}
				case DOWN: {
					currentLat -= difference;
					break;
				}
				case LEFT: {
					currentLog -= difference;
					break;
				}
				case RIGHT: {
					currentLog += difference;
					break;
				}
			}
			centerMap.updateMap(direction, difference, currentLat, currentLog);
			weather.updateCurrent(currentLat, currentLog);
			sideMap.updateMap(speed, direction, currentLat, currentLog);
		}

		repaint();
	}

	public void setDirection(int direction) {
		this.direction = direction;
		sideMap.setDirection(direction);
	}

	public void toggleMute() {
		if (sound) {
			cockpit.stopMusic();
			// noise.stopMusic();
			sound = false;
		}
		else {
			sound = true;
			noise = new PlaneNoise();
			// noise.start();
		}
	}

	private class PauseAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			togglePlay();
		}
	};

	private class Direction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private int direction;

		public Direction(int direction) {
			this.direction = direction;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			setDirection(direction);
		}
	};
}