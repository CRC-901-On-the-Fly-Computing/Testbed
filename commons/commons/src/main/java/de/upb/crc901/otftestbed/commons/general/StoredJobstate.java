package de.upb.crc901.otftestbed.commons.general;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "jobstates_new")
public class StoredJobstate {
	@Id
	private UUID requestUUID;

	private JobState jobstate;

	private InterviewData interview;
	
	public StoredJobstate() {}

	public StoredJobstate(UUID requestUUID, JobState jobstate, InterviewData interview) {
		super();
		this.requestUUID = requestUUID;
		this.jobstate = jobstate;
		this.interview = interview;
	}

	public UUID getRequestUUID() {
		return requestUUID;
	}

	public void setRequestUUID(UUID requestUUID) {
		this.requestUUID = requestUUID;
	}

	public JobState getJobstate() {
		return jobstate;
	}

	public void setJobstate(JobState jobstate) {
		this.jobstate = jobstate;
	}

	public InterviewData getInterviewData() {
		return interview;
	}

	public void setInterviewData(InterviewData interview) {
		this.interview = interview;
	}

}
