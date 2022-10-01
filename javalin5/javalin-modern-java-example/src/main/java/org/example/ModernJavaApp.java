package org.example;

import io.javalin.Javalin;
import io.javalin.validation.JavalinValidation;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import static java.net.http.HttpResponse.BodyHandlers.ofString;

public class ModernJavaApp {

    static final HttpClient httpClient = HttpClient.newBuilder().build();

    public static void main(String[] args) {

        // We want to accept a String URI from a user, so we need to inform Javalin of how to convert the
        // user provided String into an actual URI. We can use HttpRequest.Builder for validation.
        JavalinValidation.register(URI.class, s -> HttpRequest.newBuilder().uri(URI.create(s)).build().uri());

        var app = Javalin.create(javalinConfig -> {
            javalinConfig.http.asyncTimeout = 10_000;
        }).start(7070);

        app.get("/async-proxy/<url>", ctx -> {
            var validUri = ctx.pathParamAsClass("url", URI.class).get();
            var request = HttpRequest.newBuilder().GET().uri(validUri).build();
            ctx.future(() -> httpClient.sendAsync(request, ofString())
                .thenAccept(response -> {
                    ctx.html(response.body()).status(response.statusCode());
                })
                .exceptionally(exception -> {
                    ctx.html("Error: " + exception.getMessage());
                    return null;
                })
            );
        });

    }

}
