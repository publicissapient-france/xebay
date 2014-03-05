package fr.xebia.xebay.domain.internal;

import fr.xebia.xebay.domain.internal.Item;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemTest {
    @Test
    public void round_amount() throws Exception {
        Item item = new Item("category", "an item", 4.337);

        double value = item.getValue();

        assertThat(value).isEqualTo(4.34);
    }
}
