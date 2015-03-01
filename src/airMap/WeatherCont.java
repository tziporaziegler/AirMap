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
	
	public WeatherCont() throws MalformedURLException {
		setPreferredSize(new Dimension(250,600));
		setLayout(new GridLayout(3, 1));
		
		depWeather = new WeatherBox("Departure", "74.0059", "40.7127");
		add(depWeather);
		desWeather = new WeatherBox("Destination", "0", "0");
		add(desWeather);
		currWeather = new WeatherBox("Current", "40.7127", "74.0059");
		add(currWeather);
	}

	public void updateAll(String address, String address2) throws IOException {
		depWeather.update(address);
		desWeather.update(address2);
		currWeather.update(address);
	}
	
	public void updateCurrent(int lat, int log) throws IOException{
		currWeather.update(lat + "," + log);
	}
}