package io.javalin.example.kotlin

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.example.kotlin.user.UserController
import io.javalin.openapi.plugin.OpenApiConfiguration
import io.javalin.openapi.plugin.OpenApiPlugin
import io.javalin.openapi.plugin.redoc.ReDocConfiguration
import io.javalin.openapi.plugin.redoc.ReDocPlugin
import io.javalin.openapi.plugin.swagger.SwaggerConfiguration
import io.javalin.openapi.plugin.swagger.SwaggerPlugin

fun main() {

    Javalin.create { config ->
        config.plugins.register(OpenApiPlugin(OpenApiConfiguration().apply {
            info.title = "Javalin OpenAIP example"
        }))
        config.plugins.register(SwaggerPlugin(SwaggerConfiguration()))
        config.plugins.register(ReDocPlugin(ReDocConfiguration()))
    }.routes {
        path("users") {
            get(UserController::getAll)
            post(UserController::create)
            path("{userId}") {
                get(UserController::getOne)
                patch(UserController::update)
                delete(UserController::delete)
            }
        }
    }.start(7001)

    println("Check out ReDoc docs at http://localhost:7001/redoc")
    println("Check out Swagger UI docs at http://localhost:7001/swagger")

}
