package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import utils.ImageBucketList;
import utils.ImagesCollection;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		ImagesCollection mongo = new ImagesCollection("localhost", 27017);
		List<ISImage> sourceImages = mongo.getAllImages();
		ImageBucketList buckets = new ImageBucketList(sourceImages);
		long before=System.currentTimeMillis();
//		MosaicBuilder builder = new MosaicBuilder(buckets);
		MBT builder = new MBT(buckets);
		BufferedImage mosaic = builder.buildMosaic(ImageIO.read(new File(
				"D:\\Files\\Pictures\\Nature\\monkey.jpg")),16,16);
		long after = System.currentTimeMillis();
		System.out.println(after-before+" 8 threads");
		
		before=System.currentTimeMillis();
		MosaicBuilder builder2 = new MosaicBuilder(buckets);
		BufferedImage mosaic2 = builder2.buildMosaic(ImageIO.read(new File(
				"D:\\Files\\Pictures\\Nature\\monkey.jpg")),16,16);
		after = System.currentTimeMillis();
		System.out.println(after-before+" non-threaded");
//		ImageIO.write(mosaic, "png", new File("D:\\Files\\Pictures\\Nature\\monkey16.jpg"));

	}

}
