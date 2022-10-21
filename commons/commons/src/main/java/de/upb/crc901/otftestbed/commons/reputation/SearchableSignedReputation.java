package de.upb.crc901.otftestbed.commons.reputation;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;

import de.upb.crc901.otftestbed.commons.general.CompositionAndOTFProvider;
import de.upb.crc901.otftestbed.commons.requester.SimpleComposition;
import de.upb.crc901.otftestbed.commons.utils.Helpers;
import de.upb.crypto.math.serialization.ByteArrayRepresentation;
import de.upb.crypto.math.serialization.ObjectRepresentation;
import de.upb.crypto.math.serialization.Representable;
import de.upb.crypto.math.serialization.Representation;
import de.upb.crypto.react.acs.review.impl.react.ReactRepresentableReview;

/**
 * Used for storing reputations in the mongo-database. The service-reputation is
 * also included in the signed-message. However, for efficiency reasons it is
 * stored in the signature field.
 * 
 * @author mirkoj
 *
 */
@Document(collection = "signed-reputation")
public class SearchableSignedReputation implements Representable {

	private ReactRepresentableReview signature;

	private ServiceReputation reputation;

	private List<String> services;

	private String itemName;

	private UUID otfpUUID;

	public SearchableSignedReputation() {
	}

	public SearchableSignedReputation(Representation repr) {
		byte[] jsonReputationBytes = repr.obj().get("reputation").bytes().get();
		try {
			this.reputation = Helpers.getMapper().readValue(jsonReputationBytes, ServiceReputation.class);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		this.signature = new ReactRepresentableReview(repr.obj().get("signature"));
	}

	public SearchableSignedReputation(ReactRepresentableReview signature, ServiceReputation reputation) {
		this.signature = signature;
		this.reputation = reputation;
	}

	@Override
	public Representation getRepresentation() {
		ObjectRepresentation toReturn = new ObjectRepresentation();
		byte[] jsonBytes;
		try {
			jsonBytes = Helpers.getMapper().writeValueAsBytes(reputation);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}
		toReturn.put("reputation", new ByteArrayRepresentation(jsonBytes));
		toReturn.put("signature", signature.getRepresentation());
		return toReturn;
	}

	public ReactRepresentableReview getSignature() {
		return signature;
	}

	public ServiceReputation getReputation() {
		return reputation;
	}

	public ExtendedServiceReputation getExtendedReputation() {
		return new ExtendedServiceReputation(reputation, otfpUUID, itemName, services);
	}

	public UUID getOtfpUUID() {
		return otfpUUID;
	}

	public void setOtfpUUID(UUID otfpUUID) {
		this.otfpUUID = otfpUUID;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public List<String> getServices() {
		return services;
	}

	public void setServices(List<String> services) {
		this.services = services;
	}

	@JsonIgnore
	public CompositionAndOTFProvider getCompositionAndOtfp() {
		SimpleComposition composition = new SimpleComposition();
		composition.setServiceCompositionId(itemName);
		composition.setServiceId(services);
		return new CompositionAndOTFProvider(composition, otfpUUID);
	}
}
