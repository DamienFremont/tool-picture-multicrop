package com.damienfremont.tool.picturemulticrop;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;

import boofcv.io.UtilIO;
import boofcv.io.image.UtilImageIO;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;

@Log4j
@AllArgsConstructor
public class ImageCropProcessor implements ItemProcessor<PictureModel, PictureModel> {

	@NonNull
	Double padding = 0.02;

	@Override
	public PictureModel process(PictureModel item) throws Exception {
		checkArgument(null != item);
		checkArgument(isNotBlank(item.path));
		checkArgument(!item.crops.isEmpty());

		log.info(item.path);
		
		BufferedImage image = UtilImageIO.loadImage(UtilIO.pathExample(item.path));

		List<PictureModel.Dest> dests = new ArrayList<>();
		for (PictureModel.Rect i : item.crops) {
			checkArgument(-1 != i.x);
			
			log.info("-- rect: " + i.x + "," + i.y);

			int x = (int) (i.x + i.x * padding);
			int y = (int) (i.y + i.y * padding);
			int w = (int) (i.w - i.w * padding);
			int h = (int) (i.h - i.h * padding);
			BufferedImage img = image.getSubimage(x, y, w, h);

			dests.add(new PictureModel.Dest(img));
		}
		item.setDests(dests);
		return item;
	}
}
