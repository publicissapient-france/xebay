package fr.xebia.xebay.api.socket;

import fr.xebia.xebay.BidServer;
import fr.xebia.xebay.api.dto.BidDemand;
import fr.xebia.xebay.domain.internal.AdminUser;
import fr.xebia.xebay.domain.BidEngine;
import fr.xebia.xebay.domain.internal.Item;
import fr.xebia.xebay.domain.internal.User;
import fr.xebia.xebay.domain.BidOffer;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

public class BidEngineSocketTest {

    BidEngine bidEngineMock;

    BidEngineSocket bidEngineSocket;

    @Before
    public void before() {

        bidEngineMock = Mockito.mock(BidEngine.class);

        bidEngineSocket = new BidEngineSocket();
        bidEngineSocket.bidEngine = bidEngineMock;
    }

    @Test
    public void on_open_stores_session() throws Exception {

        Session sessionMock = Mockito.mock(Session.class);

        bidEngineSocket.onConnect(sessionMock);

        Assertions.assertThat(bidEngineSocket.sessions).containsOnly(sessionMock);
    }

    @Test
    public void on_open_removes_session() throws Exception {

        Session sessionMock1 = Mockito.mock(Session.class);
        Session sessionMock2 = Mockito.mock(Session.class);

        bidEngineSocket.onConnect(sessionMock1);
        bidEngineSocket.onConnect(sessionMock2);
        bidEngineSocket.onDisconnect(sessionMock1);

        Assertions.assertThat(bidEngineSocket.sessions).containsOnly(sessionMock2);
    }

    @Test
    public void test_started_bid_offer_are_sent_to_sessions() throws Exception {

        Session sessionMock = Mockito.mock(Session.class);
        Mockito.when(sessionMock.isOpen()).thenReturn(Boolean.TRUE);
        RemoteEndpoint.Basic basicRemoteMock = Mockito.mock(RemoteEndpoint.Basic.class);
        Mockito.when(sessionMock.getBasicRemote()).thenReturn(basicRemoteMock);
        bidEngineSocket.sessions.add(sessionMock);

        BidOffer bidOffer = new BidOffer("category", "name", 10d, 10L, "owner", "bidder", false);
        ArgumentCaptor<BidEngineSocketOutput> captor = ArgumentCaptor.forClass(BidEngineSocketOutput.class);

        bidEngineSocket.onBid(bidOffer);

        Mockito.verify(basicRemoteMock).sendObject(captor.capture());
        Assertions.assertThat(captor.getValue().getBidOffer()).isEqualTo(bidOffer);
    }

    @Test
    public void test_updated_bids_are_sent_to_sessions() throws Exception {

        Session sessionMock = Mockito.mock(Session.class);
        Mockito.when(sessionMock.isOpen()).thenReturn(Boolean.TRUE);
        RemoteEndpoint.Basic basicRemoteMock = Mockito.mock(RemoteEndpoint.Basic.class);
        Mockito.when(sessionMock.getBasicRemote()).thenReturn(basicRemoteMock);
        bidEngineSocket.sessions.add(sessionMock);
        BidOffer bidOffer = new BidOffer("category", "name", 10d, 10L, "owner", "bidder", false);
        ArgumentCaptor<BidEngineSocketOutput> captor = ArgumentCaptor.forClass(BidEngineSocketOutput.class);

        bidEngineSocket.onBid(bidOffer);

        Mockito.verify(basicRemoteMock).sendObject(captor.capture());
        Assertions.assertThat(captor.getValue().getBidOffer()).isEqualTo(bidOffer);
    }

    @Test
    public void test_resolved_bids_are_sent_to_sessions() throws Exception {

        Session sessionMock = Mockito.mock(Session.class);
        Mockito.when(sessionMock.isOpen()).thenReturn(Boolean.TRUE);
        RemoteEndpoint.Basic basicRemoteMock = Mockito.mock(RemoteEndpoint.Basic.class);
        Mockito.when(sessionMock.getBasicRemote()).thenReturn(basicRemoteMock);
        bidEngineSocket.sessions.add(sessionMock);
        BidOffer bidOffer = new BidOffer("category", "name", 10d, 10L, "owner", "bidder", false);
        ArgumentCaptor<BidEngineSocketOutput> captor = ArgumentCaptor.forClass(BidEngineSocketOutput.class);

        bidEngineSocket.onBid(bidOffer);

        Mockito.verify(basicRemoteMock).sendObject(captor.capture());
        Assertions.assertThat(captor.getValue().getBidOffer()).isEqualTo(bidOffer);
    }

    @Test
    public void test_bid_is_forwarded_to_engine() {

        User user = BidServer.BID_SERVER.users.getUser(AdminUser.KEY);
        BidDemand bidDemand = new BidDemand("name", 10d);
        BidEngineSocketInput bidEngineSocketInput = new BidEngineSocketInput();
        bidEngineSocketInput.setBid(bidDemand);

        bidEngineSocket.onMessage(bidEngineSocketInput, user.getKey());

        Mockito.verify(bidEngineMock).bid(user, "name", 10d);
    }

    @Test
    public void test_offer_is_forwarded_to_engine() {

        User user = BidServer.BID_SERVER.users.getUser(AdminUser.KEY);
        Item item = BidServer.BID_SERVER.items.find("name");
        BidDemand bidDemand = new BidDemand("name", 10d);
        BidEngineSocketInput bidEngineSocketInput = new BidEngineSocketInput();
        bidEngineSocketInput.setOffer(bidDemand);

        bidEngineSocket.onMessage(bidEngineSocketInput, user.getKey());

        Mockito.verify(bidEngineMock).offer(user, item, 10d);
    }
}
