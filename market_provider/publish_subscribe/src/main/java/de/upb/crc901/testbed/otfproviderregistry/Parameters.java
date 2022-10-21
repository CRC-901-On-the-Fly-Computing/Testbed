package de.upb.crc901.testbed.otfproviderregistry;

import java.awt.Color;

/**
 * Global parameters for adjusting the publish-subscribe system.
 * 
 * @author Michael
 *
 */
public class Parameters {
	/**
	 * Gives the interval (in ms) in which the OTFProviderRegistry wakes up. The lower the number, the more often the OTFProviderRegistry wakes up,
	 * which results in more stabilization messages being send out. Higher values result in a increased stabilization time.
	 */
	public static final long PROVIDER_REGISTRY_TIMEOUT = 1l;

	/**
	 * Gives the interval (in ms) in which each OTFProvider wakes up. The lower the number, the more often a OTFProvider wakes up, which results in
	 * more stabilization messages being send out. Higher values result in a increased stabilization time.
	 */
	public static final long OTFPROVIDER_TIMEOUT = 1l;

	/**
	 * If false, then nodes do not store changes to their edges in a local queue. This reduces the size of the local storage for a node.
	 */
	public static final boolean ACTIVATE_GUI = true;
	
	/**
	 * If true, NEW_EDGE and REMOVE_EDGE messages are sent to Elastic Search.
	 */
	public static final boolean ACTIVATE_ELASTIC_SEARCH = true;

	/**
	 * Colors for the domain edges. Only needed for visualization.
	 */
	public static final Color[] DOMAIN_COLORS = { Color.BLUE, Color.GREEN, Color.YELLOW, Color.GRAY, Color.ORANGE, Color.MAGENTA, Color.PINK,
			Color.CYAN, };

	/**
	 * If true, then OTFProvider constantly subscribe or unsubscribe to domains. Should be set to false in a real scenario.
	 */
	public static boolean SUBSCRIBER_BOT_ON = true;

	/**
	 * Only relevant, if SUBSCRIBER_BOT_ON = true. For each domain, the OTFProvider invokes a subscribe/unsubscribe event in its timeout with the
	 * given probability.
	 */
	public static final float BOT_ACTIVATION_PROBABILITY = 0.001f;

	/**
	 * Only relevant, if SUBSCRIBER_BOT_ON = true. For each domain, the OTFProvider invokes a publish event in its timeout with the given probability.
	 */
	public static final float BOT_PUBLICATION_PROBABILITY = 0.001f;

	/**
	 * Time in ms for which a request is valid. After the time is up, each subscriber removes the request from its local storage.
	 */
	public static final long REQUEST_TIMEOUT_AFTER = 5000;

	/**
	 * If this is set to true, then subscribers will periodically synchronize their set of publications. This has the consequence that subscribers get
	 * all publications for a domain - even the ones issued before the subscriber has subscribed to the respective domain.
	 */
	public static final boolean SELF_STABILIZING_PUBLICATIONS_ACTIVE = true;
}
