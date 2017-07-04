package com.damienfremont.tool.picturemulticrop;

import static com.google.common.base.Preconditions.checkArgument;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;

import boofcv.io.UtilIO;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.feature.Match;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;

@Log4j
@AllArgsConstructor
public class ImageCoordProcessor implements ItemProcessor<PictureModel, PictureModel> {

	@NonNull
	String tpl;

	@Override
	public PictureModel process(PictureModel item) throws Exception {
		checkArgument(null != item);
		checkArgument(null != item.ratio);
		checkArgument(!item.matchs.isEmpty());

		log.info(item.path);

		BufferedImage template = UtilImageIO.loadImage(UtilIO.pathExample(tpl));
		int w = template.getWidth();
		int h = template.getHeight();

		List<PictureModel.Rect> crops = new ArrayList<>();
		for (Match i : item.matchs) {
			checkArgument(-1 != i.x);

			log.info("-- match: " + i.x + "," + i.y);

			crops.add( //
					new PictureModel.Rect( //
							(int) (i.x / item.ratio), //
							(int) (i.y / item.ratio), //
							w, //
							h));
		}

		item.setCrops(crops);
		return item;
	}

}
