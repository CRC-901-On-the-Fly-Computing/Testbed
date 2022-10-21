package de.upb.crc901.otftestbed.proseco.impl.repository;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import de.upb.crc901.otftestbed.commons.requester.ConfigurationMarketMonitorSources;
@Document(collection = "mm_sources")
public class StoredMMSources {
	
	@Id
	private UUID requestUUID;
	
	private ConfigurationMarketMonitorSources sources;

	public StoredMMSources(UUID requestUUID, ConfigurationMarketMonitorSources sources) {
		super();
		this.requestUUID = requestUUID;
		this.sources = sources;
	}

	public UUID getRequestUUID() {
		return requestUUID;
	}

	public void setRequestUUID(UUID requestUUID) {
		this.requestUUID = requestUUID;
	}

	public ConfigurationMarketMonitorSources getSources() {
		return sources;
	}

	public void setSources(ConfigurationMarketMonitorSources sources) {
		this.sources = sources;
	}
	
	

}
