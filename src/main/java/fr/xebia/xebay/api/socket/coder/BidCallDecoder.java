package fr.xebia.xebay.api.socket.coder;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

import fr.xebia.xebay.api.socket.dto.BidCall;

public class BidCallDecoder implements Decoder.Text<BidCall> {

  final Gson gson = new Gson();

  @Override
  public void init(EndpointConfig config) {

  }

  @Override
  public boolean willDecode(String message) {
    return true;
  }

  @Override
  public BidCall decode(String message) throws DecodeException {
    return gson.fromJson(message, BidCall.class);
  }

  @Override
  public void destroy() {

  }
}
