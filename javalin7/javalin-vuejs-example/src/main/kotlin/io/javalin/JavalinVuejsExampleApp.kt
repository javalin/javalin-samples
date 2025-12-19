package io.javalin

import io.javalin.http.staticfiles.Location
import io.javalin.http.bodyAsClass

data class Todo(val id: Long, val title: String, val completed: Boolean)

fun main() {

    var todos = arrayOf(Todo(123123123, "My very first todo", false))

    val app = Javalin.create {
        it.staticFiles.add("/public", Location.CLASSPATH)
        it.routes.get("/todos") { ctx ->
            ctx.json(todos)
        }
        it.routes.put("/todos") { ctx ->
            todos = ctx.bodyAsClass<Array<Todo>>()
            ctx.status(204)
        }
    }.start(7070)

}
