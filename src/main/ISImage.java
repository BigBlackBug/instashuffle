package main;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ISImage {
	private final String pathToSource;
	private final ISColor averageColor;
	
	public ISImage(String pathToSource, ISColor averageColor) {
		this.pathToSource = pathToSource;
		this.averageColor = averageColor;;
	}
	
	public String getPath() {
		return pathToSource;
	}

	public ISColor getAverageColor() {
		return averageColor;
	}

}