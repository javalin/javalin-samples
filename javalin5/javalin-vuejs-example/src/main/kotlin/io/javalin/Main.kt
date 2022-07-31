package io.javalin

import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.staticfiles.Location

data class Todo(val id: Long, val title: String, val completed: Boolean)

fun main() {

    var todos = arrayOf(Todo(123123123, "My very first todo", false))

    val app = Javalin.create {
        it.addStaticFiles("/public", Location.CLASSPATH)
    }.start(getHerokuAssignedPort())

    app.routes {
        path("todos") {
            get { ctx ->
                ctx.json(todos)
            }
            put { ctx ->
                todos = ctx.bodyAsClass()
                ctx.status(204)
            }
        }
    }

}

private fun getHerokuAssignedPort() = ProcessBuilder().environment()["PORT"]?.toInt() ?: 7000
