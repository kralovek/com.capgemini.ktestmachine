package com.capgemini.ktestmachine.main;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

import com.capgemini.ktestmachine.component.ktestmachine.KTestMachine;
import com.capgemini.ktestmachine.exception.ConfigurationException;
import com.capgemini.ktestmachine.utils.bean.UtilsBean;
import com.capgemini.ktestmachine.utils.errors.TreatErrors;

public class MainKTestMachineRun {
	private static final Logger LOGGER = Logger
			.getLogger(MainKTestMachineRun.class);

	private static final String ID_BEAN = "kTestMachine";

	public static void main(final String[] pArgs) {
		final MainKTestMachineRun mainKTestMachineRun = new MainKTestMachineRun();
		mainKTestMachineRun.work(pArgs);
	}

	private void work(String[] pArgs) {
		LOGGER.trace("BEGIN");
		try {
			try {
				ConfigKTestMachine config = new ConfigKTestMachine(pArgs);

				LOGGER.info("CONFIG: ");
				LOGGER.info(config.toString());
				
				BeanFactory beanFactory = UtilsBean.getInstance().createBeanFactory(config.getConfig(), config.getProperties());

				workRun(beanFactory, config);

			} catch (final BeansException ex) {
				throw new ConfigurationException(ex.getMessage());
			}
			LOGGER.trace("OK");
		} catch (final Exception ex) {
			TreatErrors.treatException(ex);
		} finally {
			LOGGER.trace("END");
		}
	}

	private void workRun(BeanFactory beanFactory, ConfigKTestMachine config)
			throws ConfigurationException {
		KTestMachine KTestMachine = beanFactory.getBean(ID_BEAN, KTestMachine.class);
		KTestMachine.test(config.getExcel());
	}
}
