package de.upb.crc901.otftestbed.review_board.impl.repositories;

import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import de.upb.crc901.otftestbed.commons.reputation.SearchableSignedReputation;
import de.upb.crc901.otftestbed.commons.reputation.ServiceReputation;
import de.upb.crypto.math.serialization.Representation;
import de.upb.crypto.math.serialization.converter.JSONConverter;
import de.upb.crypto.react.acs.review.impl.react.ReactRepresentableReview;

@ReadingConverter
public class SearchableSignedReputationReadConverter implements Converter<Document, SearchableSignedReputation> {

	@SuppressWarnings("unchecked")
	@Override
	public SearchableSignedReputation convert(Document source) {
		ServiceReputation rep = new ServiceReputation((double) source.get("overall"), (double) source.get("usability"),
				(double) source.get("performance"), (double) source.get("security"), (double) source.get("other"));
		rep.setDate((long) source.get("date"));
		rep.setReputationMessage((String) source.get("message"));
		String serializedSignedMessage = (String) source.get("signature");
		Representation representedSignedMessage = new JSONConverter().deserialize(serializedSignedMessage);
		ReactRepresentableReview signedMessage = new ReactRepresentableReview(representedSignedMessage);
		SearchableSignedReputation toReturn = new SearchableSignedReputation(signedMessage, rep);
		toReturn.setItemName((String) source.get("serviceComposition"));
		toReturn.setOtfpUUID((UUID) source.get("otfpUUID"));
		toReturn.setServices((List<String>) source.get("services"));
		return toReturn;
	}

}
