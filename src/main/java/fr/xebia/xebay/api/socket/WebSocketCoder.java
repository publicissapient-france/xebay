package fr.xebia.xebay.api.socket;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

import fr.xebia.xebay.api.rest.dto.BidParam;

public abstract class WebSocketCoder implements Encoder, Decoder {

	final Gson gson = new Gson();

	@Override
	public void init(EndpointConfig config) {
	}

	@Override
	public void destroy() {
	}

	public static class BidParamEncoder extends WebSocketCoder implements Encoder.Text<BidParam> {

		@Override
		public String encode(BidParam bidParam) throws EncodeException {
			return gson.toJson(bidParam);
		}
	}

	public static class BidParamDecoder extends WebSocketCoder implements Decoder.Text<BidParam> {

		@Override
		public BidParam decode(String message) throws DecodeException {
			return gson.fromJson(message, BidParam.class);
		}

		@Override
		public boolean willDecode(String message) {
			return true;
		}
	}
}
