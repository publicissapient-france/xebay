package fr.xebia.xebay.api.rest;

import com.google.common.collect.Sets;
import fr.xebia.xebay.api.dto.BidDemand;
import fr.xebia.xebay.domain.BidEngine;
import fr.xebia.xebay.domain.Plugin;
import fr.xebia.xebay.domain.internal.Item;
import fr.xebia.xebay.domain.internal.Items;
import fr.xebia.xebay.domain.internal.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.SecurityContext;
import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BidEngineResourceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    SecurityContext securityContext;
    @Mock
    private BidEngine bidEngine;
    @Mock
    private Items items;
    private User user;
    private BidEngineResource bidEngineResource;

    @Before
    public void init() {
        bidEngineResource = new BidEngineResource(bidEngine, items);
        user = new User("key1", "user1");
    }

    @Test
    public void should_return_404_if_no_bid_offer_can_be_retrieved() {
        when(bidEngine.currentBidOffer()).thenReturn(null);
        thrown.expect(WebApplicationException.class);
        thrown.expectMessage("HTTP 404 Not Found");

        bidEngineResource.currentBidOffer();
    }

    @Test
    public void should_call_bidEngine_offer_when_user_offering_item() {
        Item item = new Item("category", "an item", new BigDecimal(1.2));

        when(securityContext.getUserPrincipal()).thenReturn(user);
        when(items.find("an item")).thenReturn(item);

        bidEngineResource.offer(new BidDemand("an item", 10.0), securityContext);

        verify(bidEngine, times(1)).offer(user, item, 10.0);
    }

    @Test
    public void should_throw_404_notFoundException_when_offering_unknown_item() throws Exception {
        when(securityContext.getUserPrincipal()).thenReturn(user);

        String unknownItem = "unknown item";
        when(items.find(unknownItem)).thenReturn(null);

        thrown.expect(WebApplicationException.class);
        thrown.expectMessage("HTTP 404 Not Found");

        bidEngineResource.offer(new BidDemand(unknownItem, 10.0), securityContext);
    }

    @Test
    public void should_call_bidEngine_to_activate_plugin() {
        String name = "pluginName";

        bidEngineResource.activate(name);

        verify(bidEngine, times(1)).activate(name);
    }

    @Test
    public void should_call_bidEngine_to_deactivate_plugin() {
        String name = "pluginName";

        bidEngineResource.deactivate(name);

        verify(bidEngine, times(1)).deactivate(name);
    }

    @Test
    public void should_call_bidEngine_to_get_plugins_list() {
        when(bidEngine.getPlugins()).thenReturn(Sets.newHashSet(
                new Plugin("first plugin", "1st", true),
                new Plugin("second plugin", "2nd", false)
        ));

        Set<Plugin> pluginResults = bidEngineResource.plugins();

        assertThat(pluginResults).hasSize(2).extracting("name", "description", "activated").containsOnly(
                tuple("first plugin", "1st", true),
                tuple("second plugin", "2nd", false));
    }
}
