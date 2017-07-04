package com.damienfremont.tool.picturemulticrop;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.io.FilenameUtils.getBaseName;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.batch.item.ItemWriter;
import org.springframework.core.io.Resource;

import com.damienfremont.tool.picturemulticrop.PictureModel.Dest;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;

@Log4j
@AllArgsConstructor
public class PictureItemWriter implements ItemWriter<PictureModel> {

	@NonNull
	private Resource resource;

	@Override
	public void write(List<? extends PictureModel> items) throws Exception {
		checkArgument(null != items);
		
		log.info("chunk");
		
		String parent = resource.getFile().toString();

		for (PictureModel item : items) {
			checkArgument(!item.dests.isEmpty());
			checkArgument(isNotBlank(item.path));

			log.info("-- "+item.path);
			
			String name = getBaseName(item.path);
			String ext = getExtension(item.path);

			int i = 1;
			for (Dest d : item.getDests()) {
				checkArgument(null != d.img);
				
				BufferedImage dest = d.img;
				String format = format("%s\\%s.%d.%s", parent, name, i, ext);
				File outputfile = new File(format);
				
				ImageIO.write(dest, ext, outputfile);

				log.info("---- dest: "+outputfile.toString() );
				i++;
			}
		}

	}

}
