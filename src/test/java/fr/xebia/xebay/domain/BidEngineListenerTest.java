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
        BidEngine bidEngine = new BidEngine(new Items(new Item("category", "an item", 4.3)), expiration);
        BidEngineListener bidEngineListener = mock(BidEngineListener.class);
        bidEngine.addListener(bidEngineListener);

        bidEngine.bid(user, "an item", 4.3 + 5);

        ArgumentCaptor<BidOffer> argumentCaptor = ArgumentCaptor.forClass(BidOffer.class);
        verify(bidEngineListener).onBidOfferBidded(argumentCaptor.capture());
        BidOffer updatedBidOffer = argumentCaptor.getValue();
        assertThat(updatedBidOffer.getUserName()).isNotNull().isEqualTo("user1");
        assertThat(updatedBidOffer.getItem().getName()).isEqualTo("an item");
        assertThat(updatedBidOffer.getItem().getValue()).isEqualTo(9.3);
    }

    @Test
    public void should_be_notified_when_bid_offer_is_resolved() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("category", "an item", 4.3)), expiration);
        BidEngineListener bidEngineListener = mock(BidEngineListener.class);
        bidEngine.addListener(bidEngineListener);

        resolvesBidOffer(bidEngine);

        ArgumentCaptor<BidOffer> argumentCaptor = ArgumentCaptor.forClass(BidOffer.class);
        verify(bidEngineListener).onBidOfferResolved(argumentCaptor.capture());
        BidOffer resolvedBidOffer = argumentCaptor.getValue();
        assertThat(resolvedBidOffer.getUserName()).isNull();
        assertThat(resolvedBidOffer.getItem().getName()).isEqualTo("an item");
        assertThat(resolvedBidOffer.getItem().getValue()).isEqualTo(3.87);
    }

    @Test
    public void should_be_notified_when_new_bid_offer_is_proposed() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("category", "an item", 4.3), new Item("category", "another item", 2.4)), expiration);
        BidEngineListener bidEngineListener = mock(BidEngineListener.class);
        bidEngine.addListener(bidEngineListener);

        resolvesBidOffer(bidEngine);

        ArgumentCaptor<BidOffer> argumentCaptor = ArgumentCaptor.forClass(BidOffer.class);
        verify(bidEngineListener).onNewBidOffer(argumentCaptor.capture());
        BidOffer newBidOffer = argumentCaptor.getValue();
        assertThat(newBidOffer.getUserName()).isNull();
        assertThat(newBidOffer.getItem().getName()).isEqualTo("another item");
        assertThat(newBidOffer.getItem().getValue()).isEqualTo(2.4);
    }

    private void resolvesBidOffer(BidEngine bidEngine) {
        boolean previousExpirationState = expiration.isExpired();
        when(expiration.isExpired()).thenReturn(true);
        bidEngine.currentBidOffer();
        when(expiration.isExpired()).thenReturn(previousExpirationState);
    }
}
