package app

import io.javalin.Javalin

fun main() {

    Javalin.create { config ->
        config.bundledPlugins.apply {
            enableDevLogging()
            enableCors { cors ->
                cors.addRule {
                    it.anyHost()
                }
            }
        }
        config.router.mount {
            it.get("/") { ctx ->
                ctx.result("Message from server on port 7070")
            }
        }
    }.start(7070)

    Javalin.create {
        it.router.mount {
            it.get("/") {
                it.html(
                    """
                <script>
                    fetch("http://localhost:7070")
                        .then(response => response.text())
                        .then(text => document.write("<h1>" + text + "</h1>"));
                </script>
                """.trimIndent()
                )
            }
        }
    }.start(7071)

}
