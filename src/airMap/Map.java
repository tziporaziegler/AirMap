package airMap;

import java.awt.Image;

import javax.swing.JPanel;

public class Map extends JPanel{
	private Image img;
	public Map(){
		
	}

	public void setImage(Image img){
		this.img=img;
	}
	public Image getImage(){
		return img;
	}
}
