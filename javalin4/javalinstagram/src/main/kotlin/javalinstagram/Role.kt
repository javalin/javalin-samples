package javalinstagram

import io.javalin.core.security.RouteRole

enum class Role : RouteRole {
    LOGGED_IN,
    ANYONE
}
