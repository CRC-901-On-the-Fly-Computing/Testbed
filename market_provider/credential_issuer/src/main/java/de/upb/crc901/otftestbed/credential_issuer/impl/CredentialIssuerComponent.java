package de.upb.crc901.otftestbed.credential_issuer.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import de.upb.crc901.otftestbed.commons.connect.PocConnector;
import de.upb.crc901.otftestbed.commons.io.RepresentationFile;
import de.upb.crc901.otftestbed.credential_issuer.impl.config.CredentialIssuerConfig;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactCredentialIssuer;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactIssuerKeyPair;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactIssuerKeyPairFactory;
import de.upb.crypto.react.acs.setup.impl.react.ReactPublicParameters;
import de.upb.crypto.react.buildingblocks.attributes.AttributeDefinition;

@Component
public class CredentialIssuerComponent {

	private static final Logger log = LoggerFactory.getLogger(CredentialIssuerComponent.class);

	private final PocConnector connect;

	private final CredentialIssuerConfig credentialIssuerConfig;

	private final Map<String, AttributeDefinition> attributes = new HashMap<>();

	private ReactCredentialIssuer credentialIssuer = null;


	@Autowired
	public CredentialIssuerComponent(CredentialIssuerConfig credentialIssuerConfig, PocConnector connect) {
		Assert.notNull(credentialIssuerConfig, "credentialIssuerConfig must not be null");
		Assert.notNull(connect, "connect must not be null");

		this.credentialIssuerConfig = credentialIssuerConfig;
		this.connect = connect;

		init();
	}


	private void init() {
		// initialization

		// load attributes from file
		Collection<AttributeDefinition> collection = RepresentationFile.readLines(
				CredentialIssuerConfig.ATTRIBUTE_FILENAME, r -> (AttributeDefinition) r.repr().recreateRepresentable());
		for (AttributeDefinition definition : collection) {
			attributes.put(definition.getAttributeName(), definition);
		}

		// check whether we use an already existing version
		if (credentialIssuerConfig.isPersistent()) {
			log.debug("Trying to recreate...");

			this.credentialIssuer = RepresentationFile.readFile(
					CredentialIssuerConfig.CREDENTIAL_ISSUER_FILENAME, ReactCredentialIssuer::new);

		}
	}

	public Map<String, AttributeDefinition> getAttributes() {
		return this.attributes;
	}

	public ReactCredentialIssuer getCredentialIssuer() {
		return this.credentialIssuer;
	}

	public ReactCredentialIssuer generateCredentialIssuer() {

		log.debug("Requesting PublicParameters via the SystemManager...");
		ReactPublicParameters pp = connect.toMarketProvider().callSystemManager().getPublicParameter();

		log.debug("Reading attributes...");
		List<AttributeDefinition> attributes = new ArrayList<>(getAttributes().values());
		log.debug("Found {} attribute definitions.", attributes.size());

		log.debug("Creating keys...");
		ReactIssuerKeyPair keyPair = new ReactIssuerKeyPairFactory().create(pp, attributes.size());
		log.debug("Done creating keys");

		return new ReactCredentialIssuer(pp, keyPair, attributes);
	}
}
