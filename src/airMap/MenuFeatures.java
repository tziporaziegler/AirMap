package airMap;

import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JMenu;

public class MenuFeatures extends JMenu {
	private static final long serialVersionUID = 1L;
	private NavigationMap map;
	private Font font;

	public MenuFeatures(NavigationMap map) {
		font = new Font("Arial", Font.PLAIN, 12);
		setText("Features");
		setToolTipText("Features to display");
		setFont(font.deriveFont(14f));

		this.map = map;

		String[] options = { "all", "water" };
		String[] optionDescriptions = { "Apply the rule to all selector types.", "Apply the rule to bodies of water." };
		new MenuSubmenu(this, options, optionDescriptions);

		addAdmnMenu();
		addLandscapeMenu();
		addPOIMenu();
		addRoadMenu();
		addTransitMenu();
	}

	private void addAdmnMenu() {
		String[] admnOptions = { "all", "country", "land_parcel", "locatity", "neighborhood", "province" };
		String[] admnOptDes = { "Apply the rule to administrative areas.", "Apply the rule to countries.",
				"Apply the rule to land parcels.", "Apply the rule to localities.", "Apply the rule to neighborhoods.",
				"Apply the rule to provinces" };
		add(new MenuSubmenu("administrative", admnOptions, admnOptDes));
	}

	private void addLandscapeMenu() {
		String[] landOptions = { "all", "man_made" };
		String[] landOptDes = { "Apply the rule to landscapes.", "Apply the rule to man made structures." };

		ArrayList<JMenu> submenus = new ArrayList<JMenu>();
		String[] naturalOpt = { "all", "landcover", "terrain" };
		String[] naturalOptDes = { "Apply the rule to natural features.", "Apply the rule to landcover.",
				"Apply the rule to terrain." };
		submenus.add(new MenuSubmenu("natural", naturalOpt, naturalOptDes));

		submenus.add(new MenuElement());
		add(new MenuSubmenu("landscape", landOptions, landOptDes, submenus));
	}

	// points of interest
	private void addPOIMenu() {
		String[] options = { "all", "attraction", "business", "government", "medical", "park", "place_of_worship",
				"school", "sports_complex" };
		String[] optionDescriptions = { "Apply the rule to points of interest.",
				"Apply the rule to attractions for tourists.", "Apply the rule to businesses.",
				"Apply the rule to government buildings.",
				"Apply the rule to emergency services (hospitals, pharmacies, police, doctors, etc).",
				"Apply the rule to parks.",
				"Apply the rules to places of worship, such as churches, temples, or mosques.",
				"Apply the rule to schools.", "Apply the rule to sports complexes." };

		ArrayList<JMenu> submenus = new ArrayList<JMenu>();
		submenus.add(new MenuElement());

		add(new MenuSubmenu("poi", options, optionDescriptions, submenus));
	}

	private void addRoadMenu() {
		String[] options = { "all", "local" };
		String[] optionDescriptions = { "Apply the rule to all roads.", "Apply the rule to local roads." };

		String[] hwyOptions = { "all", "controlled_access" };
		String[] hwyOptDes = { "Apply the rule to highways.", "Apply the rule to controlled-access highways." };
		ArrayList<JMenu> submenus = new ArrayList<JMenu>();
		submenus.add(new MenuSubmenu("highway", hwyOptions, hwyOptDes));

		submenus.add(new MenuElement());

		add(new MenuSubmenu("road", options, optionDescriptions, submenus));
	}

	private void addTransitMenu() {
		String[] options = { "all", "line" };
		String[] optionDescriptions = { "Apply the rule to all transit stations and lines.",
				"Apply the rule to all transit lines." };

		String[] stationOptions = { "all", "airport", "bus", "rail" };
		String[] stationOptDes = { "Apply the rule to all transit stations.", "Apply the rule to airports.",
				"Apply the rule to bus stops.", "Apply the rule to rail stations." };
		ArrayList<JMenu> submenus = new ArrayList<JMenu>();
		submenus.add(new MenuSubmenu("station", stationOptions, stationOptDes));

		submenus.add(new MenuElement());

		add(new MenuSubmenu("transit", options, optionDescriptions, submenus));
	}
}