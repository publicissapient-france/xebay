package bid.api.rest;

import bid.BidOffer;
import com.google.gson.Gson;

import javax.websocket.*;

public class BidEncoders {

    static final Gson gson = new Gson();

    public static class BidOfferEncoder implements Encoder.Text<BidOffer> {

        @Override
        public String encode(BidOffer bidOffer) throws EncodeException {
            return gson.toJson(bidOffer);
        }

        @Override
        public void init(EndpointConfig endpointConfig) {

        }

        @Override
        public void destroy() {

        }
    }
    public static class BidOfferDecoder implements Decoder.Text<BidOffer> {

        @Override
        public BidOffer decode(String s) throws DecodeException {
            return gson.fromJson(s, BidOffer.class);
        }

        @Override
        public boolean willDecode(String s) {
            return false;
        }

        @Override
        public void init(EndpointConfig endpointConfig) {

        }

        @Override
        public void destroy() {

        }
    }

}
