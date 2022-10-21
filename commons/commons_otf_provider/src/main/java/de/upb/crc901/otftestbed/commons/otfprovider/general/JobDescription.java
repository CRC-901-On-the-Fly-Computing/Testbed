package de.upb.crc901.otftestbed.commons.otfprovider.general;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import de.upb.crc901.otftestbed.commons.general.JobState;

@Document(collection = "job_states")
public class JobDescription {

  @Id
  private UUID uuid;

  private JobState jobState = null;
  private boolean errorState = false;
  private JobErrorMessage errorMessage = null;

  private Map<String, String> interviewMap = null;

  private List<JobVerificationStatus> verification = null;

  public List<JobVerificationStatus> getVerifications() {
    return verification;
  }

  public void setVerifications(List<JobVerificationStatus> verification) {
    this.verification = verification;
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public JobState getJobState() {
    return jobState;
  }

  public void setJobState(JobState jobState) {
    this.jobState = jobState;
  }

  public boolean isErrorState() {
    return errorState;
  }

  public void setErrorState(boolean errorState) {
    this.errorState = errorState;
  }

  public JobErrorMessage getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(JobErrorMessage errorMessage) {
    this.errorMessage = errorMessage;
  }

  public Map<String, String> getInterviewMap() {
    return interviewMap;
  }

  public void setInterviewMap(Map<String, String> interviewMap) {
    this.interviewMap = interviewMap;
  }
}
