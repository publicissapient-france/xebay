package fr.xebia.xebay.domain.internal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

public class AdminUserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void should_have_predefined_initial_values() throws Exception {
        AdminUser adminUser = new AdminUser();

        assertThat(adminUser.getBalance().value()).isZero();
        assertThat(adminUser.getKey()).isEqualTo("4dm1n");
        assertThat(adminUser.getName()).isEqualTo("admin");
        assertThat(adminUser.getItems()).isEmpty();
    }

    @Test
    public void should_not_buy() {
        AdminUser adminUser = new AdminUser();
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("admin can't buy");

        adminUser.buy(null);
    }

    @Test
    public void should_not_sell() {
        AdminUser adminUser = new AdminUser();
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("admin can't sell");

        adminUser.sell(null);
    }

    @Test
    public void should_be_in_admin_role() {
        AdminUser adminUser = new AdminUser();

        boolean isInAdminRole = adminUser.isInRole("admin");

        assertThat(isInAdminRole).isTrue();
    }

    @Test
    public void should_not_be_in_any_other_role() {
        AdminUser adminUser = new AdminUser();

        boolean isInAnotherRole = adminUser.isInRole("another role");

        assertThat(isInAnotherRole).isFalse();
    }

    @Test
    public void should_be_exported_to_model_user() {
        AdminUser adminUser = new AdminUser();

        fr.xebia.xebay.domain.User user = adminUser.toUser();

        assertThat(user.getName()).isEqualTo("admin");
        assertThat(user.getBalance()).isZero();
        assertThat(user.getItems()).isEmpty();
    }
}
