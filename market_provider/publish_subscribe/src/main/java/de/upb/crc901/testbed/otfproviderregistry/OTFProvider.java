package de.upb.crc901.testbed.otfproviderregistry;

import java.util.List;

/**
 * Implementation of the OTFProvider. This is just the part that interacts with
 * the publish-subscribe system written by A1.
 * 
 * @author Michael
 *
 */
public class OTFProvider extends ServiceProvider implements Subject {
	OTFProviderSubscriber subscriber; // handles the publish-subscribe protocol from A1
	String otfpURL;

	public OTFProvider(OTFProviderRegistry sup, String id, String url) {// TODO how does the OTFProvider receive the
																		// OTFProviderRegistry object?
		super(id);
		subscriber = new OTFProviderSubscriber(sup, this);
		otfpURL = url;
	}

	public void run() {
		subscriber.run();
	}

	@Override
	public void timeout() {
		subscriber.timeout();
	}

	@Override
	public void receiveMessage(Object m) {
		subscriber.receiveMessage(m);
	}

	/**
	 * Returns all publications for the given domain stored at this OTFProvider, or
	 * null, if this OTFSubscriber is not a subscriber for the given domain.
	 * 
	 * @param d
	 * @return
	 */ // TODO: is this method sufficient?
	public List<Object> getPublicationsForDomain(Domain d) {
		return subscriber.getPublicationsForDomain(d);
	}

	@Override
	public String getIdentifier() {
		return this.getID();
	}

	public String getURL() {
		return this.otfpURL;
	}
}
