package de.upb.crc901.otftestbed.commons.telemetry.es.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface TelemetryRepository<T> extends ElasticsearchRepository<T, String> {

}
