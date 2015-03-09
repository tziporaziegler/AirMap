package airMap;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.MalformedURLException;

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

	private final static int LANDINGSPEED = 1;
	private final static int UP = 8;
	private final static int DOWN = 2;
	private final static int RIGHT = 6;
	private final static int LEFT = 4;
	private static final int INCREASE = 3;
	private static final int DECREASE = -3;

	// three panels
	private final SideMap sideMap;
	private final WeatherCont weather;
	private final CenterMap centerMap;

	// current address
	private double currentLat;
	private double currentLog;
	private double destinationLat;
	private double destinationLog;
	// plane controls
	private int direction;
	private int speed;

	private boolean sound;
	private boolean paused;
	private Cockpit cockpit;
	private PlaneNoise noise;
	private LandingNoise landingNoise;
	private LandedNoise landed;
	private boolean landing;

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
		speed = 50;
		sound = true;
		landing = false;
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

	public void updateLatLog(double curLat, double curLog, double endLat,
			double endLog) throws IOException {
		currentLat = curLat;
		currentLog = curLog;
		destinationLat = endLat;
		destinationLog = endLog;
		weather.updateAll(currentLat, currentLog, endLat, endLog);
		sideMap.newTrip(currentLat, currentLog, endLat, endLog);
		centerMap.updateMap(speed, 0, 0, currentLat, currentLog);
	}

	public void setUpKeyBindings() {
		InputMap inputMap = getRootPane().getInputMap(
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = getRootPane().getActionMap();
		// inputMap.put(KeyStroke.getKeyStroke("P"), "togglePause");
		inputMap.put(KeyStroke.getKeyStroke("8"), "directionUp");
		inputMap.put(KeyStroke.getKeyStroke("2"), "directionDown");
		inputMap.put(KeyStroke.getKeyStroke("4"), "directionLeft");
		inputMap.put(KeyStroke.getKeyStroke("6"), "directionRight");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8, 0),
				"directionUp");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, 0),
				"directionDown");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0),
				"directionLeft");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, 0),
				"directionRight");
		inputMap.put(KeyStroke.getKeyStroke("UP"), "directionUp");
		inputMap.put(KeyStroke.getKeyStroke("DOWN"), "directionDown");
		inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "directionRight");
		inputMap.put(KeyStroke.getKeyStroke("LEFT"), "directionLeft");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, 0), "speedPlus");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, 0), "speedPlus");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "speedMinus");
		actionMap.put("speedPlus", new Speed(INCREASE));
		actionMap.put("speedMinus", new Speed(DECREASE));
		actionMap.put("directionUp", new Direction(UP));
		actionMap.put("directionDown", new Direction(DOWN));
		actionMap.put("directionRight", new Direction(RIGHT));
		actionMap.put("directionLeft", new Direction(LEFT));
		// actionMap.put("togglePause", new PauseAction());
	}

	public void togglePlay() {
		if (paused) {
			paused = false;
			landing = false;
		} else {
			paused = true;
		}
		menu.togglePauseText();

	}

	public void adjustSpeed(int adjust) {

		speed += adjust;
		if (speed < 0) {
			speed = 0;
		} else if (speed > 69) {
			speed = 69;
		}
		System.out.println(speed);
	}

	public void update() throws IOException, InterruptedException {

		if (!paused & !landing) {

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
			centerMap.updateMap(speed, direction, difference, currentLat,
					currentLog);
			weather.updateCurrent(currentLat, currentLog);
			sideMap.updateMap(speed, direction, currentLat, currentLog);
			reachDestination();
		} else if (landing) {
			centerMap
					.updateMap(0, direction, 0, destinationLat, destinationLog);
			weather.updateCurrent(destinationLat, destinationLog);
			sideMap.landPlane(destinationLat, destinationLog);
		}

		repaint();
	}

	public void setDirection(int direction) {
		this.direction = direction;
		sideMap.setDirection(direction);
	}

	public int getDirection() {
		return direction;
	}

	public void reachDestination() throws IOException, InterruptedException {
		System.out
				.println("lat diff: " + Math.abs(currentLat - destinationLat));
		System.out
				.println("log diff: " + Math.abs(currentLog - destinationLog));

		if (sound
				&& (Math.abs(currentLat - destinationLat) <= .1 && Math
						.abs(currentLog - destinationLog) <= .1)) {

			// landingMode();
			DingNoise ding = new DingNoise();
			ding.start();
			landing = true;
			landingNoise = new LandingNoise();
			landingNoise.start();
			landPlane();
		}
	}

	public void landPlane() throws IOException, InterruptedException {

		togglePlay();
		if (sound) {
			System.out.println("land");
			landingNoise.join();
			System.out.println("new noise");
			landed = new LandedNoise();
			landed.start();

		}

	}

	public void landingMode() throws IOException {
		System.out.println("landing mode");
		System.out
				.println("lat diff: " + Math.abs(currentLat - destinationLat));
		System.out
				.println("log diff: " + Math.abs(currentLog - destinationLog));
		landing = true;
		double difference = LANDINGSPEED / 69.0;
		if (Math.abs(destinationLat - currentLat) < .009
				&& Math.abs(destinationLog - currentLog) > .009) {
			togglePlay();
			landing = false;

		} else if (destinationLat > currentLat) {

			currentLat += difference;
			sideMap.updateMap(LANDINGSPEED, UP, currentLat, currentLog);
		} else if (destinationLat < currentLat) {

			currentLat -= difference;
			sideMap.updateMap(LANDINGSPEED, DOWN, currentLat, currentLog);
		} else if (destinationLog < currentLog) {
			currentLog -= difference;
			sideMap.updateMap(LANDINGSPEED, LEFT, currentLat, currentLog);
		} else if (destinationLog > currentLog) {
			currentLog += difference;
			sideMap.updateMap(LANDINGSPEED, RIGHT, currentLat, currentLog);
		}

	}

	public void toggleMute() {
		if (sound) {
			cockpit.stopMusic();
			noise.stopMusic();
			landed.stopMusic();
			landingNoise.stopMusic();
			sound = false;
		} else {
			sound = true;
			noise = new PlaneNoise();
			noise.start();
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

	private class Speed extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private int speed;

		public Speed(int speed) {
			this.speed = speed;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			adjustSpeed(speed);
		}
	};
}
