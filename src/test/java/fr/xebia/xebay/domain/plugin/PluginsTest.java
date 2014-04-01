package fr.xebia.xebay.domain.plugin;

import fr.xebia.xebay.domain.PluginInfo;
import fr.xebia.xebay.domain.internal.Item;
import fr.xebia.xebay.domain.internal.Items;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

public class PluginsTest {
    @Test
    public void should_activate_plugin() {
        Plugins plugins = new Plugins();

        plugins.activate("BankBuyEverything", new Items(new Item("category", "name", new BigDecimal(4.3))));

        assertThat(getPlugin(plugins, "BankBuyEverything").isActivated()).isTrue();
    }

    @Test
    public void should_deactivate_plugin() {
        Plugins plugins = new Plugins();

        plugins.activate("AllItemsInCategory", new Items(new Item("category", "name", new BigDecimal(4.3))));

        assertThat(getPlugin(plugins, "AllItemsInCategory").isActivated()).isTrue();
    }

    @Test
    public void should_return_set_of_plugins() {
        Plugins plugins = new Plugins();

        Set<PluginInfo> pluginSet = plugins.toPluginSet();

        assertThat(pluginSet).hasSize(2).extracting("name", "activated").containsOnly(
                tuple("BankBuyEverything", false),
                tuple("AllItemsInCategory", false));
    }

    @Test
    public void should_get_plugin_description() {
        Plugins plugins = new Plugins();

        plugins.activate("AllItemsInCategory", new Items(new Item("category", "name", new BigDecimal(4.3))));

        assertThat(plugins.getDescription("AllItemsInCategory")).isEqualTo("Bonus to user having all items in category");
    }

    private PluginInfo getPlugin(Plugins plugins, String pluginName) {
        return plugins.getPlugin(pluginName).map(plugin -> plugin.toPlugin()).get();
    }
}
