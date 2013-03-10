package strings;

import java.awt.Color;
import java.util.Comparator;

import main.MyColor;
import main.MyImage;
public class ImageComparatorString implements Comparator<MyImageString>{
	
	private MyColor color;
	
	public ImageComparatorString(MyColor color) {
		this.color = color;
	}

	@Override
	public int compare(MyImageString image1, MyImageString image2) {
		double d1 = MyColor.calculateDistance(image1.getAverageColor().getColor(), color.getColor());
		double d2 = MyColor.calculateDistance(image2.getAverageColor().getColor(), color.getColor());
		double diff = d1-d2;
		return (int) Math.signum(diff);
	}
	
}