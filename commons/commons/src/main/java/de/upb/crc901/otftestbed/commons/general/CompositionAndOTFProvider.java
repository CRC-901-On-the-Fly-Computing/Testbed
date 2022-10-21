package de.upb.crc901.otftestbed.commons.general;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.upb.crc901.otftestbed.commons.requester.SimpleComposition;

/**
 * An item for the review board and otf market.
 * 
 * @author elppa
 *
 */
@JsonPropertyOrder({"simpleComposition", "otfpUUID"})
public class CompositionAndOTFProvider {

	private SimpleComposition simpleComposition;

	private UUID otfpUUID;

	public CompositionAndOTFProvider(SimpleComposition simpleComposition, UUID otfpUUID) {
		this.simpleComposition = simpleComposition;
		this.otfpUUID = otfpUUID;
	}

	public CompositionAndOTFProvider() {
	}

	public SimpleComposition getSimpleComposition() {
		return simpleComposition;
	}

	public UUID getOtfpUUID() {
		return otfpUUID;
	}
}
