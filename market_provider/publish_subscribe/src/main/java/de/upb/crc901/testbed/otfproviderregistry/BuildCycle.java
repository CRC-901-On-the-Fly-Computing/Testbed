package de.upb.crc901.testbed.otfproviderregistry;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;

import de.upb.crc901.testbed.otfproviderregistry.common.Configuration;
import de.upb.crc901.testbed.otfproviderregistry.common.Label;
import de.upb.crc901.testbed.otfproviderregistry.common.Message;
import de.upb.crc901.testbed.otfproviderregistry.common.MessageType;
import de.upb.crc901.testbed.otfproviderregistry.common.Tuple;
import de.upb.crc901.testbed.otfproviderregistry.gui.DomainColorMapper;
import de.upb.crc901.testbed.otfproviderregistry.gui.GUIEvent;
import de.upb.crc901.testbed.otfproviderregistry.gui.SubjectEvent;

/**
 * Implementation of the BuildCycle protocol that is also able to detect corrupt node labels.
 * 
 * @author Michael
 *
 */
public class BuildCycle {
	protected OTFProvider parent;
	protected Queue<SubjectEvent> event_queue;
	protected OTFProviderRegistry supervisor;

	protected Tuple<String, Subject> left, right, cycle;
	protected String label;
	protected final Domain domain;

	public BuildCycle(OTFProvider parent, Domain d, OTFProviderRegistry supervisor) {
		this.parent = parent;
		this.domain = d;
		this.supervisor = supervisor;
		event_queue = new LinkedList<SubjectEvent>();
		label = null;
		left = null;
		right = null;
	}

	@SuppressWarnings("unchecked")
	void processMessage(Message m) {
		MessageType type = (MessageType) ((Message) m).getParameters()[0];
		switch (type) {
		case REPORT_GUI:
			if (Parameters.ACTIVATE_GUI)
				this.reportToGui((Subject) ((Message) m).getParameters()[1]);
			break;
		case RECEIVE_CONFIGURATION:
			this.receiveConfiguration((Configuration) ((Message) m).getParameters()[2]);
			break;
		case INTRODUCE_CYC:
			this.introduce_cyc((Tuple<String, Subject>) ((Message) m).getParameters()[2]);
			break;
		case REMOVE_CONNECTION:
			this.remove_connection((Subject) ((Message) m).getParameters()[2]);
			break;
		case CHECK:
			this.check((Tuple<String, Subject>) ((Message) m).getParameters()[2], (String) ((Message) m).getParameters()[3],
					(boolean) ((Message) m).getParameters()[4]);
			break;
		case LINEARIZE:
			this.linearize((Tuple<String, Subject>) ((Message) m).getParameters()[2]);
			break;
		default:
			break;
		}
	}

	public void timeout() {
		if (label != null) {
			float idHash = Label.convertLabelToFloat(label);

			// Check cycle:
			if (this.cycle == null) {
				if (this.left == null && this.right != null)
					this.right.y.receiveMessage(new Message(MessageType.INTRODUCE_CYC, domain, new Tuple<String, Subject>(label, parent)));
				if (this.left != null && this.right == null)
					this.left.y.receiveMessage(new Message(MessageType.INTRODUCE_CYC, domain, new Tuple<String, Subject>(label, parent)));
			} else {
				float id_cyc = Label.convertLabelToFloat(cycle.x);
				if (this.left != null && id_cyc > idHash) {
					this.left.y.receiveMessage(new Message(MessageType.INTRODUCE_CYC, domain, new Tuple<String, Subject>(cycle.x, cycle.y)));
					this.setCycle(null, null);
				}
				if (this.right != null && id_cyc < idHash) {
					this.right.y.receiveMessage(new Message(MessageType.INTRODUCE_CYC, domain, new Tuple<String, Subject>(cycle.x, cycle.y)));
					this.setCycle(null, null);
				}
				if ((this.left == null && id_cyc > idHash) || (this.right == null && id_cyc < idHash)) {
					this.cycle.y.receiveMessage(new Message(MessageType.CHECK, domain, new Tuple<String, Subject>(label, parent), cycle.x, true));
				}
			}

			// BuildList Timeout
			// Check left:
			if (this.left != null) {
				float idLeft = Label.convertLabelToFloat(left.x);
				if (idLeft < idHash)
					this.left.y.receiveMessage(new Message(MessageType.CHECK, domain, new Tuple<String, Subject>(label, parent), left.x, false));
				else {
					parent.receiveMessage(new Message(MessageType.LINEARIZE, domain, new Tuple<String, Subject>(left.x, left.y)));
					this.setLeft(null, null);
				}
			}

			// Check right:
			if (this.right != null) {
				float idRight = Label.convertLabelToFloat(right.x);
				if (idRight > idHash)
					this.right.y.receiveMessage(new Message(MessageType.CHECK, domain, new Tuple<String, Subject>(label, parent), right.x, false));
				else {
					parent.receiveMessage(new Message(MessageType.LINEARIZE, domain, new Tuple<String, Subject>(right.x, right.y)));
					this.setRight(null, null);
				}
			}
		} else {
			// Remove all connections:
			if (this.cycle != null) {
				this.cycle.y.receiveMessage(new Message(MessageType.REMOVE_CONNECTION, domain, parent));
				this.setCycle(null, null);
			}
			if (this.left != null) {
				this.left.y.receiveMessage(new Message(MessageType.REMOVE_CONNECTION, domain, parent));
				this.setLeft(null, null);
			}
			if (this.right != null) {
				this.right.y.receiveMessage(new Message(MessageType.REMOVE_CONNECTION, domain, parent));
				this.setRight(null, null);
			}
		}
	}

	protected void linearize(Tuple<String, Subject> v) {
		if (label != null) {
			float idHash = Label.convertLabelToFloat(label);
			float id_v = Label.convertLabelToFloat(v.x);

			if ((left != null && v.y == this.left.y) || (right != null && v.y == this.right.y)) {
				if (left != null && v.y == left.y && !v.x.equals(left.x)) {
					if (id_v < idHash)
						left.x = v.x;
					else {
						parent.receiveMessage(new Message(MessageType.INTRODUCE_CYC, domain, new Tuple<String, Subject>(v.x, v.y)));
						this.setLeft(null, null);
					}
				}
				if (right != null && v.y == right.y && !v.x.equals(right.x)) {
					if (id_v > idHash)
						right.x = v.x;
					else {
						parent.receiveMessage(new Message(MessageType.INTRODUCE_CYC, domain, new Tuple<String, Subject>(v.x, v.y)));
						this.setRight(null, null);
					}
				}
			} else {
				// Left side:
				if (this.left != null) {
					float idLeft = Label.convertLabelToFloat(left.x);
					if (id_v < idLeft)
						left.y.receiveMessage(new Message(MessageType.LINEARIZE, domain, new Tuple<String, Subject>(v.x, v.y)));
					if (idLeft < id_v && id_v < idHash) {
						v.y.receiveMessage(new Message(MessageType.LINEARIZE, domain, new Tuple<String, Subject>(left.x, left.y)));
						this.setLeft(v.x, v.y);
					}
				} else if (id_v < idHash) {
					this.setLeft(v.x, v.y);
				} else if (id_v == idHash) {
					v.y.receiveMessage(new Message(MessageType.CHECK, domain, new Tuple<String, Subject>(label, parent), v.x, false));
				}

				// Right side:
				if (this.right != null) {
					float idRight = Label.convertLabelToFloat(right.x);
					if (idRight < id_v)
						right.y.receiveMessage(new Message(MessageType.LINEARIZE, domain, new Tuple<String, Subject>(v.x, v.y)));
					if (idHash < id_v && id_v < idRight) {
						v.y.receiveMessage(new Message(MessageType.LINEARIZE, domain, new Tuple<String, Subject>(right.x, right.y)));
						this.setRight(v.x, v.y);
					}
				} else if (id_v > idHash) {
					this.setRight(v.x, v.y);
				} else if (id_v == idHash) {
					v.y.receiveMessage(new Message(MessageType.CHECK, domain, new Tuple<String, Subject>(label, parent), v.x, false));
				}
			}
		} else {
			v.y.receiveMessage(new Message(MessageType.REMOVE_CONNECTION, domain, parent));
		}
	}

	private void introduce_cyc(Tuple<String, Subject> v) {
		if (label != null) {
			float idHash = Label.convertLabelToFloat(label);
			float id_v = Label.convertLabelToFloat(v.x);

			if (cycle != null && this.cycle.y == v.y) {
				if (!cycle.x.equals(v.x)) {
					float id_cyc = Label.convertLabelToFloat(cycle.x);
					if ((id_v < idHash && id_cyc < idHash) || (id_v > idHash && id_cyc > idHash))
						this.cycle.x = v.x;
					else {
						parent.receiveMessage(new Message(MessageType.INTRODUCE_CYC, domain, new Tuple<String, Subject>(v.x, v.y)));
						this.setCycle(null, null);
					}
				}
			} else {
				if (this.cycle == null) {
					if ((id_v < idHash && this.right == null) || (id_v > idHash && this.left == null)) {
						this.setCycle(v.x, v.y);
					} else if (id_v < idHash && this.right != null)
						right.y.receiveMessage(new Message(MessageType.INTRODUCE_CYC, domain, new Tuple<String, Subject>(v.x, v.y)));
					else if (id_v > idHash && this.left != null)
						left.y.receiveMessage(new Message(MessageType.INTRODUCE_CYC, domain, new Tuple<String, Subject>(v.x, v.y)));
					else {
						v.y.receiveMessage(new Message(MessageType.CHECK, domain, new Tuple<String, Subject>(label, parent), v.x, true));
					}
				} else {
					float id_cyc = Label.convertLabelToFloat(cycle.x);

					// Lies on the same side as this node:
					if ((id_v < idHash && id_cyc < idHash) || (id_v > idHash && id_cyc > idHash)) {
						if (cycle.y != v.y) {
							Subject w_1 = Math.abs(id_v - idHash) > Math.abs(id_cyc - idHash) ? v.y : cycle.y;
							Subject w_2 = Math.abs(id_v - idHash) < Math.abs(id_cyc - idHash) ? v.y : cycle.y;
							String w_1_id = w_1 == cycle ? cycle.x : v.x;
							String w_2_id = w_2 == cycle ? cycle.x : v.x;

							this.setCycle(w_1_id, w_1);

							parent.receiveMessage(new Message(MessageType.LINEARIZE, domain, new Tuple<String, Subject>(w_2_id, w_2)));
							w_1.receiveMessage(new Message(MessageType.LINEARIZE, domain, new Tuple<String, Subject>(w_2_id, w_2)));
						}
					} else { // Opposite side
						parent.receiveMessage(new Message(MessageType.LINEARIZE, domain, new Tuple<String, Subject>(v.x, v.y)));
						parent.receiveMessage(new Message(MessageType.LINEARIZE, domain, new Tuple<String, Subject>(cycle.x, cycle.y)));
						this.setCycle(null, null);
					}
				}
			}
		} else
			v.y.receiveMessage(new Message(MessageType.REMOVE_CONNECTION, domain, parent));
	}

	private void check(Tuple<String, Subject> sender, String own_label, boolean cyc) {
		if (label != null) {
			if (!own_label.equals(this.label)) {
				// Send correct id:
				if (cyc)
					sender.y.receiveMessage(new Message(MessageType.INTRODUCE_CYC, domain, new Tuple<String, Subject>(label, parent)));
				else
					sender.y.receiveMessage(new Message(MessageType.LINEARIZE, domain, new Tuple<String, Subject>(label, parent)));
			} else {
				if (cyc)
					this.introduce_cyc(sender);
				else
					this.linearize(sender);
			}
		} else
			sender.y.receiveMessage(new Message(MessageType.REMOVE_CONNECTION, domain, parent));
	}

	protected void remove_connection(Subject subject) {
		if (this.cycle != null && cycle.y == subject) {
			this.setCycle(null, null);
		}
		if (this.left != null && left.y == subject) {
			this.setLeft(null, null);
		}
		if (this.right != null && right.y == subject) {
			this.setRight(null, null);
		}
	}

	private void setCycle(String label, Subject subject) {
		Tuple<String, Subject> oldCycle = cycle;
		if (this.cycle != null && cycle.y != subject)
			this.addRemoveEdgeEvent(cycle.y.getIdentifier(), DomainColorMapper.mapDomain(domain), "CYCLE_CYCLE");
		if (label != null)
			this.cycle = new Tuple<String, Subject>(label, subject);
		else
			this.cycle = null;
		if (this.cycle != null && (oldCycle == null || cycle.y != oldCycle.y))
			this.addNewEdgeEvent(cycle.y.getIdentifier(), DomainColorMapper.mapDomain(domain), "CYCLE_CYCLE");
	}

	private void setLeft(String label, Subject subject) {
		Tuple<String, Subject> oldLeft = left;
		if (this.left != null && left.y != subject) {
			this.addRemoveEdgeEvent(left.y.getIdentifier(), DomainColorMapper.mapDomain(domain), "CYCLE_LEFT");
		}
		if (label != null)
			this.left = new Tuple<String, Subject>(label, subject);
		else
			this.left = null;
		if (this.left != null && (oldLeft == null || left.y != oldLeft.y))
			this.addNewEdgeEvent(left.y.getIdentifier(), DomainColorMapper.mapDomain(domain), "CYCLE_LEFT");
	}

	private void setRight(String label, Subject subject) {
		Tuple<String, Subject> oldRight = right;
		if (this.right != null && right.y != subject)
			this.addRemoveEdgeEvent(right.y.getIdentifier(), DomainColorMapper.mapDomain(domain), "CYCLE_RIGHT");
		if (label != null)
			this.right = new Tuple<String, Subject>(label, subject);
		else
			this.right = null;
		if (this.right != null && (oldRight == null || right.y != oldRight.y))
			this.addNewEdgeEvent(right.y.getIdentifier(), DomainColorMapper.mapDomain(domain), "CYCLE_RIGHT");
	}

	protected void receiveConfiguration(Configuration configuration) {
		if (label == null || (label != null && !label.equals(configuration.label))) {
			label = configuration.label;
		}

		Tuple<String, Subject> l = configuration.left;
		if (l != null) {
			if (Label.convertLabelToFloat(l.x) > Label.convertLabelToFloat(label)) {
				Tuple<String, Subject> oldLeft = left;
				Tuple<String, Subject> oldCycle = cycle;
				if (cycle == null || (cycle != null && !cycle.x.equals(l.x))) {
					this.setCycle(l.x, l.y);
				}

				if (oldLeft != null) {
					this.setLeft(null, null);
					if (oldLeft.y != cycle.y) {
						this.linearize(oldLeft);
					}
				}

				if (oldCycle != null && Label.convertLabelToFloat(oldCycle.x) > Label.convertLabelToFloat(label)) {
					if (oldCycle.y != cycle.y) {
						this.linearize(oldCycle);
					}
				}
			} else {
				Tuple<String, Subject> oldLeft = left;
				Tuple<String, Subject> oldCycle = cycle;
				if (left == null || (left != null && !left.x.equals(l.x))) {
					this.setLeft(l.x, l.y);
				}

				if (oldLeft != null && oldLeft.y != left.y) {
					this.linearize(oldLeft);
				}

				if (oldCycle != null && Label.convertLabelToFloat(oldCycle.x) > Label.convertLabelToFloat(label)) {
					this.setCycle(null, null);
					if (oldCycle.y != left.y)
						this.linearize(oldCycle);
				}
			}
		} else {
			if (left != null) {
				this.setLeft(null, null);
			}
			if (cycle != null && (label == null || Label.convertLabelToFloat(cycle.x) > Label.convertLabelToFloat(label))) {
				this.setCycle(null, null);
			}
		}

		Tuple<String, Subject> r = configuration.right;
		if (r != null) {
			if (Label.convertLabelToFloat(r.x) < Label.convertLabelToFloat(label)) {
				Tuple<String, Subject> oldRight = right;
				Tuple<String, Subject> oldCycle = cycle;
				if (cycle == null || (cycle != null && !cycle.x.equals(r.x))) {
					this.setCycle(r.x, r.y);
				}

				if (oldRight != null) {
					this.setRight(null, null);
					if (oldRight.y != cycle.y) {
						this.linearize(oldRight);
					}
				}

				if (oldCycle != null && Label.convertLabelToFloat(oldCycle.x) < Label.convertLabelToFloat(label)) {
					if (oldCycle.y != cycle.y) {
						this.linearize(oldCycle);
					}
				}
			} else {
				Tuple<String, Subject> oldRight = right;
				Tuple<String, Subject> oldCycle = cycle;
				if (right == null || (right != null && !right.x.equals(r.x))) {
					this.setRight(r.x, r.y);
				}

				if (oldRight != null && oldRight.y != right.y) {
					this.linearize(oldRight);
				}

				if (oldCycle != null && Label.convertLabelToFloat(oldCycle.x) < Label.convertLabelToFloat(label)) {
					this.setCycle(null, null);
					if (oldCycle.y != right.y)
						this.linearize(oldCycle);
				}
			}
		} else {
			if (right != null) {
				this.setRight(null, null);
			}
			if (cycle != null && (label == null || Label.convertLabelToFloat(cycle.x) < Label.convertLabelToFloat(label))) {
				this.setCycle(null, null);
			}
		}
	}

	void reportToGui(Subject gui) {
		if (!event_queue.isEmpty()) {
			Queue<SubjectEvent> queue = new LinkedList<>();
			queue.addAll(event_queue);
			this.event_queue.clear();
			gui.receiveMessage(new Message(queue));
		}
	}

	Queue<SubjectEvent> getEventQueue() {
		return this.event_queue;
	}

	protected void addNewLabelEvent(String new_label) {
		
		
		SubjectEvent e = new SubjectEvent(GUIEvent.UPDATE_NODE_LABEL, new Object[] { parent.getID(), new_label });
		if (Parameters.ACTIVATE_GUI) {
			this.event_queue.add(e);
		}
	}

	protected void addNewEdgeEvent(String targetID, Color color, String type) {
		SubjectEvent e = new SubjectEvent(GUIEvent.NEW_EDGE, new Object[] { parent.getID(), targetID, color, type });
		
		if (Parameters.ACTIVATE_ELASTIC_SEARCH) {
			ElasticSearchUtil.log(GUIEvent.NEW_EDGE, parent.getID(), targetID, color, type);
		}
		
		if (Parameters.ACTIVATE_GUI) {
			this.event_queue.add(e);
		}
	}

	protected void addRemoveEdgeEvent(String targetID, Color color, String type) {
		SubjectEvent e = new SubjectEvent(GUIEvent.REMOVE_EDGE, new Object[] { parent.getID(), targetID, color, type });
		
		if (Parameters.ACTIVATE_ELASTIC_SEARCH) {
			ElasticSearchUtil.log(GUIEvent.REMOVE_EDGE, parent.getID(), targetID, color, type);
		}
		
		if (Parameters.ACTIVATE_GUI) {
			this.event_queue.add(e);
		}
	}
}
