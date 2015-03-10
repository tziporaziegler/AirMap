package airMap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MenuZoom extends JMenuItem {
	private static final long serialVersionUID = 1L;
	private JButton zoomout;
	private JButton zoomin;
	private JPanel parentPanel;
	protected int zoom;

	public MenuZoom(JPanel parentPanel, int initialZoom) {
		setLayout(new BoxLayout(this, 2));
		this.parentPanel = parentPanel;
		
		zoom = initialZoom; // 1-21 disable + button is more
		// zoom 0 shows whole world regardless to container size, so not used

		// create and add the zoomout button
		zoomout = new JButton("-");
		zoomout.setToolTipText("zoom out");
		zoomout.addActionListener(zoomoutListen);
		add(zoomout);

		// create and add the zoomin button
		zoomin = new JButton("+");
		zoomin.setToolTipText("zoom in");
		zoomin.addActionListener(zoominListen);
		add(zoomin);
	}

	private void load() throws MalformedURLException {
		// cast parentPanel so can call the correct loadImg()
		if (parentPanel instanceof NavigationMap) {
			((NavigationMap) parentPanel).loadImg();
		}
		else if (parentPanel instanceof CenterMap) {
			((CenterMap) parentPanel).loadImg();
		}
	}

	ActionListener zoominListen = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent event) {
			zoom++;
			// if reach maximum zoom (which is 21) then disable zoomin button
			// until map is zoomed out again
			if (zoom == 21) {
				zoomin.setEnabled(false);
			}
			if (!zoomout.isEnabled()) {
				zoomout.setEnabled(true);
			}
			try {
				load();
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
			// if reach minimum zoom (1) then disable zoomout button until map is zoomed in again
			// zoom 0 shows whole world regardless to container size, so not used
			if (zoom == 1) {
				zoomout.setEnabled(false);
			}
			if (!zoomin.isEnabled()) {
				zoomin.setEnabled(true);
			}
			try {
				load();
			}
			catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	};
}