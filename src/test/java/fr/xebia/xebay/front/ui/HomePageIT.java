package fr.xebia.xebay.front.ui;

import fr.xebia.xebay.front.test.PhantomJsTest;
import fr.xebia.xebay.front.test.TomcatRule;
import org.fluentlenium.core.annotation.Page;
import org.junit.ClassRule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.withText;

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

    @Test
    public void should_go_to_register_page() {
        goTo(homePage);

        click($("a", withText("Sign up »")));

        assertThat(title()).isEqualTo("Xebay - Signup");
    }
}
