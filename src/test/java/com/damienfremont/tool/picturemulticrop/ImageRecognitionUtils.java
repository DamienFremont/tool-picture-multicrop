package com.damienfremont.tool.picturemulticrop;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import boofcv.alg.feature.detect.template.TemplateMatching;
import boofcv.factory.feature.detect.template.FactoryTemplateMatching;
import boofcv.factory.feature.detect.template.TemplateScoreType;
import boofcv.io.UtilIO;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.feature.Match;
import boofcv.struct.image.GrayF32;

public class ImageRecognitionUtils {

	public static GrayF32 toGrayF32(BufferedImage image) {
		return ConvertBufferedImage.convertFromSingle(image, null, GrayF32.class);
	}

	public static BufferedImage toImage(String path) {
		return UtilImageIO.loadImage(UtilIO.pathExample(path));
	}

	public static List<Match> findMatch(GrayF32 image, GrayF32 template) {
		// create template matcher.
		TemplateMatching<GrayF32> matcher = FactoryTemplateMatching.createMatcher(TemplateScoreType.SUM_DIFF_SQ,
				GrayF32.class);
		// Find the points which match the template the best
		matcher.setImage(image);
		matcher.setTemplate(template, null, 4);
		matcher.process();
		List<Match> res = matcher.getResults().toList();

		for (Match match : res) {
			System.out.println(match.x + "," + match.y);
		}
		return res;
	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

	public static void save(String path, String name, String ext, BufferedImage image, BufferedImage template,
			double ratio, List<Match> res) throws IOException {
		int i = 1;
		double padding = 0.02;
		for (Match match : res) {
			BufferedImage dest = crop(image, template, ratio, padding, match);
			File outputfile = new File(String.format("%s\\%s.%d.%s", path, name, i, ext));
			ImageIO.write(dest, ext, outputfile);
			i++;
		}
	}

	private static BufferedImage crop(BufferedImage image, BufferedImage template, double ratio, double padding,
			Match match) {
		int x = (int) (match.x / (ratio - ratio * padding));
		int y = (int) (match.y / (ratio - ratio * padding));
		int w = (int) (template.getWidth() * (1 - padding));
		int h = (int) (template.getHeight() * (1 - padding));
		BufferedImage dest = image.getSubimage(x, y, w, h);
		return dest;
	}
}
