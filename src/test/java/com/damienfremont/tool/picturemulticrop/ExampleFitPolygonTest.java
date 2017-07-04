package com.damienfremont.tool.picturemulticrop;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Test;

import boofcv.alg.feature.detect.template.TemplateMatching;
import boofcv.factory.feature.detect.template.FactoryTemplateMatching;
import boofcv.factory.feature.detect.template.TemplateScoreType;
import boofcv.io.UtilIO;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.feature.Match;
import boofcv.struct.image.GrayF32;

// https://boofcv.org/index.php?title=Example_Template_Matching
public class ExampleFitPolygonTest {
	String path = BatchTest.path + "\\src\\test\\resources\\cv";

	@Test
	public void test() throws Exception {
		GrayF32 image32 = toGrayF32(toImage(path + "\\image.jpg"));
		GrayF32 template32 = toGrayF32(toImage(path + "\\template.jpg"));

		List<Match> res = findMatch(image32, template32);

		assertThat(res).isNotEmpty().hasSize(3);
		assertThat_Match_IsEqualTo_X_Y(res.get(0), 125, 50);
		assertThat_Match_IsEqualTo_X_Y(res.get(1), 6, 0);
		assertThat_Match_IsEqualTo_X_Y(res.get(2), 4, 81);
	}

	@Test
	public void test_big() throws Exception {
		BufferedImage image = toImage(path + "\\image-big.jpg");
		BufferedImage template = toImage(path + "\\template-big.jpg");

		int width = image.getWidth();
		double ratio = (240.0 / width);
		image = resize(image, (int) (ratio * image.getWidth()), (int) (ratio * image.getHeight()));
		template = resize(template, (int) (ratio * template.getWidth()), (int) (ratio * template.getHeight()));

		GrayF32 image32 = toGrayF32(image);
		GrayF32 template32 = toGrayF32(template);

		List<Match> res = findMatch(image32, template32);

		assertThat(res).isNotEmpty().hasSize(3);
		assertThat_Match_IsEqualTo_X_Y(res.get(0), 125, 50);
		assertThat_Match_IsEqualTo_X_Y(res.get(1), 6, 0);
		assertThat_Match_IsEqualTo_X_Y(res.get(2), 4, 81);
	}

	@Test
	public void test_big_save() throws Exception {
		BufferedImage image = toImage(path + "\\image-big.jpg");
		BufferedImage template = toImage(path + "\\template-big.jpg");

		int width = image.getWidth();
		double ratio = (240.0 / width);
		image = resize(image, (int) (ratio * image.getWidth()), (int) (ratio * image.getHeight()));
		template = resize(template, (int) (ratio * template.getWidth()), (int) (ratio * template.getHeight()));

		GrayF32 image32 = toGrayF32(image);
		GrayF32 template32 = toGrayF32(template);

		List<Match> res = findMatch(image32, template32);

		assertThat(res).isNotEmpty().hasSize(3);
		assertThat_Match_IsEqualTo_X_Y(res.get(0), 125, 50);
		assertThat_Match_IsEqualTo_X_Y(res.get(1), 6, 0);
		assertThat_Match_IsEqualTo_X_Y(res.get(2), 4, 81);

		save(toImage(path + "\\image-big.jpg"), toImage(path + "\\template-big.jpg"), ratio, res);
	}

	private void save(BufferedImage image, BufferedImage template, double ratio, List<Match> res) throws IOException {
		int i = 1;
		double padding = 0.02;
		for (Match match : res) {
			int x = (int) (match.x / (ratio - ratio * padding));
			int y = (int) (match.y / (ratio - ratio * padding));
			int w = (int) (template.getWidth() * (1 - padding));
			int h = (int) (template.getHeight() * (1 - padding));
			BufferedImage dest = image.getSubimage(x, y, w, h);
			File outputfile = new File(BatchTest.path + "\\target\\test-classes\\image-big." + i + ".jpg");
			ImageIO.write(dest, "jpg", outputfile);
			i++;
		}
	}

	private void assertThat_Match_IsEqualTo_X_Y(Match res, int exp_x, int exp_y) {
		double margin = 0.05;
		assertThat(res.x).isBetween((int) (exp_x - exp_x * margin), (int) (exp_x + exp_x * margin));
		assertThat(res.y).isBetween((int) (exp_y - exp_y * margin), (int) (exp_y + exp_y * margin));
	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

	private List<Match> findMatch(GrayF32 image, GrayF32 template) {
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

	private GrayF32 toGrayF32(BufferedImage image) {
		return ConvertBufferedImage.convertFromSingle(image, null, GrayF32.class);
	}

	private BufferedImage toImage(String path) {
		return UtilImageIO.loadImage(UtilIO.pathExample(path));
	}

}
