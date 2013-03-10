package comparators;

import java.util.Comparator;

import main.MyColor;

public class ColorComparator implements Comparator<MyColor>{
	
	private MyColor color;
	
	public ColorComparator(MyColor color) {
		this.color = color;
	}

	@Override
	public int compare(MyColor color1, MyColor color2) {
		double d1 = MyColor.calculateDistance(color1.getColor(), color.getColor());
		double d2 = MyColor.calculateDistance(color2.getColor(), color.getColor());
		double diff = d1-d2;
		return (int) Math.signum(diff);
	}
	
}