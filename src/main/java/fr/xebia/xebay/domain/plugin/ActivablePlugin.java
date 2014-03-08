package fr.xebia.xebay.domain.plugin;

import fr.xebia.xebay.domain.internal.BidOffer;
import fr.xebia.xebay.domain.internal.BidOfferToSell;
import fr.xebia.xebay.domain.internal.Items;

public abstract class ActivablePlugin implements Plugin {
    private final String name;

    private boolean activated;

    protected ActivablePlugin(String pluginName) {
        this.name = pluginName;
        this.activated = false;
    }

    public String getName() {
        return name;
    }

    public void activate(Items items) {
        activated = true;
        onActivation(items);
    }

    public void deactivate() {
        activated = false;
    }

    protected void onActivation(Items items) {
    }

    @Override
    public final void onBidOfferResolved(BidOffer bidOffer, Items items) {
        if (activated) {
            onBidOfferResolvedIfActivated(bidOffer, items);
        }
    }

    protected void onBidOfferResolvedIfActivated(BidOffer bidOffer, Items items) {
    }

    @Override
    public final boolean authorize(BidOfferToSell bidOfferToSell) {
        return !activated || authorizeIfActivated(bidOfferToSell);
    }

    protected boolean authorizeIfActivated(BidOfferToSell bidOfferToSell) {
        return true;
    }
}
