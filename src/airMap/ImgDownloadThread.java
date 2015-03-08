package airMap;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class ImgDownloadThread extends Thread {
	private URL url;
	private JComponent component;

	public ImgDownloadThread(URL url, JComponent component) {
		this.url = url;
		this.component = component;
	}

	@Override
	public void run() {
		System.out.println(url);
		ImageIcon icon = new ImageIcon(url);
		if(component instanceof JLabel){
		((JLabel) component).setIcon(icon);
		}
		else if (component instanceof CenterMap){
			((CenterMap) component).setImage(icon.getImage());
		}
		else if (component instanceof Map){
			((Map) component).setImage(icon.getImage());
		}
	}
}
