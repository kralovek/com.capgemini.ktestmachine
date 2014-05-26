package com.capgemini.ktestmachine.main;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

import com.capgemini.ktestmachine.component.ktestsynthese.KTestSynthese;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.ConfigurationException;
import com.capgemini.ktestmachine.utils.bean.UtilsBean;
import com.capgemini.ktestmachine.utils.errors.TreatErrors;

public class MainKTestSynthese {
	private static final Logger LOGGER = Logger
			.getLogger(MainKTestSynthese.class);

	private static final String ID_BEAN = "kTestSynthese";

	public static void main(final String[] pArgs) {
		final MainKTestSynthese mainKTestMachineSynthese = new MainKTestSynthese();
		mainKTestMachineSynthese.work(pArgs);
	}

	private void work(String[] pArgs) {
		LOGGER.trace("BEGIN");
		try {
			try {
				ConfigKTestMachine config = new ConfigKTestMachine(pArgs);

				BeanFactory beanFactory = UtilsBean.getInstance()
						.createBeanFactory(config.getConfig(),
								config.getProperties());

				workSynthese(beanFactory, config);

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

	private void workSynthese(BeanFactory beanFactory, ConfigKTestMachine config)
			throws ABaseException {
		KTestSynthese kTestSynthese = beanFactory.getBean(ID_BEAN,
				KTestSynthese.class);
		kTestSynthese.synthese(config.getExcel());
	}
}
