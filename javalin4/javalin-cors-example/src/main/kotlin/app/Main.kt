package app

import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
import org.json.JSONObject
import java.util.*

fun main(args: Array<String>) {

    Javalin.create {
        it.enableCorsForAllOrigins()
        it.enableDevLogging()
    }.get("/") { ctx ->
        val res = JSONObject().apply {
            put("version", "1.0.0")
            put("date", Date().toString())
            put("contact", "admin@domain.com")
        }
        ctx.result(res.toString()).contentType("application/json")
    }.start(80)

    Javalin.create {
        it.addStaticFiles("/public", Location.CLASSPATH)
    }.start(8081)

}
