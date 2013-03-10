package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import utils.ImageBucketList;
import utils.ImagesCollection;

public class Main {

	public static void main(String[] args) throws IOException {
		ImagesCollection mongo = new ImagesCollection("localhost", 27017);
		List<ISImage> sourceImages = mongo.getAll();
		ImageBucketList buckets = new ImageBucketList(sourceImages);
		MosaicBuilder builder = new MosaicBuilder(buckets);
		BufferedImage mosaic = builder.buildMosaic(ImageIO.read(new File(
				"D:\\Files\\Pictures\\Nature\\monkey.jpg")),48,48);
		ImageIO.write(mosaic, "png", new File("D:\\Files\\Pictures\\Nature\\monkey48.jpg"));

	}

}
