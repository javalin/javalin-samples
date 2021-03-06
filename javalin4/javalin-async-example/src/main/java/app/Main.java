package app;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class Main {

    private static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.server(() -> ServerUtil.createHttp2Server(new QueuedThreadPool(10, 2, 60_000)));
            config.addStaticFiles("/public", Location.CLASSPATH);
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
