package de.upb.crc901.testbed.otfproviderregistry;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import de.upb.crc901.testbed.otfproviderregistry.common.Configuration;
import de.upb.crc901.testbed.otfproviderregistry.common.Message;
import de.upb.crc901.testbed.otfproviderregistry.common.MessageType;
import de.upb.crc901.testbed.otfproviderregistry.common.Tuple;
import de.upb.crc901.testbed.otfproviderregistry.gui.GUIEvent;
import de.upb.crc901.testbed.otfproviderregistry.gui.SubjectEvent;
import de.upb.crc901.testbed.patriciatrie.Publication;

/**
 * Implements the self-stabilizing protocol for subscribers. Can be used as a blackbox.
 * 
 * @author Michael
 *
 */
public class OTFProviderSubscriber {
	private OTFProviderRegistry supervisor;
	private OTFProvider parent;

	private boolean running;
	private long wakeupTime;
	private BlockingQueue<Message> messageQueue;
	List<BuildPublish> domains;
	private int d; // For selecting a domain round-robin
	private Queue<SubjectEvent> event_queue;
	private OTFProviderBot bot;

	public OTFProviderSubscriber(OTFProviderRegistry sup, OTFProvider otfProvider) {
		this.parent = otfProvider;
		this.supervisor = sup;
		supervisor = sup;
		wakeupTime = Parameters.OTFPROVIDER_TIMEOUT;
		running = false;
		messageQueue = new LinkedBlockingQueue<Message>();
		domains = new ArrayList<>();
		d = 0;
		event_queue = new LinkedList<SubjectEvent>();
		SubjectEvent e1 = new SubjectEvent(GUIEvent.NEW_EDGE, new Object[] { parent.getID(), "MP", Color.RED, "SUB_SUP" });
		SubjectEvent e2 = new SubjectEvent(GUIEvent.UPDATE_NODE_LABEL, new Object[] { parent.getID(), parent.getID() });

		if(Parameters.ACTIVATE_ELASTIC_SEARCH) {
			
			ElasticSearchUtil.log(GUIEvent.NEW_EDGE, parent.getID(), "MP", Color.RED, "SUB_SUP");
		}
		
		if (Parameters.ACTIVATE_GUI) {
			this.event_queue.add(e1);
			this.event_queue.add(e2);
		}

		this.bot = new OTFProviderBot(parent, supervisor);
	}

	public List<Object> getPublicationsForDomain(Domain d) {
		for (BuildPublish protocol : domains)
			if (protocol.domain.getName().equals(d.getName())) {
				List<Object> erg = new ArrayList<>();
				for (Publication p : protocol.getPublications())
					erg.add(p.getContent());
				return erg;
			}
		return null;
	}

	public void run() {
		this.running = true;
		long lastWakeUpTime = System.currentTimeMillis();

		while (this.running) {
			long timeSinceLastWakeUp = System.currentTimeMillis() - lastWakeUpTime;
			long waitTime = Math.max(this.wakeupTime - timeSinceLastWakeUp, 0);

			if (waitTime <= 0) {
				timeout();
				lastWakeUpTime = System.currentTimeMillis();
				waitTime = this.wakeupTime;
			}

			try {
				Message message = messageQueue.poll(this.wakeupTime, TimeUnit.MILLISECONDS);
				if (message != null) {
					this.processMessage(message);
				}
				Thread.sleep(1);
			} catch (InterruptedException e) {
				System.err.println("receive interrupted");
				e.printStackTrace();
			}
		}
	}

	public void timeout() {
		if (domains.size() > 0) {
			d = (d + 1) % domains.size();
			domains.get(d).timeout();
		}

		if (Parameters.SUBSCRIBER_BOT_ON)
			this.bot.timeout();
	}

	public void receiveMessage(Object m) {
		try {
			messageQueue.put((Message) m);
		} catch (InterruptedException e) {
			System.err.println("send interrupted");
			e.printStackTrace();
		}
	}

	private BuildSON getProtocol(Domain d) {
		for (BuildSON p : domains)
			if (p.domain.getName().equals(d.getName()))
				return p;
		return null;
	}

	@SuppressWarnings("unchecked")
	private void processMessage(Message m) {
		MessageType type = (MessageType) ((Message) m).getParameters()[0];
		switch (type) {
		case RECEIVE_CONFIGURATION:
			Domain d = (Domain) m.getParameters()[1];
			if (getProtocol(d) != null) {
				// Remove protocol if config is null:
				if (((Configuration) m.getParameters()[2]).label == null) {
					getProtocol(d).processMessage(m); // Invokes removal of all edges
					this.event_queue.addAll(getProtocol(d).getEventQueue());
					domains.remove(getProtocol(d));
				} else
					getProtocol(d).processMessage(m);
			} else if (((Configuration) m.getParameters()[2]).label != null) {
				domains.add(new BuildPublish(parent, d, supervisor));
				getProtocol(d).processMessage(m);
			}
			break;
		case CHECK:
			d = (Domain) m.getParameters()[1];
			if (getProtocol(d) != null)
				getProtocol(d).processMessage(m);
			else {
				Tuple<String, Subject> t = (Tuple<String, Subject>) m.getParameters()[2];
				t.y.receiveMessage(new Message(MessageType.REMOVE_CONNECTION, d, parent));
			}
			break;
		case CHECK_SHORTCUT:
			d = (Domain) m.getParameters()[1];
			if (getProtocol(d) != null)
				getProtocol(d).processMessage(m);
			else {
				Subject sender = (Subject) m.getParameters()[4];
				sender.receiveMessage(new Message(MessageType.REMOVE_CONNECTION, d, parent));
			}
			break;
		case INTRODUCE_CYC:
			d = (Domain) m.getParameters()[1];
			if (getProtocol(d) != null)
				getProtocol(d).processMessage(m);
			// No else-part here, since introduce_cyc messages do not store the sender.
			break;
		case INTRODUCE_SHORTCUT:
			d = (Domain) m.getParameters()[1];
			if (getProtocol(d) != null)
				getProtocol(d).processMessage(m);
			else {
				Subject t = (Subject) m.getParameters()[3];
				if (t != null) // Shortcuts equal to null may be introduced by BuildSON!
					t.receiveMessage(new Message(MessageType.REMOVE_CONNECTION, d, parent));
			}
			break;
		case LINEARIZE:
			d = (Domain) m.getParameters()[1];
			if (getProtocol(d) != null)
				getProtocol(d).processMessage(m);
			// No else-part here, since introduce_cyc messages do not store the sender.
			break;
		case PROBE_SHORTCUT:
			d = (Domain) m.getParameters()[1];
			if (getProtocol(d) != null)
				getProtocol(d).processMessage(m);
			else {
				Subject t = (Subject) m.getParameters()[3];
				t.receiveMessage(new Message(MessageType.REMOVE_CONNECTION, d, parent));
			}
			break;
		case REMOVE_CONNECTION:
			d = (Domain) m.getParameters()[1];
			if (getProtocol(d) != null)
				getProtocol(d).processMessage(m);
			// No else-part here, since all connections have already been removed.
			break;
		case PUBLISH:
			d = (Domain) m.getParameters()[1];
			if (getProtocol(d) != null)
				getProtocol(d).processMessage(m);
			// No else-part here, since this subscriber is not subscribed to the given domain.
			break;
		case CHECK_TRIE:
			d = (Domain) m.getParameters()[1];
			if (getProtocol(d) != null)
				getProtocol(d).processMessage(m);
			// No else-part here, since this subscriber is not subscribed to the given domain.
			break;
		case CHECK_AND_PUBLISH:
			d = (Domain) m.getParameters()[1];
			if (getProtocol(d) != null)
				getProtocol(d).processMessage(m);
			// No else-part here, since this subscriber is not subscribed to the given domain.
			break;
		case REPORT_GUI:
			this.reportToGui((Subject) ((Message) m).getParameters()[1]);
			break;
		default:
			break;
		}
	}

	private void reportToGui(Subject gui) {
		if (!event_queue.isEmpty()) {
			Queue<SubjectEvent> queue = new LinkedList<>();
			queue.addAll(event_queue);
			this.event_queue.clear();
			gui.receiveMessage(new Message(queue));
		}
		for (BuildSON p : domains)
			p.reportToGui(gui);
	}
}
