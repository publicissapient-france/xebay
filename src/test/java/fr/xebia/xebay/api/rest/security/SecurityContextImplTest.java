package fr.xebia.xebay.api.rest.security;

import fr.xebia.xebay.domain.internal.User;
import org.junit.Test;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class SecurityContextImplTest {
    @Test
    public void SecurityContext_role_should_defer_to_user() {
        User user = spy(new User("key", "name"));
        SecurityContextImpl securityContext = new SecurityContextImpl(user);

        securityContext.isUserInRole("some role");

        verify(user).isInRole(eq("some role"));
    }
}
