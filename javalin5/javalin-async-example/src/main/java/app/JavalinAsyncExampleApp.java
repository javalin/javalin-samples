package app;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import io.javalin.ssl.plugin.SSLPlugin;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class JavalinAsyncExampleApp {

    private static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) {

        Supplier<Server> serverSupplier = () ->{
            //Create a new server with a custom thread pool
            Server server = new Server(new QueuedThreadPool(10, 2, 60_000));

            //Configure the SSL plugin
            SSLPlugin sslPlugin = new SSLPlugin(sslConfig -> {
                sslConfig.keystoreFromClasspath("keystore.jks","password");
            });

            //Patch the server with the SSL plugin, to enable SSL and HTTP/2 support
            sslPlugin.patch(server);

            return server;
        };

        Javalin app = Javalin.create(config -> {
            config.jetty.server(serverSupplier);
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start();


        app.get("/async", ctx -> {
            long taskTime = ctx.queryParamAsClass("task-time", Long.class).get();
            ctx.future(getFuture(taskTime));
        });

        app.get("/blocking", ctx -> {
            long taskTime = ctx.queryParamAsClass("task-time", Long.class).get();
            Thread.sleep(taskTime);
            ctx.result("done");
        });

        app.exception(Exception.class, (exception, ctx) -> {
            System.out.println(exception.getMessage());
        });

    }

    private static CompletableFuture<String> getFuture(long taskTime) {
        CompletableFuture<String> future = new CompletableFuture<>();
        executorService.schedule(() -> future.complete("done"), taskTime, TimeUnit.MILLISECONDS);
        return future;
    }

}
