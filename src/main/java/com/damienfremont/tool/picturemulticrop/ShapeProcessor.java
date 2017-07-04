package com.damienfremont.tool.picturemulticrop;

import static com.google.common.base.Preconditions.checkArgument;

import org.springframework.batch.item.ItemProcessor;

public class ShapeProcessor implements ItemProcessor<PictureModel, PictureModel> {

	@Override
	public PictureModel process(PictureModel item) throws Exception {
		checkArgument(null != item);
		checkArgument(null != item.getImg());

		return null;
	}

}
