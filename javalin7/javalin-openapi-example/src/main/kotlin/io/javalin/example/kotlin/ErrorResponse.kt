package io.javalin.example.kotlin

data class ErrorResponse(
        val title: String,
        val status: Int,
        val type: String,
        val details: Map<String, String>?
)
