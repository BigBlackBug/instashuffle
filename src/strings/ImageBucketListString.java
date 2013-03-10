package strings;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeSet;

import main.MyColor;
import main.MyImage;
import main.Utils;

import comparators.ColorComparator;
import comparators.ImageComparator;

public class ImageBucketListString{
	private static final double DEFAULT_THRESHOLD = 30;
	private static final double MULTIPLY_FACTOR = 1.5;
	
	private final HashMap<MyColor, TreeSet<MyImageString>> buckets;
	
	public ImageBucketListString() {
		buckets = new HashMap<>();
	}
	public HashMap<MyColor, TreeSet<MyImageString>> getBuckets() {
		return buckets;
	}
	public void put(MyImageString image){
		MyColor keyColor = image.getAverageColor();
		TreeSet<MyImageString> treeSet = get(keyColor, DEFAULT_THRESHOLD);
		if(treeSet == null){
			TreeSet<MyImageString> newSet = new TreeSet<>(new ImageComparatorString(keyColor));
			newSet.add(image);
			buckets.put(keyColor, newSet);
		}else{
			treeSet.add(image);
		}
	}
	
	private TreeSet<MyImageString> get(MyColor key){
		TreeSet<MyImageString> treeSet;
		double threshold = DEFAULT_THRESHOLD;
		do{
			 treeSet = get(key, threshold);
			 threshold *= MULTIPLY_FACTOR;
		}while(treeSet==null);
		return treeSet;
	}
	
	public TreeSet<MyImageString> get(BufferedImage key){
		return get(new MyColor(Utils.calculateAverage(key)));
	}
	
	private TreeSet<MyImageString> get(MyColor key, double threshold){
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