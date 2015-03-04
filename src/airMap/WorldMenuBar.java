package airMap;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JMenuBar;

public class WorldMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	protected MenuPlayButton play;
	private MenuTextField location;
	private MenuTextField destination;
	private JButton go;
	private World world;

	public WorldMenuBar(World world, GameLoopThread loop) {
		this.world = world;
		setLayout(new FlowLayout(FlowLayout.CENTER, 20, 3));

		play = new MenuPlayButton(loop);
		add(play);

		add(Box.createHorizontalStrut(90));

		location = new MenuTextField("Departure", world);
		add(location);

		destination = new MenuTextField("Destination", world);
		add(destination);

		go = new JButton("Go!");
		go.addActionListener(click);
		add(go);
	}

	public void setAddress(String adr, String adr2) throws UnsupportedEncodingException {

	}

	public void gobutton() throws IOException {
		String adr = location.getText();
		String adr2 = destination.getText();
		if (!adr.equals("Departure") && !adr2.equals("Destination")) {
			new AddressThread(world, adr, adr2).start();
			location.reset();
			destination.reset();
		}
	}

	ActionListener click = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				gobutton();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	};
}
