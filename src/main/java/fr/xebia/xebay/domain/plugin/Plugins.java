package fr.xebia.xebay.domain.plugin;

import fr.xebia.xebay.domain.PluginInfo;
import fr.xebia.xebay.domain.internal.BidOffer;
import fr.xebia.xebay.domain.internal.BidOfferToSell;
import fr.xebia.xebay.domain.internal.Items;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

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
        getPlugin(pluginName).ifPresent(plugin -> plugin.activate(items));
    }

    public void deactivate(String pluginName) {
        getPlugin(pluginName).ifPresent(ActivablePlugin::deactivate);
    }

    public String getDescription(String pluginName) {
        return getPlugin(pluginName).get().getDescription();
    }

    public Set<PluginInfo> toPluginSet() {
        return plugins.stream().map(plugin -> plugin.toPlugin()).collect(toSet());
    }

    Optional<ActivablePlugin> getPlugin(String pluginName) {
        return plugins.stream().filter((plugin) -> plugin.getName().equals(pluginName)).findFirst();
    }
}
