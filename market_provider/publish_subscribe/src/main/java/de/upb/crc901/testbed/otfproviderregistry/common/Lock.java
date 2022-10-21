package de.upb.crc901.testbed.otfproviderregistry.common;

/**
 * A lock that is used when certain variables have to be mutually excluded in multiple methods of an object. Is used for method addDomain,
 * removeDomain and getDomains at the OTFProviderRegistry, because we need to avoid parallel reads and writes to the set of domains.
 * 
 * @author Michael
 *
 */
public class Lock {

	private boolean isLocked = false;

	/**
	 * Tries to access and lock the critical area. Waits until it is unlocked.
	 * 
	 * @throws InterruptedException
	 */
	public synchronized void lock() throws InterruptedException {
		while (isLocked) {
			wait();
		}
		isLocked = true;
	}

	/**
	 * Unlocks the critical area.
	 */
	public synchronized void unlock() {
		isLocked = false;
		notify();
	}
}
