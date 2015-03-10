package airMap;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenuBar;

public class WorldMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	private JButton play;
	private MenuTextField location;
	private MenuTextField destination;
	private JButton go;
	private World world;
	private JCheckBox mute;
	private JButton help;

	public WorldMenuBar(World world) {
		this.world = world;
		setLayout(new FlowLayout(FlowLayout.CENTER, 15, 3));
		mute = new JCheckBox("mute");
		mute.addActionListener(muteButton);
		add(mute);
		play = new JButton();
		play.setText(">");
		play.addActionListener(pause);
		add(play);
		add(Box.createHorizontalStrut(60));
		location = new MenuTextField("Departure", world);
		add(location);
		destination = new MenuTextField("Destination", world);
		add(destination);
		go = new JButton("Go!");
		go.addActionListener(click);
		add(go);

		add(Box.createHorizontalStrut(10));
		help = new JButton("?");
		help.setToolTipText("Open instructions dialog.");
		help.addActionListener(helpButton);
		add(help);
	}

	public void gobutton() throws IOException {
		String adr = location.getText();
		String adr2 = destination.getText();
		if (!adr.equals("Departure") && !adr2.equals("Destination")) {
			new AddressThread(world, adr, adr2).start();
			location.reset();
			destination.reset();
		}
		world.setAutoLand();
	}

	public void togglePauseText() {
		if (play.getText().equals(">")) {
			play.setText("||");
		}
		else {
			play.setText(">");
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

	ActionListener pause = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				world.togglePlay();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

	ActionListener muteButton = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent event) {
			world.toggleMute();
		}
	};

	ActionListener helpButton = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent event) {
			world.openInstructions();
		}
	};
}