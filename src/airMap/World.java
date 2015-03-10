package airMap;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

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

	// game state controls
	private boolean sound;
	private boolean paused;
	private boolean landing;
	private boolean autoLand;

	// sound
	private Sound cockpit;
	private Sound planeNoise;
	private Sound landingNoise;
	private Sound landed;

	// latch used to play audio clips in order
	private CountDownLatch latch;

	public World() throws IOException, InterruptedException {
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

		// default speed and direction
		direction = LEFT;
		speed = 0;

		// game state controls
		autoLand = false;
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

		latch = new CountDownLatch(1);
		// start sound
		cockpit = new Sound(latch, 10000, "sound/seat.wav", false);
		cockpit.start();
		latch.await();
		planeNoise = new Sound(latch, 0, "sound/airTraffic.wav", true);
		planeNoise.start();
	}

	public void updateLatLog(double curLat, double curLog, double endLat, double endLog) throws IOException {
		currentLat = curLat;
		currentLog = curLog;
		destinationLat = endLat;
		destinationLog = endLog;
		weather.updateAll(currentLat, currentLog, endLat, endLog);
		sideMap.newTrip(currentLat, currentLog, endLat, endLog);
		centerMap.updateMap(speed, 0, 0, currentLat, currentLog, false);
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
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, 0), "speedPlus");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, 0), "speedPlus");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "speedMinus");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0), "speedPlus");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), "speedMinus");

		actionMap.put("speedPlus", new Speed(INCREASE));
		actionMap.put("speedMinus", new Speed(DECREASE));
		actionMap.put("directionUp", new Direction(UP));
		actionMap.put("directionDown", new Direction(DOWN));
		actionMap.put("directionRight", new Direction(RIGHT));
		actionMap.put("directionLeft", new Direction(LEFT));
		// actionMap.put("togglePause", new PauseAction());
	}

	// setters & getters
	public void setAutoLand() {
		autoLand = true;
	}

	public void setDirection(int direction) {
		this.direction = direction;
		sideMap.setDirection(direction);
	}

	public int getDirection() {
		return direction;
	}

	public void adjustSpeed(int adjust) {
		speed += adjust;
		if (speed < 0) {
			speed = 0;
		}
		// 1 degree lat = 69 miles
		// 69 miles per hour = 1 degree lat per hour
		else if (speed > 69) {
			speed = 69;
		}

	}

	public void update() throws IOException, InterruptedException {
		if (!paused & !landing) {
			// determine the change in speed
			// 1 degree lat = 69 miles
			// 69 miles per hour = 1 degree lat per hour
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

			// update all panels
			centerMap.updateMap(speed, direction, difference, currentLat, currentLog, false);
			weather.updateCurrent(currentLat, currentLog);
			sideMap.updateMap(speed, direction, currentLat, currentLog);

			// determine if reached destination
			if (autoLand && speed > 0) {
				reachDestination();
			}

		}
		repaint();
	}

	public void reachDestination() throws IOException, InterruptedException {
		if (Math.abs(currentLat - destinationLat) <= .15 && Math.abs(currentLog - destinationLog) <= .15) {
			centerMap.updateMap(0, direction, 0, destinationLat, destinationLog, true);
			weather.updateCurrent(destinationLat, destinationLog);
			sideMap.landPlane(destinationLat, destinationLog);
			repaint();
			if (planeNoise != null) {
				planeNoise.stopMusic();
			}
			if (sound) {
				latch = new CountDownLatch(1);
				Sound ding = new Sound(latch, 2000, "sound/ding.wav", false);
				ding.start();
				latch.await();
				latch = new CountDownLatch(1);
				landingNoise = new Sound(latch, 7000, "sound/landing.wav", false);
				landingNoise.start();
			}
			landPlane();
		}
	}

	public void landPlane() throws IOException, InterruptedException {
		autoLand = false;

		// when land, plane stops and so the speed becomes 0
		speed = 0;

		// show that in process of landing the plane
		landing = true;

		// play the sequence of landing sound tracks
		if (sound) {
			latch.await();
			latch = new CountDownLatch(1);
			if (sound) {
				landed = new Sound(latch, 7000, "sound/landed.wav", false);
				landed.start();
				latch.await();
			}
			if (sound) {
				latch = new CountDownLatch(1);
				cockpit = new Sound(latch, 10000, "sound/seat.wav", false);
				cockpit.start();
				latch.await();
			}
			if (sound) {
				planeNoise = new Sound(latch, 0, "sound/airTraffic.wav", true);
				planeNoise.start();
			}
		}

		// when landing sound tracks finish, the land is complete
		// now user can fly the plane again by increasing the speed
		landing = false;
	}

	public void togglePlay() throws InterruptedException {
		if (paused) {
			paused = false;
		}
		else {
			paused = true;
		}
		menu.togglePauseText();
	}

	public void toggleMute() {
		if (sound) {
			cockpit.stopMusic();
			if (planeNoise != null) {
				planeNoise.stopMusic();
			}
			if (landed != null) {
				landed.stopMusic();
			}
			if (landingNoise != null) {
				landingNoise.stopMusic();
			}
			this.sound = false;
		}
		else {
			sound = true;
			planeNoise = new Sound(latch, 0, "sound/airTraffic.wav", true);
			planeNoise.start();
		}
	}

	// pause action
	@SuppressWarnings("unused")
	private class PauseAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				togglePlay();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

	// direction action
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

	// speed Action
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