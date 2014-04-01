package fr.xebia.xebay.domain;

public interface BidEngineListener {

    void onBid(BidOffer bidOffer);

    void onNews(PluginInfo news);

}
