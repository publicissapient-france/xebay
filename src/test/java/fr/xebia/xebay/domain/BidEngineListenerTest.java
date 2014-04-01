package fr.xebia.xebay.domain;

import fr.xebia.xebay.domain.internal.Item;
import fr.xebia.xebay.domain.internal.Items;
import fr.xebia.xebay.domain.internal.User;
import fr.xebia.xebay.domain.internal.Users;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BidEngineListenerTest {
    @Mock
    private Expirable expiration;

    private Users users = new Users();
    private User user;

    @Before
    public void createUser() {
        user = users.create("user1");
    }

    @Before
    public void bidOffersNeverExpires() {
        when(expiration.isExpired()).thenReturn(false);
    }

    @Test
    public void should_be_notified_when_bid() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("category", "an item", new BigDecimal(4.3))), expiration);
        BidEngineListener bidEngineListener = mock(BidEngineListener.class);
        bidEngine.addListener(bidEngineListener);

        bidEngine.bid(user, "an item", 4.3 + 5);

        ArgumentCaptor<BidOffer> argumentCaptor = ArgumentCaptor.forClass(BidOffer.class);
        verify(bidEngineListener).onBid(argumentCaptor.capture());
        BidOffer updatedBidOffer = argumentCaptor.getValue();
        assertThat(updatedBidOffer.getOwner()).isNull();
        assertThat(updatedBidOffer.getBidder()).isNotNull().isEqualTo("user1");
        assertThat(updatedBidOffer.getItem().getName()).isEqualTo("an item");
        assertThat(updatedBidOffer.getItem().getValue()).isEqualTo(9.3);
    }

    @Test
    public void should_be_notified_when_bid_offer_is_resolved() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("category", "an item", new BigDecimal(4.3))), expiration);
        BidEngineListener bidEngineListener = mock(BidEngineListener.class);
        bidEngine.addListener(bidEngineListener);

        resolvesBidOffer(bidEngine);

        ArgumentCaptor<BidOffer> argumentCaptor = ArgumentCaptor.forClass(BidOffer.class);
        verify(bidEngineListener, times(2)).onBid(argumentCaptor.capture());
        List<BidOffer> bidOfferList = argumentCaptor.getAllValues();
        BidOffer bidOffer1 = bidOfferList.get(0);
        assertThat(bidOffer1.getOwner()).isNull();
        assertThat(bidOffer1.getBidder()).isNull();
        assertThat(bidOffer1.getItem().getName()).isEqualTo("an item");
        assertThat(bidOffer1.getItem().getValue()).isEqualTo(3.87);
        BidOffer bidOffer2 = bidOfferList.get(1);
        assertThat(bidOffer2.getBidder()).isNull();
        assertThat(bidOffer2.getOwner()).isNull();
        assertThat(bidOffer2.getItem().getName()).isEqualTo("an item");
        assertThat(bidOffer2.getItem().getValue()).isEqualTo(3.87);
    }

    @Test
    public void should_be_notified_when_new_bid_offer_is_proposed() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("category", "an item", new BigDecimal(4.3)), new Item("category", "another item", new BigDecimal(2.4))), expiration);
        BidEngineListener bidEngineListener = mock(BidEngineListener.class);
        bidEngine.addListener(bidEngineListener);

        resolvesBidOffer(bidEngine);

        ArgumentCaptor<BidOffer> argumentCaptor = ArgumentCaptor.forClass(BidOffer.class);
        verify(bidEngineListener, times(2)).onBid(argumentCaptor.capture());
        List<BidOffer> bidOfferList = argumentCaptor.getAllValues();
        BidOffer bidOffer1 = bidOfferList.get(0);
        assertThat(bidOffer1.getOwner()).isNull();
        assertThat(bidOffer1.getBidder()).isNull();
        assertThat(bidOffer1.getItem().getName()).isEqualTo("an item");
        assertThat(bidOffer1.getItem().getValue()).isEqualTo(3.87);
        BidOffer bidOffer2 = bidOfferList.get(1);
        assertThat(bidOffer2.getBidder()).isNull();
        assertThat(bidOffer2.getOwner()).isNull();
        assertThat(bidOffer2.getItem().getName()).isEqualTo("another item");
        assertThat(bidOffer2.getItem().getValue()).isEqualTo(2.4);
    }

    private void resolvesBidOffer(BidEngine bidEngine) {
        boolean previousExpirationState = expiration.isExpired();
        when(expiration.isExpired()).thenReturn(true);
        bidEngine.currentBidOffer();
        when(expiration.isExpired()).thenReturn(previousExpirationState);
    }
}
