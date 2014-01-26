package fr.xebia.xebay.api.socket.dto;

import fr.xebia.xebay.domain.BidOffer;

public class BidAnswer {

  BidAnswerType type;

  String cause;

  String name;

  double value;

  double increment;

  long timeToLive;

  BidAnswer(BidAnswerType type, String cause, BidCall bidCall) {
    this.type = type;
    this.cause = cause;
    this.name = bidCall.getItemName();
    this.value = bidCall.getCurValue();
    this.increment = bidCall.getIncrement();
  }

  BidAnswer(BidAnswerType type, BidOffer bidOffer) {
    this.type = type;
    this.name = bidOffer.getItem().getName();
    this.value = bidOffer.getCurrentValue();
    this.timeToLive = bidOffer.getTimeToLive();
  }

  public static BidAnswer newInfo(BidOffer bidOffer) {
    return new BidAnswer(BidAnswerType.BID_INFO, bidOffer);
  }

  public static BidAnswer newSuccess(BidCall bidCall) {
    return new BidAnswer(BidAnswerType.BID_SUCCESS, null, bidCall);
  }

  public static BidAnswer newFailure(String cause, BidCall bidCall) {
    return new BidAnswer(BidAnswerType.BID_FAILURE, cause, bidCall);
  }

  public BidAnswerType getType() {
    return type;
  }

  public void setType(BidAnswerType type) {
    this.type = type;
  }

  public String getCause() {
    return cause;
  }

  public void setCause(String cause) {
    this.cause = cause;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  public double getIncrement() {
    return increment;
  }

  public void setIncrement(double increment) {
    this.increment = increment;
  }

  public long getTimeToLive() {
    return timeToLive;
  }

  public void setTimeToLive(long timeToLive) {
    this.timeToLive = timeToLive;
  }
}
