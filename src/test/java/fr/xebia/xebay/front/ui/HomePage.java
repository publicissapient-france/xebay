package fr.xebia.xebay.front.ui;

import org.fluentlenium.core.FluentPage;

public class HomePage extends FluentPage {
    @Override
    public String getUrl() {
        return "http://localhost:8080/index.html";
    }
}
