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
        val deprecatedDocsPath = "/swagger-docs"

        val openApiConfiguration = OpenApiConfiguration()
        openApiConfiguration.info.title = "AwesomeApp"
        openApiConfiguration.documentationPath = deprecatedDocsPath // by default it's /openapi

        config.plugins.register(OpenApiPlugin(openApiConfiguration))

        val swaggerConfiguration = SwaggerConfiguration()
        swaggerConfiguration.uiPath = "/swagger-ui" // by default it's /swagger
        swaggerConfiguration.documentationPath = deprecatedDocsPath
        config.plugins.register(SwaggerPlugin(swaggerConfiguration))

        val reDocConfiguration = ReDocConfiguration()
        reDocConfiguration.uiPath = "/redoc" // redundant, by default it's /redoc
        reDocConfiguration.documentationPath = deprecatedDocsPath
        config.plugins.register(ReDocPlugin(reDocConfiguration))
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
    println("Check out Swagger UI docs at http://localhost:7001/swagger-ui")

}
