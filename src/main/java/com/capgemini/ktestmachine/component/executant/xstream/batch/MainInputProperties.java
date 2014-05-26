package com.capgemini.ktestmachine.component.executant.xstream.batch;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.capgemini.ktestmachine.component.executant.xstream.data.InputParameters;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.TechnicalException;
import com.capgemini.ktestmachine.utils.errors.TreatErrors;
import com.capgemini.ktestmachine.utils.xstream.UtilsXStream;



public class MainInputProperties {
	public static void main(final String[] pArgs) {
		final MainInputProperties mainInputProperties = new MainInputProperties();
		mainInputProperties.work(pArgs);
	}

	private void work(final String[] pArgs) {
		try {
			final ConfigInputProperties config = new ConfigInputProperties(
					pArgs);

			if (!config.getDirOutput().isDirectory()
					&& !config.getDirOutput().mkdirs()) {
				throw new TechnicalException("Cannot create the directory: "
						+ config.getDirOutput().getAbsolutePath());
			}

			final Class<?> clazz = loadClass(config.getClassName());

			final Method[] methods = clazz.getMethods();

			for (final Method method : methods) {
				if (Object.class.equals(method.getDeclaringClass())) {
					continue;
				}
				workMethod(config, clazz, method);
			}
		} catch (final Exception ex) {
			TreatErrors.treatException(ex);
		}
	}

	private Class<?> loadClass(final String pClassName) throws ABaseException {
		try {
			return Class.forName(pClassName);
		} catch (final ClassNotFoundException ex) {
			throw new TechnicalException("Cannot create the class: "
					+ pClassName, ex);
		}
	}

	private void workMethod(final ConfigInputProperties pConfig,
			final Class<?> pClazz, final Method pMethod) throws ABaseException {
		final InputParameters inputParameters = new InputParameters();
		inputParameters.setInstanceName(pClazz.getName());
		inputParameters.setMethodname(pMethod.getName());

		final List<Object> parameters = new ArrayList<Object>();
		inputParameters.setParameters(parameters);

		for (final Class<?> clazz : pMethod.getParameterTypes()) {
			final Object parameter = instantiateObject(clazz);
			parameters.add(parameter);
		}

		final String filenameRoot = generateFilenameRoot(pClazz, pMethod);
		final File file = new File(pConfig.getDirOutput(), filenameRoot
				+ ".xml");
		UtilsXStream.getInstance().toXMLFile(inputParameters, file);
	}

	private String generateFilenameRoot(final Class<?> pClass,
			final Method pMethod) {
		return pClass.getSimpleName() + "_" + pMethod.getName();
	}

	private Object instantiateObject(final Class<?> pClass) {
		return null;
	}
}
