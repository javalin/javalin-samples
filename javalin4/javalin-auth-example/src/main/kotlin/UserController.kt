import io.javalin.http.Context
import java.util.*

object UserController {

    private data class User(val name: String = "", val email: String = "")

    private val users = hashMapOf(
            randomId() to User(name = "Alice", email = "alice@alice.kt"),
            randomId() to User(name = "Bob", email = "bob@bob.kt"),
            randomId() to User(name = "Carol", email = "carol@carol.kt"),
            randomId() to User(name = "Dave", email = "dave@dave.kt")
    )

    fun getAllUserIds(ctx: Context) {
        ctx.json(users.keys)
    }

    fun createUser(ctx: Context) {
        users[randomId()] = ctx.bodyAsClass(User::class.java)
    }

    fun getUser(ctx: Context) {
        ctx.json(users[ctx.pathParam("userId")]!!)
    }

    fun updateUser(ctx: Context) {
        users[ctx.pathParam("userId")] = ctx.bodyAsClass(User::class.java)
    }

    fun deleteUser(ctx: Context) {
        users.remove(ctx.pathParam("userId"))
    }

    private fun randomId() = UUID.randomUUID().toString()

}
