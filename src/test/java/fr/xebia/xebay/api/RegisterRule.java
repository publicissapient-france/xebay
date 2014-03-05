package fr.xebia.xebay.api;

import fr.xebia.xebay.domain.internal.AdminUser;
import org.junit.rules.ExternalResource;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.HttpHeaders;

/**
 * Use this rule to register a user before each test, get his key during test and unregister him after.
 */
public final class RegisterRule extends ExternalResource {
    private final Client client;

    private String key;

    public RegisterRule() {
        this.client = ClientBuilder.newBuilder().build();
    }

    @Override
    protected void before() throws Throwable {
        this.key = register(client);
    }

    @Override
    protected void after() {
        unregister(client, key);
        client.close();
    }

    public String getKey() {
        return key;
    }

    public static String register(Client client) {
        return client.target("http://localhost:8080/rest/users/register")
                .queryParam("name", "user1")
                .request()
                .header(HttpHeaders.AUTHORIZATION, AdminUser.KEY)
                .get(String.class);
    }

    public static void unregister(Client client, String key) {
        client.target("http://localhost:8080/rest/users/unregister")
                .queryParam("key", key)
                .request()
                .header(HttpHeaders.AUTHORIZATION, AdminUser.KEY)
                .delete();
    }
}
