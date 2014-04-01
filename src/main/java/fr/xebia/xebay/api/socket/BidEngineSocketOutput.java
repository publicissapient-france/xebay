package fr.xebia.xebay.api.socket;

import fr.xebia.xebay.domain.BidOffer;
import fr.xebia.xebay.domain.PluginInfo;

public class BidEngineSocketOutput {

    BidOffer bidOffer;

    PluginInfo news;

    String error;

    public BidOffer getBidOffer() {
        return bidOffer;
    }

    public void setBidOffer(BidOffer bidOffer) {
        this.bidOffer = bidOffer;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public PluginInfo getNews() {
        return news;
    }

    public void setNews(PluginInfo news) {
        this.news = news;
    }

}
