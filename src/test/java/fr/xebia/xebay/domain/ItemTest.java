package fr.xebia.xebay.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemTest {
    @Test
    public void round_amount() throws Exception {
        Item item = new Item("an item", 4.337);

        double value = item.getValue();

        assertThat(value).isEqualTo(4.34);
    }
}
