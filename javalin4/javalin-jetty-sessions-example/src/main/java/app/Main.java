package app;

import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;

public class Main {
    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.sessionHandler(Sessions::fileSessionHandler);
            config.registerPlugin(new RouteOverviewPlugin("/"));
        }).start(7070);

        app.get("/write", ctx -> {
            // values written to the session will be available on all your instances if you use a session db
            ctx.sessionAttribute("my-key", "My value");
            ctx.result("Wrote value: " + ctx.sessionAttribute("my-key"));
        });

        app.get("/read", ctx -> {
            // values on the session will be available on all your instances if you use a session db
            String myValue = ctx.sessionAttribute("my-key");
            ctx.result("Value: " + myValue);
        });

        app.get("/invalidate", ctx -> {
            // if you want to invalidate a session, jetty will clean everything up for you
            ctx.req.getSession().invalidate();
            ctx.result("Session invalidated");
        });

        app.get("/change-id", ctx -> {
            // it could be wise to change the session id on login, to protect against session fixation attacks
            ctx.req.changeSessionId();
            ctx.result("Session ID changed");
        });

    }

}
