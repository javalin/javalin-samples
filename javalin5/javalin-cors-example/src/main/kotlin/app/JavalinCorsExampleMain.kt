package app

import io.javalin.Javalin

fun main() {

    Javalin.create { config ->
        config.plugins.enableCors { cors ->
            cors.add {
                it.anyHost()
            }
        }
        config.plugins.enableDevLogging()
    }.get("/") { ctx ->
        ctx.result("Message from server on port 7070")
    }.start(7070)

    Javalin.create()
        .get("/") {
            it.html(
                """
                <script>
                    fetch("http://localhost:7070")
                        .then(response => response.text())
                        .then(text => document.write("<h1>" + text + "</h1>"));
                </script>
                """.trimIndent()
            )
        }.start(7071)


}
