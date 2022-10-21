package de.upb.crc901.otftestbed.review_board.impl.repositories;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import de.upb.crc901.otftestbed.commons.reputation.SearchableSignedReputation;
import de.upb.crypto.math.serialization.converter.JSONConverter;


@WritingConverter
public class SearchableSignedReputationWriteConverter implements Converter<SearchableSignedReputation, Document>{

	@Override
	public Document convert(SearchableSignedReputation source) {
		Document dbo = new Document();
		dbo.put("signature", new JSONConverter().serialize(source.getSignature().getRepresentation()));
		dbo.put("usability", source.getReputation().getUsability());
		dbo.put("other", source.getReputation().getOther());
		dbo.put("performance", source.getReputation().getPerformance());
		dbo.put("security", source.getReputation().getSecurity());
		dbo.put("message", source.getReputation().getReputationMessage());
		dbo.put("date", source.getReputation().getDate().getTime());
		dbo.put("overall", source.getReputation().getOverall());
		dbo.put("compositionName", source.getItemName());
		dbo.put("serviceComposition", source.getItemName());
		dbo.put("otfpUUID", source.getOtfpUUID());
		dbo.put("services", source.getServices());
		return dbo;
	}
	

}
