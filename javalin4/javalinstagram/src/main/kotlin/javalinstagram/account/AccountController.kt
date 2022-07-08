package javalinstagram.account

import io.javalin.core.util.Header
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.UnauthorizedResponse
import javalinstagram.currentUser
import org.mindrot.jbcrypt.BCrypt

data class Credentials(val username: String = "", val password: String = "")

object AccountController {

    fun signIn(ctx: Context) {
        val (username, password) = ctx.bodyValidator<Credentials>()
                .check({ it.username.isNotBlank() && it.password.isNotBlank() }, "Username or password missing")
                .get()
        val user = AccountDao.findById(username)
        if (user != null && BCrypt.checkpw(password, user.password)) {
            ctx.status(200)
            ctx.currentUser = username
        } else {
            throw UnauthorizedResponse("Incorrect username/password")
        }
    }

    fun signUp(ctx: Context) {
        val (username, password) = ctx.bodyValidator<Credentials>()
                .check({ it.username.isNotBlank() && it.password.isNotBlank() }, "Username or password missing")
                .get()
        val user = AccountDao.findById(username)
        if (user == null) {
            AccountDao.add(id = username, password = BCrypt.hashpw(password, BCrypt.gensalt()))
            ctx.status(201)
            ctx.currentUser = username
        } else {
            throw BadRequestResponse("Username '$username' is taken")
        }
    }

    fun signOut(ctx: Context) {
        ctx.currentUser = null
        if (ctx.header(Header.ACCEPT)?.contains("html") == true) {
            ctx.redirect("/signin")
        }
    }

}
