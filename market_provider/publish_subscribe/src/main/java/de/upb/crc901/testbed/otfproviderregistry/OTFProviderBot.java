package de.upb.crc901.testbed.otfproviderregistry;

import java.util.ArrayList;
import java.util.List;

import de.upb.crc901.testbed.otfproviderregistry.common.Coin;

/**
 * A bot that automatically communicates with the OTFProviderRegistry.
 * 
 * @author Michael
 *
 */
public class OTFProviderBot {
	private OTFProvider parent;
	private OTFProviderRegistry supervisor;
	private int publishRequestCounter;

	public OTFProviderBot(OTFProvider parent, OTFProviderRegistry supervisor) {
		super();
		this.parent = parent;
		this.supervisor = supervisor;
		this.publishRequestCounter = 0;
	}

	/**
	 * Is called in the timeout method of the {@link OTFProvider}.
	 */
	public void timeout() {
		List<Domain> subscribedDomains = this.subscribedDomains();
		List<Domain> unsubscribedDomains = this.unsubscribedDomains(subscribedDomains);
		Coin c = new Coin();

		for (Domain d : unsubscribedDomains) {
			if (c.tossCoin(Parameters.BOT_ACTIVATION_PROBABILITY)) {
				supervisor.register(parent, d);
			}

			if (c.tossCoin(Parameters.BOT_PUBLICATION_PROBABILITY)) {
				supervisor.sendRequest("R(" + String.valueOf(publishRequestCounter) + ")", parent.getID(), d, Parameters.REQUEST_TIMEOUT_AFTER);
				this.publishRequestCounter++;
			}
		}

		for (Domain d : subscribedDomains) {
			if (c.tossCoin(Parameters.BOT_ACTIVATION_PROBABILITY)) {
				supervisor.deregister(parent, d);
			}

			if (c.tossCoin(Parameters.BOT_PUBLICATION_PROBABILITY)) {
				supervisor.sendRequest(String.valueOf(publishRequestCounter), parent.getID(), d, Parameters.REQUEST_TIMEOUT_AFTER);
				this.publishRequestCounter++;
			}
		}
	}

	private List<Domain> subscribedDomains() {
		List<Domain> erg = new ArrayList<>();
		for (BuildSON p : parent.subscriber.domains)
			erg.add(p.domain);
		return erg;
	}

	private List<Domain> unsubscribedDomains(List<Domain> subscribedDomains) {
		List<Domain> erg = new ArrayList<>();
		List<Domain> domains = supervisor.getDomains();
		for (Domain d : domains) {
			if (!subscribedDomains.contains(d))
				erg.add(d);
		}
		return erg;
	}
}
