package fr.xebia.xebay.front.ui;

import fr.xebia.xebay.api.RegisterRule;
import fr.xebia.xebay.front.test.PhantomJsTest;
import fr.xebia.xebay.utils.TomcatRule;
import org.fluentlenium.core.annotation.Page;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.withText;

public class HomePageIT extends PhantomJsTest {
    @ClassRule
    public static TomcatRule webServer = new TomcatRule();

    @Rule
    public RegisterRule registerRule = new RegisterRule();

    @Page
    public HomePage homePage;

    @Test
    public void should_display_current_bid_offer() {
        goTo(homePage);

        assertThat($("#currentBidOffer").getText()).contains("an item", "category");
    }

    @Test
    public void should_signin() {
        goTo(homePage);

        fill("#key").with(registerRule.getKey());
        $("button", withText("Sign in")).click();

        await().atMost(2000).until("button").withText("Sign out").isPresent();
        $("a", withText().startsWith("Account")).click();
        assertThat($("#name-display").getText()).isEqualTo("user1");
        assertThat($("#key-display").getText()).isEqualTo(registerRule.getKey());
    }
}
