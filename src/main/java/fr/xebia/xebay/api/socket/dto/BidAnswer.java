package fr.xebia.xebay.api.socket.dto;

import fr.xebia.xebay.domain.BidOffer;

public class BidAnswer {

    BidAnswerType type;

    String cause;

    String name;

    double value;

    double increment;

    long timeToLive;

    String futureBuyerEmail;

    BidAnswer(BidAnswerType type, String cause, BidCall bidCall) {
        this.type = type;
        this.cause = cause;
        this.name = bidCall.getItemName();
        this.value = bidCall.getCurValue();
        this.increment = bidCall.getIncrement();
        this.futureBuyerEmail = null;
    }

    BidAnswer(BidAnswerType type, BidOffer bidOffer) {
        this.type = type;
        this.name = bidOffer.itemName;
        this.value = bidOffer.currentValue;
        this.timeToLive = bidOffer.timeToLive;
        this.futureBuyerEmail = bidOffer.futureBuyerEmail.orElse(null);
    }

    public static BidAnswer newAccepted(BidOffer bidOffer) {
        return new BidAnswer(BidAnswerType.ACCEPTED, bidOffer);
    }

    public static BidAnswer newRejected(String cause, BidCall bidCall) {
        return new BidAnswer(BidAnswerType.REJECTED, cause, bidCall);
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

    public String getFutureBuyerEmail() {
        return futureBuyerEmail;
    }

    public void setFutureBuyerEmail(String futureBuyerEmail) {
        this.futureBuyerEmail = futureBuyerEmail;
    }
}
