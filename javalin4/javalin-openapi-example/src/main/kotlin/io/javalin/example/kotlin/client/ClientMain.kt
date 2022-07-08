package io.javalin.example.kotlin.client

import org.openapitools.client.apis.UserApi
import org.openapitools.client.infrastructure.ClientException
import org.openapitools.client.infrastructure.ServerException
import org.openapitools.client.models.NewUserRequest

// This file uses a client which is auto-generated from OpenAPI spec.
// To use it, first start Main.kt so the server is running.

private val apiInstance = UserApi("http://localhost:7001")

fun main() {

    try {
        apiInstance.getAllUsers().forEach { println(it) }
    } catch (e: ServerException) {
        println("5xx response calling UserApi#getAllUsers")
    }

    try {
        val newUserRequest = NewUserRequest("Elaine", "Elaine@elaine.kt")
        apiInstance.createUser(newUserRequest)
        println("Added new user: ${newUserRequest.name}")
        apiInstance.getAllUsers().forEach { println(it) }
    } catch (e: ClientException) {
        println("4xx response calling UserApi#createUser")
    } catch (e: ServerException) {
        println("5xx response calling UserApi#createUser")
    }

}
