package de.upb.crc901.otftestbed.commons.requester;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigurationMarketMonitorSources {

	@JsonProperty
	private String nodeSupplierURL;
	
	@JsonProperty
	private String edgeSupplierURL;

	public ConfigurationMarketMonitorSources() {
	}
	
	public ConfigurationMarketMonitorSources(String nodeSupplierURL, String edgeSupplierURL) {
		super();
		this.nodeSupplierURL = nodeSupplierURL;
		this.edgeSupplierURL = edgeSupplierURL;
	}

	public String getNodeSupplierURL() {
		return nodeSupplierURL;
	}

	public void setNodeSupplierURL(String nodeSupplierURL) {
		this.nodeSupplierURL = nodeSupplierURL;
	}

	public String getEdgeSupplierURL() {
		return edgeSupplierURL;
	}

	public void setEdgeSupplierURL(String edgeSupplierURL) {
		this.edgeSupplierURL = edgeSupplierURL;
	}

	
	
}
