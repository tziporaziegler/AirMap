package airMap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;

public class MenuFeatures extends JMenu {
	private static final long serialVersionUID = 1L;
	private NavigationMap map;

	public MenuFeatures(NavigationMap map) {
		setText("Features");
		setToolTipText("Features to display");

		this.map = map;

		String[] featuresNames = { "Roads", "Landscape", "Transit", "transit.station.airports", "pio.school" };
		JCheckBoxMenuItem[] features = new JCheckBoxMenuItem[featuresNames.length];
		for (int i = 0; i < features.length; i++) {
			features[i] = new JCheckBoxMenuItem(featuresNames[i]);
			features[i].addActionListener(featuresView);
			add(features[i]);
		}
	}

	ActionListener featuresView = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
			String feature = (String) item.getText();
			try {
				map.updateFeature(feature);
			}
			catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
		}
	};
}
