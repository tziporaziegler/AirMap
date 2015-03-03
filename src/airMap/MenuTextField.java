package airMap;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JTextField;

public class MenuTextField extends JTextField {
	private static final long serialVersionUID = 1L;
	private String name;

	public MenuTextField(String name, final World world) {
		setColumns(20);
		setFont(new Font("Arial", Font.PLAIN, 18));
		setSelectedTextColor(Color.BLUE);
		this.name = name;
		setText(name);

		addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				JTextField f = (JTextField) e.getSource();
				f.selectAll();
			}
		});

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						world.gobutton();
					}
					catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		setFocusable(true);
	}

	public void reset() {
		setText(name);
	}
}