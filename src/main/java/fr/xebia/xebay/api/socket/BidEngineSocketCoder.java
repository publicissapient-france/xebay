package fr.xebia.xebay.api.socket;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.*;
import java.io.IOException;

public class BidEngineSocketCoder implements Encoder.Text<BidEngineSocketOutput>, Decoder.Text<BidEngineSocketInput> {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public String encode(BidEngineSocketOutput output) throws EncodeException {
        try {
            return objectMapper.writeValueAsString(output);
        } catch (IOException e) {
            throw new EncodeException(output, "", e);
        }
    }

    @Override
    public BidEngineSocketInput decode(String input) throws DecodeException {
        try {
            return objectMapper.readValue(input, BidEngineSocketInput.class);
        } catch (IOException e) {
            throw new DecodeException(input, "", e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void destroy() {
    }
}
