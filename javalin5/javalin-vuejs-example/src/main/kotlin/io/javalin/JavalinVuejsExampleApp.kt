package io.javalin

import io.javalin.http.staticfiles.Location
import io.javalin.http.bodyAsClass

data class Todo(val id: Long, val title: String, val completed: Boolean)

fun main() {

    var todos = arrayOf(Todo(123123123, "My very first todo", false))

    val app = Javalin.create {
        it.staticFiles.add("/public", Location.CLASSPATH)
    }
    app.get("/todos") { ctx ->
        ctx.json(todos)
    }
    app.put("/todos") { ctx ->
        todos = ctx.bodyAsClass()
        ctx.status(204)
    }
    app.start(getHerokuAssignedPort())

}

private fun getHerokuAssignedPort() = ProcessBuilder().environment()["PORT"]?.toInt() ?: 7070
