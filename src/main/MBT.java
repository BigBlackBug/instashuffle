package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import utils.ImageBucketList;
import utils.ImageCache;

public class MBT {

	private static final int DEFAULT_PIECE_WIDTH = 24;
	private static final int DEFAULT_PIECE_HEIGHT = 24;
	private static final int THREAD_COUNT = 8;
	private static final Integer USAGE_THRESHOLD = 100;

	private ImageBucketList images;
	private ImageCache cache;
//	private CountDownLatch latch;
	private ExecutorService executor;
	
	public MBT(ImageBucketList images) {
		this.images = images;
		this.cache = new ImageCache();
//		this.latch = new CountDownLatch(THREAD_COUNT);
	}
	
	public BufferedImage buildMosaic(BufferedImage source, int pieceWidth, int pieceHeight) throws InterruptedException {
		this.executor = Executors.newFixedThreadPool(THREAD_COUNT);
		final int width = source.getWidth();
		final int height = source.getHeight();
		BufferedImage canvas = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < THREAD_COUNT; i++) {
			int blockWidth = width;
			int blockHeight = height/THREAD_COUNT;
			int startY = i*blockHeight;
			int startX = 0;
			
			executor.execute(new MosaicBuilderThread(canvas, source, startX,
					startY, blockWidth, blockHeight, pieceWidth, pieceHeight));
		}
		executor.shutdown();
		executor.awaitTermination(Long.MAX_VALUE,TimeUnit.MILLISECONDS);
//		latch.await();
		return canvas;
	}
	
	public BufferedImage buildMosaic(BufferedImage source) throws InterruptedException {
		return buildMosaic(source, DEFAULT_PIECE_WIDTH, DEFAULT_PIECE_HEIGHT);
	}
	
	private class MosaicBuilderThread implements Runnable{

		private BufferedImage canvas;
		private BufferedImage source;
		private int startX;
		private int startY;
		private int blockWidth;
		private int blockHeight;
		private int pieceHeight;
		private int pieceWidth;
		
		public MosaicBuilderThread(BufferedImage canvas, BufferedImage source,
				int startX, int startY,
				int blockWidth, int blockHeight,
				int pieceWidth, int pieceHeight) {
			this.canvas = canvas;
			this.source = source;
			this.startX = startX;
			this.startY = startY;
			this.blockWidth = blockWidth;
			this.blockHeight = blockHeight;
			this.pieceWidth=pieceWidth;
			this.pieceHeight=pieceHeight;
		}

		public void buildMosaic() {
			final int width = blockWidth;
			final int height = blockHeight;
			int horSteps = width % pieceWidth == 0 ? width
					/ pieceWidth : width / pieceWidth + 1;
			int verSteps = height % pieceHeight == 0 ? height
					/ pieceHeight : height / pieceHeight + 1;
			for (int x = startX; x < horSteps; x++) {
				for (int y = startY; y < verSteps; y++) {
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
					BufferedImage subimage;
					synchronized(source){
						subimage = source.getSubimage(x
								* pieceWidth, y * pieceHeight,
								currentPieceWidth, currentPieceHeight);
					}
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
					synchronized(canvas){
						addPiece(image, x * pieceWidth, y
								* pieceHeight, currentPieceWidth, currentPieceHeight);
					}
				}
			}
		}
		
		private void addPiece(BufferedImage image, int x, int y, int pieceWidth,
				int pieceHeight) {
			Graphics2D g2d = canvas.createGraphics();
			g2d.drawImage(image, x, y, pieceWidth, pieceHeight, null);
			g2d.dispose();
		}

		@Override
		public void run() {
			buildMosaic();
//			latch.countDown();
		}
	}


}
