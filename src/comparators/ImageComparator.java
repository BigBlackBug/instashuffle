package comparators;

import java.util.Comparator;

import main.ISColor;
import main.ISImage;
public class ImageComparator implements Comparator<ISImage>{
	
	private ISColor color;
	
	public ImageComparator(ISColor color) {
		this.color = color;
	}

	@Override
	public int compare(ISImage image1, ISImage image2) {
		double d1 = ISColor.calculateDistance(image1.getAverageColor().getColor(), color.getColor());
		double d2 = ISColor.calculateDistance(image2.getAverageColor().getColor(), color.getColor());
		double diff = d1-d2;
		return (int) Math.signum(diff);
	}
	
}