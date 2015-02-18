package airMap;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ImgDownloadThread extends Thread {
	private URL url;
	private JLabel label;

	public ImgDownloadThread(URL url, JLabel currentWeather) {
		this.url = url;
		this.label = currentWeather;
	}

	@Override
	public void run() {
		ImageIcon icon = new ImageIcon(url);
		label.setIcon(icon);
	}
}
