package comparators;

import java.util.Comparator;

import main.ISColor;


public class ColorComparator implements Comparator<ISColor>{
	
	private ISColor color;
	
	public ColorComparator(ISColor color) {
		this.color = color;
	}

	@Override
	public int compare(ISColor color1, ISColor color2) {
		double d1 = ISColor.calculateDistance(color1.getColor(), color.getColor());
		double d2 = ISColor.calculateDistance(color2.getColor(), color.getColor());
		double diff = d1-d2;
		return (int) Math.signum(diff);
	}
	
}