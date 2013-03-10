package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ImageCache {
	private static final int RESIZE_FACTOR = 64;
	private static Logger LOG = LoggerFactory.getLogger(ImageCache.class);
	
	private Map<String,BufferedImage> usageMap = new HashMap<>();
	
	public BufferedImage get(String key){
		BufferedImage image = usageMap.get(key);
		if(image == null){
			try {
				image = ImageIO.read(new File(key));
			} catch (IOException e) {
				LOG.error("unable to load image");
				return null;
			}
			int factor = image.getWidth() / RESIZE_FACTOR;
			image = Utils.resize(image, image.getWidth() / factor, image.getHeight() / factor);
			usageMap.put(key, image);
		}
		return image;
	}

}
