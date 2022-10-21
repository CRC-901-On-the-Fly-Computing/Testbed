package de.upb.crc901.otftestbed.commons.otfprovider.general;

public class JobVerificationStatus {
	
	private String flowName;
	  private boolean verified = false;
	  private boolean correct = false;

	  public String getFlowName() {
	    return flowName;
	  }

	  public void setFlowName(String flowName) {
	    this.flowName = flowName;
	  }
	  
	  public boolean isVerified() {
	    return verified;
	  }

	  public void setVerified(boolean verified) {
	    this.verified = verified;
	  }

	  public boolean isCorrect() {
	    return correct;
	  }

	  public void setCorrect(boolean correct) {
	    this.correct = correct;
	  }
}
