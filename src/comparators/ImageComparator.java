package comparators;

import java.awt.Color;
import java.util.Comparator;

import main.MyColor;
import main.MyImage;
public class ImageComparator implements Comparator<MyImage>{
	
	private MyColor color;
	
	public ImageComparator(MyColor color) {
		this.color = color;
	}

	@Override
	public int compare(MyImage image1, MyImage image2) {
		double d1 = MyColor.calculateDistance(image1.getAverageColor().getColor(), color.getColor());
		double d2 = MyColor.calculateDistance(image2.getAverageColor().getColor(), color.getColor());
		double diff = d1-d2;
		return (int) Math.signum(diff);
	}
	
}