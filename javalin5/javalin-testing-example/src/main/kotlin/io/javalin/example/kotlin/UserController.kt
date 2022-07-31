package io.javalin.example.kotlin

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context

object UserController {

    var users = mutableListOf("User1", "User2", "User3")

    fun create(ctx: Context) {
        val username = ctx.queryParam("username")
        if (username == null || username.length < 5) {
            throw BadRequestResponse()
        } else {
            users.add(username)
            ctx.status(201)
        }
    }

    fun getAll(ctx: Context) {
        ctx.json(users)
    }

}
