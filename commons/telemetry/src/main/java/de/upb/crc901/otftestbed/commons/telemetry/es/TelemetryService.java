package de.upb.crc901.otftestbed.commons.telemetry.es;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;

import de.upb.crc901.otftestbed.commons.environment.Detector;
import de.upb.crc901.otftestbed.commons.reputation.ExtendedServiceReputation;
import de.upb.crc901.otftestbed.commons.reputation.SearchableSignedReputation;
import de.upb.crc901.otftestbed.commons.requester.Offer;
import de.upb.crc901.otftestbed.commons.requester.PerformanceMetrics;
import de.upb.crc901.otftestbed.commons.requester.SimpleComposition;
import de.upb.crc901.otftestbed.commons.requester.SimplePolicy;
import de.upb.crc901.otftestbed.commons.telemetry.es.index.Analysis;
import de.upb.crc901.otftestbed.commons.telemetry.es.index.Authentications;
import de.upb.crc901.otftestbed.commons.telemetry.es.index.CodeProviders;
import de.upb.crc901.otftestbed.commons.telemetry.es.index.NlpRequests;
import de.upb.crc901.otftestbed.commons.telemetry.es.index.OtfProviderNetwork;
import de.upb.crc901.otftestbed.commons.telemetry.es.index.OtfProviders;
import de.upb.crc901.otftestbed.commons.telemetry.es.index.ReputationsComposition;
import de.upb.crc901.otftestbed.commons.telemetry.es.index.ReputationsService;
import de.upb.crc901.otftestbed.commons.telemetry.es.index.Requesters;
import de.upb.crc901.otftestbed.commons.telemetry.es.index.Requests;
import de.upb.crc901.otftestbed.commons.telemetry.es.index.ServiceProviders;
import de.upb.crc901.otftestbed.commons.telemetry.es.index.TransactionsComposition;
import de.upb.crc901.otftestbed.commons.telemetry.es.index.TransactionsService;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.AnalysisComposition;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.Authentication;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.Code;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.CodeProvider;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.Composition;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.Domain;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.NlpRequest;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.OtfProvider;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.Reputation;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.Request;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.Requester;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.ServiceProvider;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.Transaction;
import de.upb.crc901.otftestbed.commons.telemetry.es.repository.TelemetryRepositories;
import de.upb.crc901.otftestbed.commons.telemetry.utils.AuthenticationType;
import de.upb.crc901.otftestbed.commons.utils.MethodQueue;
import de.upb.crc901.otftestbed.postgrest.generated.java_client.api.ServiceRegistryApi;
import de.upb.crc901.otftestbed.postgrest.generated.java_client.model.ServiceRegistry;

@Service
public class TelemetryService {

	/**
	 * The logger
	 */
	private static final Logger log = LoggerFactory.getLogger(TelemetryService.class);

	private TelemetryRepositories repositories;

	private ElasticsearchTemplate esTemplate;


	private static final int RETRIES = 100;
	private static final int RETRY_DELAY = 3000;
	private static final MethodQueue loggingQueue = new MethodQueue(RETRY_DELAY, RETRIES);


	@Autowired
	public TelemetryService(TelemetryRepositories repositories, ElasticsearchTemplate esTemplate) {
		Assert.notNull(repositories, "repositories must not be null");
		Assert.notNull(esTemplate, "esTemplate must not be null");

		this.repositories = repositories;
		this.esTemplate = esTemplate;
	}


	/**
	 * Class used for thread-safe lazy-initialization of singleton.
	 */
	private static class Loader {
		/**
		 * Spring context to get {@link TelemetryService} beans.
		 */
		private static AnnotationConfigApplicationContext context;

		/**
		 * Singleton instance of the {@link TelemetryService} class.
		 */
		private static TelemetryService instance;

		static {
			context = new AnnotationConfigApplicationContext(TelemetryConfiguration.class);
			context.getEnvironment().setActiveProfiles(Detector.detectProfile().toString());
			instance = context.getBean(TelemetryService.class);
		}

		private Loader() {}
	}

	/**
	 * Get an instance of {@link TelemetryService}. This method creates on first
	 * call a static Spring context.
	 *
	 * @return an instance of TelemetryService
	 */
	public static TelemetryService apiFromOwnSpringContext() {
		return Loader.instance;
	}


	public boolean logMessage(String type, Runnable function) {
		return this.loggingQueue.add(() -> {
			try {
				function.run();
				log.debug("Gathered telemetry data ({})...", type);

				return true;
			} catch (NoNodeAvailableException e) {
				log.warn("Error in gathering telemetry data ({}): No node available.", type);
				log.trace("", e);
			} catch (Exception e) {
				log.error("Error in gathering telemetry data ({}): {}", type, e);
			}

			return false;
		});
	}

	/**
	 * Log composition analysis.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param analysis - analysis of composition
	 */
	public void logAnalysisComposition(Analysis analysis) {
		logMessage("composition analysed",
				() -> {
					analysis.setId(analysis.getRequest().getId());
					Analysis es = repositories.analysisRepository.findById(analysis.getId()).orElse(analysis);

					if (es != analysis) {
						es.setVersion(es.getVersion() + 1);

						// get latest end time
						Long endtime = null;
						if ((analysis.getTime() != null) && (analysis.getDuration() != null)) {
							endtime = analysis.getTime() + analysis.getDuration();
						}
						if ((es.getTime() != null) && (es.getDuration() != null)) {
							long tmpTime = es.getTime() + es.getDuration();
							if ((endtime == null) || (endtime < tmpTime)) {
								endtime = tmpTime;
							}
						}

						// set earliest start time
						Long time = analysis.getTime();
						if ((time != null) && ((es.getTime() == null) || (time < es.getTime()))) {
							es.setTime(time);
						}

						// set new duration
						if ((es.getTime() != null) && (endtime != null)) {
							es.setDuration(endtime - es.getTime());
						}


						AnalysisComposition newAna = analysis.getAnalysis();
						if (newAna != null) {

							if (es.getAnalysis() == null) {
								es.setAnalysis(new AnalysisComposition());
							}
							AnalysisComposition esAna = es.getAnalysis();


							// set new number of analysis
							if (esAna.getNumComp() == null) {
								esAna.setNumComp(newAna.getNumComp());
							} else if (newAna.getNumComp() != null) {
								esAna.setNumComp(newAna.getNumComp() + esAna.getNumComp());
							}

							// set new number of errors
							if (esAna.getNumError() == null) {
								esAna.setNumError(newAna.getNumError());
							} else if (newAna.getNumError() != null) {
								esAna.setNumError(newAna.getNumError() + esAna.getNumError());
							}

							// set new error rate
							if (esAna.getNumError() == null || esAna.getNumComp() == null || esAna.getNumComp() == 0) {
								esAna.setError(null);
							} else {
								esAna.setError((float)(esAna.getNumError() / (double)esAna.getNumComp()));
							}
						}
					}

					repositories.analysisRepository.save(es);
				});
	}

	/**
	 * Log composition analysis.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param duration - of analysis
	 * @param requestId - for which the analysis is processed
	 * @param numberCompositions - number of analysed compositions
	 * @param numberErrors - number of unfitting compositions
	 */
	public void logAnalysisComposition(Long duration, String requestId, Long numberCompositions, Long numberErrors) {
		this.logAnalysisComposition(System.currentTimeMillis(), duration, requestId, numberCompositions, numberErrors);
	}

	/**
	 * Log composition analysis.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param time - of analysis
	 * @param duration - of analysis
	 * @param requestId - for which the analysis is processed
	 * @param numberCompositions - number of analysed compositions
	 * @param numberErrors - number of unfitting compositions
	 */
	public void logAnalysisComposition(Long time, Long duration, String requestId, Long numberCompositions, Long numberErrors) {
		Float error = (numberErrors == null || numberCompositions == null || numberCompositions == 0 ? null : (float)(numberErrors / (double)numberCompositions));

		logAnalysisComposition(
				new Analysis()
				.time(time)
				.duration(duration)
				.request(
						new Request()
						.id(requestId))
				.analysis(
						new AnalysisComposition()
						.numComp(numberCompositions)
						.numError(numberErrors)
						.error(error)));
	}

	/**
	 */
	public void logOtfProviderNetworkMessage(List<String> message) {
		logMessage("otf provider network message",
				() -> repositories.otfProviderNetworkRepository.save(
						new OtfProviderNetwork().message(message)
						));
	}

	/**
	 * Log authentication.
	 *
	 * @param type - of authentication
	 * @param time - of authentication
	 * @param success - true, when the authentication was successful
	 */
	public void logAuthentication(AuthenticationType type, Long time, Boolean success, SimplePolicy policy, String reviewMessage) {
		Authentications entity =
				new Authentications()
				.time(time)
				.authentication(
						new Authentication()
						.type(type.toLowerCase())
						.success(success))
				.accessPolicy(policy)
				.reputation(
						new Reputation()
						.message(reviewMessage));

		logMessage("authentication " + type.toLowerCase(),
				() -> repositories.authenticationsRepository.save(entity));
	}

	/**
	 * Log authentication with actual time as time stamp.
	 *
	 * @param type - of authentication
	 * @param success - true, when the authentication was successful
	 */
	public void logAuthentication(AuthenticationType type, Boolean success, SimplePolicy policy, String reviewMessage) {
		this.logAuthentication(type, System.currentTimeMillis(), success, policy, reviewMessage);
	}

	/**
	 * Log authentication to buy.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param time - of authentication
	 * @param success - true, when the authentication was successful
	 */
	public void logAuthenticationBuy(Long time, Boolean success, SimplePolicy policy) {
		this.logAuthentication(AuthenticationType.BUY, time, success, policy, null);
	}

	/**
	 * Log authentication to buy with actual time as time stamp.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param success - true, when the authentication was successful
	 */
	public void logAuthenticationBuy(Boolean success, SimplePolicy policy) {
		this.logAuthentication(AuthenticationType.BUY, success, policy, null);
	}

	/**
	 * Log authentication to review.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param time - of authentication
	 * @param success - true, when the authentication was successful
	 * @param reviewMessage - the message of the review
	 */
	public void logAuthenticationReview(Long time, Boolean success, String reviewMessage) {
		this.logAuthentication(AuthenticationType.REVIEW, time, success, null, reviewMessage);
	}

	/**
	 * Log authentication to review with actual time as time stamp.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param success - true, when the authentication was successful
	 * @param reviewMessage - the message of the review
	 */
	public void logAuthenticationReview(Boolean success, String reviewMessage) {
		this.logAuthentication(AuthenticationType.REVIEW, success, null, reviewMessage);
	}

	/**
	 * Log that code provider added code.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param providerId - id of adding provider
	 * @param providerName - name of adding provider
	 * @param code - which was added to the provider
	 */
	public void logCodeProviderAdded(String providerId, String providerName, String code) {
		CodeProviders entity =
				new CodeProviders()
				.codeProvider(
						new CodeProvider()
						.id(providerId)
						.name(providerName))
				.code(
						new Code()
						.name(code));

		logMessage("code provider added code",
				() -> repositories.codeProvidersRepository.save(entity));
	}

	/**
	 * Log that code provider was removed.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param providerId - id of removed provider
	 */
	public void logCodeProviderRemoved(String providerId) {
		DeleteQuery deleteQuery = new DeleteQuery();
		deleteQuery.setQuery(matchQuery("codeProvider.id", providerId));

		logMessage("code provider removed",
				() -> esTemplate.delete(deleteQuery, CodeProviders.class));
	}

	/**
	 * Log that code provider removed code.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param providerId - id of removing provider
	 * @param code - which the provider removed
	 */
	public void logCodeProviderRemovedCode(String providerId, String code) {
		DeleteQuery deleteQuery = new DeleteQuery();
		deleteQuery.setQuery(queryStringQuery("codeProvider.id:\"" + providerId + "\" AND code.name:\"" + code + "\""));

		logMessage("code provider removed code",
				() -> esTemplate.delete(deleteQuery, CodeProviders.class));
	}

	/**
	 * Log that code was removed.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param code - name of removed code
	 */
	public void logCodeRemoved(String code) {
		DeleteQuery deleteQuery = new DeleteQuery();
		deleteQuery.setQuery(matchQuery("code.name", code));

		logMessage("code removed",
				() -> esTemplate.delete(deleteQuery, CodeProviders.class));
	}

	/**
	 * Log that domain was added.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param domain - which was added
	 */
	public void logDomainAdded(String domain) {
		OtfProviders entity =
				new OtfProviders()
				.domain(
						new Domain()
						.name(domain));

		logMessage("domain added",
				() -> repositories.otfProvidersRepository.save(entity));
	}

	/**
	 * Log that domain was removed.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param domain - which was removed
	 */
	public void logDomainRemoved(String domain) {
		DeleteQuery deleteQuery = new DeleteQuery();
		deleteQuery.setQuery(matchQuery("domain.name", domain));

		logMessage("domain removed",
				() -> esTemplate.delete(deleteQuery, OtfProviders.class));
	}

	/**
	 * Log a request for natural language processing (NLP).
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param time - the request was sent
	 * @param duration - of the processing
	 * @param nlpRequest - metrics of the processing
	 */
	public void logNlpRequest(Long time, Long duration, NlpRequest nlpRequest) {
		NlpRequests entity =
				new NlpRequests()
				.time(time)
				.duration(duration)
				.nlpRequest(nlpRequest);

		logMessage("nlp request",
				() -> repositories.nlpRequestsRepository.save(entity));
	}

	/**
	 * Log a request for natural language processing (NLP).
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param time - the request was sent
	 * @param duration - of the processing
	 * @param metrics - performance metrics of the processing
	 */
	public void logNlpRequest(Long time, Long duration, PerformanceMetrics metrics) {
		this.logNlpRequest(time, duration, new NlpRequest(metrics));
	}

	/**
	 * Log a request for natural language processing (NLP) with actual time as time stamp.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param duration - of the processing
	 * @param nlpRequest - metrics of the processing
	 */
	public void logNlpRequest(Long duration, NlpRequest nlpRequest) {
		this.logNlpRequest(System.currentTimeMillis(), duration, nlpRequest);
	}

	/**
	 * Log a request for natural language processing (NLP) with actual time as time stamp.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param metrics - performance metrics of the processing
	 */
	public void logNlpRequest(PerformanceMetrics metrics) {
		this.logNlpRequest(System.currentTimeMillis(), metrics.getExecutionTime(), metrics);
	}

	/**
	 * Delete all registered otf providers and domains.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 */
	public void logOtfProvidersReset() {
		logMessage("otf providers reset",
				() -> repositories.otfProvidersRepository.deleteAll());
	}

	/**
	 * Log that otf provider subscribed domain.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param providerId - id of added provider
	 * @param providerName - name of added provider
	 * @param domain - which the provider subscribed
	 */
	public void logOtfProviderAdded(String providerId, String providerName, String domain) {
		OtfProviders entity =
				new OtfProviders()
				.otfProvider(
						new OtfProvider()
						.id(providerId)
						.name(providerName))
				.domain(
						new Domain()
						.name(domain));

		logMessage("otf provider subscribed domain",
				() -> repositories.otfProvidersRepository.save(entity));
	}

	/**
	 * Log that otf provider was removed.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param providerId - id of removed provider
	 */
	public void logOtfProviderRemoved(String providerId) {
		DeleteQuery deleteQuery = new DeleteQuery();
		deleteQuery.setQuery(matchQuery("otfProvider.id", providerId));

		logMessage("otf provider removed",
				() -> esTemplate.delete(deleteQuery, OtfProviders.class));
	}

	/**
	 * Log that otf provider unsubscribed from domain.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param providerId - id of unsubscribing provider
	 * @param domain - from which the provider unsubscribed
	 */
	public void logOtfProviderUnsubscribed(String providerId, String domain) {
		DeleteQuery deleteQuery = new DeleteQuery();
		deleteQuery.setQuery(queryStringQuery("otfProvider.id:\"" + providerId + "\" AND domain.name:\"" + domain + "\""));

		logMessage("otf provider unsubscribed from domain",
				() -> esTemplate.delete(deleteQuery, OtfProviders.class));
	}

	/**
	 * Log that a service composition was rated and add the rating for the single services.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param reputation - for the service composition containing the services
	 */
	public void logReputation(ExtendedServiceReputation reputation) {
		logReputationComposition(reputation);
		logReputationServices(reputation);
	}

	/**
	 * Log that a service composition was rated and add the rating for the single services.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param reputation - for the service composition containing the services
	 */
	public void logReputation(SearchableSignedReputation reputation) {
		logReputation(reputation.getExtendedReputation());
	}

	/**
	 * Log that a service composition was rated.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param reputation - for the service composition
	 */
	public void logReputationComposition(ReputationsComposition reputation) {
		logMessage("service composition reputation",
				() -> repositories.reputationsCompositionRepository.save(reputation));
	}

	/**
	 * Log that a service composition was rated.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param reputation - for the service composition
	 */
	public void logReputationComposition(ExtendedServiceReputation reputation) {
		logReputationComposition(ReputationsComposition.fromExtendedServiceReputation(reputation));
	}

	/**
	 * Log that a service composition was rated.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param reputation - for the service composition
	 */
	public void logReputationComposition(SearchableSignedReputation reputation) {
		logReputationComposition(ReputationsComposition.fromSearchableSignedReputation(reputation));
	}

	/**
	 * Log that a service was rated.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param reputation - for the service
	 */
	public void logReputationService(ReputationsService reputation) {
		logMessage("service reputation",
				() -> repositories.reputationsServiceRepository.save(reputation));
	}

	/**
	 * Log that a service was rated.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param reputation - for the service
	 */
	public void logReputationService(ExtendedServiceReputation reputation) {
		logReputationService(ReputationsService.fromExtendedServiceReputation(reputation));
	}

	/**
	 * Log that a service was rated.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param reputation - for the service
	 */
	public void logReputationService(SearchableSignedReputation reputation) {
		logReputationService(ReputationsService.fromSearchableSignedReputation(reputation));
	}


	/**
	 * Log that multiple services were rated.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param reputation - for the service composition containing the services
	 */
	public void logReputationServices(ExtendedServiceReputation reputation) {
		ServiceRegistryApi serviceRegistryApi = new ServiceRegistryApi();

		for (String service : reputation.getServices()) {
			String provider = null;

			try {
				List<ServiceRegistry> response = serviceRegistryApi.serviceRegistryGet("eq." + service, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
				if (!response.isEmpty()) {
					provider = response.get(0).getCodeProviderId();
				}
			} catch (RestClientException e) {
				log.warn("Could not get additional informations for service '{}'.", service, e);
			}

			/*
			 * Get reputation for composition and change the
			 * names and id to the ones of the service.
			 */
			ReputationsService repu = ReputationsService.fromExtendedServiceReputation(reputation);
			repu.getService().setName(service);
			repu.getServiceProvider().setId(provider);
			repu.getServiceProvider().setName(provider);

			logReputationService(repu);
		}
	}


	/**
	 * Log that multiple services were rated.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param reputation - for the service composition containing the services
	 */
	public void logReputationServices(SearchableSignedReputation reputation) {
		logReputationServices(reputation.getExtendedReputation());
	}

	/**
	 * Log that a service was requested.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param request - the service request
	 */
	public void logRequest(Requests request) {
		logMessage("service request",
				() -> repositories.requestsRepository.save(request));
	}

	/**
	 * Log that a service was requested.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param time - of service request
	 * @param requestId - id of the request
	 * @param domain - of the requested service
	 * @param otfpId - id of choosen otf provider
	 * @param otfpName - name of choosen otf provider
	 */
	public void logRequest(Long time, String requestId, String domain, String otfpId, String otfpName) {
		logRequest(
				new Requests()
				.time(time)
				.request(new Request().id(requestId))
				.domain(new Domain().name(domain))
				.otfProvider(
						new OtfProvider()
						.id(otfpId)
						.name(otfpName)));
	}

	/**
	 * Log that a service was requested with actual time as time stamp.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param requestId - id of the request
	 * @param domain - of the requested service
	 * @param otfpId - id of choosen otf provider
	 * @param otfpName - name of choosen otf provider
	 */
	public void logRequest(String requestId, String domain, String otfpId, String otfpName) {
		this.logRequest(System.currentTimeMillis(), requestId, domain, otfpId, otfpName);
	}

	/**
	 * Log that a service requester registered.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param requester - which registered
	 */
	public void logRequesterAdded(String requester) {
		Requesters entity =
				new Requesters()
				.requester(
						new Requester()
						.name(requester));

		logMessage("requester added",
				() -> repositories.requestersRepository.save(entity));
	}

	/**
	 * Log that a service requester was removed.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param requester - which was removed
	 */
	public void logRequesterRemoved(String requester) {
		DeleteQuery deleteQuery = new DeleteQuery();
		deleteQuery.setQuery(matchQuery("requester.name", requester));

		logMessage("requester removed",
				() -> esTemplate.delete(deleteQuery, Requesters.class));
	}

	/**
	 * Log that service provider added service.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param providerId - id of adding provider
	 * @param providerName - name of adding provider
	 * @param service - which was added to the provider
	 */
	public void logServiceProviderAdded(String providerId, String providerName, String service) {
		ServiceProviders entity =
				new ServiceProviders()
				.serviceProvider(
						new ServiceProvider()
						.id(providerId)
						.name(providerName))
				.service(
						new de.upb.crc901.otftestbed.commons.telemetry.es.property.Service()
						.name(service));

		logMessage("service provider added service",
				() -> repositories.serviceProvidersRepository.save(entity));
	}

	/**
	 * Log that service provider was removed.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param providerId - id of removed provider
	 */
	public void logServiceProviderRemoved(String providerId) {
		DeleteQuery deleteQuery = new DeleteQuery();
		deleteQuery.setQuery(matchQuery("serviceProvider.id", providerId));

		logMessage("service provider removed",
				() -> esTemplate.delete(deleteQuery, ServiceProviders.class));
	}

	/**
	 * Log that service provider removed service.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param providerId - id of removing provider
	 * @param service - which the provider removed
	 */
	public void logServiceProviderRemovedService(String providerId, String service) {
		DeleteQuery deleteQuery = new DeleteQuery();
		deleteQuery.setQuery(queryStringQuery("serviceProvider.id:\"" + providerId + "\" AND service.name:\"" + service + "\""));

		logMessage("service provider removed service",
				() -> esTemplate.delete(deleteQuery, ServiceProviders.class));
	}

	/**
	 * Delete all registered service providers and services.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 */
	public void logServiceProvidersReset() {
		logMessage("service providers reset",
				() -> repositories.serviceProvidersRepository.deleteAll());
	}

	/**
	 * Log that service was removed.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param service - name of removed service
	 */
	public void logServiceRemoved(String service) {
		DeleteQuery deleteQuery = new DeleteQuery();
		deleteQuery.setQuery(matchQuery("service.name", service));

		logMessage("service removed",
				() -> esTemplate.delete(deleteQuery, ServiceProviders.class));
	}

	public void logTransaction(Long time, Offer offer) {
		SimpleComposition sc = offer.getCompositionAndOTFProvider().getSimpleComposition();
		String otfpId = offer.getCompositionAndOTFProvider().getOtfpUUID().toString();
		String compId = sc.getServiceCompositionId();

		Matcher m = Pattern.compile("^[^0-9]*([0-9]+([.][0-9]*)?).*").matcher(offer.getNonFunctionalProperties().get("price"));
		Double salePriceComp = (m.find() ? Double.parseDouble(m.group(1)) : null);
		Double buyPriceComp = 0d;

		for(String service : sc.getServiceId()) {
			String servProvName = null;
			Double salePriceServ = null;
			try {
				ServiceRegistryApi serviceRegistryApi = new ServiceRegistryApi();
				List<ServiceRegistry> response = serviceRegistryApi.serviceRegistryGet("eq." + service, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

				if (!response.isEmpty()) {
					salePriceServ = response.get(0).getPrice().doubleValue();

					// round to 2 digits
					salePriceServ = Math.round(salePriceServ * 100d) / 100d;
					buyPriceComp += salePriceServ;

					servProvName = response.get(0).getCodeProviderId();
				}
			} catch (RestClientException e) {
				log.warn("Could not get additional informations for service '{}'.", service, e);
			}
			logTransactionService(time, otfpId, null, servProvName, servProvName, compId, service, salePriceServ, null);
		}

		// round to 2 digits
		buyPriceComp = Math.round(buyPriceComp * 100d) / 100d;
		logTransactionComposition(time, "anonymous", otfpId, null, compId, salePriceComp, buyPriceComp);
	}

	public void logTransaction(Offer offer) {
		this.logTransaction(System.currentTimeMillis(), offer);
	}

	/**
	 * Log transaction of a service-composition.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param transaction - of the service composition
	 */
	public void logTransactionComposition(TransactionsComposition transaction) {
		logMessage("transaction composition",
				() -> repositories.transactionsCompositionRepository.save(transaction));
	}

	/**
	 * Log transaction of a service-composition.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param time - of transaction
	 * @param requester - buying service-requester
	 * @param providerId - id of selling otf provider
	 * @param providerName - name of selling otf provider
	 * @param composition - that was traded
	 * @param salePrice - the price the service composition was sold to requester
	 * @param buyPrice - the price the services were bought from service providers
	 */
	public void logTransactionComposition(Long time, String requester, String providerId, String providerName, String composition, Double salePrice, Double buyPrice) {
		logTransactionComposition(
				new TransactionsComposition()
				.time(time)
				.requester(
						new Requester()
						.name(requester))
				.otfProvider(
						new OtfProvider()
						.id(providerId)
						.name(providerName))
				.composition(
						new Composition()
						.name(composition))
				.transaction(
						new Transaction()
						.salePrice(salePrice)
						.buyPrice(buyPrice)));
	}

	/**
	 * Log transaction of a service-composition with actual time as time stamp.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param requester - buying service-requester
	 * @param providerId - id of selling otf provider
	 * @param providerName - name of selling otf provider
	 * @param composition - that was traded
	 * @param salePrice - the price the service composition was sold to requester
	 * @param buyPrice - the price the services were bought from service providers
	 */
	public void logTransactionComposition(String requester, String providerId, String providerName, String composition, Double salePrice, Double buyPrice) {
		this.logTransactionComposition(System.currentTimeMillis(), requester, providerId, providerName, composition, salePrice, buyPrice);
	}

	/**
	 * Log transaction of a service.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param transaction - of the service
	 */
	public void logTransactionService(TransactionsService transaction) {
		logMessage("transaction service",
				() -> repositories.transactionsServiceRepository.save(transaction));
	}

	/**
	 * Log transaction of a service.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param time - of transaction
	 * @param otfProviderId - id of buying otf provider
	 * @param otfProviderName - name of buying otf provider
	 * @param serviceProviderId - id of selling service provider
	 * @param serviceProviderName - name of selling service provider
	 * @param composition - the traded service is part of
	 * @param service - that was traded
	 * @param salePrice - the price the service was sold to otf provider
	 * @param buyPrice - the production costs of service
	 */
	@SuppressWarnings("squid:S00107")
	public void logTransactionService(Long time, String otfProviderId, String otfProviderName, String serviceProviderId, String serviceProviderName, String composition, String service, Double salePrice, Double buyPrice) {
		logTransactionService(
				new TransactionsService()
				.time(time)
				.otfProvider(
						new OtfProvider()
						.id(otfProviderId)
						.name(otfProviderName))
				.serviceProvider(
						new ServiceProvider()
						.id(serviceProviderId)
						.name(serviceProviderName))
				.composition(
						new Composition()
						.name(composition))
				.service(
						new de.upb.crc901.otftestbed.commons.telemetry.es.property.Service()
						.name(service))
				.transaction(
						new Transaction()
						.salePrice(salePrice)
						.buyPrice(buyPrice)));
	}

	/**
	 * Log transaction of a service with actual time as time stamp.
	 *
	 * <p>Catches exceptions from api call and logs messages for debugging and errors.</p>
	 * @param otfProviderId - id of buying otf provider
	 * @param otfProviderName - name of buying otf provider
	 * @param serviceProviderId - id of selling service provider
	 * @param serviceProviderName - name of selling service provider
	 * @param composition - the traded service is part of
	 * @param service - that was traded
	 * @param salePrice - the price the service was sold to otf provider
	 * @param buyPrice - the production costs of service
	 */
	@SuppressWarnings("squid:S00107")
	public void logTransactionService(String otfProviderId, String otfProviderName, String serviceProviderId, String serviceProviderName, String composition, String service, Double salePrice, Double buyPrice) {
		this.logTransactionService(System.currentTimeMillis(), otfProviderId, otfProviderName, serviceProviderId, serviceProviderName, composition, service, salePrice, buyPrice);
	}
}
