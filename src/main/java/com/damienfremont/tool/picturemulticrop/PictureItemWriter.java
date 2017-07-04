package com.damienfremont.tool.picturemulticrop;

import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.core.io.Resource;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class PictureItemWriter implements ItemWriter<PictureModel> {

	@NonNull
	private Resource resource;
		
	@Override
	public void write(List<? extends PictureModel> items) throws Exception {
		// TODO Auto-generated method stub
		
	}


}
