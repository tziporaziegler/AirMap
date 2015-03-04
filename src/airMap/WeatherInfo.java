package airMap;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JLabel;

public class WeatherInfo extends Container {
	private static final long serialVersionUID = 1L;
	private Weather[] weathers;
	private Container currentCont;
	private JLabel currentWeather;
	private Container conditionsCont;
	private Container minMaxCont;
	private JLabel minLabel;
	private JLabel maxLabel;

	public WeatherInfo(double lat, double lon) throws MalformedURLException {
		setLayout(new GridLayout(3, 1));

		currentCont = new Container();
		minMaxCont = new Container();
		conditionsCont = new Container();
		minLabel = new JLabel();
		maxLabel = new JLabel();
		currentWeather = new JLabel();

		// create thread that uses displayWeather
		new WeatherDownloadThread(this, lat, lon).start();
	}

	public void displayWeather(WeatherNow now) throws MalformedURLException {
		weathers = now.getWeather();
		setCurrentWeather(now);
		addMinMax(now);
		listAllCurrentConditions();
	}

	public void setCurrentWeather(WeatherNow now) throws MalformedURLException {
		currentCont.setLayout(new GridBagLayout());

		// retrieve the current temperature
		double temp = now.getMain().getTemp();
		currentWeather.setText(String.valueOf(temp) + "\u00b0" + 'F');

		// temperature turn red if hot and blue if cold
		if (temp <= 40) {
			currentWeather.setForeground(Color.decode("#0099FF"));
		}
		else if (temp >= 70) {
			currentWeather.setForeground(Color.decode("#FF5050"));
		}

		// format current temperature
		currentWeather.setFont(new Font("Gill Sans", Font.BOLD, 30));

		// add corresponding picture with thread
		String urlString = "http://openweathermap.org/img/w/" + weathers[0].getIcon() + ".png";
		ImgDownloadThread thread = new ImgDownloadThread(new URL(urlString), currentWeather);
		thread.start();

		currentCont.add(currentWeather);

		add(currentCont);
	}

	public void addMinMax(WeatherNow now) {
		minMaxCont.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

		WeatherMain main = now.getMain();

		// add today's lowest temperature to minMax container
		minLabel.setText("LOW: " + String.valueOf(main.getTemp_min()));
		minMaxCont.add(minLabel);

		// add today's highest temperature to minMax container
		maxLabel.setText("HIGH: " + String.valueOf(main.getTemp_max()));
		minMaxCont.add(maxLabel);

		// add minMax container to the center of the main frame
		add(minMaxCont);
	}

	public void listAllCurrentConditions() throws MalformedURLException {
		// conditionsCont.setLayout(new BoxLayout(conditionsCont, BoxLayout.Y_AXIS));

		// add all weather conditions and descriptions that currently exist,
		// with corresponding pictures
		// for (Weather i : weathers) {
		Weather i = weathers[0];
		
		JLabel label = new JLabel();
		label.setText(i.getMain() + ": " + i.getDescription());
		label.setFont(new Font("Calibri", Font.PLAIN, 16));
		label.setForeground(Color.DARK_GRAY);

		String urlString = "http://openweathermap.org/img/w/" + i.getIcon() + ".png";
		ImgDownloadThread thread = new ImgDownloadThread(new URL(urlString), label);
		thread.start();
		
		add(label);
		// conditionsCont.add(label);
		// }

		// add the weather condition container to the bottom of the main frame
		// add(conditionsCont);
	}
}
