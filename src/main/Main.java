package main;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.imageio.ImageIO;

public class Main {

	private static final int DEFAULT_PIECE_WIDTH = 16;
	private static final int DEFAULT_PIECE_HEIGHT = 9;
	private static final Integer USAGE_THRESHOLD = 20;

	private ImageBucketList images = new ImageBucketList();
		
	static int counter=0;
	public void initProcessImages(List<MyImage> sourceImages) {
		for (MyImage image : sourceImages) {
			BufferedImage resized = Utils.resize(image.getImage(),
					DEFAULT_PIECE_WIDTH, DEFAULT_PIECE_HEIGHT);
			images.put(new MyImage(resized, image.getId()));
			usageMap.put(image.getId(), -1);
		}
	}
	
	private Map<Integer,Integer> usageMap = new HashMap<>();

	public BufferedImage buildMosaic(BufferedImage source) {
		final int width = source.getWidth();
		System.out.println(width);
		final int height = source.getHeight();
		System.out.println(height);
		BufferedImage canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int horSteps = width % DEFAULT_PIECE_WIDTH == 0 ? width
				/ DEFAULT_PIECE_WIDTH : width / DEFAULT_PIECE_WIDTH + 1;
		int verSteps = height % DEFAULT_PIECE_HEIGHT == 0 ? height
				/ DEFAULT_PIECE_HEIGHT : height / DEFAULT_PIECE_HEIGHT + 1;
		for (int x = 0; x < horSteps ; x++) {
			System.out.println(x);
			for (int y = 0; y < verSteps ; y++) {
				int widthDiff = (x+1)*DEFAULT_PIECE_WIDTH - width;
				int heightDiff = (y+1)*DEFAULT_PIECE_HEIGHT - height;
				int pieceWidth = DEFAULT_PIECE_WIDTH;
				int pieceHeight = DEFAULT_PIECE_HEIGHT;
				if(widthDiff>0){
					pieceWidth = DEFAULT_PIECE_WIDTH-widthDiff;
				}
				if (heightDiff>0){
					pieceHeight = DEFAULT_PIECE_HEIGHT- heightDiff;
				}
				BufferedImage subimage = source.getSubimage(x*DEFAULT_PIECE_WIDTH, y*DEFAULT_PIECE_HEIGHT, pieceWidth, pieceHeight);
				TreeSet<MyImage> treeSet = images.get(subimage);
				Iterator<MyImage> iterator = treeSet.iterator();
				MyImage next = null;
				while(iterator.hasNext()){
					next = iterator.next();
					if(usageMap.get(next.getId())<USAGE_THRESHOLD){
						break;
					}
				}
				if(!iterator.hasNext()){
					next=treeSet.last();
				}
				Integer used = usageMap.get(next.getId());
				
				usageMap.put(next.getId(), used+1);
				addPiece(canvas,next,x*DEFAULT_PIECE_WIDTH, y*DEFAULT_PIECE_HEIGHT,pieceWidth,pieceHeight);
			}
		}
		return canvas;
	}

	private void addPiece(BufferedImage canvas, MyImage first, int x, int y,
			int pieceWidth, int pieceHeight) {
		Graphics2D g2d = canvas.createGraphics();
		g2d.drawImage(first.getImage(), x, y, pieceWidth, pieceHeight, null);
		g2d.dispose();
	}
	
	public static void main(String[] args) throws IOException{
		Main main = new Main();
		File f=new File("D:\\Files\\Pictures\\Nature");
		List<MyImage> images = new ArrayList<>();
		int i=0;
		for(File file:f.listFiles()){
			BufferedImage image = ImageIO.read(file);
			if(image == null || image.getWidth()==-1){
				continue;
			}
			MyImage newImage = new MyImage(image,counter++);
			images.add(newImage);
			System.out.println(i++);
			if(i==100){
				break;
			}
		}
		main.initProcessImages(images);
		BufferedImage buildMosaic = main.buildMosaic(ImageIO.read(new File("D:\\Files\\Pictures\\Nature\\Autumn Leaves.jpg")));
		ImageIO.write(buildMosaic, "png",new File("D:\\Files\\Pictures\\Nature\\5222.jpg"));
		System.out.println(1);

	}

}
