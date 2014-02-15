package fr.xebia.xebay.front.ui;

import fr.xebia.xebay.domain.AdminUser;
import fr.xebia.xebay.front.test.PhantomJsTest;
import fr.xebia.xebay.utils.TomcatRule;
import org.fluentlenium.core.annotation.Page;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    public void register() throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) new URL("http://localhost:8080/rest/users/register?name=user1").openConnection();
        urlConnection.setRequestProperty(HttpHeaders.AUTHORIZATION, AdminUser.KEY);
        assertThat(urlConnection.getResponseCode()).as("HTTP response code when registering user1").isEqualTo(200);
        try (BufferedReader in = new BufferedReader(new InputStreamReader((InputStream) urlConnection.getContent()))) {
            key = in.readLine();
        }
        assertThat(key).hasSize(16);
    }

    @After
    public void unregister() throws IOException {
        if (key == null) {
            return;
        }
        HttpURLConnection urlConnection = (HttpURLConnection) new URL("http://localhost:8080/rest/users/unregister?key=" + key).openConnection();
        urlConnection.setRequestMethod("DELETE");
        urlConnection.setRequestProperty(HttpHeaders.AUTHORIZATION, AdminUser.KEY);
        assertThat(urlConnection.getResponseCode()).as("HTTP response code when unregistering user1").isEqualTo(204);
    }

    @Test
    public void should_display_current_bid_offer() {
        goTo(homePage);

        assertThat($("#current-bid-offer").getText()).contains("an item");
    }

    @Test
    public void should_signin() {
        goTo(homePage);

        fill("#name").with("user1");
        fill("#key").with(key);
        $("button", withText("Sign in")).click();

        await().atMost(2000).until("button").withText("Sign out").isPresent();
        $("a", withText().startsWith("My infos")).click();
        assertThat($("#name-display").getText()).isEqualTo("user1");
        assertThat($("#key-display").getText()).isEqualTo(key);
    }
}
