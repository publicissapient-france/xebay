package bid.ui;

import org.fluentlenium.core.FluentPage;

import static org.assertj.core.api.Assertions.assertThat;

public class RegisterPage extends FluentPage {
    @Override
    public String getUrl() {
        return "http://localhost:8080/register.html";
    }

    @Override
    public void isAt() {
        assertThat(title()).isEqualTo("Xebay - Signup");
    }
}
