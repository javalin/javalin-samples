package app;

import io.javalin.Javalin;
import io.javalin.community.ssl.SslPlugin;
import io.javalin.http.staticfiles.Location;

public class JavalinHttp2ExampleApp {

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {

            // Set up the SSL plugin (Enables HTTP2 by default)
            config.registerPlugin(new SslPlugin(ssl -> {
                ssl.keystoreFromClasspath("keystore.jks", "password"); // The ssl plugin will enable HTTP/2 by default
                ssl.insecurePort = 8080;
                ssl.securePort = 8443;
            }));

            config.staticFiles.add("/public", Location.CLASSPATH);

            config.router.mount(router -> {
                router.get("/", ctx -> ctx.result("Hello World"));
            });
        }).start();

    }

}
