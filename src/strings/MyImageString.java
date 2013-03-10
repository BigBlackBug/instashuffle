package strings;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.MyColor;
import main.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyImageString {
	private static final int RESIZE_FACTOR = 64;
	private static Logger LOG = LoggerFactory.getLogger(MyImageString.class);
	private final File pathToSource;
	private final MyColor averageColor;
	private BufferedImage compressedImage;
	
	public MyImageString(String pathToSource, MyColor averageColor) {
		this.pathToSource = new File(pathToSource);
		this.averageColor = averageColor;;
	}

	public BufferedImage getImage(){
		if(compressedImage == null){
			BufferedImage image;
			try {
				image = ImageIO.read(pathToSource);
			} catch (IOException e) {
				LOG.error("unable to load image");
				return null;
			}
			int factor = image.getWidth() / RESIZE_FACTOR;
			this.compressedImage = Utils.resize(image, image.getWidth() / factor, image.getHeight() / factor);
		}
		return compressedImage;
		
	}
	
	public File getPath() {
		return pathToSource;
	}

	public MyColor getAverageColor() {
		return averageColor;
	}

}