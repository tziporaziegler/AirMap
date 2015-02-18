package airMap;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
	private String address;

	// menu
	private JMenu viewOptions;
	private JMenuBar menu;
	private JButton zoomout;
	private JButton zoomin;

	private int zoom;

	public CenterMap(String address) throws IOException {
		setLayout(new BorderLayout());
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		// create menu
		menu = new JMenuBar();
		viewOptions = new JMenu("View");
		zoomout = new JButton("-");
		zoomin = new JButton("+");
		setUpMenu();
		add(menu, BorderLayout.NORTH);

		view = "hybrid";
		this.address = address;
		loadImg();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			g.drawImage(img, 0, 0, 600, 600, null);
			// TODO change airMap to dashboard image
			g.drawImage(ImageIO.read(getClass().getResource("pics/airplane.jpg")), 150, 300, 100, 100, null);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
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
		String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + address + "&size=1280x1280"
				+ "&maptype=" + view + "&zoom=" + zoom;
		System.out.println("Center Img: " + url);
		// FIXME should load img in separtate thread that somehow returns and img
		img = new ImageIcon(new URL(url)).getImage();
	}

	public void updateView(String view) throws MalformedURLException {
		this.view = view.toLowerCase();
		loadImg();
	}

	public void updateMap(String address) throws MalformedURLException {
		this.address = address;
		loadImg();
	}

	public void setAddress(String address) {
		this.address = address;
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
