/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.util;

import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import SunlightFrame.config.AppConfig;
import SunlightFrame.log.AppLogger;

/**
 * 图片处理工具
 */
public class PictureUtil {
	public static int WIDTH_PICTURE = 1;

	public static int HEIGHT_PICTURE = 2;

	public static int resizePicture(String fromFilePath, String toFilePath, int width) throws Exception {

		// 缩放比例
		File f = new File(fromFilePath);
		if (!f.exists()) {
			throw new Exception("resize image don't exists : " + fromFilePath);
		}

		Image src = javax.imageio.ImageIO.read(f);
		int height = width * src.getHeight(null) / src.getWidth(null);
		return resizePicture(fromFilePath, toFilePath, width, height);
	}

	/**
	 * 按宽或者高最大尺寸等比例压缩图片
	 * 
	 * @param filePath
	 *            图片文件路径
	 * @param width
	 *            修改后的宽度
	 * @throws Exception
	 */
	public static int resizeMaxSizePicture(String fromFilePath, String toFilePath, int maxSize) throws Exception {

		// 缩放比例
		File f = new File(fromFilePath);
		if (!f.exists()) {
			throw new Exception("resize image don't exists : " + fromFilePath);
		}

		Image src = javax.imageio.ImageIO.read(f);

		int width = 0;
		int height = 0;
		if (src.getWidth(null) <= maxSize && src.getHeight(null) <= maxSize) {
			width = src.getWidth(null);
			height = src.getHeight(null);
		} else if (src.getWidth(null) >= src.getHeight(null)) {
			width = maxSize;
			height = width * src.getHeight(null) / src.getWidth(null);
		} else {
			height = maxSize;
			width = height * src.getWidth(null) / src.getHeight(null);
		}

		return resizePicture(fromFilePath, toFilePath, width, height);
	}

	public static int resizePicture(String fromFilePath, String toFilePath, int width, int height) throws Exception {

		// 缩放比例
		File f = new File(fromFilePath);
		if (!f.exists()) {
			throw new Exception("resize image don't exists : " + fromFilePath);
		}

		String imageDLL = AppConfig.getInstance().getParameterConfig().getParameter("imageDLL");
		if (imageDLL == null || imageDLL.equals("")) {
			Image src = javax.imageio.ImageIO.read(f);
			BufferedImage tag = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(src.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

			FileOutputStream out = new FileOutputStream(toFilePath);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(tag);
			out.close();
		} else {
			String quality = AppConfig.getInstance().getParameterConfig().getParameter("imageQuality");
			if (quality == null || quality.equals("")) {
				quality = "80";
			}
			String command = imageDLL + " -geometry " + width + "x" + height + "! -quality " + quality + " "
					+ fromFilePath + " " + toFilePath;
			Process proc;
			proc = Runtime.getRuntime().exec(command);
			AppLogger.getInstance().debugLog("resize picture: " + command);
			int exitStatus = 0;
			while (true) {
				try {
					proc.waitFor();
					break;
				} catch (java.lang.InterruptedException e) {
				}
			}
			if (exitStatus != 0) {
				throw new Exception("Error executing command: " + command);
			}
		}

		if (width >= height) {
			return WIDTH_PICTURE;
		} else {
			return HEIGHT_PICTURE;
		}
	}

	/**
	 * 图片加水印
	 * 
	 * @param pressImg
	 *            水印图片
	 * @param targetImg
	 *            目标图片
	 */
	public final static void pressImage(String pressImg, String targetImg) {
		try {
			File img = new File(targetImg);
			Image src = ImageIO.read(img);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);
			// 水印文件
			Image src_biao = ImageIO.read(new File(pressImg));
			int wideth_biao = src_biao.getWidth(null);
			int height_biao = src_biao.getHeight(null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1f));
			g.drawImage(src_biao, (wideth - wideth_biao) / 2, (height - height_biao) / 2, wideth_biao, height_biao,
					null);
			// 水印文件结束
			g.dispose();
			ImageIO.write((BufferedImage) image, "jpg", img);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置图片为正方形
	 * 
	 * @param img
	 *            目标图片
	 */
	public final static void makeImageToSquare(String img) {
		makeImageToSquare(img, Color.white);
	}

	/**
	 * 设置图片为正方形
	 * 
	 * @param img
	 *            目标图片
	 */
	public final static void makeImageToSquare(String img, Color bgColor) {
		try {
			Image src = ImageIO.read(new File(img));
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			if (wideth == height) {
				return;
			}
			int size = wideth > height ? wideth : height;
			BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
			Graphics g = image.createGraphics();
			g.setColor(bgColor);
			g.fillRect(0, 0, size, size);
			g.drawImage(src, (size - wideth) / 2, (size - height) / 2, wideth, height, null);
			g.dispose();
			FileOutputStream out = new FileOutputStream(img);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
