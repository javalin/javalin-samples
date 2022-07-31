package app

import io.javalin.Javalin
import io.javalin.core.util.RouteOverviewPlugin

fun main() {

    val app = Javalin.create {
        it.sessionHandler(::fileSessionHandler)
        it.registerPlugin(RouteOverviewPlugin("/"))
    }.start(7070)

    app.get("/write") { ctx ->
        // values written to the session will be available on all your instances if you use a session db
        ctx.sessionAttribute("my-key", "My value")
        ctx.result("Wrote value: " + ctx.sessionAttribute<Any>("my-key"))
    }

    app.get("/read") { ctx ->
        // values on the session will be available on all your instances if you use a session db
        val myValue = ctx.sessionAttribute<String>("my-key")
        ctx.result("Value: $myValue")
    }

    app.get("/invalidate") { ctx ->
        // if you want to invalidate a session, jetty will clean everything up for you
        ctx.req.session.invalidate()
        ctx.result("Session invalidated")
    }

    app.get("/change-id") { ctx ->
        // it could be wise to change the session id on login, to protect against session fixation attacks
        ctx.req.changeSessionId()
        ctx.result("Session ID changed")
    }

}
