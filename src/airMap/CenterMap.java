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

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;

public class CenterMap extends Map {
	private static final long serialVersionUID = 1L;
	private double currentLat;
	private double currentLog;

	// menu
	private final JMenuBar menu;
	private final MenuZoom zoomPanel;

	private final BufferedImage controlImg;
	private final Image gaugesImg;
	private final Image radarImg;
	private final Image gauges1Img;
	private final Image gauges2Img;
	private final Image alien;
	private int speed;

	public CenterMap(double currentlat, double currentlong) throws IOException {
		width = 600;
		height = 600;
		setPreferredSize(new Dimension(width, height));
		setLayout(new BorderLayout());

		// create menu
		menu = new JMenuBar();
		view = "satellite";
		menu.add(new MenuView(this));
		//add space to menu bar so zoom buttons move to right side of menus
		menu.add(Box.createHorizontalStrut(304));
		zoomPanel = new MenuZoom(this, 5);
		menu.add(zoomPanel);
		add(menu, BorderLayout.NORTH);

		this.currentLat = currentlat;
		this.currentLog = currentlong;
		img = ImageIO.read(getClass().getResource("pics/centerMap.png"));

		controlImg = ImageIO.read(getClass().getResource("pics/controlslong.png"));
		radarImg = new ImageIcon(getClass().getResource("pics/radar.gif")).getImage();
		gaugesImg = new ImageIcon(getClass().getResource("pics/gauges.gif")).getImage();
		gauges1Img = new ImageIcon(getClass().getResource("pics/gauges1.gif")).getImage();
		gauges2Img = new ImageIcon(getClass().getResource("pics/gauges2.gif")).getImage();
		alien = new ImageIcon(getClass().getResource("pics/alien.gif")).getImage();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// TODO take out print
		System.out.println(currentLat + " , " + currentLog);
		g.drawImage(img, 0, 0, 442, 485, null);
		g.drawImage(controlImg, 0, 22, 472, 520, null);
		g.setColor(Color.RED);
		g.setFont(new Font("Arial",Font.BOLD,34));
		g.drawString(String.valueOf(speed*10), 350, 480);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(radarImg, 253, 37, 25, 25, null);
		g2.drawImage(gaugesImg, 156, 484, 41, 35, null);
		g2.drawImage(gauges1Img, 221, 484, 43, 36, null);
		g2.drawImage(gauges2Img, 280, 484, 41, 35, null);
		

		if (currentLat < -90 || currentLat > 90) {
			g2.drawImage(alien, 55, height / 2, 63, 101, null);
			g2.drawImage(alien, 105, height / 2, 63, 101, null);
			g2.drawImage(alien, 155, height / 2, 63, 101, null);
			g2.drawImage(alien, 205, height / 2, 63, 101, null);
			g2.drawImage(alien, 255, height / 2, 63, 101, null);
			g2.drawImage(alien, 305, height / 2, 63, 101, null);
			g2.drawImage(alien, 355, height / 2, 63, 101, null);
			g2.setColor(Color.GREEN);
			g2.setFont(new Font("Arial", Font.PLAIN, 20));
			g2.drawString("ZAP! Out of range.", 150, 205);
			g2.drawString("PLEASE FLY BACK ON TO THE MAP!!", 60, 240);
		}
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

	public void updateMap(int speed,int direction, double difference, double currentlat, double currentlong)
			throws MalformedURLException {
		this.currentLat = currentlat;
		this.currentLog = currentlong;
		this.speed=speed;
		loadImg();
	}
}
