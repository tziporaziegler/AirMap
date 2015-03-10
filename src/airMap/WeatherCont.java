package airMap;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.MalformedURLException;

public class WeatherCont extends Container {
	private static final long serialVersionUID = 1L;
	private WeatherBox depWeather;
	private WeatherBox desWeather;
	private WeatherBox currWeather;

	public WeatherCont(double currentLat, double currentLog)
			throws MalformedURLException {
		setPreferredSize(new Dimension(260, 600));
		setLayout(new GridLayout(3, 1));

		depWeather = new WeatherBox("Departure", currentLat, currentLog);
		add(depWeather);
		desWeather = new WeatherBox("Destination", 0.0, 0);
		add(desWeather);
		currWeather = new WeatherBox("Current", currentLat, currentLog);
		add(currWeather);
	}

	public void updateAll(double lat, double lon, double lat2, double lon2)
			throws IOException {
		depWeather.update(lat, lon);
		desWeather.update(lat2, lon2);
		currWeather.update(lat, lon);
	}

	public void updateCurrent(double currentLat, double currentLog)
			throws IOException {
		currWeather.update(currentLat, currentLog);
	}
}