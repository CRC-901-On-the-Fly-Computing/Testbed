package de.upb.crc901.otftestbed.review_board.impl.repositories;

import java.util.List;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import de.upb.crc901.otftestbed.commons.reputation.SearchableSignedReputation;

public interface ReviewRepository extends MongoRepository<SearchableSignedReputation, ObjectId> {

	@Query("{'overall' : {$lte: 5.0 }, 'other' : {$lte: 5.0 }, 'performance' : {$lte: 5.0 }, 'security' : {$lte: 5.0 }, 'usability' : {$lte: 5.0 }}")
	List<SearchableSignedReputation> getAllReputations();

	@Query("{'services' : {$in : ['?0']}, 'overall' : {$lte: ?1 }, 'other' : {$lte: ?2 }, 'performance' : {$lte: ?3 }, 'security' : {$lte: ?4 }, 'usability' : {$lte: ?5 }}")
	List<SearchableSignedReputation> findReputationsByServiceId(String serviceID, double overallThreshold,
			double otherThreshold, double performanceThreshold, double securityThreshold, double usabilityThreshold);

	default List<SearchableSignedReputation> findReputationsByServiceId(String serviceID){
		return findReputationsByServiceId(serviceID, 5.0, 5.0, 5.0, 5.0, 5.0);
	}
	
	@Query("{'otfpUUID' : ?0 }")
	List<SearchableSignedReputation> findReputationsByOtfpUUID(UUID otfpUUID);

	@Query("{'compositionName' : ?0, 'overall' : {$lte: ?1 }, 'other' : {$lte: ?2 }, 'performance' : {$lte: ?3 }, 'security' : {$lte: ?4 }, 'usability' : {$lte: ?5 }}")
	List<SearchableSignedReputation> findReputationsByServiceCompositionId(String serviceID, double overallThreshold,
			double otherThreshold, double performanceThreshold, double securityThreshold, double usabilityThreshold);

	default List<SearchableSignedReputation> findReputationsByServiceCompositionId(String serviceID){
		return findReputationsByServiceCompositionId(serviceID, 5.0, 5.0, 5.0, 5.0, 5.0);
	}

    @Query(value ="{'services' : {$in : ['?0']}, 'overall' : {$lte: ?1 }, 'other' : {$lte: ?2 }, 'performance' : {$lte: ?3 }, 'security' : {$lte: ?4 }, 'usability' : {$lte: ?5 }}", count = true)
    long countReputationsByServiceId(String serviceID, double overallThreshold,
			double otherThreshold, double performanceThreshold, double securityThreshold, double usabilityThreshold);

	default long countReputationsByServiceId(String serviceID){
		return countReputationsByServiceId(serviceID, 5.0, 5.0, 5.0, 5.0, 5.0);
	}

	@Query(value ="{'compositionName' : ?0, 'overall' : {$lte: ?1 }, 'other' : {$lte: ?2 }, 'performance' : {$lte: ?3 }, 'security' : {$lte: ?4 }, 'usability' : {$lte: ?5 }}", count = true)
	long countReputationsByServiceCompositionId(String serviceID, double overallThreshold,
			double otherThreshold, double performanceThreshold, double securityThreshold, double usabilityThreshold);

	default long countReputationsByServiceCompositionId(String serviceID){
		return countReputationsByServiceCompositionId(serviceID, 5.0, 5.0, 5.0, 5.0, 5.0);
	}

	@Query(value = "{'overall' : {$lte: 5.0 }, 'other' : {$lte: 5.0 }, 'performance' : {$lte: 5.0 }, 'security' : {$lte: 5.0 }, 'usability' : {$lte: 5.0 }}", count = true)
	long countAllReputations();
}
