import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
import io.javalin.util.FileUtil

private val reservations = mutableMapOf<String?, String?>(
    "saturday" to "No reservation",
    "sunday" to "No reservation"
)

fun main() {

    val app = Javalin.create {
        it.staticFiles.add("/public", Location.CLASSPATH)
    }

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
        ctx.html("Upload successful")
    }

    app.start(7070)

}
