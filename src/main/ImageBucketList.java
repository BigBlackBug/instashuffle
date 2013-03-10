package main;

import static java.lang.Math.abs;

import java.awt.image.BufferedImage;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeSet;

import comparators.ColorComparator;
import comparators.ImageComparator;

public class ImageBucketList{
	private static final double DEFAULT_THRESHOLD = 30;
	private static final double MULTIPLY_FACTOR = 1.5;
	
	private final HashMap<MyColor, TreeSet<MyImage>> buckets;
	
	public ImageBucketList() {
		buckets = new HashMap<>();
	}
	
	public void put(MyImage image){
		MyColor keyColor = image.getAverageColor();
		TreeSet<MyImage> treeSet = get(keyColor, DEFAULT_THRESHOLD);
		if(treeSet == null){
			TreeSet<MyImage> newSet = new TreeSet<>(new ImageComparator(keyColor));
			newSet.add(image);
			buckets.put(keyColor, newSet);
		}else{
			treeSet.add(image);
		}
	}
	
	private TreeSet<MyImage> get(MyColor key){
		TreeSet<MyImage> treeSet;
		double threshold = DEFAULT_THRESHOLD;
		do{
			 treeSet = get(key, threshold);
			 threshold *= MULTIPLY_FACTOR;
		}while(treeSet==null);
		return treeSet;
	}
	
	public TreeSet<MyImage> get(BufferedImage key){
		return get(new MyColor(Utils.calculateAverage(key)));
	}
	
	private TreeSet<MyImage> get(MyColor key, double threshold){
		if (threshold == 0){
			return buckets.get(key);
		}else{
			ArrayList<MyColor> keys = new ArrayList<>(buckets.keySet());
			if(keys.isEmpty()){
				return null;
			}
			Collections.sort(keys, new ColorComparator(key));
			MyColor closestColor = keys.get(0);
			double diff = MyColor.calculateDistance(closestColor.getColor(),key.getColor());
			if(diff < threshold){
				return buckets.get(closestColor);
			}else{
				return null;
			}
		}
	}

}