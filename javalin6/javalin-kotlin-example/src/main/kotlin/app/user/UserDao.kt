package app.user

import java.util.concurrent.atomic.AtomicInteger

class UserDao {

    // Initialize with a few users
    // This demonstrates type inference, map-literals and named parameters
    val users = hashMapOf(
            0 to User(name = "Alice", email = "alice@alice.kt", id = 0),
            1 to User(name = "Bob", email = "bob@bob.kt", id = 1),
            2 to User(name = "Carol", email = "carol@carol.kt", id = 2),
            3 to User(name = "Dave", email = "dave@dave.kt", id = 3)
    )

    private var lastId: AtomicInteger = AtomicInteger(users.size - 1)

    fun save(name: String, email: String) {
        val id = lastId.incrementAndGet()
        users[id] = User(name = name, email = email, id = id)
    }

    fun findById(id: Int): User? = users[id]

    fun findByEmail(email: String): User? =
        users.values.find { it.email == email } // == is equivalent to java .equals() (referential equality is checked by ===)

    fun update(id: Int, user: User) {
        users[id] = User(name = user.name, email = user.email, id = id)
    }

    fun delete(id: Int) {
        users.remove(id)
    }

}
