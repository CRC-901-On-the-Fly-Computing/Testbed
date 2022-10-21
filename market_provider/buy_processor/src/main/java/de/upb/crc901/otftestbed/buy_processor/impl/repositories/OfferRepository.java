package de.upb.crc901.otftestbed.buy_processor.impl.repositories;

import java.util.List;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import de.upb.crc901.otftestbed.buy_processor.impl.models.StoredOffer;

public interface OfferRepository extends MongoRepository<StoredOffer, ObjectId>{

	@Query("{'otfpUUID' : {$eq : ?1 }, 'requestUUID' : {$eq: ?0 }}")
	List<StoredOffer> getOffersByRequestUUID (UUID requestUUID, UUID otfpUUID);

	@Query("{'otfpUUID' : {$eq : ?1 }, 'requestUUID' : {$eq: ?0 }, 'offerUUID': {$eq: ?2}}")
	StoredOffer getOffer (UUID requestUUID, UUID otfpUUID, UUID offerUUID);
}
