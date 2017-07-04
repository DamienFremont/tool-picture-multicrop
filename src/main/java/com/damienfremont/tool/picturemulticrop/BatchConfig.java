package com.damienfremont.tool.picturemulticrop;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
public class BatchConfig {

	@Value("${src}")
	String src;
	@Value("${dest}")
	String dest;

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
				.processor(processor()) //
				.writer(writer(dest)).build();
	}

	protected ItemReader<PictureModel> reader(String src) throws Exception {
		MultiResourceItemReader<PictureModel> r = new MultiResourceItemReader<PictureModel>();
		r.setResources(new ResourcesFactoryBean(src).createInstance());
		r.setDelegate(new PictureItemReader<PictureModel>());
		return r;
	}

	public ShapeProcessor processor() {
		return new ShapeProcessor();
	}

	protected ItemWriter<PictureModel> writer(String dest) {
		return new PictureItemWriter(new FileSystemResource(dest));
	}

}
