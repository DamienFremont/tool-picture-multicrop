package com.damienfremont.tool.picturemulticrop;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
public class ResourcesFactoryBean extends AbstractFactoryBean<Resource[]> {
	@NonNull
	String path;

	@Override
	protected Resource[] createInstance() throws Exception {
		return new PathMatchingResourcePatternResolver() //
				.getResources( //
						new FileSystemResource(path) //
								.getURL().toString());
	}

	@Override
	public Class<?> getObjectType() {
		return Resource[].class;
	}
}