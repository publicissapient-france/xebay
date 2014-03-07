package fr.xebia.xebay.domain.plugin;

import fr.xebia.xebay.domain.internal.BidOfferToSell;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Plugins implements Plugin {
    private final Set<ActivablePlugin> plugins;

    public Plugins() {
        this.plugins = new HashSet<>();
        this.plugins.add(new BankBuyEverything());
    }

    @Override
    public boolean authorize(BidOfferToSell bidOfferToSell) {
        boolean authorize = true;
        for (ActivablePlugin plugin : plugins) {
            authorize &= plugin.authorize(bidOfferToSell);
        }
        return authorize;
    }

    public void activate(String pluginName) {
        ifPresent(pluginName, plugin -> plugin.activate());
    }

    public void deactivate(String pluginName) {
        ifPresent(pluginName, plugin -> plugin.deactivate());
    }

    private void ifPresent(String pluginName, Consumer<ActivablePlugin> doSomethingOnPlugin) {
        plugins.stream().filter((plugin) -> plugin.getName().equals(pluginName)).findFirst().ifPresent(doSomethingOnPlugin);
    }
}
