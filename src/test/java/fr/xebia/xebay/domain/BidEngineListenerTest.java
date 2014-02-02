package fr.xebia.xebay.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Optional.empty;
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
        user = users.create("email@provider.com");
    }

    @Before
    public void bidOffersNeverExpires() {
        when(expiration.isExpired()).thenReturn(false);
    }

    @Test
    public void should_be_notified_when_bid() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3)), expiration);
        BidEngineListener bidEngineListener = mock(BidEngineListener.class);
        bidEngine.addListener(bidEngineListener);

        bidEngine.bid(user, "an item", 4.3, 5);

        ArgumentCaptor<BidOffer> argumentCaptor = ArgumentCaptor.forClass(BidOffer.class);
        verify(bidEngineListener).onBidOfferBidded(argumentCaptor.capture());
        BidOffer updatedBidOffer = argumentCaptor.getValue();
        assertThat(updatedBidOffer.futureBuyerEmail.get()).isEqualTo("email@provider.com");
        assertThat(updatedBidOffer.currentValue).isEqualTo(9.3);
        assertThat(updatedBidOffer.itemName).isEqualTo("an item");
        assertThat(updatedBidOffer.ownerEmail).isEqualTo(empty());
        assertThat(updatedBidOffer.initialValue).isEqualTo(4.3);
    }

    @Test
    public void should_be_notified_when_bid_offer_is_resolved() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3)), expiration);
        BidEngineListener bidEngineListener = mock(BidEngineListener.class);
        bidEngine.addListener(bidEngineListener);

        resolvesBidOffer(bidEngine);

        ArgumentCaptor<BidOffer> argumentCaptor = ArgumentCaptor.forClass(BidOffer.class);
        verify(bidEngineListener).onBidOfferResolved(argumentCaptor.capture());
        BidOffer resolvedBidOffer = argumentCaptor.getValue();
        assertThat(resolvedBidOffer.futureBuyerEmail).isEqualTo(empty());
        assertThat(resolvedBidOffer.initialValue).isEqualTo(4.3);
        assertThat(resolvedBidOffer.itemName).isEqualTo("an item");
        assertThat(resolvedBidOffer.currentValue).isEqualTo(3.87);
        assertThat(resolvedBidOffer.ownerEmail).isEqualTo(empty());
    }

    @Test
    public void should_be_notified_when_new_bid_offer_is_proposed() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3), new Item("another item", 2.4)), expiration);
        BidEngineListener bidEngineListener = mock(BidEngineListener.class);
        bidEngine.addListener(bidEngineListener);

        resolvesBidOffer(bidEngine);

        ArgumentCaptor<BidOffer> argumentCaptor = ArgumentCaptor.forClass(BidOffer.class);
        verify(bidEngineListener).onNewBidOffer(argumentCaptor.capture());
        BidOffer newBidOffer = argumentCaptor.getValue();
        assertThat(newBidOffer.futureBuyerEmail).isEqualTo(empty());
        assertThat(newBidOffer.initialValue).isEqualTo(2.4);
        assertThat(newBidOffer.itemName).isEqualTo("another item");
        assertThat(newBidOffer.currentValue).isEqualTo(2.4);
        assertThat(newBidOffer.ownerEmail).isEqualTo(empty());
    }

    private void resolvesBidOffer(BidEngine bidEngine) {
        boolean previousExpirationState = expiration.isExpired();
        when(expiration.isExpired()).thenReturn(true);
        bidEngine.currentBidOffer();
        when(expiration.isExpired()).thenReturn(previousExpirationState);
    }
}
