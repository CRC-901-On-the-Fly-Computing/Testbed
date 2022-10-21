package de.upb.crc901.otftestbed.commons.general;

import java.util.UUID;

public class StateChange {

	private JobState oldState;
	private JobState newState;

	private UUID jobUUID;

	public JobState getOldState() {
		return oldState;
	}

	public void setOldState(JobState oldState) {
		this.oldState = oldState;
	}

	public JobState getNewState() {
		return newState;
	}

	public void setNewState(JobState newState) {
		this.newState = newState;
	}

	public UUID getJobUUID() {
		return jobUUID;
	}

	public void setJobUUID(UUID jobUUID) {
		this.jobUUID = jobUUID;
	}

}
