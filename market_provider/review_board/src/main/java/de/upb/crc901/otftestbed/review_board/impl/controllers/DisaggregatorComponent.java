package de.upb.crc901.otftestbed.review_board.impl.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import de.upb.crc901.otftestbed.commons.reputation.ExtendedServiceReputation;

@Component
public class DisaggregatorComponent {

	/**
	 * Takes all the service reputations of service compositions where the service
	 * was a part of and disaggregates them into a List of individual ratings for
	 * the service.
	 * 
	 * @param serviceReputations the list of serviceReputation for service compositions
	 * @return the list of disaggregated service-reputations
	 */
	public List<ExtendedServiceReputation> disaggregateList(List<ExtendedServiceReputation> serviceReputations) {
		List<ExtendedServiceReputation> toReturn = new ArrayList<>();
		serviceReputations.forEach(serviceRep -> toReturn.add(disaggregateServiceReputation(serviceRep)));
		return toReturn;
	}

	/**
	 * Disaggregates a reputation for a service composition into a reputation for an individual service.
	 * @param serviceReputation the reputation of the service composition
	 * @return the rating of the individual service
	 */
	public ExtendedServiceReputation disaggregateServiceReputation(ExtendedServiceReputation serviceReputation) {
		return serviceReputation;
	}
}
