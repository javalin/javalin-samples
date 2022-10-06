package app

import io.javalin.Javalin
import io.javalin.community.ssl.SSLPlugin
import io.javalin.http.staticfiles.Location

fun main() {

    val app = Javalin.create {
        it.plugins.register(SSLPlugin {
            it.keystoreFromClasspath("keystore.jks", "password")
            it.insecurePort = 8080
            it.securePort = 8443
        })
        it.staticFiles.add("/public", Location.CLASSPATH)
    }.start()

    app.get("/") { it.result("Hello World") }

}

