package javalinvue

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.security.RouteRole
import io.javalin.http.Header
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.javalin.http.UnauthorizedResponse
import io.javalin.http.staticfiles.Location
import io.javalin.plugin.bundled.JavalinVuePlugin
import io.javalin.vue.VueComponent

enum class Role : RouteRole { ANYONE, LOGGED_IN }

fun main() {

    val app = Javalin.create { config ->
        config.staticFiles.enableWebjars()
        config.registerPlugin(JavalinVuePlugin { vue ->
            vue.rootDirectory("/vue", Location.CLASSPATH)
            vue.stateFunction = { ctx -> mapOf("currentUser" to ctx.currentUser()) }
            vue.vueInstanceNameInJs = "app"
        })
        config.routes.beforeMatched { ctx ->
            if (Role.LOGGED_IN in ctx.routeRoles() && ctx.currentUser() == null) {
                ctx.header(Header.WWW_AUTHENTICATE, "Basic")
                throw UnauthorizedResponse()
            }
        }
        config.routes.apiBuilder { // frontend routes
            get("/", VueComponent("hello-world"), Role.ANYONE)
            get("/users", VueComponent("user-overview"), Role.ANYONE)
            get("/users/{user-id}", VueComponent("user-profile"), Role.LOGGED_IN)
        }
        config.routes.apiBuilder { // api routes
            get("/api/users", UserController::getAll, Role.ANYONE)
            get("/api/users/{user-id}", UserController::getOne, Role.LOGGED_IN)
        }
        config.routes.error(HttpStatus.NOT_FOUND, "html", VueComponent("not-found"))
    }.start(7070)

}

private fun Context.currentUser() = this.basicAuthCredentials()?.username
