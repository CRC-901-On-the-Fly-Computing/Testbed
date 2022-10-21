package de.upb.crc901.otftestbed.otfprovider.gatekeeper.db;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.upb.crc901.otftestbed.commons.otfprovider.general.JobDescription;

public interface Repository extends MongoRepository<JobDescription, UUID>{

}