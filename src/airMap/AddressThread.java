package airMap;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JLabel;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

//TODO no one used this thread as of now. Only need it to get log and lat of plane.
public class AddressThread extends Thread {
	private String address;
	private String lat;
	private String log;
	private int zoom;
	private String view;

	private JLabel label;

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public AddressThread(String address, int zoom, String view, JLabel label) {
		System.out.println(address);
		this.label = label;
		this.address = address;
		this.zoom = zoom;
		this.view = view;
	}

	public void run() {
		try {
			Gson gson = new Gson();
			URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + address
					+ "&key=AIzaSyAirHEsA08agmW9uizDvXagTjWS3mRctPE");
			URLConnection connection = url.openConnection();
			InputStream in = connection.getInputStream();
			String json = IOUtils.toString(in);
			AdrResults info = gson.fromJson(json, AdrResults.class);
			AdrResult[] results = info.getResults();

			for (AdrResult i : results) {
				AdrLocation location = i.getGeometry().getLocation();
				lat = location.getLat();
				log = location.getLng();
			}

			// TODO street view
			// lat = "46.414382";
			// log = "10.014";
			// int heading = 90;// panaramo postion - use when turn plane
			// int pitch = 10; // use when press up/down arrow
			// String urls =
			// "https://maps.googleapis.com/maps/api/streetview?size=500x700&location="+ lat + "," +
			// log+ "&fov=90&heading=" + heading + "&pitch=" + pitch;

			String half1 = "https://maps.googleapis.com/maps/api/staticmap?center=";
			String half2 = "&size=" + 600 + "x" + 600 + "&maptype=" + view + "&zoom=" + zoom;

			String urls = half1 + lat + "," + log + half2;
			System.out.println("address url" + urls);
			new ImgDownloadThread(new URL(urls), label).start();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
