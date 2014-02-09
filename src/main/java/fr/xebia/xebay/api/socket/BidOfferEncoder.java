package fr.xebia.xebay.api.socket;

import com.google.gson.Gson;
import fr.xebia.xebay.domain.BidOffer;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class BidOfferEncoder implements Encoder.Text<BidOffer> {

  final Gson gson = new Gson();

  @Override
  public void init(EndpointConfig endpointConfig) {

  }

  @Override
  public String encode(BidOffer bidOffer) throws EncodeException {
    return gson.toJson(bidOffer);
  }

  @Override
  public void destroy() {

  }
}
