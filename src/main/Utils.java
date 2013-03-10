package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class Utils {

	public static Color calculateAverage(BufferedImage image) {
		int sumRed = 0;
		int sumGreen = 0;
		int sumBlue = 0;
		int sumAlpha = 0;
		final int imageSize = image.getHeight() * image.getWidth();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				Color color = new Color(image.getRGB(x, y), true);
				sumRed += color.getRed();
				sumGreen += color.getGreen();
				sumBlue += color.getBlue();
				sumAlpha += color.getAlpha();
			}
		}
		return new Color(sumRed / imageSize, sumGreen / imageSize, sumBlue
				/ imageSize, sumAlpha / imageSize);
	}
	
	public static BufferedImage resize(BufferedImage source, int width, int height) {
		BufferedImage img = new BufferedImage(width, height, source.getType());
		Graphics2D g = img.createGraphics();
		try {
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(source, 0, 0, width, height, null);
		} finally {
			g.dispose();
		}
		return img;
	}
	
	public static Image scale(BufferedImage source, double factorW,double factorH) {
		Image scaledImage = source.getScaledInstance((int)(source.getWidth()*factorW), (int)(source.getHeight()*factorH), Image.SCALE_SMOOTH);
        BufferedImage imageBuff =  new BufferedImage((int)(source.getWidth()*factorW), (int)(source.getHeight()*factorH), source.getType());
        Graphics g = imageBuff.createGraphics();
        g.drawImage(scaledImage, 0, 0, new Color(0,0,0), null);
        g.dispose();
        return scaledImage;
	}

}
