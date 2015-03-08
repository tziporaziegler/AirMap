package airMap;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;

public class CenterMap extends Map {
	private static final long serialVersionUID = 1L;
	private double currentlat;
	private double currentlong;

	// menu
	private final JMenuBar menu;
	private final MenuZoom zoomPanel;

	private final BufferedImage controlImg;
	private final Image gaugesImg;
	private final Image radarImg;
	private final Image gauges1Img;
	private final Image gauges2Img;

	public CenterMap(double currentlat, double currentlong) throws IOException {
		width = 600;
		height = 600;
		setPreferredSize(new Dimension(width, height));
		setLayout(new BorderLayout());

		// create menu
		menu = new JMenuBar();
		view = "satellite";
		menu.add(new MenuView(this, "cenMap"));
		zoomPanel = new MenuZoom(this, 5, "cenMap");
		menu.add(zoomPanel);
		add(menu, BorderLayout.NORTH);

		this.currentlat = currentlat;
		this.currentlong = currentlong;
		img = ImageIO.read(getClass().getResource("pics/centerMap.png"));
		
		controlImg = ImageIO.read(getClass().getResource("pics/controlslong.png"));
		radarImg = new ImageIcon(getClass().getResource("pics/radar.gif")).getImage();
		gaugesImg = new ImageIcon(getClass().getResource("pics/gauges.gif")).getImage();
		gauges1Img = new ImageIcon(getClass().getResource("pics/gauges1.gif")).getImage();
		gauges2Img = new ImageIcon(getClass().getResource("pics/gauges2.gif")).getImage();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	
		// TODO take out print
		System.out.println(currentlat + " , " + currentlong);
		g.drawImage(img, 0, 0, 442, 485, null);
		g.drawImage(controlImg, 0, 30, 472, 520, null);

		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(radarImg, 253, 45, 25, 25, null);
		g2.drawImage(gaugesImg, 156, 492, 41, 35, null);
		g2.drawImage(gauges1Img, 221, 492, 43, 36, null);
		g2.drawImage(gauges2Img, 280, 492, 41, 35, null);
	}

	public void loadImg() throws MalformedURLException {
		String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + currentlat + "," + currentlong
				+ "&size=640x640" + "&maptype=" + view + "&zoom=" + zoomPanel.zoom
				+ "&key=AIzaSyAirHEsA08agmW9uizDvXagTjWS3mRctPE";
		new ImgDownloadThread(new URL(url), this).start();
	}

	public void updateView(String view) throws MalformedURLException {
		this.view = view.toLowerCase();
		loadImg();
	}

	public void updateMap(int direction, double difference, double currentlat, double currentlong)
			throws MalformedURLException {
		this.currentlat = currentlat;
		this.currentlong = currentlong;
		loadImg();
	}
}
