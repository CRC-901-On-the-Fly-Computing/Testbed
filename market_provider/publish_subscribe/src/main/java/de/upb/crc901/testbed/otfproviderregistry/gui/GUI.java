package de.upb.crc901.testbed.otfproviderregistry.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;
import org.graphstream.graph.implementations.MultiGraph;

import de.upb.crc901.testbed.otfproviderregistry.Subject;
import de.upb.crc901.testbed.otfproviderregistry.common.Message;
import de.upb.crc901.testbed.otfproviderregistry.common.MessageType;

/**
 * GUI that visualizes the publish-/subscribe-system. It uses the <a href="http://graphstream-project.org/">GraphStream</a> framework. It acts as a
 * regular {@link Subject} and periodically sends out messages to all clients that have registered to this gui. Clients then respond by sending all
 * {@link SubjectEvent} that occurred since the last call from the gui. {@link Node Nodes} & {@link Edge Edges} in this vizualization may have
 * different colors and labels.
 * 
 * @see <a href="http://graphstream-project.org/">GraphStream</a>
 * @author Michael
 *
 */
public class GUI extends Thread implements Subject {
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	/**
	 * Turns node labels on or off.
	 */
	private static final boolean ENABLE_LABELS = true;
	/**
	 * Period in which clients are notified by the gui to send it their {@link SubjectEvent}s.
	 */
	private static final long GUI_TIMEOUT = 100;

	private Graph graph;
	private Map<String, Subject> subjects;
	private String styleSheet = "node{fill-mode: dyn-plain;} edge{fill-mode: dyn-plain;}";

	private boolean running;
	private long wakeupTime;
	private BlockingQueue<Message> messageQueue;
	private List<Color> hidingColors;
	private UIController controller;

	public GUI() {
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		graph = new MultiGraph("ps_graph");
		graph.addAttribute("ui.stylesheet", styleSheet);
		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.antialias");
		subjects = new HashMap<>();
		wakeupTime = GUI_TIMEOUT;
		running = false;
		messageQueue = new LinkedBlockingQueue<Message>();
		hidingColors = new ArrayList<>();
		controller = new UIController(this);
		controller.setVisible(true);
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

	@Override
	public void timeout() {
		for (Subject s : subjects.values()) {
			s.receiveMessage(new Message(MessageType.REPORT_GUI, this));
		}
	}

	@SuppressWarnings("unchecked")
	private void processMessage(Message m) {
		if (m.getParameters()[0] instanceof SubjectEvent) {
			this.processSubjectEvent((SubjectEvent) m.getParameters()[0]);
		} else if (m.getParameters()[0] instanceof Queue<?>) {
			this.updateSubject((Queue<SubjectEvent>) m.getParameters()[0]);
		} else
			LOGGER.info("Wrong message format at gui!");
	}

	@Override
	public void receiveMessage(Object m) {
		try {
			messageQueue.put((Message) m);
		} catch (InterruptedException e) {
			LOGGER.info("send interrupted");
			e.printStackTrace();
		}
	}

	/**
	 * Shuts down the subject.
	 */
	public final synchronized void stopSubject() {
		running = false;
	}

	/**
	 * Processes all {@link SubjectEvent SubjectEvents} that are contained in the given queue in order to update the visualization of the given
	 * {@link PS_Subject}.
	 * 
	 * @param s
	 *            the {@link Subject} that has its visualization updated
	 * @param queue
	 *            stores all {@link SubjectEvent SubjectEvents} that have occurred since the last update for s
	 */
	private void updateSubject(Queue<SubjectEvent> queue) {
		while (!queue.isEmpty()) {
			SubjectEvent e = queue.poll();
			this.processSubjectEvent(e);
		}
	}

	private void processSubjectEvent(SubjectEvent e) {
		switch (e.getEventType()) {
		case NEW_NODE:
			this.add_node((Subject) e.getData()[0], (String) e.getData()[1]);
			break;
		case REMOVE_NODE:
			this.remove_node((Subject) e.getData()[0]);
			break;
		case NEW_EDGE:
			this.addEdge((String) e.getData()[0], (String) e.getData()[1], (Color) e.getData()[2], (String) e.getData()[3]);
			break;
		case REMOVE_EDGE:
			this.removeEdge((String) e.getData()[0], (String) e.getData()[1], (Color) e.getData()[2], (String) e.getData()[3]);
			break;
		case UPDATE_NODE_COLOR:
			this.updateNodeColor((String) e.getData()[0], (Color) e.getData()[1]);
			break;
		case UPDATE_NODE_LABEL:
			this.updateNodeLabel((String) e.getData()[0], (String) e.getData()[1]);
			break;
		case SUPERVISOR_LOG:
			this.controller.appendText((String) e.getData()[0]);
			break;
		default:
			break;
		}
	}

	private void add_node(Subject s, String label) {
		if (!subjects.containsKey(s.getIdentifier())) {
			subjects.put(s.getIdentifier(), s);
			if (graph.getNode(s.getIdentifier()) == null) {
				Node node = graph.addNode(s.getIdentifier());
				if (ENABLE_LABELS)
					node.addAttribute("ui.label", "    " + label);
				node.addAttribute("ui.color", Color.GREEN);
			}
		}
	}

	private void remove_node(Subject s) {
		graph.removeNode(s.getIdentifier());
	}

	private void updateNodeColor(String node_id, Color color) {
		Node node = graph.getNode(node_id);
		node.setAttribute("ui.color", color);
	}

	private void updateNodeLabel(String node_id, String label) {
		Node node = graph.getNode(node_id);
		node.setAttribute("ui.label", label);
	}

	private void addEdge(String sID, String tID, Color color, String name) {
		String edge_name = sID + "_" + tID + "_" + color + "_" + name;
		Edge e = graph.addEdge(edge_name, sID, tID, true);
		e.addAttribute("ui.class", edge_name);
		e.addAttribute("ui.color", color);
		if (hidingColors.contains(color))
			e.addAttribute("ui.hide");
	}

	private void removeEdge(String sID, String tID, Color color, String name) {
		graph.removeEdge(sID + "_" + tID + "_" + color + "_" + name);
	}

	/**
	 * Hides all edges of the given color in the visualization.
	 * 
	 * @param color
	 */
	public void hide_Edges(Color color) {
		if (!hidingColors.contains(color))
			hidingColors.add(color);
		for (Edge e : graph.getEdgeSet()) {
			if (e.getAttribute("ui.color") == color) {
				e.addAttribute("ui.hide");
			}
		}
	}

	/**
	 * Shows all edges of the given color in the visualization.
	 * 
	 * @param color
	 */
	public void show_Edges(Color color) {
		if (hidingColors.contains(color))
			hidingColors.remove(color);
		for (Edge e : graph.getEdgeSet()) {
			if (e.getAttribute("ui.color") == color) {
				e.removeAttribute("ui.hide");
			}
		}
	}

	/**
	 * Returns the graph that lies underneath this gui.
	 * 
	 * @return
	 */
	public Graph getGraph() {
		return this.graph;
	}

	@Override
	public String getIdentifier() {
		return "GUI";
	}
}
