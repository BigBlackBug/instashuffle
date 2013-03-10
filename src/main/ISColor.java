package main;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.awt.Color;


public class ISColor {

	private final Color color;
	private double average = -1;
	
	public ISColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public double getAverage() {
		if(average == -1){
			average = calculateAverage(color);
		}
		return average;
	}
	
	public static double calculateAverage(Color color){
		return (color.getAlpha() 
				+ color.getBlue()
				+ color.getGreen() 
				+ color.getRed()) / 4;
	}
	
	public static double calculateDistance(Color from, Color to) {
		return sqrt(pow(from.getRed() - to.getRed(), 2)
				+ pow(from.getBlue() - to.getBlue(), 2)
				+ pow(from.getGreen() - to.getGreen(), 2)
				+ pow(from.getAlpha() - to.getAlpha(), 2));
	}
	

}
