package fr.xebia.xebay.front.ui;

import fr.xebia.xebay.front.test.PhantomJsTest;
import fr.xebia.xebay.utils.TomcatRule;
import org.fluentlenium.core.annotation.Page;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.withText;

public class HomePageIT extends PhantomJsTest {
    @ClassRule
    public static TomcatRule webServer = new TomcatRule();

    @Page
    public HomePage homePage;

    private String key;

    @Before
    public void initializeKey() {
        key = null;
    }

    @After
    public void unregister() throws IOException {
        if (key == null) {
            return;
        }
        HttpURLConnection urlConnection = (HttpURLConnection) new URL("http://localhost:8080/rest/users/unregister?email=abc@def.ghi&key=" + key).openConnection();
        assertThat(urlConnection.getResponseCode()).as("HTTP response code when unregistering abc@def.ghi").isEqualTo(204);
    }

    @Test
    public void should_display_current_bid_offer() {
        goTo(homePage);

        assertThat($("#current-bid-offer").getText()).contains("an item");
    }

    @Test
    public void should_signup() {
        goTo(homePage);

        fill("#email").with("abc@def.ghi");
        $("button", withText("Sign up")).click();

        await().atMost(2000).until("button").withText("Sign out").isPresent();
        $("a", withText().startsWith("My infos")).click();
        assertThat($("#email-display").getText()).isEqualTo("abc@def.ghi");
        assertThat($("#key-display").getText()).hasSize(16);
        key = $("#key-display").getText();
    }
}
