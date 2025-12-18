package javalinstagram

import io.javalin.security.RouteRole

enum class Role : RouteRole {
    LOGGED_IN,
    ANYONE
}
