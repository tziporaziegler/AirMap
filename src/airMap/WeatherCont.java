package airMap;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

public class WeatherCont extends Container {
	private static final long serialVersionUID = 1L;

	public WeatherCont() {
		setPreferredSize(new Dimension(250,600));
		setLayout(new GridLayout(3, 1));
	}
}