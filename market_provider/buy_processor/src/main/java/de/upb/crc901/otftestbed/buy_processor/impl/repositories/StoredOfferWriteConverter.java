package de.upb.crc901.otftestbed.buy_processor.impl.repositories;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import com.fasterxml.jackson.core.JsonProcessingException;

import de.upb.crc901.otftestbed.buy_processor.impl.models.StoredOffer;
import de.upb.crc901.otftestbed.commons.utils.Helpers;
import de.upb.crypto.math.serialization.converter.JSONConverter;

@WritingConverter
public class StoredOfferWriteConverter implements Converter<StoredOffer, Document> {

	@Override
	public Document convert(StoredOffer source) {
		Document dbo = new Document();
		dbo.put(ConverterConstants.OFFER_KEY, source.getOfferUUID());
		dbo.put(ConverterConstants.OTFP_KEY, source.getOtfpUUID());
		dbo.put(ConverterConstants.GLOBAL_REQUEST_KEY, source.getRequestUUID());
		dbo.put(ConverterConstants.IMAGE_KEY, source.getDockerImageURL());
		JSONConverter jsonConverter = new JSONConverter();
		String piJson = jsonConverter.serialize(source.getOffer().getPolicyInformation().getRepresentation());
		dbo.put(ConverterConstants.OFFER_PI_KEY, piJson);
		dbo.put(ConverterConstants.OFFER_NFP_KEY, source.getOffer().getNonFunctionalProperties());
		dbo.put(ConverterConstants.OFFER_SCORE_KEY, source.getOffer().getOfferScore());
		try {
			dbo.put(ConverterConstants.OFFER_SIMPLEPI_KEY, Helpers.getMapper().writeValueAsString(source.getOffer().getSimplePolicy()));
			dbo.put(ConverterConstants.OFFER_COMPOSITION_KEY,
					Helpers.getMapper().writeValueAsString(source.getOffer().getCompositionAndOTFProvider()));
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		return dbo;
	}

}
