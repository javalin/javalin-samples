import io.javalin.Javalin
import io.javalin.core.util.FileUtil
import io.javalin.http.staticfiles.Location

private val reservations = mutableMapOf<String?, String?>(
        "saturday" to "No reservation",
        "sunday" to "No reservation"
)

fun main(args: Array<String>) {

    val app = Javalin.create {
        it.addStaticFiles("/public", Location.CLASSPATH)
    }.start(7070)

    app.post("/make-reservation") { ctx ->
        reservations[ctx.formParam("day")] = ctx.formParam("time")
        ctx.html("Your reservation has been saved")
    }

    app.get("/check-reservation") { ctx ->
        ctx.html(reservations[ctx.queryParam("day")]!!)
    }

    app.post("/upload-example") { ctx ->
        ctx.uploadedFiles("files").forEach {
            FileUtil.streamToFile(it.content, "upload/${it.filename}")
        }
        ctx.html("Upload complete")
    }

}
