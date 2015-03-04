package airMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

public class AddressThread extends Thread {
	private World world;
	private String address;
	private String address2;
	private Gson gson;
	private String lat;
	private String log;

	public AddressThread(World world, String address, String address2) throws UnsupportedEncodingException {
		this.world = world;
		this.address = URLEncoder.encode(address, "UTF-8");
		this.address2 = URLEncoder.encode(address2, "UTF-8");
		gson = new Gson();
	}

	public void run() {
		try {
			getLatLog(address);
			double lat1 = Double.parseDouble(lat);
			double log1 = Double.parseDouble(log);
			getLatLog(address2);
			double lat2 = Double.parseDouble(lat);
			double log2 = Double.parseDouble(log);
			world.updateLatLog(lat1, log1, lat2, log2);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		// TODO street view
		/* lat = "46.414382"; log = "10.014"; int heading = 90;// panaramo postion - use when turn
		 * plane int pitch = 10; // use when press up/down arrow String urls =
		 * "https://maps.googleapis.com/maps/api/streetview?size=500x700&location=" + lat + "," +
		 * log + "&fov=90&heading=" + heading + "&pitch=" + pitch; String half1 =
		 * "https://maps.googleapis.com/maps/api/staticmap?center="; String half2 = "&size=" + 600 +
		 * "x" + 600 + "&maptype=" + view + "&zoom=" + zoom; String urls = half1 + lat + "," + log +
		 * half2; System.out.println("address url" + urls); new ImgDownloadThread(new URL(urls),
		 * label).start(); */
	}

	private void getLatLog(String address) throws IOException {
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
	}
}
