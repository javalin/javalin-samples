package app

import io.javalin.Javalin
import io.javalin.community.ssl.SslPlugin
import io.javalin.http.staticfiles.Location

fun main() {

    Javalin.create { javalinConfig ->
        // Set up the SSL plugin (Enables HTTP2 by default)
        javalinConfig.registerPlugin(SslPlugin { ssl ->
            ssl.keystoreFromClasspath("keystore.jks", "password")
            ssl.insecurePort = 8080
            ssl.securePort = 8443
        })
        javalinConfig.staticFiles.add("/public", Location.CLASSPATH)

        javalinConfig.router.mount { router ->
            router.get("/") { it.result("Hello World") }
        }
    }.start()

}

