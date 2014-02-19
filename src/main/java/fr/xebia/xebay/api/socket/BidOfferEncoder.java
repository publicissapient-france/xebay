package fr.xebia.xebay.api.socket;

import fr.xebia.xebay.domain.model.BidOffer;
import org.codehaus.jackson.map.ObjectMapper;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

public class BidOfferEncoder implements Encoder.Text<BidOffer> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public String encode(BidOffer bidOffer) throws EncodeException {
        try {
            return objectMapper.writeValueAsString(bidOffer);
        } catch (IOException e) {
            throw new EncodeException(bidOffer, "", e);
        }
    }

    @Override
    public void destroy() {
    }
}
