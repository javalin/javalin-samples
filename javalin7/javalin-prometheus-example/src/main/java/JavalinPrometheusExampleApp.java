import io.javalin.Javalin;
import io.prometheus.client.exporter.HTTPServer;
import java.io.IOException;
import java.util.Random;
import kong.unirest.Unirest;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.LoggerFactory;
import static io.javalin.apibuilder.ApiBuilder.get;

public class JavalinPrometheusExampleApp {

    public static void main(String[] args) throws Exception {

        StatisticsHandler statisticsHandler = new StatisticsHandler();
        QueuedThreadPool queuedThreadPool = new QueuedThreadPool(200, 8, 60_000);

        Javalin app = Javalin.create(config -> {
            config.jetty.threadPool = queuedThreadPool;
            config.jetty.modifyServer(server -> {
                server.setHandler(statisticsHandler);
            });
            config.routes.apiBuilder(() -> {
                get("/1", ctx -> ctx.result("Hello World"));
                get("/2", ctx -> {
                    Thread.sleep((long) (Math.random() * 2000));
                    ctx.result("Slow Hello World");
                });
                get("/3", ctx -> ctx.redirect("/2"));
                get("/4", ctx -> ctx.status(400));
                get("/5", ctx -> ctx.status(500));
            });
        }).start(7070);

        initializePrometheus(statisticsHandler, queuedThreadPool);

        while (true) {
            spawnRandomRequests();
        }

    }

    private static void initializePrometheus(StatisticsHandler statisticsHandler, QueuedThreadPool queuedThreadPool) throws IOException {
        StatisticsHandlerCollector.initialize(statisticsHandler);
        QueuedThreadPoolCollector.initialize(queuedThreadPool);
        HTTPServer prometheusServer = new HTTPServer(7080);
        LoggerFactory.getLogger("JavalinPrometheusExampleApp").info("Prometheus is listening on: http://localhost:7080");
    }

    private static void spawnRandomRequests() throws InterruptedException {
        new Thread(() -> {
            for (int i = 0; i < new Random().nextInt(50); i++) {
                Unirest.get("http://localhost:7070/1").asString(); // we want a lot more "200 - OK" traffic
                Unirest.get("http://localhost:7070/" + (1 + new Random().nextInt(5))).asString(); // hit a random (1-5) endpoint
            }
        }).start();
        Thread.sleep((int) (Math.random() * 250));
    }

}
