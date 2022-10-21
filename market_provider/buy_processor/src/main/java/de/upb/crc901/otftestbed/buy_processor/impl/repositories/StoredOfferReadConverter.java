package de.upb.crc901.otftestbed.buy_processor.impl.repositories;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import de.upb.crc901.otftestbed.buy_processor.impl.models.StoredOffer;
import de.upb.crc901.otftestbed.commons.general.CompositionAndOTFProvider;
import de.upb.crc901.otftestbed.commons.requester.Offer;
import de.upb.crc901.otftestbed.commons.requester.SimplePolicy;
import de.upb.crc901.otftestbed.commons.utils.Helpers;
import de.upb.crypto.math.serialization.converter.JSONConverter;
import de.upb.crypto.react.acs.policy.PolicyInformation;

@ReadingConverter
public class StoredOfferReadConverter implements Converter<Document, StoredOffer> {

	@Override
	public StoredOffer convert(Document source) {
		StoredOffer toReturn = new StoredOffer();
		toReturn.setOfferUUID((UUID) source.get(ConverterConstants.OFFER_KEY));
		toReturn.setOtfpUUID((UUID) source.get(ConverterConstants.OTFP_KEY));
		toReturn.setRequestUUID((UUID) source.get(ConverterConstants.GLOBAL_REQUEST_KEY));
		toReturn.setDockerImageURL((String) source.get(ConverterConstants.IMAGE_KEY));
		JSONConverter converter = new JSONConverter();
		PolicyInformation pi = new PolicyInformation(
				converter.deserialize((String) source.get(ConverterConstants.OFFER_PI_KEY)));
		@SuppressWarnings("unchecked")
		Map<String, String> offerNFP = (Map<String, String>) source.get(ConverterConstants.OFFER_NFP_KEY);
		CompositionAndOTFProvider composition;
		try {
			composition = Helpers.getMapper().readValue((String) source.get(ConverterConstants.OFFER_COMPOSITION_KEY),
					CompositionAndOTFProvider.class);
			Offer offer = new Offer();
			offer.setPolicyInformation(pi);
			offer.setNonFunctionalProperties(offerNFP);
			offer.setCompositionAndOTFProvider(composition);
			offer.setOfferUUID((UUID) source.get(ConverterConstants.OFFER_KEY));
			offer.setOfferScore(source.getDouble(ConverterConstants.OFFER_SCORE_KEY));
			offer.setSimplePolicy(Helpers.getMapper()
					.readValue((String) source.get(ConverterConstants.OFFER_SIMPLEPI_KEY), SimplePolicy.class));
			toReturn.setOffer(offer);
			toReturn.getOffer().setCompositionID(composition.getSimpleComposition().getServiceCompositionId());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return toReturn;
	}

}
