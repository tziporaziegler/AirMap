package airMap;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

public class WeatherDownloadThread extends Thread {
	private WeatherInfo box;
	private URL url;

	public WeatherDownloadThread(WeatherInfo box, String address) throws MalformedURLException {
		this.box = box;
		System.out.println("weather url:" + url);
		url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + address + "&units=imperial");
		// url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + lat + "," + log +
		// "&units=imperial");
	}

	@Override
	public void run() {
		try {
			// retrieve weather data from online
			URLConnection connection = url.openConnection();
			// TODO remove println
			System.out.println("run url:" + url);
			InputStream in = connection.getInputStream();
			String json = IOUtils.toString(in);
			Gson gson = new Gson();
			WeatherNow now = gson.fromJson(json, WeatherNow.class);

			box.displayWeather(now);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
