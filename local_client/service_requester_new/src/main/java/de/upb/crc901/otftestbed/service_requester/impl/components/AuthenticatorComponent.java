package de.upb.crc901.otftestbed.service_requester.impl.components;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.upb.crc901.otftestbed.commons.connect.PocConnector;
import de.upb.crc901.otftestbed.commons.utils.Helpers;
import de.upb.crypto.react.acs.user.impl.react.credentials.ReactNonInteractiveCredentialRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.upb.crc901.otftestbed.commons.reputation.SimpleAttributes;
import de.upb.crc901.otftestbed.service_requester.impl.exceptions.InvalidAttributeException;
import de.upb.crypto.react.acs.issuer.credentials.Attributes;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactCredentialIssueResponse;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactCredentialIssuerPublicIdentity;
import de.upb.crypto.react.acs.policy.PolicyInformation;
import de.upb.crypto.react.acs.pseudonym.impl.react.ReactIdentity;
import de.upb.crypto.react.acs.setup.impl.react.ReactPublicParameters;
import de.upb.crypto.react.acs.user.impl.react.ReactNonInteractivePolicyProof;
import de.upb.crypto.react.acs.user.impl.react.ReactUser;
import de.upb.crypto.react.acs.user.impl.react.credentials.ReactCredentialNonInteractiveResponseHandler;
import de.upb.crypto.react.acs.verifier.impl.react.credentials.ReactVerifierPublicIdentity;
import de.upb.crypto.react.buildingblocks.attributes.AttributeNameValuePair;
import de.upb.crypto.react.buildingblocks.attributes.AttributeSpace;
import de.upb.crypto.react.buildingblocks.attributes.BigIntegerAttributeDefinition;
import de.upb.crypto.react.buildingblocks.attributes.StringAttributeDefinition;

@Component
public class AuthenticatorComponent {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticatorComponent.class);

	@Autowired
	private PocConnector connect;


	public void createCredential(ReactUser user, Attributes attribute) {
		logger.debug("Creating credential for {}",user.toString());
		logger.debug("Requesting credential issuer public identity ...");
		ReactCredentialIssuerPublicIdentity identity = connect.toMarketProvider().callCredentialIssuer().getPublicIdentity();
		logger.debug("Committing to user identity...");
		ReactIdentity userIdentity = user.createIdentity();
		logger.debug("Creating credential issuance request ...");
		ReactCredentialNonInteractiveResponseHandler handler = user.createNonInteractiveIssueCredentialRequest(identity,
				userIdentity, attribute);
		logger.debug("Requesting credentials...");
		ReactNonInteractiveCredentialRequest request = (ReactNonInteractiveCredentialRequest) handler.getRequest();
		ReactCredentialIssueResponse response = connect.toMarketProvider().callCredentialIssuer().requestCredential(request);
		logger.debug("Registering the credential in the user...");
		user.receiveCredentialNonInteractively(handler, response);
		logger.debug("Returning to caller");
	}

	public ReactNonInteractivePolicyProof createAuthenticationToken(ReactUser user, PolicyInformation policyInformation,
			ReactPublicParameters pp) {
		logger.debug("Creating authentication token for {}", user);
		logger.debug("Requesting credential verifier public identity ...");
		Helpers.setInjectableValue(ReactPublicParameters.class.toString(), pp);
		ReactVerifierPublicIdentity publicIdentity = connect.toMarketProvider().callBuyProcessor().getPublicIdentity();
		logger.debug("Committing to user identity...");
		ReactIdentity userIdentity = user.createIdentity();
		logger.debug("Creating proof...");
		ReactNonInteractivePolicyProof toReturn = user.createNonInteractivePolicyProof(userIdentity, policyInformation,
				publicIdentity);
		logger.debug("Returning...");
		return toReturn;
	}

	public Attributes generateAttributesForUser(SimpleAttributes body,
			ReactCredentialIssuerPublicIdentity credPublicIdentity) {

		AttributeSpace credAttributeSpace = credPublicIdentity.getAttributeSpace();
		BigIntegerAttributeDefinition ageAD = (BigIntegerAttributeDefinition) credAttributeSpace.get("age");
		StringAttributeDefinition countryAD = (StringAttributeDefinition) credAttributeSpace.get("country");
		StringAttributeDefinition subscriptionLevelAD = (StringAttributeDefinition) credAttributeSpace
				.get("subscriptionLevel");
		StringAttributeDefinition userLicenseAD = (StringAttributeDefinition) credAttributeSpace.get("userLicense");

		List<AttributeNameValuePair> toReturn = new ArrayList<>();

		for (Map.Entry<String, Object> unsignedAnvp : body.getAttributePairs().entrySet()) {
			String attributeName = unsignedAnvp.getKey();
			switch (attributeName) {
			case ("age"):
				logger.debug("Found user attribute age");
				try {
					AttributeNameValuePair anvp = ageAD
							.createAttribute(BigInteger.valueOf((Integer) unsignedAnvp.getValue()));
					toReturn.add(anvp);
				} catch (IllegalArgumentException e) {
					throw new InvalidAttributeException();
				}
				break;
			case ("subscriptionLevel"):
				logger.debug("Found user attribute subscriptionLevel");
				try {
					AttributeNameValuePair anvp = subscriptionLevelAD.createAttribute((String) unsignedAnvp.getValue());
					toReturn.add(anvp);
				} catch (IllegalArgumentException e) {
					throw new InvalidAttributeException();
				}
				break;
			case ("country"):
				logger.debug("Found user attribute country");
				try {
					AttributeNameValuePair anvp = countryAD.createAttribute((String) unsignedAnvp.getValue());
					toReturn.add(anvp);
				} catch (IllegalArgumentException e) {
					logger.error("Invalid attribute found", e);
					throw new InvalidAttributeException();
				}
				break;
			case ("userLicense"):
				logger.debug("Found user attribute userLicense");
				try {
					AttributeNameValuePair anvp = userLicenseAD.createAttribute((String) unsignedAnvp.getValue());
					toReturn.add(anvp);
				} catch (IllegalArgumentException e) {
					throw new InvalidAttributeException();

				}
				break;
			default:
				throw new InvalidAttributeException();
			}
		}
		AttributeNameValuePair[] pairsAsArray = new AttributeNameValuePair[toReturn.size()];
		return new Attributes(toReturn.toArray(pairsAsArray));
	}
}
