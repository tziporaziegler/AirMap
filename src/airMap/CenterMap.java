package airMap;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class CenterMap extends JPanel {
	private static final long serialVersionUID = 1L;
	private Image img;
	private String view;
	private double currentlat;
	private double currentlong;

	// menu
	private JMenu viewOptions;
	private JMenuBar menu;
	private JButton zoomout;
	private JButton zoomin;

	private int zoom;

	private BufferedImage controlImg;
	private Image gaugesImg;
	private Image radarImg;
	private Image gauges1Img;
	private Image gauges2Img;

	// use so don't download new pic every time
	private int movedHor; // can go from -85 to 85
	private int movedVer; // can go from -75 to 75

	public CenterMap(double currentlat, double currentlong) throws IOException {
		setPreferredSize(new Dimension(600, 600));
		setLayout(new BorderLayout());
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		// create menu
		menu = new JMenuBar();
		viewOptions = new JMenu("View");
		zoomout = new JButton("-");
		zoomin = new JButton("+");
		setUpMenu();
		add(menu, BorderLayout.NORTH);

		view = "satellite";
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
		g.drawImage(img, -85 + movedHor, -75 + movedVer, 640, 640, null);
		// TODO change airMap to dashboard image
		g.drawImage(controlImg, 0, 32, 472, 520, null);

		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(radarImg, 253, 45, 25, 25, null);
		g2.drawImage(gaugesImg, 156, 492, 41, 35, null);
		g2.drawImage(gauges1Img, 221, 492, 43, 36, null);
		g2.drawImage(gauges2Img, 280, 492, 41, 35, null);
	}

	public void setUpMenu() {
		String[] viewNames = { "Satellite", "Roadmap", "Hybrid", "Terrain", "Street View" };
		JMenuItem[] views = new JMenuItem[viewNames.length];

		for (int i = 0; i < views.length; i++) {
			views[i] = new JMenuItem(viewNames[i]);
			views[i].setMnemonic(KeyEvent.VK_S);
			views[i].addActionListener(mapView);
			viewOptions.add(views[i]);
		}

		menu.add(viewOptions);
		viewOptions.setToolTipText("Map View");
		menu.add(viewOptions);

		// add zoom buttons to menu after adding a zoomListen
		zoomout.addActionListener(zoomoutListen);
		zoomin.addActionListener(zoominListen);
		// initialize zoom value
		zoom = 2; // 0-21 disable + button is more
		menu.add(zoomout);
		menu.add(zoomin);
	}

	public void loadImg() throws MalformedURLException {
		String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + currentlat + "," + currentlong
				+ "&size=640x640" + "&maptype=" + view + "&zoom=" + zoom+"&key=AIzaSyAirHEsA08agmW9uizDvXagTjWS3mRctPE";
		System.out.println("NEW Center Img: " + url);
		// FIXME should load img in separtate thread that somehow returns and img
		img = new ImageIcon(new URL(url)).getImage();
	}

	public void updateView(String view) throws MalformedURLException {
		this.view = view.toLowerCase();
		loadImg();
	}

	public void updateMap(int direction,double difference, double currentlat, double currentlong) throws MalformedURLException {
		this.currentlat = currentlat;
		this.currentlong = currentlong;
		double moveVer=0;
		double moveHor=0;
		double pixels=difference*.0137329*Math.pow( 2,zoom);
		switch (direction) {
		case 8: {
			
			// FIXME not sure how many pixels really want to add
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
			
			moveVer = -pixels;
			break;
		}
	}
		movedHor += moveHor;
		movedVer += moveVer;
		if (movedHor > 85 || movedHor < -85 || movedVer > 75 || movedVer < -75) {
			loadImg();
			movedHor = 0;
			movedVer = 0;
		}
	}

	ActionListener zoominListen = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent event) {
			zoom++;
			if (zoom == 21) {
				zoomin.setEnabled(false);
			}
			if (!zoomout.isEnabled()) {
				zoomout.setEnabled(true);
			}

			try {
				loadImg();
			}
			catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	};

	ActionListener zoomoutListen = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent event) {
			zoom--;
			if (zoom == 1) {
				zoomout.setEnabled(false);
			}
			if (!zoomin.isEnabled()) {
				zoomin.setEnabled(true);
			}
			try {
				loadImg();
			}
			catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	};

	ActionListener mapView = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem item = (JMenuItem) e.getSource();
			String view = (String) item.getText();
			try {
				updateView(view);
			}
			catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
		}
	};
}
