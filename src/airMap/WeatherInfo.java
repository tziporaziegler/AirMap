package airMap;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.Format;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class WeatherInfo extends Container {
	private static final long serialVersionUID = 1L;
	private Weather[] weathers;
	private Container currentCont;
	private JLabel currentWeather;
	private Container conditionsCont;
	private Container minMaxCont;
	private JLabel minLabel;
	private JLabel maxLabel;
	private double lat;
	private double log;
	private Format formatter;

	public WeatherInfo(double lat, double log) throws MalformedURLException {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		currentCont = new Container();
		minMaxCont = new Container();
		conditionsCont = new JPanel();
		minLabel = new JLabel();
		maxLabel = new JLabel();
		currentWeather = new JLabel();

		// create thread that uses displayWeather
		formatter = new DecimalFormat("#0.##");
		this.lat = lat;
		this.log = log;
		new WeatherDownloadThread(this, lat, log).start();
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
		currentWeather.setBorder(new EmptyBorder(-10, 0, 0, 0));

		// add corresponding picture with thread
		String urlString = "http://openweathermap.org/img/w/" + weathers[0].getIcon() + ".png";
		new ImgDownloadThread(new URL(urlString), currentWeather).start();

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
		int length = weathers.length;

		// if there is only one weather condition, display the corresponding latitude and longitude
		if (length == 1) {
			
			//increase the length so GridLayout will create a row for the lat/log label
			length++;
			
			char latsym = 'N';
			if (lat < 0) {
				latsym = 'S';
			}
			char logsym = 'E';
			if (log < 0) {
				logsym = 'W';
			}
			
			JLabel label = new JLabel(formatter.format(lat) + "\u00b0" + latsym + " and " + formatter.format(log)
					+ "\u00b0" + logsym);
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBorder(new EmptyBorder(9, 0, 0, 0));
			conditionsCont.add(label);
		}

		conditionsCont.setLayout(new GridLayout(length, 1));

		// add all weather conditions and descriptions that currently exist,
		// with corresponding pictures
		for (Weather i : weathers) {
			JLabel label = new JLabel();
			label.setBorder(new EmptyBorder(-3, 0, -12, 0));
			label.setText(i.getMain() + ": " + i.getDescription());
			label.setFont(new Font("Calibri", Font.PLAIN, 15));
			label.setForeground(Color.DARK_GRAY);

			String urlString = "http://openweathermap.org/img/w/" + i.getIcon() + ".png";
			new ImgDownloadThread(new URL(urlString), label).start();
			conditionsCont.add(label);
		}

		// add the weather condition container to the bottom of the main frame
		add(conditionsCont);
	}
}
