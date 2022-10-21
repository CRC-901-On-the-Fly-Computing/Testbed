package de.upb.crc901.testbed.otfproviderregistry;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import de.upb.crc901.testbed.otfproviderregistry.common.Configuration;
import de.upb.crc901.testbed.otfproviderregistry.common.Label;
import de.upb.crc901.testbed.otfproviderregistry.common.Message;
import de.upb.crc901.testbed.otfproviderregistry.common.MessageType;
import de.upb.crc901.testbed.otfproviderregistry.common.Tuple;
import de.upb.crc901.testbed.otfproviderregistry.gui.DomainColorMapper;

/**
 * Protocol that builds upon {@link BuildCycle} and adds additional shortcut edges to the topology of a domain.
 * 
 * @author Michael
 *
 */
public class BuildSON extends BuildCycle {

	private List<Tuple<String, Subject>> shortcuts;
	private int s;
	private Color shortcut_color;

	public BuildSON(OTFProvider parent, Domain d, OTFProviderRegistry supervisor) {
		super(parent, d, supervisor);
		shortcuts = new ArrayList<>();
		s = 0;
		shortcut_color = DomainColorMapper.mapDomain(domain);
		for (int i = 0; i < 2; i++)
			shortcut_color = shortcut_color.darker();
	}

	void processMessage(Message m) {
		super.processMessage(m);
		MessageType type = (MessageType) ((Message) m).getParameters()[0];
		switch (type) {
		case INTRODUCE_SHORTCUT:
			this.introduceShortcut((String) m.getParameters()[2], (Subject) m.getParameters()[3]);
			break;
		case PROBE_SHORTCUT:
			this.probeShortcut((String) m.getParameters()[2], (Subject) m.getParameters()[3]);
			break;
		case CHECK_SHORTCUT:
			this.checkShortcut((String) m.getParameters()[2], (String) m.getParameters()[3], (Subject) m.getParameters()[4]);
			break;
		default:
			break;
		}
	}

	public void timeout() {
		super.timeout();
		// System.out.println("Label: " + label + ", shortcuts: " + shortcuts);
		this.updateShortcuts();

		// Probe for empty shortcuts:
		if (shortcuts.size() > 0) {
			for (Tuple<String, Subject> s : shortcuts) {
				if (s.y == null) {
					this.probeShortcut(s.x, parent);
				}
			}

			// Choose an established shortcut in a round-robin fashion:
			s = (s + 1) % shortcuts.size();
			Tuple<String, Subject> shortcut = shortcuts.get(s);
			if (shortcut.y != null) {
				shortcut.y.receiveMessage(new Message(MessageType.CHECK_SHORTCUT, domain, shortcut.x, label, parent));
			}
		}
	}

	private void checkShortcut(String own_label, String sender_label, Subject sender) {
		if (!own_label.equals(label)) {
			sender.receiveMessage(new Message(MessageType.INTRODUCE_SHORTCUT, domain, own_label, null));
		}
	}

	protected void remove_connection(Subject subject) {
		super.remove_connection(subject);
		List<Tuple<String, Subject>> rem = new ArrayList<>();
		for (Tuple<String, Subject> t : this.shortcuts)
			if (t.y == subject)
				rem.add(t);
		List<Subject> removed = new ArrayList<>();
		for (Tuple<String, Subject> t : rem) {
			if (!removed.contains(t.y)) {
				this.addRemoveEdgeEvent(t.y.getIdentifier(), shortcut_color, "SHORTCUT");
				removed.add(t.y);
			}
			this.shortcuts.remove(t);
		}
	}

	private void introduceShortcut(String label, Subject subject) {
		Tuple<String, Subject> t = this.containsShortcut(shortcuts, label);
		if (t != null) {
			if (t.y != null && t.y != subject)
				this.addRemoveEdgeEvent(t.y.getIdentifier(), shortcut_color, "SHORTCUT");
			if (t.y != subject) {
				boolean hasEdge = alreadyHasEdge(subject);
				t.y = subject;
				if (t.y != null && !hasEdge)
					this.addNewEdgeEvent(t.y.getIdentifier(), shortcut_color, "SHORTCUT");
			}
		}
	}

	private boolean alreadyHasEdge(Subject subject) {
		for (Tuple<String, Subject> t : shortcuts)
			if (t.y == subject)
				return true;
		return false;
	}

	private void probeShortcut(String target, Subject sender) {
		if (label != null && label.equals(target)) {
			sender.receiveMessage(new Message(MessageType.INTRODUCE_SHORTCUT, domain, target, parent));
		} else if (label != null) {
			float t = Label.convertLabelToFloat(target);

			Tuple<String, Subject> dest = new Tuple<>(label, parent);

			if (left != null) {
				float l = Label.convertLabelToFloat(left.x);
				if (Math.abs(t - l) < Math.abs(t - Label.convertLabelToFloat(dest.x))) {
					dest.x = left.x;
					dest.y = left.y;
				}
			}

			if (right != null) {
				float l = Label.convertLabelToFloat(right.x);
				if (Math.abs(t - l) < Math.abs(t - Label.convertLabelToFloat(dest.x))) {
					dest.x = right.x;
					dest.y = right.y;
				}
			}

			if (cycle != null) {
				float l = Label.convertLabelToFloat(cycle.x);
				if (Math.abs(t - l) < Math.abs(t - Label.convertLabelToFloat(dest.x))) {
					dest.x = cycle.x;
					dest.y = cycle.y;
				}
			}

			for (Tuple<String, Subject> s : shortcuts) {
				if (s.y != null) {
					float l = Label.convertLabelToFloat(s.x);
					if (Math.abs(t - l) < Math.abs(t - Label.convertLabelToFloat(dest.x))) {
						dest.x = s.x;
						dest.y = s.y;
					}
				}
			}

			if (dest.y != parent) {
				dest.y.receiveMessage(new Message(MessageType.PROBE_SHORTCUT, domain, target, sender));
			}
		}
	}

	protected void receiveConfiguration(Configuration configuration) {
		super.receiveConfiguration(configuration);

		this.updateShortcuts();
	}

	private void updateShortcuts() {
		List<Tuple<String, Subject>> oldShortcuts = shortcuts;
		List<Tuple<String, Subject>> newShortcuts = new ArrayList<>();
		if (label != null) {
			if (left != null) {
				String l = String.valueOf(left.x);
				while (isYounger(l, label) && !l.equals("1")) {
					float lFloat = Label.convertLabelToFloat(l);
					float labelFloat = Label.convertLabelToFloat(label);
					float newShortcut = 2 * lFloat - labelFloat;
					if (newShortcut >= 0 && newShortcut <= 1) {
						l = Label.convertFloatLabel(newShortcut);

						// Add to list:
						if (this.containsShortcut(newShortcuts, l) == null)
							newShortcuts.add(new Tuple<String, Subject>(l, null));
					} else
						break;
				}
			}
			if (right != null) {
				String r = String.valueOf(right.x);
				while (isYounger(r, label) && !r.equals("1")) {
					float rFloat = Label.convertLabelToFloat(r);
					float labelFloat = Label.convertLabelToFloat(label);
					float newShortcut = 2 * rFloat - labelFloat;
					if (newShortcut >= 0 && newShortcut <= 1) {
						r = Label.convertFloatLabel(newShortcut);

						// Add to list:
						if (this.containsShortcut(newShortcuts, r) == null)
							newShortcuts.add(new Tuple<String, Subject>(r, null));
					} else
						break;
				}
			}
			if (cycle != null && label.equals("0")) {
				String l = String.valueOf(cycle.x);
				while (isYounger(l, label) && !l.equals("1")) {
					float lFloat = Label.convertLabelToFloat(l);
					float labelFloat = 1;
					float newShortcut = 2 * lFloat - labelFloat;
					if (newShortcut >= 0 && newShortcut <= 1) {
						l = Label.convertFloatLabel(newShortcut);

						// Add to list:
						if (this.containsShortcut(newShortcuts, l) == null)
							newShortcuts.add(new Tuple<String, Subject>(l, null));
					} else
						break;
				}
			}
		}

		// Try to include old shortcuts:
		for (Tuple<String, Subject> t : oldShortcuts) {
			Tuple<String, Subject> nT = this.containsShortcut(newShortcuts, t.x);
			if (nT != null) {
				nT.y = t.y;
			} else if (t.y != null) {
				this.addRemoveEdgeEvent(t.y.getIdentifier(), shortcut_color, "SHORTCUT");
			}
		}

		this.shortcuts = newShortcuts;
	}

	private Tuple<String, Subject> containsShortcut(List<Tuple<String, Subject>> newShortcuts, String label) {
		for (Tuple<String, Subject> t : newShortcuts)
			if (t.x.equals(label))
				return t;
		return null;
	}

	private boolean isYounger(String l, String label) {
		if (l.length() == label.length())
			return false;
		int l_n = Label.reconvertLabelToNumber(l);
		int label_n = Label.reconvertLabelToNumber(label);

		return l_n > label_n;
	}
}
