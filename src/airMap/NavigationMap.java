package airMap;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class NavigationMap extends JPanel {
	private static final long serialVersionUID = 1L;
	private int width;
	private int height;
	private Image mapImg;
	private double currentlat;
	private double currentlong;

	private Plane plane;
	private BufferedImage planeImg;
	private int degrees;

	private JMenuBar menu;
	private String view;
	@SuppressWarnings("unused")
	private String feature;
	private MenuZoom zoomPanel;
	private int count;
	private double diffBuffer;

	public NavigationMap(double startlat, double startlong) throws IOException {
		width = 300;
		height = 300;
		setPreferredSize(new Dimension(width, height));
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setLayout(new BorderLayout());
		currentlat = startlat;
		currentlong = startlong;
		count=0;
		// set up menu bar
		menu = new JMenuBar();
		view = "terrain";
		menu.add(new MenuView(this, "navMap"));
		feature = "transit.station.airports";
		menu.add(new MenuFeatures(this));
		zoomPanel = new MenuZoom(this, 5, "navMap");
		menu.add(zoomPanel);
		add(menu, BorderLayout.NORTH);

		// TODO set plane location to start
		plane = new Plane(width / 2, height / 2);
		planeImg = ImageIO.read(getClass().getResource("pics/airplane.jpg"));

		diffBuffer=0;
		// FIXME move to separate thread
		loadImg();
	}

	public void update(int speed, int direction,double currentlat,double currentlong) throws MalformedURLException {
		movePlane(speed, direction,currentlat,currentlong);
	}

	public void setDegree(int direction) {
		switch (direction) {
			case 2: {
				degrees = 135;
				break;
			}
			case 4: {
				degrees = -135;
				break;
			}
			case 6: {
				degrees = 45;
				break;
			}
			case 8: {
				degrees = -45;
				break;
			}
		}
	}

	public void movePlane(int speed, int direction, double currentlat,double currentlong) throws MalformedURLException {
		double pixelPerLong=(300*(Math.pow(2, (zoomPanel.getZoom()-1)))/360);
		System.out.println("pixel "+pixelPerLong);
		int difference;
		System.out.println(count++);
		double diff= ((speed/69.0)*pixelPerLong);
		if(diffBuffer!=0){
			diff+=diffBuffer;
			diffBuffer=0;
		}
		if(diff%1!=0){
			diffBuffer=diff%1;
			difference=(int)diff;
		}
		else{
			difference=(int)diff;
		}
		System.out.println("diff "+diff);
		System.out.println("diffbuffer "+diffBuffer);
		System.out.println("difference "+difference);
		switch (direction) {
			case 2: {
				plane.setY(plane.getY() + difference);
				break;
			}
			case 4: {
				plane.setX(plane.getX() - difference);
				break;
			}
			case 6: {
				plane.setX(plane.getX() + difference);
				break;
			}
			case 8: {
				plane.setY(plane.getY() -difference);
				break;
			}
		}
		if(plane.getX()<=0||plane.getX()>=300||plane.getY()<=0||plane.getY()>=300){
			this.currentlat=currentlat;
			this.currentlong=currentlong;
			plane.setX(150);
			plane.setY(150);
			loadImg();
			
		}
	}

	public void newMap(double newLat, double newLog) throws MalformedURLException {
		plane.setX(150);
        plane.setY(150);
        currentlat = newLat;
		currentlong = newLog;
		plane.setX(150);
		plane.setY(150);
		loadImg();
	}

	public void paintComponent(Graphics g) {
		g.drawImage(mapImg, 0, 0, width, height, null);
		AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(degrees), planeImg.getWidth() / 2,
				planeImg.getHeight() / 2);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		g.drawImage(op.filter(planeImg, null), plane.getX(), plane.getY(), 20, 20, null);
	}

	public void loadImg() throws MalformedURLException {
		String zooms = "";
		int zoom = zoomPanel.getZoom();
		if (zoom != 0) {
			zooms = "&zoom=" + zoom;
		}

		String adrhalf = "https://maps.googleapis.com/maps/api/staticmap?center=" + currentlat + "," + currentlong
				+ "&size=" + width + "x" + height + "&maptype=" + view;

		String airports = "&markers=size:mid%7Ccolor:green%7C" + "atl+airport" + "%7C" + "anc+airport" + "%7C"
				+ "aus+airport" + "%7C" + "bwi+airport" + "%7C" + "bos+airport" + "%7C" + "clt+airport" + "%7C"
				+ "mdw+airport" + "%7C" + "ord+airport" + "%7C" + "cvg+airport" + "%7C" + "cle+airport" + "%7C"
				+ "cmh+airport" + "%7C" + "dfw+airport" + "%7C" + "den+airport" + "%7C" + "dtw+airport" + "%7C"
				+ "fll+airport" + "%7C" + "rsw+airport" + "%7C" + "bdl+airport" + "%7C" + "hnl+airport" + "%7C"
				+ "iah+airport" + "%7C" + "hou+airport" + "%7C" + "ind+airport" + "%7C" + "mci+airport" + "%7C"
				+ "las+airport" + "%7C" + "lax+airport" + "%7C" + "mem+airport" + "%7C" + "mia+airport" + "%7C"
				+ "msp+airport" + "%7C" + "bna+airport" + "%7C" + "msy+airport" + "%7C" + "jfk+airport" + "%7C"
				+ "ont+airport" + "%7C" + "lga+airport" + "%7C" + "ewr+airport" + "%7C" + "oak+airport" + "%7C"
				+ "mco+airport" + "%7C" + "phl+airport" + "%7C" + "phx+airport" + "%7C" + "pit+airport" + "%7C"
				+ "pdx+airport" + "%7C" + "rdu+airport" + "%7C" + "smf+airport" + "%7C" + "slc+airport" + "%7C"
				+ "sat+airport" + "%7C" + "san+airport" + "%7C" + "sfo+airport" + "%7C" + "sjc+airport" + "%7C"
				+ "sna+airport" + "%7C" + "sea+airport" + "%7C" + "stl+airport" + "%7C" + "tpa+airport" + "%7C"
				+ "iad+airport" + "%7C" + "dca+airport" + "%7C";

		// URL url = new URL(adrhalf + airports);
		URL url = new URL(adrhalf + airports + zooms + "&key=AIzaSyAirHEsA08agmW9uizDvXagTjWS3mRctPE");

		mapImg = new ImageIcon(url).getImage();
		// new ImgDownloadThread(url, new JLabel()).start();
	}

	public void updateFeature(String feature) throws MalformedURLException {
		this.feature = feature.toLowerCase();
		loadImg();
	}

	public void updateView(String view) throws MalformedURLException {
		this.view = view.toLowerCase();
		loadImg();
	}
}
