package fr.xebia.xebay.api.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.xebia.xebay.api.rest.dto.BidDemand;
import fr.xebia.xebay.domain.model.BidOffer;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

public class BidEngineSocketCoderTest {

    BidEngineSocketCoder bidEngineSocketCoder;

    ObjectMapper objectMapper;

    @Before
    public void before() {

        objectMapper = Mockito.mock(ObjectMapper.class);

        bidEngineSocketCoder = new BidEngineSocketCoder();
        bidEngineSocketCoder.objectMapper = objectMapper;
    }

    @Test
    public void encoder_must_call_mapper_lib_and_return_its_result() throws Exception {

        BidOffer bidOffer = new BidOffer("category", "name", 10d, 10L, "user", "avatarURL", false);
        BidEngineSocketOutput output = new BidEngineSocketOutput();
        output.setStarted(bidOffer);
        output.setUpdated(bidOffer);
        output.setResolved(bidOffer);
        String expected = UUID.randomUUID().toString();
        Mockito.when(objectMapper.writeValueAsString(output)).thenReturn(expected);

        String result = bidEngineSocketCoder.encode(output);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    public void decoder_must_call_mapper_lib_and_return_its_result() throws Exception {

        BidDemand bidDemand = new BidDemand("name", 10d);
        BidEngineSocketInput expected = new BidEngineSocketInput();
        expected.setBid(bidDemand);
        expected.setOffer(bidDemand);

        String input = UUID.randomUUID().toString();
        Mockito.when(objectMapper.readValue(input, BidEngineSocketInput.class)).thenReturn(expected);

        BidEngineSocketInput result = bidEngineSocketCoder.decode(input);

        Assertions.assertThat(result).isEqualTo(expected);
    }
}
