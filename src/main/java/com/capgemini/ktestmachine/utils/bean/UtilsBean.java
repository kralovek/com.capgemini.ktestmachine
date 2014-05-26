package com.capgemini.ktestmachine.utils.bean;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class UtilsBean {

	private static UtilsBean instance = new UtilsBean();

	public static final UtilsBean getInstance() {
		return instance;
	}

	public BeanFactory createBeanFactory(String config, List<String> propertiesList) {
		final File fileConfig = new File(config);
		ConfigurableApplicationContext configurableApplicationContext;
		if (fileConfig.exists()) {
			FileSystemXmlApplicationContext fileSystemXmlApplicationContext = new FileSystemXmlApplicationContext();
			fileSystemXmlApplicationContext.setConfigLocation(config);
			configurableApplicationContext = fileSystemXmlApplicationContext;
		} else {
			ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext();
			classPathXmlApplicationContext
					.setConfigLocation(config);
			configurableApplicationContext = classPathXmlApplicationContext;
		}

		if (propertiesList != null && !propertiesList.isEmpty()) {
			Resource[] resources = new Resource[propertiesList.size()];
			for (int i = 0; i < propertiesList.size();i++) {
				String properties = propertiesList.get(i);
				File fileProperties = new File(properties);

				Resource resource = fileProperties.isFile() ? new FileSystemResource(
						fileProperties) : new ClassPathResource(
								properties);
				resources[i] = resource;
			}
			PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
			propertyPlaceholderConfigurer.setLocations(resources);
			propertyPlaceholderConfigurer
					.setBeanFactory(configurableApplicationContext);

			configurableApplicationContext
					.addBeanFactoryPostProcessor(propertyPlaceholderConfigurer);
		}
		configurableApplicationContext.refresh();
		
		return configurableApplicationContext;
	}
}
