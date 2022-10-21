package de.upb.crc901.otftestbed.commons.requester;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.upb.crc901.otftestbed.commons.general.JobState;

public class JobstateAndRequest {
	
	@JsonProperty
	private final String requestName;
	
	@JsonProperty
	private final UUID requestUUID;
	
	@JsonProperty
	private final JobState jobstate;

	public JobstateAndRequest(String requestName, UUID requestUUID, JobState jobstate) {
		super();
		this.requestName = requestName;
		this.requestUUID = requestUUID;
		this.jobstate = jobstate;
	}

}
