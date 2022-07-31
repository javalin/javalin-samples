package io.javalin.example.kotlin

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.post

class Application(dependency: String) {

    val app = Javalin.create().routes {
        get("/users", UserController::getAll)
        post("/users", UserController::create)
        get("/ui") { it.html("<h1>User UI</h1>") }
    }

}


