package fr.xebia.xebay.front.test;

import org.fluentlenium.core.FluentAdapter;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;

import java.io.File;

import static org.openqa.selenium.phantomjs.PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY;

public class PhantomJsTest extends FluentAdapter {
    private static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(1024, 768);

    private static WebDriver driver;

    @Rule
    public LifeCycle lifecycle = new LifeCycle();

    class LifeCycle extends TestWatcher {
        @Override
        protected void starting(Description description) {
            if (null == driver) {
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    if (driver != null) {
                        driver.quit();
                    }
                }));

                driver = createDriver();
            }

            driver.manage().deleteAllCookies();
            driver.manage().window().setSize(DEFAULT_WINDOW_SIZE);
            initFluent(driver);
            initTest();
        }

        private WebDriver createDriver() {
            File phantomJsExe = new PhantomJsDownloader().downloadAndExtract();

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomJsExe.getAbsolutePath());

            DriverService service = PhantomJSDriverService.createDefaultService(capabilities);

            return new PhantomJSDriver(service, capabilities);
        }
    }
}