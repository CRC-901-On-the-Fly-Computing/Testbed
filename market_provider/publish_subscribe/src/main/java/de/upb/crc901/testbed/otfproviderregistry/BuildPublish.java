package de.upb.crc901.testbed.otfproviderregistry;

import java.util.ArrayList;
import java.util.List;

import de.upb.crc901.testbed.otfproviderregistry.common.Message;
import de.upb.crc901.testbed.otfproviderregistry.common.MessageType;
import de.upb.crc901.testbed.otfproviderregistry.common.RandomPicker;
import de.upb.crc901.testbed.otfproviderregistry.common.Tuple;
import de.upb.crc901.testbed.otfproviderregistry.gui.GUIEvent;
import de.upb.crc901.testbed.otfproviderregistry.gui.SubjectEvent;
import de.upb.crc901.testbed.patriciatrie.PatriciaTrie;
import de.upb.crc901.testbed.patriciatrie.Publication;
import de.upb.crc901.testbed.patriciatrie.TrieNode;

/**
 * This protocol builds upon {@link BuildSON} and assures the self-stabilizing delivery of publications to all subscribers of a domain.
 * 
 * @author Michael
 *
 */
public class BuildPublish extends BuildSON {
	private PatriciaTrie trie; // For storing the publications

	public BuildPublish(OTFProvider parent, Domain d, OTFProviderRegistry supervisor) {
		super(parent, d, supervisor);
		trie = new PatriciaTrie();
	}

	@SuppressWarnings("unchecked")
	void processMessage(Message m) {
		super.processMessage(m);
		MessageType type = (MessageType) ((Message) m).getParameters()[0];
		switch (type) {
		case PUBLISH:
			this.publish((List<Publication>) m.getParameters()[2]);
			break;
		case CHECK_TRIE:
			this.checkTrie((Subject) m.getParameters()[2], (List<Tuple<String, String>>) m.getParameters()[3]);
			break;
		case CHECK_AND_PUBLISH:
			this.checkAndPublish((Subject) m.getParameters()[2], (List<Tuple<String, String>>) m.getParameters()[3], (String) m.getParameters()[4]);
			break;
		default:
			break;
		}
	}

	/**
	 * Returns all publication stored in the patricia trie of this protocol.
	 * 
	 * @return
	 */
	public List<Publication> getPublications() {
		return this.trie.getPublications();
	}

	private void checkAndPublish(Subject sender, List<Tuple<String, String>> tuples, String prefix) {
		this.checkTrie(sender, tuples);
		List<Publication> pub = trie.getPublications(prefix);
		sender.receiveMessage(new Message(MessageType.PUBLISH, domain, pub));
	}

	private void checkTrie(Subject sender, List<Tuple<String, String>> tuples) {
		for (Tuple<String, String> t : tuples) {
			TrieNode v = trie.search(t.x);
			if (v != null) {
				if (!v.getHash().equals(t.y) && !v.isLeafNode()) {
					TrieNode v_1 = v.getLeftChild();
					TrieNode v_2 = v.getRightChild();
					List<Tuple<String, String>> erg = new ArrayList<>();
					erg.add(new Tuple<String, String>(v_1.getLabel(), v_1.getHash()));
					erg.add(new Tuple<String, String>(v_2.getLabel(), v_2.getHash()));
					sender.receiveMessage(new Message(MessageType.CHECK_TRIE, domain, parent, erg));
				} // else: success
			} else {
				TrieNode c = trie.searchMinimalLabelPrefix(t.x);
				if (c != null) {
					List<Tuple<String, String>> erg = new ArrayList<>();
					erg.add(new Tuple<String, String>(c.getLabel(), c.getHash()));
					String c_label = c.getLabel();
					String p = c_label.substring(0, t.x.length() + 1);
					if (p.substring(p.length() - 1, p.length()).equals("0"))
						p = p.substring(0, p.length() - 1) + "1";
					else
						p = p.substring(0, p.length() - 1) + "0";
					sender.receiveMessage(new Message(MessageType.CHECK_AND_PUBLISH, domain, parent, erg, p));
				} else
					sender.receiveMessage(new Message(MessageType.CHECK_AND_PUBLISH, domain, parent, new ArrayList<>(), t.x));
			}
		}
	}

	private void publish(List<Publication> list) {
		for (Publication p : list) {
			if (trie.search(p.getPublicationLabel()) == null && !p.requestTimedOut()) {
				trie.insert(p);
				SubjectEvent event = new SubjectEvent(GUIEvent.SUPERVISOR_LOG,
						new Object[] { parent.getID() + ": Domain " + this.domain.getName() + " received publication " + p.toString() + "\n" });
				if (Parameters.ACTIVATE_GUI) {
					this.event_queue.add(event);
				}
			}
		}
	}

	public void timeout() {
		super.timeout();

		// Remove all timed out request from the trie:
		for (Publication p : trie.getPublications())
			if (p.requestTimedOut()) {
				SubjectEvent event = new SubjectEvent(GUIEvent.SUPERVISOR_LOG,
						new Object[] { "Timeout: Node " + parent.getID() + " removes publication " + p + "\n" });
				
				if (Parameters.ACTIVATE_GUI) {
					
					this.event_queue.add(event);
				}
				trie.remove(p);
			}

		if (Parameters.SELF_STABILIZING_PUBLICATIONS_ACTIVE) {
			if (!trie.isEmpty()) {
				// Start self-stabilizing protocol:
				List<Subject> candidates = new ArrayList<>();
				if (left != null && left.y != null)
					candidates.add(left.y);
				if (right != null && right.y != null)
					candidates.add(right.y);
				if (cycle != null && cycle.y != null)
					candidates.add(cycle.y);

				if (candidates.size() > 0) {
					RandomPicker<Subject> p = new RandomPicker<>(candidates);
					Subject v = p.pickObject();
					List<Tuple<String, String>> erg = new ArrayList<>();
					erg.add(new Tuple<String, String>(trie.getRootLabel(), trie.getRootHash()));
					v.receiveMessage(new Message(MessageType.CHECK_TRIE, domain, parent, erg));
				}
			}
		}
	}

}
