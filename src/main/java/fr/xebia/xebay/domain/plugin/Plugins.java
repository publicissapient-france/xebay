package fr.xebia.xebay.domain.plugin;

import fr.xebia.xebay.domain.internal.BidOffer;
import fr.xebia.xebay.domain.internal.BidOfferToSell;
import fr.xebia.xebay.domain.internal.Items;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Plugins implements Plugin {
    private final Set<ActivablePlugin> plugins;

    public Plugins() {
        this.plugins = new HashSet<>();
        this.plugins.add(new BankBuyEverything());
        this.plugins.add(new AllItemsInCategory());
    }

    @Override
    public boolean authorize(BidOfferToSell bidOfferToSell) {
        boolean authorize = true;
        for (ActivablePlugin plugin : plugins) {
            authorize &= plugin.authorize(bidOfferToSell);
        }
        return authorize;
    }

    @Override
    public void onBidOfferResolved(BidOffer bidOffer, Items items) {
        plugins.forEach(plugin -> plugin.onBidOfferResolved(bidOffer, items));
    }

    public void activate(String pluginName, Items items) {
        ifPresent(pluginName, plugin -> plugin.activate(items));
    }

    public void deactivate(String pluginName) {
        ifPresent(pluginName, plugin -> plugin.deactivate());
    }

    private void ifPresent(String pluginName, Consumer<ActivablePlugin> doSomethingOnPlugin) {
        plugins.stream().filter((plugin) -> plugin.getName().equals(pluginName)).findFirst().ifPresent(doSomethingOnPlugin);
    }
}
