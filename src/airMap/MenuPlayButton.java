package airMap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class MenuPlayButton extends JButton {
	private static final long serialVersionUID = 1L;
	private boolean playing;
	private boolean running;

	public MenuPlayButton() {
		setText(">");
		playing = false;
		addActionListener(click);
	}

	public void toggle() {
		if (playing) {
			setText(">");
			playing = false;
		}
		else {
			setText("||");
			if (!running) {
				running = true;
			}
			playing = true;
		}
	}

	ActionListener click = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent event) {
			toggle();
		}
	};

}
