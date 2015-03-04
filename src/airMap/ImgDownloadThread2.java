package airMap;

import java.net.URL;

import javax.swing.ImageIcon;

public class ImgDownloadThread2 extends Thread {
	private URL url;
	private CenterMap map;

	public ImgDownloadThread2(URL url, CenterMap map) {
		this.url = url;
		this.map = map;
	}

	@Override
	public void run() {
		//TODO remove println
		System.out.println("NEW Center Img: " + url);
		map.setImage(new ImageIcon(url).getImage());
	}
}
