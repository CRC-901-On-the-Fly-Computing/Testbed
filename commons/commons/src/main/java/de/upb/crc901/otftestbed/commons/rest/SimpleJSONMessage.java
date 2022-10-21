package de.upb.crc901.otftestbed.commons.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Very simple message to circumvent two response types within a REST interface.
 * @author mirkoj
 *
 */
public class SimpleJSONMessage {
	
	@JsonProperty
	String message = "";
	
	public SimpleJSONMessage() {
	}
	public SimpleJSONMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "SimpleJSONMessage [message=" + message + "]";
	}

}
