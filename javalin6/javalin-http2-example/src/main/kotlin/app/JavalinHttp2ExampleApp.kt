package app

import io.javalin.Javalin
import io.javalin.community.ssl.SSLPlugin
import io.javalin.http.staticfiles.Location

fun main() {

    val app = Javalin.create { javalinConfig ->
        // Set up the SSL plugin (Enables HTTP2 by default)
        javalinConfig.registerPlugin(SSLPlugin { ssl ->
            ssl.keystoreFromClasspath("keystore.jks", "password")
            ssl.insecurePort = 8080
            ssl.securePort = 8443
        })
        javalinConfig.staticFiles.add("/public", Location.CLASSPATH)
    }.start()

    app.get("/") { it.result("Hello World") }

}

