package fr.xebia.xebay.front.ui;

import fr.xebia.xebay.front.test.PhantomJsTest;
import fr.xebia.xebay.front.test.TomcatRule;
import org.fluentlenium.core.annotation.Page;
import org.junit.ClassRule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HomePageIT extends PhantomJsTest {
    @ClassRule
    public static TomcatRule webServer = new TomcatRule();

    @Page
    public HomePage homePage;

    @Test
    public void should_display_current_bid_offer() {
        goTo(homePage);

        assertThat($("#current-bid-offer").getText()).contains("an item");
    }
}
