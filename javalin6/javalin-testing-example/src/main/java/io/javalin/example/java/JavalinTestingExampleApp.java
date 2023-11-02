package io.javalin.example.java;

import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

public class JavalinTestingExampleApp {

    private Javalin app;

    public JavalinTestingExampleApp(String dependency) {
        app = Javalin.create(config -> {
            config.router.apiBuilder(() -> {
                get("/users", UserController::getAll);
                post("/users", UserController::create);
                get("/ui", ctx -> ctx.html("<h1>User UI</h1>"));
            });
        });
    }

    public Javalin javalinApp() {
        return app;
    }

}
