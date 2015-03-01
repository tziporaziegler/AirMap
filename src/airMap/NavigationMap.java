package airMap;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JMenuBar;

public class NavigationMap {
	private Image img;
	private JButton zoomout;
	private JButton zoomin;
	private int zoom;
	private JMenuBar menu;
	private double currentlat;
	private double currentlong;
	private Plane plane;
	public NavigationMap(double startlat,double startlong) throws IOException{
		currentlat=startlat;
		currentlong=startlong;
		// create and add zoom buttons
		zoomout = new JButton("-");
		zoomin = new JButton("+");
		zoomout.addActionListener(zoomoutListen);
		zoomin.addActionListener(zoominListen);
		zoom = 2; // 0-21 disable + button is more
		menu = new JMenuBar();
		menu.add(zoomout);
		//TODO set plane location to start
		plane= new Plane(112,450);
		img=ImageIO.read(getClass().getResource("pics/airplane.jpg"));
	}
	public void update(int speed, int direction){
		movePlane(speed,direction);
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
	public void movePlane(int speed, int direction){
		switch(direction){
		case 2:{
			plane.setY(plane.getY()+(speed/100));
			break;
		}
		case 4:{
			plane.setX(plane.getX()-(speed/100));
			break;
		}
		case 6:{
			plane.setX(plane.getX()+(speed/100));
			break;
		}
		case 8:{
			plane.setY(plane.getY()-(speed/100));
			break;
		}
		}
	}
	public void drawPlane(Graphics g){
		
		g.drawImage(img, plane.getX(), plane.getY(),20,20, null);
	}
	public void drawMap(Graphics g){
		
		g.drawImage(map, 0, 0,250,300, null);
	}
}
