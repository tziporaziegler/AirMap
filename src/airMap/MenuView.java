package airMap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MenuView extends JMenu {
	private static final long serialVersionUID = 1L;
	private JPanel parentPanel;
	private String mapCode;

	public MenuView(JPanel parentPanel, String mapCode) {
		setText("View");
		setToolTipText("Map View");

		this.parentPanel = parentPanel;
		this.mapCode = mapCode;

		String[] viewNames = { "Satellite", "Roadmap", "Hybrid", "Terrain" };
		JMenuItem[] views = new JMenuItem[viewNames.length];

		for (int i = 0; i < views.length; i++) {
			views[i] = new JMenuItem(viewNames[i]);
			views[i].setMnemonic(KeyEvent.VK_S);
			views[i].addActionListener(mapView);
			add(views[i]);
		}

		addActionListener(mapView);
	}

	// FIXME don't want to always cast
	ActionListener mapView = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem item = (JMenuItem) e.getSource();
			String view = (String) item.getText();
			try {
				switch (mapCode) {
					case "navMap":
						((NavigationMap) parentPanel).updateView(view);
					break;
					case "cenMap":
						((CenterMap) parentPanel).updateView(view);
					break;
				}
			}
			catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
		}
	};
}
