package de.upb.crc901.otftestbed.proseco.impl.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MarketMonitorSourcesRepository extends MongoRepository<StoredMMSources, UUID>{
	
}
