package app

import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
import io.javalin.ssl.plugin.SSLPlugin


fun main() {

    val app = Javalin.create {
        it.plugins.register(SSLPlugin{
            it.keystoreFromClasspath("keystore.jks", "password")
        })
        it.staticFiles.add("/public", Location.CLASSPATH)
    }.start()

    app.get("/") { it.result("Hello World") }

}

