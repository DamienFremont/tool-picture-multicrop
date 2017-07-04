package com.damienfremont.tool.picturemulticrop;

import java.awt.image.BufferedImage;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import boofcv.alg.feature.detect.template.TemplateMatching;
import boofcv.factory.feature.detect.template.FactoryTemplateMatching;
import boofcv.factory.feature.detect.template.TemplateScoreType;
import boofcv.io.UtilIO;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.feature.Match;
import boofcv.struct.image.GrayF32;

public class ExampleFitPolygonTest {

	@Test
	public void test() throws Exception {
		GrayF32 image = toGrayF32(BatchTest.path + "\\src\\test\\resources\\cv\\image.jpg");
		GrayF32 template = toGrayF32(BatchTest.path + "\\src\\test\\resources\\cv\\template.jpg");
		GrayF32 mask = toGrayF32(BatchTest.path + "\\src\\test\\resources\\cv\\template.jpg");

		// create template matcher.
		TemplateMatching<GrayF32> matcher = FactoryTemplateMatching.createMatcher(TemplateScoreType.SUM_DIFF_SQ,
				GrayF32.class);

		// Find the points which match the template the best
		matcher.setImage(image);
		matcher.setTemplate(template, mask, 3);
		matcher.process();

		List<Match> res = matcher.getResults().toList();

		Assertions.assertThat(res).isNotEmpty().hasSize(3);
		for (Match match : res) {
			System.out.println(match.x + ","+match.y);
		}
	}

	private GrayF32 toGrayF32(String path) {
		BufferedImage image = UtilImageIO.loadImage(UtilIO.pathExample(path));
		GrayF32 gray = ConvertBufferedImage.convertFromSingle(image, null, GrayF32.class);
		return gray;
	}

}
