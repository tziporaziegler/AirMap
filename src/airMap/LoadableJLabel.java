package airMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class LoadableJLabel extends JLabel implements ImageLoadable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void setImage(ImageIcon icon) {
		setIcon(icon);
		
	}
}
