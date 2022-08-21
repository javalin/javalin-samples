package javalinvue

import io.javalin.Javalin
import io.javalin.security.RouteRole
import io.javalin.http.Header
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.javalin.http.staticfiles.Location
import io.javalin.vue.VueComponent

enum class Role : RouteRole { ANYONE, LOGGED_IN }

fun main() {

    val app = Javalin.create { config ->
        config.staticFiles.enableWebjars()
        config.accessManager { handler, ctx, permittedRoles ->
            when {
                Role.ANYONE in permittedRoles -> handler.handle(ctx)
                Role.LOGGED_IN in permittedRoles && currentUser(ctx) != null -> handler.handle(ctx)
                else -> ctx.status(401).header(Header.WWW_AUTHENTICATE, "Basic")
            }
        }
        // The line below should be deleted if you are opening the project standalone
        config.vue.rootDirectory("javalin5/javalinvue-example/javalinvue3-example/src/main/resources/vue", Location.EXTERNAL)
        config.vue.stateFunction = { ctx -> mapOf("currentUser" to currentUser(ctx)) }
        config.vue.vueAppName = "app"
    }.start(7070)

    app.get("/", VueComponent("hello-world"), Role.ANYONE)
    app.get("/users", VueComponent("user-overview"), Role.ANYONE)
    app.get("/users/{user-id}", VueComponent("user-profile"), Role.LOGGED_IN)
    app.error(HttpStatus.NOT_FOUND, "html", VueComponent("not-found"))

    app.get("/api/users", UserController::getAll, Role.ANYONE)
    app.get("/api/users/{user-id}", UserController::getOne, Role.LOGGED_IN)

}

private fun currentUser(ctx: Context) = ctx.basicAuthCredentials()?.username
