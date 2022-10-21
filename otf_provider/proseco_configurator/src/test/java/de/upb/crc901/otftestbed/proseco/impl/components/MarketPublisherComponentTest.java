package de.upb.crc901.otftestbed.proseco.impl.components;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.upb.crc901.otftestbed.commons.general.ProsecoProcessConfiguration;
import de.upb.crc901.otftestbed.commons.requester.SimpleComposition;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class MarketPublisherComponentTest {

	@Test
	public void calculatePriceTest() throws IOException {

//		MarketPublisherComponent marketPublisherComponent = new MarketPublisherComponent();

		SimpleComposition simpleComposition = new SimpleComposition();
		simpleComposition.setServiceCompositionId(
				"Distributed-GrayScale-SobelEdgeDetector-LocalBinaryPattern-AttributeSelection-MultilayerPerceptron");

		List<String> serviceIds = new ArrayList<>();
		serviceIds.add("Catalano.Imaging.Filters.GrayScale");
		serviceIds.add("Catalano.Imaging.Filters.SobelEdgeDetector");
		serviceIds.add("Catalano.Imaging.Texture.BinaryPattern.LocalBinaryPattern");
		serviceIds.add("weka.attributeSelection.AttributeSelection");
		serviceIds.add("weka.classifiers.functions.MultilayerPerceptron");
		simpleComposition.setServiceId(serviceIds);

		ProsecoProcessConfiguration ppConfiguration = ProsecoProcessConfiguration.getOfflineConfiguration();
		// ppConfiguration.toProperties(new File("asdf.properties"));

//		float price = marketPublisherComponent.calculatePrice(simpleComposition);
//
//		assertTrue(price>0);
	}
}
