package airMap;

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

	public WeatherBox(String title, double lat, double lon) throws MalformedURLException {
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
		info = new WeatherInfo(lat, lon);
		add(info);
	}

	public void update(double lat, double lon) throws IOException {
		remove(info);
		info = new WeatherInfo(lat, lon);
		add(info);
	}

}
