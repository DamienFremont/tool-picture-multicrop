package com.damienfremont.tool.picturemulticrop;

import static com.damienfremont.tool.picturemulticrop.ImageRecognitionUtils.findMatch;
import static com.damienfremont.tool.picturemulticrop.ImageRecognitionUtils.resize;
import static com.damienfremont.tool.picturemulticrop.ImageRecognitionUtils.save;
import static com.damienfremont.tool.picturemulticrop.ImageRecognitionUtils.toGrayF32;
import static com.damienfremont.tool.picturemulticrop.ImageRecognitionUtils.toImage;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.util.List;

import org.junit.Test;

import boofcv.struct.feature.Match;
import boofcv.struct.image.GrayF32;

// https://boofcv.org/index.php?title=Example_Template_Matching
public class ExampleFitPolygonTest {
	String resources = BatchTest.path + "\\src\\test\\resources\\cv";
	String target = BatchTest.path + "\\target\\test-classes";

	@Test
	public void test() throws Exception {
		GrayF32 image32 = toGrayF32(toImage(resources + "\\image.jpg"));
		GrayF32 template32 = toGrayF32(toImage(resources + "\\template.jpg"));

		List<Match> res = findMatch(image32, template32);

		assertThat(res).isNotEmpty().hasSize(3);
		assertThat_Match_IsEqualTo_X_Y(res.get(0), 125, 50);
		assertThat_Match_IsEqualTo_X_Y(res.get(1), 6, 0);
		assertThat_Match_IsEqualTo_X_Y(res.get(2), 4, 81);
	}

	@Test
	public void test_big() throws Exception {
		double finderWith = 240.0;

		BufferedImage image = toImage(resources + "\\image-big.jpg");
		BufferedImage template = toImage(resources + "\\template-big.jpg");

		int width = image.getWidth();
		double ratio = (finderWith / width);
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
		BufferedImage image = toImage(resources + "\\image-big.jpg");
		BufferedImage template = toImage(resources + "\\template-big.jpg");

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

		save(target, "image-big", "jpg", //
				toImage(resources + "\\image-big.jpg"), //
				toImage(resources + "\\template-big.jpg"), ratio, res);
	}

	private void assertThat_Match_IsEqualTo_X_Y(Match res, int exp_x, int exp_y) {
		double margin = 0.05;
		assertThat(res.x).isBetween((int) (exp_x - exp_x * margin), (int) (exp_x + exp_x * margin));
		assertThat(res.y).isBetween((int) (exp_y - exp_y * margin), (int) (exp_y + exp_y * margin));
	}

}
