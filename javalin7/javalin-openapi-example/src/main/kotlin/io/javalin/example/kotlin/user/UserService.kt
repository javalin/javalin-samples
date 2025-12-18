package io.javalin.example.kotlin.user

import java.util.concurrent.atomic.AtomicInteger

// This is a service, it should contain no Javalin imports
object UserService {

    private val users = hashMapOf(
        0 to User(id = 0, name = "Alice", email = "alice@alice.kt"),
        1 to User(id = 1, name = "Bob", email = "bob@bob.kt"),
        2 to User(id = 2, name = "Carol", email = "carol@carol.kt"),
        3 to User(id = 3, name = "Dave", email = "dave@dave.kt")
    )

    private var lastId: AtomicInteger = AtomicInteger(users.size - 1)

    fun getAll() = users.values

    fun save(name: String, email: String) {
        val id = lastId.incrementAndGet()
        users[id] = User(name = name, email = email, id = id)
    }

    fun findById(id: Int): User? = users[id]

    fun update(id: Int, name: String, email: String) {
        users[id] = User(name = name, email = email, id = id)
    }

    fun delete(id: Int) {
        users.remove(id)
    }

}
