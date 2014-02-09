package fr.xebia.xebay.domain;

public class BidDemand {

  String itemName;
  double curValue;
  double increment;

  public BidDemand() {

  }

  public BidDemand(String itemName, double curValue, double increment) {
    this.itemName = itemName;
    this.curValue = curValue;
    this.increment = increment;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public void setCurValue(double curValue) {
    this.curValue = curValue;
  }

  public void setIncrement(double increment) {
    this.increment = increment;
  }

  public String getItemName() {
    return itemName;
  }

  public double getCurValue() {
    return curValue;
  }

  public Double getIncrement() {
    return increment;
  }
}
