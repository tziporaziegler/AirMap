package airMap;

import java.awt.BorderLayout;
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
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class CenterMap extends JPanel {
	private static final long serialVersionUID = 1L;
	private Image img;
	private double currentlat;
	private double currentlong;

	// menu
	private JMenuBar menu;
	private String view;
	private MenuZoom zoomPanel;

	private BufferedImage controlImg;
	private Image gaugesImg;
	private Image radarImg;
	private Image gauges1Img;
	private Image gauges2Img;

	// use so don't download new pic every time
	private double movedHor; // can go from -85 to 85
	private double movedVer; // can go from -75 to 75

	private double adjustlat;
	private double adjustlog;

	public CenterMap(double currentlat, double currentlong) throws IOException {
		// setPreferredSize(new Dimension(600, 600));
		setLayout(new BorderLayout());
		setBorder(new BevelBorder(BevelBorder.LOWERED));

		// create menu
		menu = new JMenuBar();
		view = "satellite";
		menu.add(new MenuView(this, "cenMap"));
		zoomPanel = new MenuZoom(this, 5, "cenMap");
		menu.add(zoomPanel);
		add(menu, BorderLayout.NORTH);

		this.currentlat = currentlat;
		this.currentlong = currentlong;
		movedHor = 0;
		movedVer = 0;
		loadImg();

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
		int ho = (int) movedHor;
		int ve = (int) movedVer;
		System.out.println(currentlat + " , " + currentlong);
	//	g.drawImage(img, (-85 + ho), (-75 + ve), 640, 640, null);
		g.drawImage(img,0 ,0 , 442, 485, null);
		g.drawImage(controlImg, 0, 30, 472, 520, null);

		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(radarImg, 253, 45, 25, 25, null);
		g2.drawImage(gaugesImg, 156, 492, 41, 35, null);
		g2.drawImage(gauges1Img, 221, 492, 43, 36, null);
		g2.drawImage(gauges2Img, 280, 492, 41, 35, null);
	}

	//FIXME instead of getZoom, MenuZoom should update zoom whenever actionListener called.
	public void loadImg() throws MalformedURLException {
		String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + currentlat + "," + currentlong
				+ "&size=640x640" + "&maptype=" + view + "&zoom=" + zoomPanel.getZoom()
				+ "&key=AIzaSyAirHEsA08agmW9uizDvXagTjWS3mRctPE";
		System.out.println("NEW Center Img: " + url);
		// FIXME should load img in separate thread that somehow returns and img
		img = new ImageIcon(new URL(url)).getImage();
	}

	public void updateView(String view) throws MalformedURLException {
		this.view = view.toLowerCase();
		loadImg();
	}

	public void updateMap(int direction, double difference, double currentlat, double currentlong)
			throws MalformedURLException {
		int zoom = zoomPanel.getZoom();
		this.currentlat = currentlat;
		this.currentlong = currentlong;
/*
		double moveVer = 0;
		double moveHor = 0;

		// double pixels = difference * .0137329 * Math.pow(2, zoom);
		double pixels = (640 * (Math.pow(2, (zoom - 1)))) / 360;
		switch (direction) {
			case 8: {
				moveVer = pixels;
				break;
			}
			case 2: {
				moveVer = -pixels;
				break;
			}
			case 4: {
				moveHor = pixels;
				break;
			}
			case 6: {
				moveHor = -pixels;
				break;
			}
		}

		movedHor += moveHor;
		movedVer += moveVer;
		if (movedHor > 85 || movedHor < -85 || movedVer > 75 || movedVer < -75) {
			double adjustPix;
			if (direction == 6) {
				adjustPix = 12;
				adjustlog = adjustPix / (.0137329 * Math.pow(2, zoom));
			}
			else if (direction == 4) {
				adjustPix = 12;
				adjustlog = adjustPix / (.0137329 * Math.pow(2, zoom));
			}
			else if (direction == 8) {
				//
			}
			else {
				//
			}
			movedHor = 0;
			movedVer = 0;
			
			loadImg();
		}
		*/
		loadImg();
	}
}
