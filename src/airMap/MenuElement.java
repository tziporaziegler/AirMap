package airMap;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;

public class MenuElement extends JMenu {
	private static final long serialVersionUID = 1L;
	private Font font;

	public MenuElement() {
		setText("elements");
		font = new Font("Arial", Font.PLAIN, 12);
		setFont(font);
		String[] optionNames = { "all", "geometry", "labels" };
		JCheckBoxMenuItem[] options = new JCheckBoxMenuItem[optionNames.length];
		for (int i = 0; i < options.length; i++) {
			options[i] = new JCheckBoxMenuItem(optionNames[i]);
			options[i].addActionListener(elementListener);
			options[i].setFont(font);
			add(options[i]);
		}
	}

	ActionListener elementListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
			String element = (String) item.getText();

		}
	};
}
