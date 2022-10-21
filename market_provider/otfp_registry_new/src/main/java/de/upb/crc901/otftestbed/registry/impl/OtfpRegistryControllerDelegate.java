package de.upb.crc901.otftestbed.registry.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.upb.crc901.otftestbed.commons.model.OTFProvider;
import de.upb.crc901.otftestbed.commons.requester.OTFProviderConfidence;
import de.upb.crc901.otftestbed.commons.rest.SimpleJSONMessage;
import de.upb.crc901.otftestbed.commons.telemetry.es.TelemetryService;
import de.upb.crc901.otftestbed.registry.generated.spring_server.api.ProviderRegistryApiDelegate;
import de.upb.crc901.otftestbed.registry.generated.spring_server.model.DeregisterObj;
import de.upb.crc901.otftestbed.registry.generated.spring_server.model.ListOfConfidence;
import de.upb.crc901.otftestbed.registry.generated.spring_server.model.ListOfDomains;
import de.upb.crc901.otftestbed.registry.generated.spring_server.model.RegisterObj;
import de.upb.crc901.testbed.otfproviderregistry.Domain;
import de.upb.crc901.testbed.otfproviderregistry.OTFProviderRegistryImpl;
import de.upb.crc901.testbed.otfproviderregistry.Subject;
import de.upb.crc901.testbed.otfproviderregistry.common.Message;
import de.upb.crc901.testbed.otfproviderregistry.common.MessageType;

@Component
public class OtfpRegistryControllerDelegate implements ProviderRegistryApiDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(OtfpRegistryControllerDelegate.class);

	private final Map<String, Subject> providers = new HashMap<>();

	private ListOfConfidence listOfConfidences = new ListOfConfidence();

//	@Autowired
//	private TelemetryService telemetry;

	@Autowired
	private OTFProviderRegistryImpl otfProviderRegistry;

	// only needed for import of multiple swagger generated apis
	@Override
	public Optional<ObjectMapper> getObjectMapper() {
		return Optional.empty();
	}

	// only needed for import of multiple swagger generated apis
	@Override
	public Optional<HttpServletRequest> getRequest() {
		return Optional.empty();
	}

	// only needed for import of multiple swagger generated apis
	@Override
	public Optional<String> getAcceptHeader() {
		return getRequest().map(r -> r.getHeader("Accept"));
	}

	@PreDestroy
	public void deleteTelemetryData() {
//		telemetry.logOtfProvidersReset();
//		telemetry.logServiceProvidersReset();
	}

	@Override
	public ResponseEntity<ListOfDomains> getDomains() {
		ListOfDomains listOfDomains = new ListOfDomains();
		for (Domain domain : otfProviderRegistry.getDomains()) {
			listOfDomains.addDomainListItem(domain.getName());
		}
		return ResponseEntity.ok(listOfDomains);
	}

	@Override
	public ResponseEntity<Void> receiveMessage(Message message) {
		Message newMessage = new Message();
		newMessage.addParameter(MessageType.valueOf((String) message.getParameters()[0])); // this is message type
		Domain domain = new Domain();
		domain.setName((String) message.getParameters()[1]); // this is domain
		newMessage.addParameter(domain);
		for (int i = 2; i < message.getParameters().length; i++) {
			newMessage.addParameter(message.getParameters()[i]);
		}
		this.otfProviderRegistry.receiveMessage(newMessage);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Void> addDomain(Domain domainObj) {
		this.otfProviderRegistry.addDomain(domainObj);
//		telemetry.logDomainAdded(domainObj.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Void> removeDomain(String domainObj) {
		this.otfProviderRegistry.removeDomain(this.otfProviderRegistry.getDomain(domainObj));
//		telemetry.logDomainRemoved(domainObj);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Void> register(RegisterObj registerObj) {
		Subject subj = new RemoteOTFProvider(this.otfProviderRegistry, registerObj.getOtfpId(),
				registerObj.getOtfpUrl());
		providers.put(registerObj.getOtfpId(), subj);

		Message newMessage = new Message();
		newMessage.addParameter(MessageType.SUBSCRIBE);
		newMessage.addParameter(subj);
		newMessage.addParameter(this.otfProviderRegistry.getDomain(registerObj.getDomain()));
		this.otfProviderRegistry.receiveMessage(newMessage);

//		telemetry.logOtfProviderAdded(registerObj.getOtfpId(), null, registerObj.getDomain());

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Void> deregister(DeregisterObj deregisterObj) {
		Subject subj = providers.getOrDefault(deregisterObj.getOtfpId(),
				new RemoteOTFProvider(this.otfProviderRegistry, deregisterObj.getOtfpId(), ""));

		Message newMessage = new Message();
		newMessage.addParameter(MessageType.UNSUBSCRIBE);
		newMessage.addParameter(subj);
		newMessage.addParameter(this.otfProviderRegistry.getDomain(deregisterObj.getDomain()));
		this.otfProviderRegistry.receiveMessage(newMessage);

//		telemetry.logOtfProviderUnsubscribed(deregisterObj.getOtfpId(), deregisterObj.getDomain());

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ListOfConfidence> getOtfpConfidence() {
		return ResponseEntity.ok(this.listOfConfidences);
	}

	@Override
	public ResponseEntity<Void> otfpConfidence(Map initialInterview) {
		Message newMessage = new Message();
		newMessage.addParameter(MessageType.PUBLISH);
		String domainName = (String) initialInterview.get("domain");
		Domain domain = this.otfProviderRegistry.getDomain(domainName);
		newMessage.addParameter(domain);
		newMessage.addParameter(initialInterview);
		this.otfProviderRegistry.receiveMessage(newMessage);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 
	 * BELOW is the new API
	 * 
	 */

	/* Maps the domain to the otf-provider */
	private Map<String, List<OTFProvider>> registeredProviders = new HashMap<>();

	@Override
	public ResponseEntity<List<String>> listDomains() {
		return ResponseEntity.ok(registeredProviders.keySet().stream().distinct().collect(Collectors.toList()));
	}

	@Override
	public ResponseEntity<List<OTFProvider>> listOtfProvidersForDomain(@PathVariable("domain") String domain) {
		if (registeredProviders.containsKey(domain)) {
			return ResponseEntity.ok(registeredProviders.get(domain));
		} else {
			return ResponseEntity.ok(new ArrayList<>());
		}
	}

	@Override
	public ResponseEntity<SimpleJSONMessage> insertOtfProviderForDomain(OTFProvider body, String domain) {
		logger.debug("Got request for registering {} at domain {}", body, domain);
		if (registeredProviders.containsKey(domain)) {
			List<OTFProvider> providersForDomain = registeredProviders.get(domain);
            for (OTFProvider otfProvider :
                    providersForDomain) {
                // TODO: 04.11.19 Issue 193: Update when same UUID and different Speccs
                if (body.getOtfpUUID().equals(otfProvider.getOtfpUUID()) &&
                        body.getGatekeeperURL().equals(otfProvider.getGatekeeperURL()) &&
                        body.getOtfProviderName().equals(otfProvider.getOtfProviderName()) &&
                        body.getIconURL().equals(otfProvider.getIconURL())
                ) {
                    return ResponseEntity.ok(new SimpleJSONMessage("OTFProvider already in Domain"));
                }
            }
			providersForDomain.add(body);
			return ResponseEntity.ok(new SimpleJSONMessage("Successfully registered for domain " + domain));
		} else {
			List<OTFProvider> providersForDomain = new ArrayList<>();
			providersForDomain.add(body);
			logger.debug("Created domain {}", domain);
			registeredProviders.put(domain, providersForDomain);
			return ResponseEntity.ok(new SimpleJSONMessage("Successfully registered for new domain " + domain));
		}
	}

	@Override
	public ResponseEntity<OTFProvider> getOtfProviderForDomainAndUUID(String domain, UUID otfpUUID) {
		if (registeredProviders.containsKey(domain)) {
			List<OTFProvider> providersForDomain = registeredProviders.get(domain);
			Optional<OTFProvider> potentialProvider = providersForDomain.stream()
					.filter(p -> p.getOtfpUUID().equals(otfpUUID)).findAny();
			if (potentialProvider.isPresent()) {
				return ResponseEntity.ok(potentialProvider.get());
			} else {
				return ResponseEntity.notFound().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	public ResponseEntity<List<OTFProviderConfidence>> getConfidencesForProviders(Map body, String domain) {
		logger.debug("Got confidence request for domain {} and interview map {}", domain, body);
		if (registeredProviders.containsKey(domain)) {
			List<OTFProvider> providersForDomain = registeredProviders.get(domain);
			// FIXME: resolve via matching of the interview
			return ResponseEntity.ok(providersForDomain.stream()
					.map(provider -> new OTFProviderConfidence(provider, 1.0)).collect(Collectors.toList()));
		} else {
			return ResponseEntity.ok(new ArrayList<>());
		}
	}

	@Override
	public ResponseEntity<SimpleJSONMessage> deleteOtfProviderForDomainAndUUID(String domain, UUID otfpUUID) {
		if (registeredProviders.containsKey(domain)) {
			List<OTFProvider> providersForDomain = registeredProviders.get(domain);
			if (providersForDomain.stream().anyMatch(p -> p.getOtfpUUID().equals(otfpUUID))) {
				List<OTFProvider> removedProvider = providersForDomain.stream()
						.filter(p -> !p.getOtfpUUID().equals(otfpUUID)).collect(Collectors.toList());
				registeredProviders.remove(domain);
				registeredProviders.put(domain, removedProvider);
				return ResponseEntity
						.ok(new SimpleJSONMessage("Succesfuly removed provider with uuid " + otfpUUID.toString()));
			} else {
				return ResponseEntity.notFound().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}