package fr.xebia.xebay.api.socket;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

import fr.xebia.xebay.domain.BidOffer;

public abstract class WebSocketCoder implements Encoder, Decoder {

	final Gson gson = new Gson();

	@Override
	public void init(EndpointConfig config) {
	}

	@Override
	public void destroy() {
	}

	public static class BidOfferEncoder extends WebSocketCoder implements Encoder.Text<BidOffer> {

		@Override
		public String encode(BidOffer bidOffer) throws EncodeException {
			return gson.toJson(bidOffer);
		}
	}

	public static class WebSocketMessageDecoder extends WebSocketCoder implements Decoder.Text<WebSocketMessage> {

		@Override
		public WebSocketMessage decode(String message) throws DecodeException {
			return gson.fromJson(message, WebSocketMessage.class);
		}

		@Override
		public boolean willDecode(String message) {
			return true;
		}
	}
}
