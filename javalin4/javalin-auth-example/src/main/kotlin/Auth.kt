import io.javalin.core.security.RouteRole
import io.javalin.http.Context
import io.javalin.http.Handler

enum class Role : RouteRole { ANYONE, USER_READ, USER_WRITE }

object Auth {

    // our access-manager is simple
    // when endpoint has Role.ANYONE, we will always handle the request
    // when the request has the permitted roles (determined by inspecting the request) we handle the request.
    // else, we set status 401
    fun accessManager(handler: Handler, ctx: Context, permittedRoles: Set<RouteRole>) {
        when {
            permittedRoles.contains(Role.ANYONE) -> handler.handle(ctx)
            ctx.userRoles.any { it in permittedRoles } -> handler.handle(ctx)
            else -> ctx.status(401).json("Unauthorized")
        }
    }

    // get roles from userRoleMap after extracting username/password from basic-auth header
    private val Context.userRoles: List<Role>
        get() = this.basicAuthCredentials().let { (username, password) ->
            userRoleMap[Pair(username, password)] ?: listOf()
        } ?: listOf()

    // we'll store passwords in clear text (and in memory) for this example, but please don't
    // do this if you have actual users
    // typically you would store hashed passwords in a database and validate them using using
    // something like bcrypt (http://www.mindrot.org/projects/jBCrypt/)
    private val userRoleMap = hashMapOf(
            Pair("alice", "weak-password") to listOf(Role.USER_READ),
            Pair("bob", "better-password") to listOf(Role.USER_READ, Role.USER_WRITE)
    )

}
