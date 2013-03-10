package strings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import main.MyColor;
import main.Pair;
import main.Utils;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class InitDatabase {

	public static MongoClient mongo;
	public static DB db;
	public static DBCollection collection;
	static {
		try {
			mongo = new MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		db = mongo.getDB("instashuffle");
		collection = db.getCollection("images");
	}
	private static final int DEFAULT_PIECE_WIDTH = 8;
	private static final int DEFAULT_PIECE_HEIGHT = 8;
//	private static final Integer USAGE_THRESHOLD = 100;
	private static final Integer USAGE_THRESHOLD = 100;

	public static BufferedImage buildMosaic(ImageBucketListString images,
			BufferedImage source) {
		final int width = source.getWidth();
		final int height = source.getHeight();
		BufferedImage canvas = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		int horSteps = width % DEFAULT_PIECE_WIDTH == 0 ? width
				/ DEFAULT_PIECE_WIDTH : width / DEFAULT_PIECE_WIDTH + 1;
		int verSteps = height % DEFAULT_PIECE_HEIGHT == 0 ? height
				/ DEFAULT_PIECE_HEIGHT : height / DEFAULT_PIECE_HEIGHT + 1;
		for (int x = 0; x < horSteps; x++) {
			System.out.println(x);
			for (int y = 0; y < verSteps; y++) {
				int widthDiff = (x + 1) * DEFAULT_PIECE_WIDTH - width;
				int heightDiff = (y + 1) * DEFAULT_PIECE_HEIGHT - height;
				int pieceWidth = DEFAULT_PIECE_WIDTH;
				int pieceHeight = DEFAULT_PIECE_HEIGHT;
				if (widthDiff > 0) {
					pieceWidth = DEFAULT_PIECE_WIDTH - widthDiff;
				}
				if (heightDiff > 0) {
					pieceHeight = DEFAULT_PIECE_HEIGHT - heightDiff;
				}
				BufferedImage subimage = source.getSubimage(x
						* DEFAULT_PIECE_WIDTH, y * DEFAULT_PIECE_HEIGHT,
						pieceWidth, pieceHeight);
				TreeSet<MyImageString> treeSet = images.get(subimage);
//				// if(y==85){
//				// System.out.println(1);
//				// }
//				Iterator<MyImageString> iterator = treeSet.iterator();
//				MyImageString next = null;
//				while (iterator.hasNext()) {
//					next = iterator.next();
//					if (usageMap.get(next.getImage()) < USAGE_THRESHOLD) {
//						break;
//					}
//				}
//				if (!iterator.hasNext()) {
//					next = treeSet.last();
//				}
//				Integer used = usageMap.get(next.getImage());
//
//				usageMap.put(next.getImage(), used + 1);
				addPiece(canvas, treeSet.first(), x * DEFAULT_PIECE_WIDTH, y
						* DEFAULT_PIECE_HEIGHT, pieceWidth, pieceHeight);
			}
//			try {
//				Thread.sleep(70);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			count++;
		}
		return canvas;
	}
//	private static int count=0;
	private static Map<String,BufferedImage> usageMap = new HashMap<>();
//	private static Map<String,Pair<BufferedImage,Integer>> usageMap = new HashMap<>();
	private static void addPiece(BufferedImage canvas,
			MyImageString myImageString, int x, int y, int pieceWidth,
			int pieceHeight) {
		Graphics2D g2d = canvas.createGraphics();
		String path = myImageString.getPath();
		
//		Pair<BufferedImage, Integer> p = usageMap.get(path);
		BufferedImage p = usageMap.get(path);

		if (p == null) {
			try {
//				BufferedImage image = ImageIO.read(new File(path));
//				p = new Pair<BufferedImage, Integer>(image, 0);
				p = ImageIO.read(new File(path));
				usageMap.put(path, p);
			} catch (IOException e) {
				return;
			}
		}
//		else{
//			p.setSecond(0);
//		}
//		g2d.drawImage(p.getFirst(), x, y, pieceWidth, pieceHeight, null);
		g2d.drawImage(p, x, y, pieceWidth, pieceHeight, null);
		g2d.dispose();
//		Iterator<String> i = usageMap.keySet().iterator();
//		while(i.hasNext()){
//			String key = i.next();
//			Pair<BufferedImage, Integer> pair = usageMap.get(key);
//			if(pair.getSecond()==USAGE_THRESHOLD){
//				i.remove();
//				System.out.println("remov");
//			}else{
//				pair.setSecond(pair.getSecond()+1);
//			}
//		}
	}

	public static void main(String[] args) throws IOException {
		ImageBucketListString s = new ImageBucketListString();
		DBCursor find = collection.find();
		while (find.hasNext()) {
			DBObject piece = find.next();
			String path = (String) piece.get("path");
			BasicDBObject color = (BasicDBObject) piece.get("color");
			Integer red = (Integer) color.get("red");
			Integer green = (Integer) color.get("green");
			Integer blue = (Integer) color.get("blue");
			s.put(new MyImageString(path, new MyColor(new Color(red, green,
					blue))));
//			usageMap.put(path, -1);
		}
		BufferedImage mosaic = buildMosaic(s, ImageIO.read(new File(
				"D:\\Files\\Pictures\\Nature\\Autumn Leaves.jpg")));
		ImageIO.write(mosaic, "png", new File("D:\\Files\\Pictures\\Nature\\52222.jpg"));
	}

	private static void loadDatabase(File dir) {
		if (dir.isDirectory()) {
			for (File f : dir.listFiles()) {
				loadDatabase(f);
			}
		} else {
			if (dir.getPath().endsWith(".png")
					|| dir.getPath().endsWith(".jpg")
					|| dir.getPath().endsWith(".PNG")
					|| dir.getPath().endsWith(".JPG")) {
				BufferedImage image;
				try {
					image = ImageIO.read(dir);
				} catch (IOException e) {
					return;
				}
				Color color = Utils.calculateAverage(Utils.resize(image,
						image.getWidth() / 5, image.getHeight() / 5));
				collection.insert(new BasicDBObject().append("path",
						dir.getAbsolutePath()).append(
						"color",
						new BasicDBObject().append("red", color.getRed())
								.append("green", color.getGreen())
								.append("blue", color.getBlue())));
			}
		}
	}
}
