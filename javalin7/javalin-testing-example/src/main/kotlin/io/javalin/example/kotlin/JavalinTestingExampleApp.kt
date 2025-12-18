package io.javalin.example.kotlin

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.post

class JavalinTestingExampleApp(dependency: String) {

    val app = Javalin.create {
        it.router.apiBuilder {
            get("/users", UserController::getAll)
            post("/users", UserController::create)
            get("/ui") { it.html("<h1>User UI</h1>") }
        }
    }

}
