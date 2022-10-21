package de.upb.crc901.testbed.otfproviderregistry.tests.publish;

import java.awt.Color;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import de.upb.crc901.testbed.otfproviderregistry.Domain;
import de.upb.crc901.testbed.otfproviderregistry.MarketProvider;
import de.upb.crc901.testbed.otfproviderregistry.OTFProvider;
import de.upb.crc901.testbed.otfproviderregistry.Parameters;
import de.upb.crc901.testbed.otfproviderregistry.Subject;
import de.upb.crc901.testbed.otfproviderregistry.common.Message;
import de.upb.crc901.testbed.otfproviderregistry.common.RandomPicker;
import de.upb.crc901.testbed.otfproviderregistry.gui.GUIEvent;
import de.upb.crc901.testbed.otfproviderregistry.gui.GUI;
import de.upb.crc901.testbed.otfproviderregistry.gui.SubjectEvent;

/**
 * A static test with multiple domains, where each subscriber randomly subscribes to exactly one domain.
 * 
 * @author Michael
 *
 */
public class PublishOneDomainSubscribeAfterTest {

	public static void main(String[] args) throws InterruptedException {
		int n = 8;
		int d = 1;
		Parameters.SUBSCRIBER_BOT_ON = false;
		List<Domain> domains = new ArrayList<>();
		for (int i = 0; i < d; i++) {
			Domain domain = new Domain();
			domain.setName("" + i);
			domains.add(domain);
		}
		List<OTFProvider> providers = new ArrayList<>();
		Random r = new SecureRandom();

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
			otf.start();
			gui.receiveMessage(new Message(new SubjectEvent(GUIEvent.NEW_NODE, new Object[] { otf, "" })));

			Domain domain = domains.get(r.nextInt(domains.size()));
			// Choose a domain to subscribe to:

			// Let the node subscribe:
			mp.getRegistry().register(otf, domain);
		}

		// Wait
		Thread.sleep(2000);

		for (int i = 0; i < 1; i++) {
			RandomPicker<OTFProvider> p = new RandomPicker<>(providers);
			RandomPicker<Domain> domainPicker = new RandomPicker<>(domains);
			OTFProvider sender = p.pickObject();
			Domain domain = domainPicker.pickObject();
			mp.getRegistry().sendRequest(String.valueOf(i), sender.getID(), domain, Parameters.REQUEST_TIMEOUT_AFTER);
		}

		// Wait
		Thread.sleep(1000);
		
		//Subscribe:
		OTFProvider otf = new OTFProvider(mp.getRegistry(), "" + n, "");
		providers.add(otf);
		otf.start();
		gui.receiveMessage(new Message(new SubjectEvent(GUIEvent.NEW_NODE, new Object[] { otf, "" })));

		Domain domain = domains.get(r.nextInt(domains.size()));

		// Let the node subscribe:
		mp.getRegistry().register(otf, domain);
	}

}
