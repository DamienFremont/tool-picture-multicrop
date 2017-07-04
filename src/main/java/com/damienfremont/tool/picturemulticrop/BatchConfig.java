package com.damienfremont.tool.picturemulticrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
public class BatchConfig {

	@Value("${src}")
	String src;
	@Value("${tpl}")
	String tpl;
	@Value("${dest}")
	String dest;

	Integer maxMatches = 4;
	Double reduceWidth = 240.0;
	Double padding = 0.02;

	@Autowired
	JobBuilderFactory jobs;

	@Bean
	Job job() throws Exception {
		return jobs.get("multicrop") //
				.start(step()) //
				.build();
	}

	@Autowired
	private StepBuilderFactory steps;

	@Bean
	Step step() throws Exception {
		return steps.get("crop").<PictureModel, PictureModel> chunk(1) //
				.reader(reader(src + "/*.jpg")) //
				.processor(processors()) //
				.writer(writer(dest)).build();
	}

	protected ItemReader<PictureModel> reader(String src) throws Exception {
		Resource[] res = new ResourcesFactoryBean(src).createInstance();
		ResourceAwareItemReaderItemStream<PictureModel> ir = new PictureItemReader();
		MultiResourceItemReader<PictureModel> mri = new MultiResourceItemReader<PictureModel>();
		mri.setResources(res);
		mri.setDelegate(ir);
		return mri;
	}

	public ItemProcessor<PictureModel, PictureModel> processors() {
		CompositeItemProcessor<PictureModel, PictureModel> cip = new CompositeItemProcessor<>();
		List<ItemProcessor<PictureModel, PictureModel>> ips = new ArrayList<>();
		ips.add(processor1());
		ips.add(processor2());
		ips.add(processor3());
		cip.setDelegates(ips);
		return cip;
	}

	private ImageFindProcessor processor1() {
		return new ImageFindProcessor(tpl, reduceWidth, maxMatches);
	}

	private ImageCoordProcessor processor2() {
		return new ImageCoordProcessor(tpl);
	}

	private ImageCropProcessor processor3() {
		return new ImageCropProcessor(padding);
	}

	protected ItemWriter<PictureModel> writer(String dest) {
		File f = new File(dest);
		if (!f.exists())
			f.mkdirs();
		return new PictureItemWriter(new FileSystemResource(dest));
	}

}
