package fr.xebia.xebay.api.socket;

import fr.xebia.xebay.domain.BidOffer;
import fr.xebia.xebay.domain.Item;

import java.util.List;

public class WebSocketMessage {

  BidOffer bidOffer;

  List<Item> itemListToSell;

  public BidOffer getBidOffer() {
    return bidOffer;
  }

  public void setBidOffer(BidOffer bidOffer) {
    this.bidOffer = bidOffer;
  }

  public List<Item> getItemListToSell() {
    return itemListToSell;
  }

  public void setItemListToSell(List<Item> itemListToSell) {
    this.itemListToSell = itemListToSell;
  }
}
