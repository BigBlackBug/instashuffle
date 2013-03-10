package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.TreeSet;

import utils.ImageBucketList;
import utils.ImageCache;

public class MosaicBuilder {
	private static final int DEFAULT_PIECE_WIDTH = 24;
	private static final int DEFAULT_PIECE_HEIGHT = 24;
	private static final Integer USAGE_THRESHOLD = 100;

	private ImageBucketList images;
	private ImageCache cache;
	
	public MosaicBuilder(ImageBucketList images) {
		this.images = images;
		this.cache = new ImageCache();
	}
	public BufferedImage buildMosaic(BufferedImage source, int pieceWidth, int pieceHeight) {
		final int width = source.getWidth();
		final int height = source.getHeight();
		BufferedImage canvas = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		int horSteps = width % pieceWidth == 0 ? width
				/ pieceWidth : width / pieceWidth + 1;
		int verSteps = height % pieceHeight == 0 ? height
				/ pieceHeight : height / pieceHeight + 1;
		for (int x = 0; x < horSteps; x++) {
			System.out.println(x);
			for (int y = 0; y < verSteps; y++) {
				int widthDiff = (x + 1) * pieceWidth - width;
				int heightDiff = (y + 1) * pieceHeight - height;
				int currentPieceWidth = pieceWidth;
				int currentPieceHeight = pieceHeight;
				if (widthDiff > 0) {
					currentPieceWidth = pieceWidth - widthDiff;
				}
				if (heightDiff > 0) {
					currentPieceHeight = pieceHeight - heightDiff;
				}
				BufferedImage subimage = source.getSubimage(x
						* pieceWidth, y * pieceHeight,
						currentPieceWidth, currentPieceHeight);
				TreeSet<ISImage> treeSet = images.get(subimage);
				
				BufferedImage image = null;
				Iterator<ISImage> iter = treeSet.iterator();
				while(iter.hasNext() && image == null){
					ISImage next = iter.next();
					image = cache.get(next.getPath());
				}
				if(image == null){
					throw new RuntimeException("GG");
				}
				addPiece(canvas, image, x * pieceWidth, y
						* pieceHeight, currentPieceWidth, currentPieceHeight);
			}
		}
		return canvas;
	}
	
	public BufferedImage buildMosaic(BufferedImage source) {
		return buildMosaic(source, DEFAULT_PIECE_WIDTH, DEFAULT_PIECE_HEIGHT);
	}
	
	private void addPiece(BufferedImage canvas,
			BufferedImage image, int x, int y, int pieceWidth,
			int pieceHeight) {
		Graphics2D g2d = canvas.createGraphics();
		g2d.drawImage(image, x, y, pieceWidth, pieceHeight, null);
		g2d.dispose();
	}

}
