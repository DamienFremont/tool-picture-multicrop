package com.damienfremont.tool.picturemulticrop;

import java.io.IOException;
import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.core.io.Resource;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

/**
 * https://docs.spring.io/spring-batch/reference/htmlsingle/#restartableReader
 */
@Log4j
@Data
@NoArgsConstructor
public class PictureItemReader implements ResourceAwareItemReaderItemStream<PictureModel> {

	@NonNull
	private Resource resource;

	@Override
	public PictureModel read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

		log.info(resource.toString());

		if (currentIndex < 1) {
			currentIndex++;
			try {
				return new PictureModel(resource.getFile().toPath().toString());
			} catch (IOException e) {
				throw new ItemStreamException(e);
			}
		}

		return null;
	}

	int currentIndex = 0;
	private static final String CURRENT_INDEX = "current.index";

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		if (executionContext.containsKey(CURRENT_INDEX)) {
			currentIndex = new Long(executionContext.getLong(CURRENT_INDEX)).intValue();
		} else {
			currentIndex = 0;
		}
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		executionContext.putLong(CURRENT_INDEX, new Long(currentIndex).longValue());
	}

	@Override
	public void close() throws ItemStreamException {

	}

}
