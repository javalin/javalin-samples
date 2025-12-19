package app;

import io.javalin.Javalin;

import static app.SessionUtil.fileSessionHandler;

public class JavalinJettySessionExampleApp {
    public static void main(String[] args) {

        var app = Javalin.create(config -> {
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(fileSessionHandler()));
            config.bundledPlugins.enableRouteOverview("/");

            config.routes.get("/write", ctx -> {
                // values written to the session will be available on all your instances if you use a session db
                ctx.sessionAttribute("my-key", "My value");
                ctx.result("Wrote value: " + ctx.sessionAttribute("my-key"));
            });

            config.routes.get("/read", ctx -> {
                // values on the session will be available on all your instances if you use a session db
                String myValue = ctx.sessionAttribute("my-key");
                ctx.result("Value: " + myValue);
            });

            config.routes.get("/invalidate", ctx -> {
                // if you want to invalidate a session, jetty will clean everything up for you
                ctx.req().getSession().invalidate();
                ctx.result("Session invalidated");
            });

            config.routes.get("/change-id", ctx -> {
                // it could be wise to change the session id on login, to protect against session fixation attacks
                ctx.req().changeSessionId();
                ctx.result("Session ID changed");
            });
        }).start(7070);

    }

}
