package fr.xebia.xebay.api.socket.coder;

import com.google.gson.Gson;
import fr.xebia.xebay.api.socket.dto.BidAnswer;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class BidAnswerEncoder implements Encoder.Text<BidAnswer> {

  final Gson gson = new Gson();

  @Override
  public void init(EndpointConfig endpointConfig) {

  }

  @Override
  public String encode(BidAnswer bidAnswer) throws EncodeException {
    return gson.toJson(bidAnswer);
  }

  @Override
  public void destroy() {

  }
}
