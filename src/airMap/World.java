package airMap;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class World extends JFrame implements KeyListener {
	private static final long serialVersionUID = 1L;
	private final GameLoopThread loop;
	protected WorldMenuBar menu;

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

		addKeyListener(this);
		setFocusable(true);

		loop = new GameLoopThread(this);
		menu = new WorldMenuBar(this, loop);
		// don't want setJMenuBar(menu); because by default it adds it to north
		add(menu, BorderLayout.SOUTH);

		direction = 4;
		speed = 69;

		// create the three panels and set up their location on the screen
		centerMap = new CenterMap(currentLat, currentLog);
		add(centerMap, BorderLayout.CENTER);
		sideMap = new SideMap(currentLat, currentLog, direction);
		add(sideMap, BorderLayout.WEST);
		weather = new WeatherCont(currentLat, currentLog);
		add(weather, BorderLayout.EAST);
		setVisible(true);
	}

	public void updateLatLog(double curLat, double curLog, double endLat, double endLog) throws IOException {
		currentLat = curLat;
		currentLog = curLog;
		weather.updateAll(currentLat, currentLog, endLat, endLog);
		sideMap.newTrip(currentLat, currentLog, endLat, endLog);
		centerMap.updateMap(0, 0, currentLat, currentLog);
	}

	public void update() throws IOException {
		//FIXME want to instead refocus after other listener is called
		requestFocus(true);
		
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
				currentLog -= difference;
				break;
			}
			case 6: {
				currentLog += difference;
				break;
			}
		}
		centerMap.updateMap(direction, difference, currentLat, currentLog);
		weather.updateCurrent(currentLat, currentLog);
		sideMap.updateMap(speed, direction, currentLat, currentLog);
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
				menu.play.toggle();
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