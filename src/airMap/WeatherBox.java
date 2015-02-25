package airMap;

import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class WeatherBox extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel titleLabel;
	private WeatherInfo info;

	public WeatherBox(String title, String lat, String log) throws MalformedURLException {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// FIXME center alignment not working
		// FIXME conditions for some reason move over
		// setAlignmentX(CENTER_ALIGNMENT);
		// setAlignmentY(CENTER_ALIGNMENT);
		titleLabel = new JLabel(title);
		// titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		setBorder(new BevelBorder(BevelBorder.LOWERED));

		add(titleLabel);
		info = new WeatherInfo(lat + "," + log);
		add(info);
	}

	public void update(String address) throws IOException {
		remove(info);
		info = new WeatherInfo(address);
		add(info);
	}

}
