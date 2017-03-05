package airMap;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;

public class MenuSubmenu extends JMenu {
	private static final long serialVersionUID = 1L;
	private Font font;

	public MenuSubmenu(String title, String[] optionNames, String[] optionDescriptions, JMenu... submenus) {
		setText(title);
		font = new Font("Arial", Font.PLAIN, 12);
		setFont(font);
		JCheckBoxMenuItem[] options = new JCheckBoxMenuItem[optionNames.length];
		for (int i = 0; i < options.length; i++) {
			JCheckBoxMenuItem option = new JCheckBoxMenuItem(optionNames[i]);
			option.addActionListener(menuListener);
			option.setFont(font);
			option.setToolTipText(optionDescriptions[i]);
			add(option);
		}

		for (JMenu sub : submenus) {
			add(sub);
		}
	}

	public MenuSubmenu(JMenu menuFeatures, String[] optionNames, String[] optionDescriptions) {
		font = new Font("Arial", Font.PLAIN, 12);
		setFont(font);
		JCheckBoxMenuItem[] options = new JCheckBoxMenuItem[optionNames.length];
		for (int i = 0; i < options.length; i++) {
			JCheckBoxMenuItem option = new JCheckBoxMenuItem(optionNames[i]);
			option.addActionListener(menuListener);
			option.setFont(font);
			option.setToolTipText(optionDescriptions[i]);
			menuFeatures.add(option);
		}
	}

	private ActionListener menuListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			//JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
			//String chosen = (String) item.getText();
			// TODO add selected features to URL
		}
	};
}
