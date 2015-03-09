package airMap;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Plane {
	private int x;
	private int y;
	private int degrees;
	private BufferedImage planeImg;

	public Plane(int x, int y) throws IOException {
		this.x = x;
		this.y = y;
		planeImg = ImageIO.read(getClass().getResource("pics/airplane.jpg"));
	}

	public void setDegree(int direction) {
		switch (direction) {
			case 2: {
				degrees = 135;
				break;
			}
			case 4: {
				degrees = -135;
				break;
			}
			case 6: {
				degrees = 45;
				break;
			}
			case 8: {
				degrees = -45;
				break;
			}
		}
	}

	public void paintComponent(Graphics g) {
		AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(degrees), planeImg.getWidth() / 2,
				planeImg.getHeight() / 2);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		g.drawImage(op.filter(planeImg, null), x, y, 20, 20, null);
	}

	public void reset() {
		x = 150;
		y = 272/2-10;
	}

	public void changeY(int difference) {
		y += difference;
	}

	public void changeX(int difference) {
		x += difference;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}