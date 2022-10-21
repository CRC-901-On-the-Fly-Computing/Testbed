package de.upb.crc901.testbed.otfproviderregistry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import de.upb.crc901.testbed.otfproviderregistry.common.Lock;
import de.upb.crc901.testbed.otfproviderregistry.common.Message;
import de.upb.crc901.testbed.otfproviderregistry.common.MessageType;
import de.upb.crc901.testbed.otfproviderregistry.common.Tuple;

/**
 * Realization of the OTFProviderRegistry.
 * 
 * @author Michael
 *
 */
public class OTFProviderRegistryImpl implements OTFProviderRegistry, Subject {
	private boolean running;
	private long wakeupTime;
	private BlockingQueue<Message> messageQueue;
	private BuildSupervisor supervisor_protocol;
	private Lock lock = new Lock();

	public OTFProviderRegistryImpl() {
		wakeupTime = Parameters.PROVIDER_REGISTRY_TIMEOUT;
		running = false;
		messageQueue = new LinkedBlockingQueue<Message>();
		supervisor_protocol = new BuildSupervisor();
	}

	@Override
	public void timeout() {
		this.running = true;
		long lastWakeUpTime = System.currentTimeMillis();

		while (this.running) {
			long timeSinceLastWakeUp = System.currentTimeMillis() - lastWakeUpTime;
			long waitTime = Math.max(this.wakeupTime - timeSinceLastWakeUp, 0);

			if (waitTime <= 0) {
				supervisor_protocol.timeout();
				lastWakeUpTime = System.currentTimeMillis();
				waitTime = this.wakeupTime;
			}

			try {
				Message message = messageQueue.poll(this.wakeupTime, TimeUnit.MILLISECONDS);
				if (message != null) {
					supervisor_protocol.processMessage(message);
				}
				Thread.sleep(1);
			} catch (InterruptedException e) {
				System.err.println("receive interrupted");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void receiveMessage(Object m) {
		try {
			messageQueue.put((Message) m);
		} catch (InterruptedException e) {
			System.err.println("send interrupted");
			e.printStackTrace();
		}
	}

	@Override
	public boolean register(OTFProvider otfp, Domain domain) {
		this.receiveMessage(new Message(MessageType.SUBSCRIBE, otfp, domain));
		return true;
	}

	@Override
	public boolean deregister(OTFProvider otfp, Domain domain) {
		this.receiveMessage(new Message(MessageType.UNSUBSCRIBE, otfp, domain));
		return true;
	}

	/**
	 * Do not use for publications anymore. Instead use
	 * {@link #sendSPRegistration(ServiceProvider, Object, Domain)} in order to let
	 * a ServiceProvider publish messages.
	 */
	@Deprecated
	@Override
	public void sendRequest(String request, String requesterAddress, Domain domain, long timeoutAfter) {
		List<Object> t = new ArrayList<>();
		t.add(requesterAddress);
		t.add(request);
		t.add(timeoutAfter);
		this.receiveMessage(new Message(MessageType.PUBLISH, domain, t));
	}

	@Override
	public boolean sendSPRegistration(Object o, Domain domain) {
		this.receiveMessage(new Message(MessageType.PUBLISH, domain, o));
		return true;
	}

	@Override
	public void addDomain(Domain domain) {
		try {
			lock.lock();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.supervisor_protocol.addDomain(domain);
		lock.unlock();
	}

	@Override
	public void removeDomain(Domain domain) {
		try {
			lock.lock();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.supervisor_protocol.removeDomain(domain);
		lock.unlock();
	}

	@Override
	public List<Domain> getDomains() {
		try {
			lock.lock();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<Domain> erg = new ArrayList<>();
		erg.addAll(this.supervisor_protocol.getDomains());
		lock.unlock();
		return erg;
	}

	@Override
	public String getIdentifier() {
		return "MP";
	}

	@Override
	public List<OTFProvider> getProviderForDomain(Domain d) {
		List<Tuple<String, Subject>> l = this.supervisor_protocol.getDatabase().getSubscribersForDomain(d);
		List<OTFProvider> erg = new ArrayList<>();
		for (Tuple<String, Subject> t : l) {
			if (t.y instanceof OTFProvider)
				erg.add((OTFProvider) t.y);
			else
				System.err.println("WARNING: Found a non-OTFProvider registered in the Database!");
		}
		return erg;
	}

	@Override
	public Domain getDomain(String name) {
		return this.supervisor_protocol.getDomain(name);
	}

}
