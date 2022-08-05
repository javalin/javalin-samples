package app;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsContext;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JavalinRealtimeCollaborationExampleApp {

    private static final Map<String, Collab> collabs = new ConcurrentHashMap<>();

    public static void main(String[] args) {

        Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).ws("/docs/{doc-id}", ws -> {
            ws.onConnect(ctx -> {
                if (getCollab(ctx) == null) {
                    createCollab(ctx);
                }
                getCollab(ctx).clients.add(ctx);
                ctx.send(getCollab(ctx).doc);
            });
            ws.onMessage(ctx -> {
                getCollab(ctx).doc = ctx.message();
                getCollab(ctx).clients.stream().filter(c -> c.session.isOpen()).forEach(s -> {
                    s.send(getCollab(ctx).doc);
                });
            });
            ws.onClose(ctx -> {
                getCollab(ctx).clients.remove(ctx);
            });
        }).start(7070);

    }

    private static Collab getCollab(WsContext ctx) {
        return collabs.get(ctx.pathParam("doc-id"));
    }

    private static void createCollab(WsContext ctx) {
        collabs.put(ctx.pathParam("doc-id"), new Collab());
    }

}
