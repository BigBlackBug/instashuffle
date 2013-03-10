package main;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class MyImage {
	private final BufferedImage image;
	private final int id;
	private final MyColor averageColor;

	public MyImage(BufferedImage image, int id) {
		this.image = image;
		this.id = id;
		this.averageColor = new MyColor(Utils.calculateAverage(image));
	}

	public BufferedImage getImage() {
		return image;
	}

	public int getId() {
		return id;
	}

	public MyColor getAverageColor() {
		return averageColor;
	}

}