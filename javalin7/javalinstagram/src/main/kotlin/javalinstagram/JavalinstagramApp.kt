package javalinstagram

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.compression.CompressionStrategy
import io.javalin.http.Header
import io.javalin.http.staticfiles.Location
import io.javalin.plugin.bundled.JavalinVuePlugin
import io.javalin.vue.VueComponent
import javalinstagram.Role.ANYONE
import javalinstagram.Role.LOGGED_IN
import javalinstagram.account.AccountController
import javalinstagram.like.LikeController
import javalinstagram.photo.PhotoController
import javalinstagram.util.DbSetupUtil
import org.jdbi.v3.core.Jdbi
import java.io.File

val Database: Jdbi = Jdbi.create("jdbc:sqlite:javalinstagram.db")

fun main() {

    File("user-uploads").mkdir() // create folder for user uploads (if it doesn't exist)
    DbSetupUtil.setupDatabase() // drop and create tables, remove this between runs if you want to keep your data

    val app = Javalin.create {
        it.http.compressionStrategy = CompressionStrategy.GZIP
        it.jetty.modifyServletContextHandler { it.sessionHandler = Session.fileSessionHandler() }
        it.staticFiles.apply {
            add("user-uploads", Location.EXTERNAL)
            add("/public", Location.CLASSPATH)
            enableWebjars()
        }
        it.registerPlugin(JavalinVuePlugin { vue ->
            vue.rootDirectory("/vue", Location.CLASSPATH)
            vue.enableCspAndNonces = true
            vue.stateFunction = { ctx -> mapOf("currentUser" to ctx.currentUser) }
        })
        it.routes.beforeMatched { ctx ->
            val permitted = ctx.routeRoles()
            when {
                ANYONE in permitted || permitted.isEmpty() -> return@beforeMatched // don't redirect if route is open
                ctx.currentUser != null && permitted.contains(LOGGED_IN) -> return@beforeMatched // don't redirect if user is logged in and role matches
                ctx.header(Header.ACCEPT)?.contains("html") == true -> ctx.redirect("/signin") // redirect browser to signin
                else -> ctx.status(401) // return 401 for API requests
            }
        }
        it.routes.apiBuilder { // frontend routes
            get("/signin", VueComponent("signin-view"), ANYONE)
            get("/", VueComponent("feed-view"), LOGGED_IN)
            get("/my-photos", VueComponent("my-photos-view"), LOGGED_IN)
        }
        it.routes.apiBuilder { // backend routes
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
        it.routes.error(404, "html", VueComponent("not-found-view"))
    }.start(7070)

}
