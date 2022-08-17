package javalinstagram

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.Header
import io.javalin.http.staticfiles.Location
import io.javalin.vue.VueComponent
import javalinstagram.Role.ANYONE
import javalinstagram.Role.LOGGED_IN
import javalinstagram.account.AccountController
import javalinstagram.like.LikeController
import javalinstagram.photo.PhotoController
import org.jdbi.v3.core.Jdbi

val Database: Jdbi = Jdbi.create("jdbc:sqlite:javalinstagram.db")

val basePath = "javalin5/javalinstagram/" // change this to "" if you are opening the project standalone

fun main() {

    val app = Javalin.create {
        it.staticFiles.add("${basePath}user-uploads", Location.EXTERNAL)
        it.staticFiles.add("${basePath}src/main/resources/public", Location.EXTERNAL)
        it.staticFiles.enableWebjars()
        it.jetty.sessionHandler { Session.fileSessionHandler() }
        it.core.accessManager { handler, ctx, permitted ->
            when {
                ANYONE in permitted -> handler.handle(ctx)
                ctx.currentUser != null && permitted.contains(LOGGED_IN) -> handler.handle(ctx)
                ctx.header(Header.ACCEPT)?.contains("html") == true -> ctx.redirect("/signin") // redirect browser to signin
                else -> ctx.status(401)
            }
        }
        it.compression.brotliAndGzip()
        it.vue.enableCspAndNonces = true
        it.vue.stateFunction = { ctx -> mapOf("currentUser" to ctx.currentUser) }
        it.vue.rootDirectory("${basePath}src/main/resources/vue", Location.EXTERNAL) // comment out this line if you are opening the project standalone
    }.start(7070)

    app.routes {
        get("/signin", VueComponent("signin-view"), ANYONE)
        get("/", VueComponent("feed-view"), LOGGED_IN)
        get("/my-photos", VueComponent("my-photos-view"), LOGGED_IN)
    }

    app.error(404, "html", VueComponent("not-found-view"))

    app.routes {
        path("api") {
            path("photos") {
                get(PhotoController::getForQuery, LOGGED_IN)
                post(PhotoController::upload, LOGGED_IN)
            }
            path("likes") {
                post(LikeController::create, LOGGED_IN)
                delete(LikeController::delete, LOGGED_IN)
            }
            path("account") {
                post("sign-up", AccountController::signUp, ANYONE)
                post("sign-in", AccountController::signIn, ANYONE)
                post("sign-out", AccountController::signOut, ANYONE)
            }
        }
    }

}
