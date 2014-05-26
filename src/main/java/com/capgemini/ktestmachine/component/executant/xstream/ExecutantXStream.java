package com.capgemini.ktestmachine.component.executant.xstream;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.capgemini.ktestmachine.component.executant.Executant;
import com.capgemini.ktestmachine.component.executant.xstream.data.InputParameters;
import com.capgemini.ktestmachine.exception.ABaseException;
import com.capgemini.ktestmachine.exception.FunctionalException;
import com.capgemini.ktestmachine.exception.TechnicalException;

public class ExecutantXStream extends AExecutantXStreamFwk implements Executant {
	private static final Logger LOGGER = Logger
			.getLogger(ExecutantXStream.class);

	public String execute(final String pSource) throws ABaseException {
		LOGGER.trace("BEGIN");
		try {

			final InputParameters inputParameters = loadInputParameters(pSource);
			final Object instance = loadInstance(inputParameters
					.getInstanceName());
			final Method method = loadTargetMethod(instance,
					inputParameters.getMethodname(),
					inputParameters.getParameters());
			final Object result = executeMethod(instance, method,
					inputParameters.getParameters());
			final String resultXML = resultToXML(result);
			LOGGER.trace("OK");
			return resultXML;
		} finally {
			LOGGER.trace("END");
		}
	}

	/**
	 * Finds the specified method in the instance
	 * 
	 * @param pInstance
	 *            Instance
	 * @param pMethodname
	 *            Method name
	 * @param pParameters
	 *            Datatypes of the method
	 * @return Method instance
	 * @throws ABaseException
	 */
	private Method loadTargetMethod(final Object pInstance,
			final String pMethodname, final List<Object> pParameters)
			throws ABaseException {
		final Class<?> clazz = pInstance.getClass();
		final Method[] methods = clazz.getMethods();
		final List<Method> foundMethods = new ArrayList<Method>();
		for (final Method method : methods) {
			if (compareMethods(method, pMethodname, pParameters)) {
				foundMethods.add(method);
			}
		}
		if (foundMethods.size() == 0) {
			throw new FunctionalException("No method '" + pMethodname
					+ "' matching input parameters found in the class: "
					+ clazz.getName());
		}
		if (foundMethods.size() != 1) {
			throw new FunctionalException("More than one method '"
					+ pMethodname
					+ "' matching input parameters found in the class: "
					+ clazz.getName());
		}
		return foundMethods.get(0);
	}

	private boolean compareMethods(final Method pMethod,
			final String pMethodname, final List<Object> pParameters) {
		if (!pMethod.getName().equals(pMethodname)) {
			return false;
		}
		if (pMethod.getGenericParameterTypes().length != (pParameters != null ? pParameters
				.size() : 0)) {
			return false;
		}
		for (int i = 0; i < pMethod.getParameterTypes().length; i++) {
			final Object parameter = pParameters.get(i);
			final Class<?> parameterClass = pMethod.getParameterTypes()[i];
			if (parameter == null && parameterClass.isPrimitive()) {
				return false;
			}
			if (parameter != null
					&& !parameterClass.isAssignableFrom(parameter.getClass())) {
				return false;
			}
		}
		return true;
	}

	private Object loadInstance(final String pInstanceName)
			throws ABaseException {
		final Object instance = instances.get(pInstanceName);
		if (instance == null) {
			throw new TechnicalException("No instance found: " + pInstanceName);
		}
		return instance;
	}

	private InputParameters loadInputParameters(final String pSource)
			throws ABaseException {
		return XStreamParser.getInstance().parseInputParameters(pSource);
	}

	private Object executeMethod(final Object pObject, final Method pMethod,
			final List<Object> pParameters) throws ABaseException {
		try {
			Object[] parameters = null;
			if (pParameters != null && pParameters.size() > 0) {
				parameters = pParameters
						.toArray(new Object[pParameters.size()]);
			}
			final Object result = pMethod.invoke(pObject, parameters);
			return result;
		} catch (final InvocationTargetException ex) {
			// this exception is the result
			return ex.getTargetException();
		} catch (final NullPointerException ex) {
			throw new TechnicalException(
					"Impossible to call the instance method '"
							+ pMethod.getName() + " on a NULL object", ex);
		} catch (final IllegalAccessException ex) {
			throw new TechnicalException("Impossible to call the method '"
					+ pMethod.getName() + " on the object "
					+ pObject.getClass().getName(), ex);
		} catch (final IllegalArgumentException ex) {
			throw new TechnicalException("Impossible to call the method '"
					+ pMethod.getName() + " on the object "
					+ pObject.getClass().getName(), ex);
		}
	}

	private String resultToXML(final Object pObject) throws ABaseException {
		final String xml = XStreamParser.getInstance().formatXML(pObject);
		return xml;
	}
}
