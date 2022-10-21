package de.upb.crc901.otftestbed.commons.otfprovider.general;

import java.sql.Timestamp;

public class JobErrorMessage {

  private String errorMessage = null;

  private Timestamp timeStamp = null;

  public void setErrorMessage(String errorMessage) {
    timeStamp = new Timestamp(System.currentTimeMillis());
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public Timestamp getTimeStamp() {
    return timeStamp;
  }

  @Override
  public String toString() {
    if (getTimeStamp() != null) {
      return getTimeStamp() + getErrorMessage();
    } else {
      return "No message yet.";
    }
  }
}
