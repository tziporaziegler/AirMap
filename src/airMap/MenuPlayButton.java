package airMap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class MenuPlayButton extends JButton {
	private static final long serialVersionUID = 1L;
	private boolean playing;
	private GameLoopThread loop;
	private boolean running;

	public MenuPlayButton(GameLoopThread loop) {
		setText(">");
		this.loop = loop;
		playing = false;
		addActionListener(click);
	}

	public void toggle() {
		if (playing) {
			setText(">");
			loop.toggle();
			playing = false;
		}
		else {
			setText("||");
			if (!running) {
				loop.start();
				running = true;
			}
			loop.toggle();
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
