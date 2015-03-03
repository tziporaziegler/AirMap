package airMap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

//TODO change to JMenuItem and adjust border spacing
public class MenuZoom extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton zoomout;
	private JButton zoomin;
	private String panelCode;
	private JPanel parentPanel;
	private int zoom;

	public MenuZoom(JPanel parentPanel, int initialZoom, String panelCode) {
		setBorder(new EmptyBorder(-5, -5, -5, -5));
		
		this.panelCode = panelCode;
		this.parentPanel = parentPanel;
		zoom = initialZoom; // 0-21 disable + button is more

		zoomout = new JButton("-");
		zoomin = new JButton("+");
		zoomout.addActionListener(zoomoutListen);
		zoomin.addActionListener(zoominListen);
		add(zoomout);
		add(zoomin);
	}

	public int getZoom() {
		return zoom;
	}

	// FIXME don't want to always cast
	private void load() throws MalformedURLException {
		switch (panelCode) {
			case "navMap":
				((NavigationMap) parentPanel).loadImg();
			break;
			case "cenMap":
				((CenterMap) parentPanel).loadImg();
			break;
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
