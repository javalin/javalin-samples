import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
import io.javalin.util.FileUtil

private val reservations = mutableMapOf<String?, String?>(
    "saturday" to "No reservation",
    "sunday" to "No reservation"
)

fun main() {
    val app = Javalin.create { config ->
        config.staticFiles.add("/public", Location.CLASSPATH)
        config.routes.post("/make-reservation") { ctx ->
            reservations[ctx.formParam("day")] = ctx.formParam("time")
            ctx.html("Your reservation has been saved")
        }
        config.routes.get("/check-reservation") { ctx ->
            ctx.html(reservations[ctx.queryParam("day")]!!)
        }
        config.routes.post("/upload-example") { ctx ->
            ctx.uploadedFiles("files").forEach {
                FileUtil.streamToFile(it.content, "upload/${it.filename}")
            }
            ctx.html("Upload successful")
        }
    }.start(7070)
}
