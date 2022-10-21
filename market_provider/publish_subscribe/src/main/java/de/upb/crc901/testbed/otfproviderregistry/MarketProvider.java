package de.upb.crc901.testbed.otfproviderregistry;

/**
 * A dummy class for the MarketProvider.
 * 
 * @author Michael
 *
 */
public class MarketProvider extends Thread {

	private OTFProviderRegistryImpl registry;

	public MarketProvider() {
		this.registry = new OTFProviderRegistryImpl();
	}

	public void run() {
		this.registry.timeout();
	}

	/**
	 * Returns the reference to the OTFProviderRegistry. Once an OTFSubscriber knows this reference, it may communicate with the OTFProviderRegistry
	 * via the publish-subscribe pattern.
	 * 
	 * @return
	 */
	public OTFProviderRegistry getRegistry() {
		return registry;
	}
}
