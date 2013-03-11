package utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import main.ISColor;
import main.ISImage;


import comparators.ColorComparator;
import comparators.ImageComparator;

public class ImageBucketList {
	private static final double DEFAULT_THRESHOLD = 30;
	private static final double MULTIPLY_FACTOR = 1.5;

	private final HashMap<ISColor, TreeSet<ISImage>> buckets;

	public ImageBucketList() {
		buckets = new HashMap<>();
	}

	public ImageBucketList(List<ISImage> all) {
		this();
		for (ISImage image : all) {
			put(image);
		}
	}

	public HashMap<ISColor, TreeSet<ISImage>> getBuckets() {
		return buckets;
	}

	public synchronized void put(ISImage image) {
		ISColor keyColor = image.getAverageColor();
		TreeSet<ISImage> treeSet = get(keyColor, DEFAULT_THRESHOLD);
		if (treeSet == null) {
			TreeSet<ISImage> newSet = new TreeSet<>(
					new ImageComparator(keyColor));
			newSet.add(image);
			buckets.put(keyColor, newSet);
		} else {
			treeSet.add(image);
		}
	}

	private TreeSet<ISImage> get(ISColor key) {
		TreeSet<ISImage> treeSet;
		double threshold = DEFAULT_THRESHOLD;
		do {
			treeSet = get(key, threshold);
			threshold *= MULTIPLY_FACTOR;
		} while (treeSet == null);
		return treeSet;
	}

	public TreeSet<ISImage> get(BufferedImage key) {
		return get(new ISColor(Utils.calculateAverage(key)));
	}

	private synchronized TreeSet<ISImage> get(ISColor key, double threshold) {
		if (threshold == 0) {
			return buckets.get(key);
		} else {
			ArrayList<ISColor> keys = new ArrayList<>(buckets.keySet());
			if (keys.isEmpty()) {
				return null;
			}
			Collections.sort(keys, new ColorComparator(key));
			ISColor closestColor = keys.get(0);
			double diff = ISColor.calculateDistance(closestColor.getColor(),
					key.getColor());
			if (diff < threshold) {
				return buckets.get(closestColor);
			} else {
				return null;
			}
		}
	}

}