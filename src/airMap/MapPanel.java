package airMap;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class MapPanel extends JPanel  implements ImageLoadable{
	private static final long serialVersionUID = 1L;
	protected int width;
	protected int height;
	protected Image img;
	protected String view;

	public MapPanel() {
		setBorder(new BevelBorder(BevelBorder.LOWERED));
	}


	@Override
	public void setImage(ImageIcon icon) {
		this.img=icon.getImage();
		
	}
	
}