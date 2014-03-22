package fr.xebia.xebay.api.rest;

import fr.xebia.xebay.domain.internal.AdminUser;
import fr.xebia.xebay.domain.PublicUser;
import fr.xebia.xebay.domain.User;
import fr.xebia.xebay.utils.TomcatRule;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.*;
import org.junit.rules.ExpectedException;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Set;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;

public class UserResourceIT {
    private Client client;
    private WebTarget target;
    private String key;

    @ClassRule
    public static TomcatRule tomcatRule = new TomcatRule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newClient().register(JacksonFeature.class);
        target = client.target("http://localhost:8080/rest/users/");
    }

    @After
    public void tearDown() throws Exception {
        if (null != key) {
            target.path("unregister").queryParam("key", key).request().header(HttpHeaders.AUTHORIZATION, AdminUser.KEY).delete();
        }
        target = null;
        client.close();
    }

    @Test
    public void register_should_create_new_user() throws Exception {
        key = target.path("register").queryParam("name", "user1").request().header(HttpHeaders.AUTHORIZATION, AdminUser.KEY).get(String.class);

        assertThat(key).hasSize(16);
    }

    @Test
    public void register_should_return_API_key_as_text() throws Exception {
        Response registerResponse = target.path("register").queryParam("name", "user1").request().header(HttpHeaders.AUTHORIZATION, AdminUser.KEY).get();
        key = registerResponse.readEntity(String.class);

        assertThat(registerResponse.getMediaType()).isEqualTo(MediaType.TEXT_PLAIN_TYPE);
    }

    @Test
    public void register_should_throw_exception_if_already_registered_user() throws Exception {
        key = target.path("register").queryParam("name", "user1").request().header(HttpHeaders.AUTHORIZATION, AdminUser.KEY).get(String.class);

        Response response = target.path("register").queryParam("name", "user1").request().header(HttpHeaders.AUTHORIZATION, AdminUser.KEY).get();

        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.getStatusCode());
        assertThat(response.readEntity(String.class)).isEqualTo("\"user1\" is already registered");
    }

    @Test(expected = BadRequestException.class)
    public void registering_without_name_is_a_bad_request() {
        target.path("register").request().header(HttpHeaders.AUTHORIZATION, AdminUser.KEY).get(String.class);
    }

    @Test
    public void should_get_public_users() {
        key = target.path("register").queryParam("name", "user1").request().header(HttpHeaders.AUTHORIZATION, AdminUser.KEY).get(String.class);

        Set<PublicUser> users = target.path("publicUsers").request().get(new GenericType<Set<PublicUser>>() {
        });

        assertThat(users).containsExactly(new PublicUser("user1", "avatar", 1000d, 0d));
    }

    @Test(expected = ForbiddenException.class)
    public void user_should_not_see_registered_users() {
        key = target.path("register").queryParam("name", "user1").request().header(HttpHeaders.AUTHORIZATION, AdminUser.KEY).get(String.class);

        target.request().header(HttpHeaders.AUTHORIZATION, key).get(new GenericType<Set<User>>() {});
    }

    @Test
    public void admin_should_see_registered_users() throws IOException {
        key = target.path("register").queryParam("name", "user1").request().header(HttpHeaders.AUTHORIZATION, AdminUser.KEY).get(String.class);

        Set<User> userSet = target.request().header(HttpHeaders.AUTHORIZATION, AdminUser.KEY).get(new GenericType<Set<User>>() {
        });

        assertThat(userSet).isNotNull().hasSize(1).extracting("name", String.class).contains("user1");
    }
}
