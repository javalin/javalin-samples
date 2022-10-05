package app;

import io.javalin.Javalin;
import io.javalin.community.ssl.SSLPlugin;
import io.javalin.http.staticfiles.Location;

public class JavalinHttp2ExampleApp {

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.plugins.register(new SSLPlugin(sslConfig -> {
                sslConfig.keystoreFromClasspath("keystore.jks", "password");
            }));
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start();

        app.get("/", ctx -> ctx.result("Hello World"));

    }

}
