package app

import app.user.User
import app.user.UserDao
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.Javalin
import io.javalin.http.HttpStatus
import io.javalin.http.NotFoundResponse
import io.javalin.http.bodyAsClass
import io.javalin.http.pathParamAsClass

fun main() {

    val userDao = UserDao()

    val app = Javalin.create { config ->
        config.routes.apiBuilder {

            get("/") { it.redirect("/users") } // redirect root to /users

            get("/users") { ctx ->
                ctx.json(userDao.users)
            }

            get("/users/{user-id}") { ctx ->
                val userId = ctx.pathParamAsClass<Int>("user-id").get()
                val user = userDao.findById(userId) ?: throw NotFoundResponse()
                ctx.json(user)
            }

            get("/users/email/{email}") { ctx ->
                val email = ctx.pathParam("email")
                val user = userDao.findByEmail(email) ?: throw NotFoundResponse()
                ctx.json(user)
            }

            post("/users") { ctx ->
                val user = ctx.bodyAsClass<User>()
                userDao.save(name = user.name, email = user.email)
                ctx.status(201)
            }

            patch("/users/{user-id}") { ctx ->
                val userId = ctx.pathParamAsClass<Int>("user-id").get()
                val user = ctx.bodyAsClass<User>()
                userDao.update(id = userId, user = user)
                ctx.status(204)
            }

            delete("/users/{user-id}") { ctx ->
                val userId = ctx.pathParamAsClass<Int>("user-id").get()
                userDao.delete(userId)
                ctx.status(204)
            }
        }

        config.routes.exception(Exception::class.java) { e, ctx -> e.printStackTrace() }
        config.routes.error(HttpStatus.NOT_FOUND) { ctx -> ctx.json("not found") }
    }.start(7070)

}
