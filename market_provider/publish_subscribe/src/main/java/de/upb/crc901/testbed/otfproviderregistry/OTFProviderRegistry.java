package de.upb.crc901.testbed.otfproviderregistry;

import java.util.List;

public interface OTFProviderRegistry {

	/**
	 * Registers the given OTFProvider within the given domain.
	 * 
	 * @param otfp
	 *            the OTFProvider to be registered
	 * @param domain
	 *            the domain the OTFProvider is to be registered to
	 * @return boolean whether the registration was successful
	 */
	public boolean register(OTFProvider otfp, Domain domain);

	/**
	 * Deregisters the given OTFProvider from the given domain.
	 * 
	 * @param otfp
	 *            the OTFProvider to be deregistered
	 * @param domain
	 *            the domain the OTFProvider is to be deregistered from
	 * @return boolean whether the deregistration was successful
	 */
	public boolean deregister(OTFProvider otfp, Domain domain);

	/**
	 * The requester calls this method. The OTFProviderRegistry decides (based on
	 * the domain) which registered OTF Providers to forward the request to. The
	 * response to a request is an offer. But the offer is sent directly to the
	 * requester (not via the OTFProviderRegistry). For this purpose, the
	 * requester's address is taken as a second input.
	 * 
	 * @param request
	 *            the request specified by the requester. For A1, this is a black
	 *            box (for now?). Within the "later" parts of the testbed, it is
	 *            transformed into an object of type RequirementsSpec to be consumed
	 *            by the configurator etc..
	 * @param requesterAddress
	 *            the address of the requester who calls this method, i.e., a
	 *            callback handle
	 * @param domain
	 *            has to be provided (can either be selected by the user from a list
	 *            of domains provided when calling getDomains() or can probably
	 *            later be inferred by some clever (B1?) mechanism)
	 * @param timeoutAfter
	 *            the amount of time (in ms) for which the request is valid. After
	 *            the time is up, each OTFProvider holding this request will delete
	 *            it from its local storage.
	 * 
	 */
	public void sendRequest(String request, String requesterAddress, Domain domain, long timeoutAfter);

	/**
	 * Triggers the announcement of the given ServiceProvider to all OTFProviders
	 * within the given domain.
	 * 
	 * @param o
	 *            the object (contents) that should be sent to all OTFProviders
	 *            registered to the domain
	 * @param domain
	 *            the domain that contains all OTFProviders to be notified
	 * @return boolean whether the announcement was successful
	 */
	public boolean sendSPRegistration(Object o, Domain domain);

	/**
	 * Adds the given domain to the OTFProvider's domain storage. We currently
	 * assume that the market provider takes care of the domain storage and its
	 * development over time itself.
	 * 
	 * @param domain
	 */
	public void addDomain(Domain domain);

	/**
	 * Removes the given domain from the OTFProvider's domain storage. We currently
	 * assume that the market provider takes care of the domain storage and its
	 * development over time itself.
	 * 
	 * @param domain
	 */
	public void removeDomain(Domain domain);

	/**
	 * Provides a list of all registered domains.
	 * 
	 * @return domains
	 */
	public List<Domain> getDomains();

	/**
	 * Returns a list containing all OTFProvider that are subscribed to the given
	 * domain.
	 * 
	 * @param d
	 *            the domain
	 * @return
	 */
	public List<OTFProvider> getProviderForDomain(Domain d);
	
	public Domain getDomain(String name);
}
