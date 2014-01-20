package fr.xebia.xebay.api.rest;

import fr.xebia.xebay.front.test.TomcatRule;
import org.junit.*;
import org.junit.rules.ExpectedException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

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
        client = ClientBuilder.newClient();
        target = client.target("http://localhost:8080/rest/users/");
    }

    @After
    public void tearDown() throws Exception {
        if (null != key) {
            target.path("unregister")
                    .queryParam("email", "abc@def.ghi")
                    .queryParam("key", key).request().get();
        }
        target = null;
        client.close();
    }

    @Test
    public void register_should_create_new_user() throws Exception {
        key = target.path("register").queryParam("email", "abc@def.ghi").request().get(String.class);
        assertEquals(16, key.length());
    }

    @Test
    public void register_should_return_API_key_as_text() throws Exception {
        Response registerResponse = target.path("register").queryParam("email", "abc@def.ghi").request().get();
        key = readFromResponse(registerResponse);

        assertThat(registerResponse.getMediaType()).isEqualTo(MediaType.TEXT_PLAIN_TYPE);
    }

    private String readFromResponse(Response registerResponse) {
        StringBuilder responseToString = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader((InputStream) registerResponse.getEntity()))) {
            String currentLine;
            while ((currentLine = in.readLine()) != null) {
                if (responseToString.length() > 0) {
                    responseToString.append('\n');
                }
                responseToString.append(currentLine);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseToString.toString();
    }

    @Test
    public void register_should_throw_forbidden_exception_if_already_registered_user() throws Exception {
        key = target.path("register").queryParam("email", "abc@def.ghi").request().get(String.class);
        assertEquals(16, key.length());

        //expectedException.expect(ForbiddenException.class);
        //expectedException.expectMessage("\"abc@def.ghi\" is already registered");
        Response response = target.path("register").queryParam("email", "abc@def.ghi").request().get(Response.class);
        assertEquals(400, response.getStatus());
    }
}
