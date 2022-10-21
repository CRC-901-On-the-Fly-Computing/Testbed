package de.upb.crc901.testbed.otfproviderregistry.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import org.graphstream.graph.Graph;
import org.graphstream.ui.view.Viewer;

import de.upb.crc901.testbed.otfproviderregistry.Parameters;

/**
 * Provides some additional functions for the gui, using java.swing.
 * 
 * @author Michael
 *
 */
public class UIController extends JFrame {
	private static final long serialVersionUID = -6056037851160709440L;

	private GUI graph_gui;
	private Viewer viewer;
	private JTextArea console;
	private JScrollPane sp;

	public UIController(GUI gui) {
		this.graph_gui = gui;
		initUI();
	}

	private void initUI() {
		setTitle("Controller");
		setSize(1200, 800);
		setLocation(200, 200);
		this.setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JCheckBox box = new JCheckBox("Show Cycle Edges");
		box.setSelected(true);
		box.setBounds(0, 0, 200, 25);
		box.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (Color c : Parameters.DOMAIN_COLORS)
					checkbox_action(box, c);
			}
		});
		this.getContentPane().add(box);

		JCheckBox box2 = new JCheckBox("Show Shortcut Edges");
		box2.setSelected(true);
		box2.setBounds(0, 30, 200, 25);
		box2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (Color c : Parameters.DOMAIN_COLORS)
					checkbox_action(box2, c.darker().darker());
			}
		});
		this.getContentPane().add(box2);

		JCheckBox box3 = new JCheckBox("Show Supervisor Edges");
		box3.setSelected(true);
		box3.setBounds(0, 60, 200, 25);
		box3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				checkbox_action(box3, Color.RED);
			}
		});
		this.getContentPane().add(box3);

		JCheckBox box4 = new JCheckBox("Enable Bots");
		box4.setSelected(Parameters.SUBSCRIBER_BOT_ON);
		box4.setBounds(0, 90, 200, 25);
		box4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				enable_bots(box4);
			}
		});
		this.getContentPane().add(box4);

		console = new JTextArea();
		console.setEditable(false);
		console.append("OTFProvider timeout interval: " + Parameters.OTFPROVIDER_TIMEOUT + "\n");
		console.append("Provider Registry timeout interval: " + Parameters.PROVIDER_REGISTRY_TIMEOUT + "\n");
		console.append("Bot activation probability: " + Parameters.BOT_ACTIVATION_PROBABILITY + "\n");
		console.append("Bot publication probability: " + Parameters.BOT_PUBLICATION_PROBABILITY + "\n");
		console.append("Self-stabilizing publications active: " + Parameters.SELF_STABILIZING_PUBLICATIONS_ACTIVE + "\n");
		console.append("Request timeout after: " + Parameters.REQUEST_TIMEOUT_AFTER + "ms\n");
		console.append("----------------------------------------------------------------------------------\n");

		DefaultCaret caret = (DefaultCaret) console.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		sp = new JScrollPane(console);
		sp.setBounds(0, 120, 350, 530);
		sp.setBorder(null);
		this.getContentPane().add(sp);

		Graph graph = this.graph_gui.getGraph();
		viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		// ...
		JPanel view = viewer.addDefaultView(false); // false indicates "no JFrame".
		// ...
		viewer.enableAutoLayout();
		view.setBounds(350, 50, 800, 600);
		this.getContentPane().add(view);
	}

	void appendText(String line) {
		this.console.append(line);
		JScrollBar sb = sp.getVerticalScrollBar();
		sb.setValue(sb.getMaximum());
	}

	private void checkbox_action(JCheckBox box, Color color) {
		if (box.isSelected()) {
			graph_gui.show_Edges(color);
		} else {
			graph_gui.hide_Edges(color);
		}
	}

	private void enable_bots(JCheckBox box) {
		if (box.isSelected()) {
			Parameters.SUBSCRIBER_BOT_ON = true;
		} else {
			Parameters.SUBSCRIBER_BOT_ON = false;
			viewer.disableAutoLayout();
		}
	}
}