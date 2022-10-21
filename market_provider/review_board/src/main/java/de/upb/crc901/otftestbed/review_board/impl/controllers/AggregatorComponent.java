package de.upb.crc901.otftestbed.review_board.impl.controllers;

import java.util.Collection;
import java.util.List;
import java.util.function.ToDoubleFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.upb.crc901.otftestbed.commons.reputation.ExtendedServiceReputation;

@Component
public class AggregatorComponent {

	@Autowired
	DisaggregatorComponent disaggregator;

	private <T> double averageDouble(Collection<T> collection, ToDoubleFunction<? super T> mapper, double other) {
		return collection.stream().mapToDouble(mapper).average().orElse(other);
	}

	/**
	 * Aggregates disaggregated reputations by averaging.
	 * 
	 * @param serviceReputations all reputations where this service is part of
	 * @return the reputation of this individual service
	 */
	public ExtendedServiceReputation aggregate(List<ExtendedServiceReputation> serviceReputations) {
		List<ExtendedServiceReputation> disaggregatedServiceReputations = disaggregator
				.disaggregateList(serviceReputations);

		double avgOverall = averageDouble(disaggregatedServiceReputations, ExtendedServiceReputation::getOverall, -1);
		double avgUsability = averageDouble(disaggregatedServiceReputations, ExtendedServiceReputation::getUsability, -1);
		double avgPerformance = averageDouble(disaggregatedServiceReputations, ExtendedServiceReputation::getPerformance, -1);
		double avgSecurity = averageDouble(disaggregatedServiceReputations, ExtendedServiceReputation::getSecurity, -1);
		double avgOther = averageDouble(disaggregatedServiceReputations, ExtendedServiceReputation::getOther, -1);

		ExtendedServiceReputation avgReputation = new ExtendedServiceReputation(avgOverall, avgUsability,
				avgPerformance, avgSecurity, avgOther, null, null, null, null, null);

		if (!serviceReputations.isEmpty()) {
			ExtendedServiceReputation tmpRepu = serviceReputations.iterator().next();

			avgReputation.setServiceCompositionId(tmpRepu.getServiceCompositionId());
			avgReputation.setServices(tmpRepu.getServices());
		}

		return avgReputation;
	}
}
