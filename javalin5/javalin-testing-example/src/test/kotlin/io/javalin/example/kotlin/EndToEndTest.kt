package io.javalin.example.kotlin

import io.github.bonigarcia.wdm.WebDriverManager
import io.javalin.testtools.JavalinTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

class EndToEndTest {

    private val app = JavalinTestingExampleApp("someDependency").app // inject any dependencies you might have

    @Test
    fun `UI contains correct heading`() = JavalinTest.test(app) { server, client ->
        System.setProperty("webdriver.http.factory", "jdk-http-client")
        WebDriverManager.chromedriver().setup()
        val driver: WebDriver = ChromeDriver(ChromeOptions().apply {
            addArguments("--headless")
            addArguments("--disable-gpu")
        })
        driver.get("${client.origin}/ui")
        assertThat(driver.pageSource).contains("<h1>User UI</h1>")
        driver.quit()

    }

}
