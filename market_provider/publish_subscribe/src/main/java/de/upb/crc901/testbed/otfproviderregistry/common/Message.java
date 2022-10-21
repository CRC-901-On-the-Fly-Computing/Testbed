package de.upb.crc901.testbed.otfproviderregistry.common;

import de.upb.crc901.testbed.otfproviderregistry.Subject;

/**
 * A message that can be send between {@link Subject Subjects} in order to communicate with each other. Note that for most messages, the value of
 * parameter[0] is set to the {@link MessageType}.
 * 
 * @author Michael
 *
 */
public class Message {
	protected Object[] parameters;

	/**
	 * Creates a new Message with the given parameters.
	 * 
	 * @param parameters
	 */
	public Message(Object... parameters) {
		this.parameters = parameters;
	}

	/**
	 * Returns the message parameters in an array.
	 * 
	 * @return
	 */
	public Object[] getParameters() {
		return parameters;
	}

	/**
	 * Adds the given parameter at the end of the parameter array.
	 * 
	 * @param parameter
	 */
	public void addParameter(Object parameter) {
		Object[] params = new Object[parameters.length + 1];
		for (int i = 0; i < parameters.length; i++)
			params[i] = parameters[i];
		params[parameters.length] = parameter;
		this.parameters = params;
	}

	/**
	 * Replaces the parameter at the given index with the given parameter.
	 * 
	 * @param o
	 * @param index
	 */
	public void setParameter(Object o, int index) {
		parameters[index] = o;
	}
}
