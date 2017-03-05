package airMap;

import java.awt.Font;
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

	public MenuView(JPanel parentPanel) {
		setText("View");
		setToolTipText("MapPanel View");
		
		Font font = new Font("Arial", Font.PLAIN, 12);
		setFont(font.deriveFont(14f));

		this.parentPanel = parentPanel;

		String[] viewNames = { "Satellite", "Roadmap", "Hybrid", "Terrain" };
		int[] mnemonics = { KeyEvent.VK_S, KeyEvent.VK_R, KeyEvent.VK_H, KeyEvent.VK_T };
		JMenuItem[] views = new JMenuItem[viewNames.length];

		for (int i = 0; i < views.length; i++) {
			views[i] = new JMenuItem(viewNames[i]);
			views[i].setMnemonic(mnemonics[i]);
			views[i].addActionListener(mapView);
			views[i].setFont(font);
			add(views[i]);
		}

		addActionListener(mapView);
	}

	// FIXME see if can find a way to not always cast
	private ActionListener mapView = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem item = (JMenuItem) e.getSource();
			String view = (String) item.getText();
			try {
				if (parentPanel instanceof NavigationMap) {
					((NavigationMap) parentPanel).updateView(view);
				}
				else if (parentPanel instanceof CenterMap) {
					((CenterMap) parentPanel).updateView(view);
				}
			}
			catch (MalformedURLException e1) {
				e1.printStackTrace();
			}

		}
	};
}
