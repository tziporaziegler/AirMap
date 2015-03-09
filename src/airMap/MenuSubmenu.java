package airMap;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;

public class MenuSubmenu extends JMenu {
	private static final long serialVersionUID = 1L;
	private Font font;
	private String[] optionNames;
	private String[] optionDescriptions;

	public MenuSubmenu(String title, String[] optionNames, String[] optionDescriptions) {
		setText(title);
		this.optionNames = optionNames;
		this.optionDescriptions = optionDescriptions;
		initialSetUp();
	}

	public MenuSubmenu(String title, String[] optionNames, String[] optionDescriptions, ArrayList<JMenu> submenus) {
		setText(title);
		this.optionNames = optionNames;
		this.optionDescriptions = optionDescriptions;
		initialSetUp();

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

	public void initialSetUp() {
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
	}

	ActionListener menuListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
			String chosen = (String) item.getText();

		}
	};
}
