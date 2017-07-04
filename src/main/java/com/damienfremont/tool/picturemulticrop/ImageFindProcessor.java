package com.damienfremont.tool.picturemulticrop;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;

import boofcv.alg.feature.detect.template.TemplateMatching;
import boofcv.factory.feature.detect.template.FactoryTemplateMatching;
import boofcv.factory.feature.detect.template.TemplateScoreType;
import boofcv.io.UtilIO;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.feature.Match;
import boofcv.struct.image.GrayF32;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;

@Log4j
@AllArgsConstructor
public class ImageFindProcessor implements ItemProcessor<PictureModel, PictureModel> {

	@NonNull
	String tpl;
	@NonNull
	Double reduceWidth = 240.0;
	@NonNull
	Integer maxMatches = 4;
	
	@Override
	public PictureModel process(PictureModel item) throws Exception {
		checkArgument(null != item);
		checkArgument(isNotBlank(item.path));

		log.info(item.path);

		BufferedImage image = UtilImageIO.loadImage(UtilIO.pathExample(item.path));
		BufferedImage template = UtilImageIO.loadImage(UtilIO.pathExample(tpl));

		int originalWidth = image.getWidth();
		double ratio = (reduceWidth / originalWidth);
		image = resize(image, (int) (ratio * image.getWidth()), (int) (ratio * image.getHeight()));
		template = resize(template, (int) (ratio * template.getWidth()), (int) (ratio * template.getHeight()));

		GrayF32 image32 = ConvertBufferedImage.convertFromSingle(image, null, GrayF32.class);
		GrayF32 template32 = ConvertBufferedImage.convertFromSingle(template, null, GrayF32.class);
		List<Match> res = findMatch(image32, template32);

		item.setMatchs(res);
		item.setRatio(ratio);
		return item;
	}

	private List<Match> findMatch(GrayF32 image, GrayF32 template) {
		TemplateMatching<GrayF32> matcher = FactoryTemplateMatching.createMatcher(TemplateScoreType.SUM_DIFF_SQ,
				GrayF32.class);

		matcher.setImage(image);
		matcher.setTemplate(template, null, maxMatches);
		matcher.process();
		return matcher.getResults().toList();
	}

	private BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

}
