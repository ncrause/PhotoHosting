/*
 * Copyright (C) 2019 Nathan Crause <nathan@crause.name>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package photohosting.services;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import photohosting.services.beans.Images;

/**
 * Performs some of the raw image IO.
 * 
 * The default compression ratio is derived from:
 * http://fotoforensics.com/tutorial-estq.php
 * but the 80% results in some finer detail loss, so it was slightly bumped up
 * based on http://pagepipe.com/quality-82-image-compression-change-for-wordpress/
 * 
 * @author Nathan Crause <nathan@crause.name>
 */
public class ImageService {
	
	public static final int MEGAPIXEL_LIMIT = 1920000;
	
	/**
	 * Performs the entire process of preparing an image for storage in HBase,
	 * such as scaling and compressing, specifically for the primary large,
	 * high-quality image. All the smaller versions are generated with other
	 * methods.
	 * 
	 * @param source the URL from whence we should read
	 * @return the scaled/compressed "high resolution" image.
	 * @throws IOException 
	 */
	public Images prepare(URL source) throws IOException {
		Images images = new Images();
		BufferedImage image = ImageIO.read(source);
		Dimension size = new Dimension(image.getWidth(), image.getHeight());
		
		images.setHighResolution(toJPG(scale(image), 82));
		images.setOpengraphImage(toJPG(scale(image, calculateOG(size)), 20));
		images.setRedditImage(toJPG(scale(image, calculateReddit(size)), 40));
		images.setTwitterImage(toJPG(scale(image, calculateTwitter(size)), 20));
		
		return images;
	}
	
	/**
	 * This version of the <code>scale</code> method is the one primarily
	 * concerned with automatically sizing any source image down to within
	 * the <code>MEGAPIXEL_LIMIT</code> pixel limit.
	 * 
	 * @param image
	 * @return 
	 */
	public BufferedImage scale(BufferedImage image) {
		Dimension size = new Dimension(image.getWidth(), image.getHeight());
		Dimension target = calculate2MB(size);
		BufferedImage result = new BufferedImage(target.width, target.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = result.createGraphics();
		
		// possibly BICUBIC would yield cleaner results, but the cost from 4 neightbours to 9 means significant CPU, and at no real benefit when downsizing.
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		graphics.drawImage(image, 0, 0, target.width, target.height, null);
		graphics.dispose();
		
		return result;
	}
	
	/**
	 * This is a low-quality version of <code>scale</code> specifically intended
	 * for the lower-quality versions of the images
	 * 
	 * @param image
	 * @param target
	 * @return 
	 */
	public BufferedImage scale(BufferedImage image, Dimension target) {
		Dimension size = new Dimension(image.getWidth(), image.getHeight());
		// since this method is intended to support cropping (such as would
		// be required for opengraph and twitter) we need to first just scale
		// down
		Dimension scaled = calculateScaled(size, target);
		
		BufferedImage result = new BufferedImage(scaled.width, scaled.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = result.createGraphics();
		
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		
		graphics.drawImage(image, 0, 0, scaled.width, scaled.height, null);
		
		graphics.dispose();
		
		// determine the left and top positions of the crop
		int x = (int) ((scaled.getWidth() - target.getWidth()) / 2d);
		int y = (int) ((scaled.getHeight() - target.getHeight()) / 2d);
		
		// when returning the result, perform the crop!
		return result.getSubimage(x, y, target.width, target.height);
	}
	
	public byte[] toJPG(BufferedImage image, int compression) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		MemoryCacheImageOutputStream stream = new MemoryCacheImageOutputStream(output);
		ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
		ImageWriteParam params = writer.getDefaultWriteParam();
		IIOImage source = new IIOImage(image, null, null);
		
		params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		params.setCompressionQuality((float) compression / 100f);
		
		writer.setOutput(stream);
		writer.write(null, source, params);
		writer.dispose();
		
		return output.toByteArray();
	}

	/**
	 * Given a particular dimension, if that dimension exceeds 2MP is size, 
	 * it brute-forces the largest possible scaled version <b>within</b> the
	 * 2MP limit.
	 * 
	 * @param size
	 * @return 
	 */
	Dimension calculate2MB(Dimension size) {
		int pixels = size.width * size.height;
		double ratio = size.getWidth() / size.getHeight();
		
		// if we're already below the limit, just return it as-is.
		if (pixels <= MEGAPIXEL_LIMIT) {
			return size;
		}
		
		int width = size.width;
		int height = size.height;
		
		while (true) {
			--width;
			height = (int) Math.floor((double) width / ratio);
			
			if (width * height <= MEGAPIXEL_LIMIT) {
				break;
			}
		}
		
		return new Dimension(width, height);
	}
	
	Dimension calculateOG(Dimension size) {
		return new Dimension(600, 600);
	}
	
	Dimension calculateReddit(Dimension size) {
		double ratio = size.getWidth() / size.getHeight();
		int width = 960;
		int height = (int) ((double) width / ratio);
		
		return new Dimension(width, height);
	}
	
	Dimension calculateTwitter(Dimension size) {
		return new Dimension(600, 300);
	}

	Dimension calculateScaled(Dimension size, Dimension target) {
		double sourceRatio = size.getWidth() / size.getHeight();
		int width, height;
		
		if (sourceRatio < 1.0d) {
			return new Dimension(target.width, Math.max((int) (target.getWidth() / sourceRatio), target.height));
		}
		
		return new Dimension(Math.max((int) (target.getHeight() * sourceRatio), target.width), target.height);
	}
	
}
