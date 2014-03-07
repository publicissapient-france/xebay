package fr.xebia.xebay.domain.plugin;

import fr.xebia.xebay.domain.internal.BidOfferToSell;

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

    public void activate() {
        activated = true;
    }

    public void deactivate() {
        activated = false;
    }

    @Override
    public final boolean authorize(BidOfferToSell bidOfferToSell) {
        return !activated || authorizeIfActivated(bidOfferToSell);
    }

    protected boolean authorizeIfActivated(BidOfferToSell bidOfferToSell) {
        return true;
    }
}
