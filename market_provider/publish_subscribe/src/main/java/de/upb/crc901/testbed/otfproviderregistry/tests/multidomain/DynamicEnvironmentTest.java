package de.upb.crc901.testbed.otfproviderregistry.tests.multidomain;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.upb.crc901.testbed.otfproviderregistry.Domain;
import de.upb.crc901.testbed.otfproviderregistry.MarketProvider;
import de.upb.crc901.testbed.otfproviderregistry.OTFProvider;
import de.upb.crc901.testbed.otfproviderregistry.Subject;
import de.upb.crc901.testbed.otfproviderregistry.common.Message;
import de.upb.crc901.testbed.otfproviderregistry.gui.GUIEvent;
import de.upb.crc901.testbed.otfproviderregistry.gui.GUI;
import de.upb.crc901.testbed.otfproviderregistry.gui.SubjectEvent;

/**
 * Sets up a system with some OTFProviders, each controlled by its own {@link OTFProviderBot}.
 * 
 * @author Michael
 *
 */
public class DynamicEnvironmentTest {

	public static void main(String[] args) {
		int n = 3;
		int d = 1;
		List<Domain> domains = new ArrayList<>();
		for (int i = 0; i < d; i++) {
			Domain domain = new Domain();
			domain.setName("" + i);
			domains.add(domain);
		}
		List<OTFProvider> providers = new ArrayList<>();

		GUI gui = new GUI();
		gui.start();

		MarketProvider mp = new MarketProvider();
		for (Domain domain : domains)
			mp.getRegistry().addDomain(domain);
		mp.start();

		gui.receiveMessage(new Message(new SubjectEvent(GUIEvent.NEW_NODE, new Object[] { mp.getRegistry(), "Supervisor" })));
		Queue<SubjectEvent> node_queue = new LinkedList<>();
		node_queue.add(new SubjectEvent(GUIEvent.UPDATE_NODE_COLOR, new Object[] { ((Subject) mp.getRegistry()).getIdentifier(), Color.RED }));
		gui.receiveMessage(new Message(node_queue));

		for (int i = 0; i < n; i++) {
			OTFProvider otf = new OTFProvider(mp.getRegistry(), "" + i, "");
			providers.add(otf);
			gui.receiveMessage(new Message(new SubjectEvent(GUIEvent.NEW_NODE, new Object[] { otf, "" })));
			otf.start();
		}
	}

}
