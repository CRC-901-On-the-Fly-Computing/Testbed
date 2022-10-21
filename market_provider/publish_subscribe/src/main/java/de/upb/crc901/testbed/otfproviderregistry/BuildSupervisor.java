package de.upb.crc901.testbed.otfproviderregistry;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.upb.crc901.testbed.otfproviderregistry.common.Configuration;
import de.upb.crc901.testbed.otfproviderregistry.common.Database;
import de.upb.crc901.testbed.otfproviderregistry.common.Label;
import de.upb.crc901.testbed.otfproviderregistry.common.Message;
import de.upb.crc901.testbed.otfproviderregistry.common.MessageType;
import de.upb.crc901.testbed.otfproviderregistry.common.Tuple;
import de.upb.crc901.testbed.otfproviderregistry.gui.GUIEvent;
import de.upb.crc901.testbed.otfproviderregistry.gui.SubjectEvent;
import de.upb.crc901.testbed.patriciatrie.Publication;

/**
 * Self-stabilizing protocol for the supervisor (i.e., the OTFProviderRegistry).
 * All domains along with their respective subscribers are maintained by this
 * protocol.
 * 
 * @author Michael
 *
 */
public class BuildSupervisor {
	protected Queue<SubjectEvent> event_queue;
	private Database database;
	private int d; // For selecting a domain round-robin
	private int i; // For selecting a subscriber round-robin

	public BuildSupervisor() {
		event_queue = new LinkedList<SubjectEvent>();
		database = new Database();
		d = 0;
		i = 0;
	}

	void processMessage(Message m) {
		MessageType type = (MessageType) ((Message) m).getParameters()[0];
		switch (type) {
		case REPORT_GUI:
			this.reportToGui((Subject) ((Message) m).getParameters()[1]);
			break;
		case SUBSCRIBE:
			this.subscribe((Subject) ((Message) m).getParameters()[1], (Domain) ((Message) m).getParameters()[2]);
			break;
		case UNSUBSCRIBE:
			this.unsubscribe((Subject) ((Message) m).getParameters()[1], (Domain) ((Message) m).getParameters()[2]);
			break;
		case ADD_DOMAIN:
			this.addDomain((Domain) m.getParameters()[1]);
			break;
		case REMOVE_DOMAIN:
			this.removeDomain((Domain) m.getParameters()[1]);
			break;
		case PUBLISH:
			this.publish((Domain) m.getParameters()[1], m.getParameters()[2]);
			break;
		default:
			System.err.println("Error: Supervisor received message with wrong message type: " + type);
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void publish(Domain domain, Object object) {
		if (Parameters.ACTIVATE_GUI)
			this.event_queue.add(new SubjectEvent(GUIEvent.SUPERVISOR_LOG,
					new Object[] { "Publish(" + domain.getName() + ", " + object.toString() + ")\n" }));
		Publication p = null;
		if (object instanceof Tuple<?, ?>) {
			p = new Publication(((Tuple<ServiceProvider, Object>) object).x.getID(),
					((Tuple<ServiceProvider, Object>) object).y);
		} else if (object instanceof List<?>) {
			List<Object> t = (List<Object>) object;
			p = new Publication((String) t.get(0), (String) t.get(1), (long) t.get(2));
		}
		List<Tuple<String, Subject>> subjects = database.getSubscribersForDomain(domain);

		// Broadcast:
		for (Tuple<String, Subject> t : subjects) {
			List<Publication> erg = new ArrayList<>();
			erg.add(p);
			t.y.receiveMessage(new Message(MessageType.PUBLISH, domain, erg));
		}
	}

	void removeDomain(Domain domain) {
		// Notify all existing subscribers of this domain to delete their connections:
		for (Tuple<String, Subject> subscriber : database.getSubscribersForDomain(domain))
			subscriber.y.receiveMessage(
					new Message(MessageType.RECEIVE_CONFIGURATION, domain, new Configuration(null, null, null)));

		// Remove domain from the database:
		this.database.removeDomain(domain);
	}

	void addDomain(Domain domain) {
		this.database.addDomain(domain);
	}

	List<Domain> getDomains() {
		return database.getDomains();
	}

	Domain getDomain(String name) {
		return this.database.getDomain(name);
	}

	Database getDatabase() {
		return this.database;
	}

	void timeout() {
		if (database.getDomains().size() > 0) {
			d = (d + 1) % database.getDomains().size();
			Domain domain = database.getDomains().get(d);

			if (database.getDatabaseSize(domain) > 0) {
				i = (i + 1) % database.getDatabaseSize(domain);
				Subject s = database.getSubjectAtIndex(i, domain);
				Configuration config = database.getConfiguration(s, domain);
				s.receiveMessage(new Message(MessageType.RECEIVE_CONFIGURATION, domain, config));
			}
		}
	}

	private void subscribe(Subject subject, Domain d) {
		if (Parameters.ACTIVATE_GUI)
			this.event_queue.add(new SubjectEvent(GUIEvent.SUPERVISOR_LOG,
					new Object[] { "Subscribe(" + subject.getIdentifier() + ", " + d.getName() + ")\n" }));
		boolean add_edge = database.containsSubject(subject);
		database.insert(subject, d);
		if (!add_edge)
			this.addNewEdgeEvent(subject.getIdentifier());

		// Send configuration to subject:
		Configuration config = database.getConfiguration(subject, d);
		subject.receiveMessage(new Message(MessageType.RECEIVE_CONFIGURATION, d, config));
	}

	private void unsubscribe(Subject subject, Domain domain) {
		if (Parameters.ACTIVATE_GUI)
			this.event_queue.add(new SubjectEvent(GUIEvent.SUPERVISOR_LOG,
					new Object[] { "Unubscribe(" + subject.getIdentifier() + ", " + domain.getName() + ")\n" }));
		boolean add_edge = database.containsSubject(subject);

		// Get the tuple from the database:
		Tuple<String, Subject> tuple = database.getTuple(subject, domain);
		if (tuple != null) {
			if (database.getDatabaseSize(domain) > 1
					&& Label.reconvertLabelToNumber(tuple.x) != database.getDatabaseSize(domain) - 1) {
				Configuration config = database.getConfiguration(tuple.y, domain);
				Subject w = database.getSubject(Label.generateLabel(database.getDatabaseSize(domain) - 1), domain);
				database.changeLabel(w, tuple.x, domain);
				w.receiveMessage(new Message(MessageType.RECEIVE_CONFIGURATION, domain, config));
			}
			database.removeTuple(tuple, domain);
		}
		Configuration config = new Configuration(null, null, null);
		subject.receiveMessage(new Message(MessageType.RECEIVE_CONFIGURATION, domain, config));

		if (add_edge) {
			if (!database.containsSubject(subject))
				this.addRemoveEdgeEvent(subject.getIdentifier());
		}
	}

	private void reportToGui(Subject gui) {
		if (!event_queue.isEmpty()) {
			Queue<SubjectEvent> queue = new LinkedList<>();
			queue.addAll(event_queue);
			this.event_queue.clear();
			gui.receiveMessage(new Message(queue));
		}
	}

	protected void addNewEdgeEvent(String targetID) {
		SubjectEvent e = new SubjectEvent(GUIEvent.NEW_EDGE,
				new Object[] { "MP", targetID, Color.RED, "SUP_EDGE" });

		if (Parameters.ACTIVATE_ELASTIC_SEARCH) {
			ElasticSearchUtil.log(GUIEvent.NEW_EDGE, "MP", targetID, Color.RED, "SUP_EDGE");
		}
		
		if (Parameters.ACTIVATE_GUI) {
			this.event_queue.add(e);
		}
	}

	protected void addRemoveEdgeEvent(String targetID) {
		SubjectEvent e = new SubjectEvent(GUIEvent.REMOVE_EDGE,
				new Object[] { "MP", targetID, Color.RED, "SUP_EDGE" });
		
		if (Parameters.ACTIVATE_ELASTIC_SEARCH) {
			ElasticSearchUtil.log(GUIEvent.REMOVE_EDGE, "MP", targetID, Color.RED, "SUP_EDGE");
		}		
		
		if (Parameters.ACTIVATE_GUI) {
			this.event_queue.add(e);
		}
	}
}
