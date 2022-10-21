package de.upb.crc901.otftestbed.commons.reputation;

public class StatisticDefinitionModel {

  private String serviceID;
  private String statisticTypeID;
  private String statisticTimeID;
  private boolean disaggregate;

  public StatisticDefinitionModel(){
    //Intentionally left empty
  }

  public String getServiceID() {
    return serviceID;
  }

  public void setServiceID(String serviceID) {
    this.serviceID = serviceID;
  }

  public boolean getDisaggregate() {
    return disaggregate;
  }

  public void setDisaggregate(boolean disaggregate) {
    this.disaggregate = disaggregate;
  }

  public String getStatisticTypeID() {
    return statisticTypeID;
  }

  public void setStatisticTypeID(String statisticTypeID) {
    this.statisticTypeID = statisticTypeID;
  }

  public String getStatisticTimeID() {
    return statisticTimeID;
  }

  public void setStatisticTimeID(String statisticTimeID) {
    this.statisticTimeID = statisticTimeID;
  }

}
