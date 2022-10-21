package de.upb.crc901.otftestbed.service_requester.impl.models;

import java.util.List;

import de.upb.crc901.otftestbed.commons.requester.ConfigurationMarketMonitorSources;
import de.upb.crc901.otftestbed.commons.requester.Offer;

public class WaitingForOfferData {

	public WaitingForOfferData(ProsecoInterviewData prosecoInterviewData, List<Offer> offers,
			ConfigurationMarketMonitorSources configurationMonitorSources) {
		super();
		this.prosecoInterviewData = prosecoInterviewData;
		this.offers = offers;
		this.configurationMonitorSources = configurationMonitorSources;
	}

	/* The data from the previous request state. */
	ProsecoInterviewData prosecoInterviewData;

	/* The collected offers. */
	List<Offer> offers;

	/*
	 * The reference to the elastic search where the graph of the configuration can
	 * be found.
	 */
	ConfigurationMarketMonitorSources configurationMonitorSources;

	public ProsecoInterviewData getProsecoInterviewData() {
		return prosecoInterviewData;
	}

	public List<Offer> getOffers() {
		return offers;
	}

	public ConfigurationMarketMonitorSources getConfigurationMonitorSources() {
		return configurationMonitorSources;
	}

	public void setOffers(List<Offer> offers) {
		this.offers = offers;
	}
}
