package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import main.ISColor;
import main.ISImage;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class ImagesCollection {
	private static final String COLLECTION_NAME = "images";
	private static final String DB_NAME = "instashuffle";
	private MongoClient mongo;
	private DB db;
	private DBCollection collection;
	
	public ImagesCollection(String host, int port) throws UnknownHostException {
		mongo = new MongoClient(host,port);
		db = mongo.getDB(DB_NAME);
		collection = db.getCollection(COLLECTION_NAME);
	}
	
	public List<ISImage> getAllImages(){
		List<ISImage> images= new ArrayList<>();
		
		DBCursor find = collection.find();
		while (find.hasNext()) {
			DBObject piece = find.next();
			String path = (String) piece.get("path");
			BasicDBObject color = (BasicDBObject) piece.get("color");
			Integer red = (Integer) color.get("red");
			Integer green = (Integer) color.get("green");
			Integer blue = (Integer) color.get("blue");
			images.add(new ISImage(path, new ISColor(new Color(red, green,
					blue))));
		}
		return images;
		
	}

	public void loadDatabase(File path, boolean wipeAll) {
		if (wipeAll) {
			collection.drop();
		}
		loadDatabase(path);
	}

	private void loadDatabase(File path) {
		if (path.isDirectory()) {
			for (File f : path.listFiles()) {
				loadDatabase(f);
			}
		} else {
			if (Utils.isImage(path.getName())) {
				BufferedImage image;
				try {
					image = ImageIO.read(path);
				} catch (IOException e) {
					image = null;
					return;
				}
				Color color = Utils.calculateAverage(Utils.resize(image,
						image.getWidth() / 5, image.getHeight() / 5));
				collection.insert(new BasicDBObject().append("path",
						path.getAbsolutePath()).append(
						"color",
						new BasicDBObject().append("red", color.getRed())
								.append("green", color.getGreen())
								.append("blue", color.getBlue())));
				image = null;
				System.out.println("loaded "+path.getAbsolutePath());
			}
		}
	}

}
