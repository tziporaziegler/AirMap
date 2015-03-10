package airMap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.Format;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;

public class CenterMap extends Map {
	private static final long serialVersionUID = 1L;
	private double currentLat;
	private double currentLog;
	private int speed;
	private boolean autoLand;

	// format used to display lat/log
	private Format formatter;

	// menu
	private final JMenuBar menu;
	private final MenuZoom zoomPanel;

	// images
	private final BufferedImage controlImg;
	private final Image gaugesImg;
	private final Image radarImg;
	private final Image gauges1Img;
	private final Image gauges2Img;
	private final Image alien;

	// fonts
	private Font latLogFont;
	private Font speedFont;
	private Font alienFont;
	private Font landFont;

	public CenterMap(double currentlat, double currentlong) throws IOException {
		width = 600;
		height = 600;
		setPreferredSize(new Dimension(width, height));
		setLayout(new BorderLayout());
		view = "satellite";

		// create menu
		menu = new JMenuBar();
		menu.add(new MenuView(this));
		// add space to menu bar so zoom buttons move to right side of menus
		menu.add(Box.createHorizontalStrut(304));
		zoomPanel = new MenuZoom(this, 5);
		menu.add(zoomPanel);
		add(menu, BorderLayout.NORTH);
		autoLand = false;

		formatter = new DecimalFormat("#0.###");

		this.currentLat = currentlat;
		this.currentLog = currentlong;

		// load initial centerMap image - since initial lat and log is hardcoded,
		// hardcoded initial img to save initial setup time
		img = ImageIO.read(getClass().getResource("pics/centerMap.png"));

		// load all final pictures and animated Images used in the centerMap
		controlImg = ImageIO.read(getClass().getResource("pics/controlslong.png"));
		radarImg = new ImageIcon(getClass().getResource("pics/radar.gif")).getImage();
		gaugesImg = new ImageIcon(getClass().getResource("pics/gauges.gif")).getImage();
		gauges1Img = new ImageIcon(getClass().getResource("pics/gauges1.gif")).getImage();
		gauges2Img = new ImageIcon(getClass().getResource("pics/gauges2.gif")).getImage();
		alien = new ImageIcon(getClass().getResource("pics/alien.gif")).getImage();

		// instantiate all fonts used by graphics
		latLogFont = new Font("Arial", Font.PLAIN, 13);
		speedFont = latLogFont.deriveFont(28f);
		alienFont = latLogFont.deriveFont(20f);
		landFont = new Font("Arial", Font.BOLD, 40);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, 442, 485, null);
		g.drawImage(controlImg, 0, 22, 472, 520, null);
		g.setColor(Color.RED);

		g.setFont(speedFont);
		g.drawString(String.valueOf(speed * 10), 370, 56);
		drawLatLog(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(radarImg, 253, 37, 25, 25, null);
		g2.drawImage(gaugesImg, 156, 484, 41, 35, null);
		g2.drawImage(gauges1Img, 221, 484, 43, 36, null);
		g2.drawImage(gauges2Img, 280, 484, 41, 35, null);

		// while auto-landing, display message that in Auto Land Mode
		if (autoLand) {
			g.setFont(landFont);
			g.drawString("Auto Land Mode", 70, 300);
		}

		// if go over north/south pole - zap!
		if (currentLat < -90 || currentLat > 90) {
			// draw row of 7 dancing aliens
			for (int i = 55; i <= 355; i += 50) {
				g2.drawImage(alien, i, height / 2, 63, 101, null);
			}
			g2.setColor(Color.GREEN);
			g2.setFont(alienFont);
			g2.drawString("ZAP! Out of range.", 150, 205);
			g2.drawString("PLEASE FLY BACK ON TO THE MAP!!", 60, 240);
		}
	}

	private void drawLatLog(Graphics g) {
		// set compass direction of lat/log
		char latsym = 'N';
		if (currentLat < 0) {
			latsym = 'S';
		}
		char logsym = 'E';
		if (currentLog < 0) {
			logsym = 'W';
		}
		g.setFont(latLogFont);
		g.drawString(
				formatter.format(Math.abs(currentLat)) + "\u00b0" + latsym + ", "
						+ formatter.format(Math.abs(currentLog)) + "\u00b0" + logsym, 10, 50);
	}

	public void loadImg() throws MalformedURLException {
		URL url = new URL("https://maps.googleapis.com/maps/api/staticmap?center=" + currentLat + "," + currentLog
				+ "&size=640x640" + "&maptype=" + view + "&zoom=" + zoomPanel.zoom
				+ "&key=AIzaSyAirHEsA08agmW9uizDvXagTjWS3mRctPE");
		new ImgDownloadThread(url, this).start();
	}

	public void updateView(String view) throws MalformedURLException {
		this.view = view.toLowerCase();
		loadImg();
	}

	public void updateMap(int speed, int direction, double difference, double currentlat, double currentlong,
			boolean autoLand) throws MalformedURLException {
		this.autoLand = autoLand;
		this.currentLat = currentlat;
		this.currentLog = currentlong;
		this.speed = speed;
		loadImg();
	}
}
