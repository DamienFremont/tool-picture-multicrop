package com.damienfremont.tool.picturemulticrop;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.core.io.Resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;

@Log4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PictureItemReader<T> implements ResourceAwareItemReaderItemStream<PictureModel> {

	@NonNull
	private Resource resource;

	@Override
	public PictureModel read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

		log.info(resource.toString());
		
		try {
			BufferedImage img = ImageIO.read(resource.getURL());
			return new PictureModel(resource.getFile().toPath().toString(), img);
		} catch (IOException e) {
			throw new ItemStreamException(e);
		}
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {

	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {

	}

	@Override
	public void close() throws ItemStreamException {

	}

}
