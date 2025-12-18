package javalinvue

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.security.RouteRole
import io.javalin.http.Header
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.javalin.http.UnauthorizedResponse
import io.javalin.vue.VueComponent

enum class Role : RouteRole { ANYONE, LOGGED_IN }

fun main() {

    val app = Javalin.create { config ->
        config.staticFiles.enableWebjars()
        config.vue.apply {
            stateFunction = { ctx -> mapOf("currentUser" to ctx.currentUser()) }
            vueInstanceNameInJs = "app"
        }
        config.router.mount {
            it.beforeMatched { ctx ->
                if (Role.LOGGED_IN in ctx.routeRoles() && ctx.currentUser() == null) {
                    ctx.header(Header.WWW_AUTHENTICATE, "Basic")
                    throw UnauthorizedResponse()
                }
            }
        }.apiBuilder { // frontend routes
            get("/", VueComponent("hello-world"), Role.ANYONE)
            get("/users", VueComponent("user-overview"), Role.ANYONE)
            get("/users/{user-id}", VueComponent("user-profile"), Role.LOGGED_IN)
        }.apiBuilder { // api routes
            get("/api/users", UserController::getAll, Role.ANYONE)
            get("/api/users/{user-id}", UserController::getOne, Role.LOGGED_IN)
        }
    }.apply {
        error(HttpStatus.NOT_FOUND, "html", VueComponent("not-found"))
    }.start(7070)

}

private fun Context.currentUser() = this.basicAuthCredentials()?.username
