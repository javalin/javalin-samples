package io.javalin.example.java;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.assertj.core.api.Assertions.assertThat;

public class EndToEndTest {

    Javalin app = new JavalinTestingExampleApp("someDependency").javalinApp(); // inject any dependencies you might have

    @Test
    public void UI_contains_correct_heading() {
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        JavalinTest.test(app, (server, client) -> {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            WebDriver driver = new ChromeDriver(options);
            driver.get(client.getOrigin() + "/ui");
            assertThat(driver.getPageSource()).contains("<h1>User UI</h1>");
            driver.quit();
        });
    }

}
