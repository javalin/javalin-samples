import io.javalin.http.Context
import io.javalin.http.Header
import io.javalin.http.UnauthorizedResponse
import io.javalin.security.RouteRole

enum class Role : RouteRole { ANYONE, USER_READ, USER_WRITE }

object Auth {

    // our access-manager is simple !
    // when endpoint has Role.ANYONE, we will not terminate the request lifecycle
    // when the request has the permitted roles we will not terminate the request lifecycle
    // else, we ask the user to authenticate and terminate the request lifecycle
    fun handleAccess(ctx: Context) {
        val permittedRoles = ctx.routeRoles()
        when {
            permittedRoles.contains(Role.ANYONE) -> return
            ctx.userRoles.any { it in permittedRoles } -> return
            else -> {
                ctx.header(Header.WWW_AUTHENTICATE, "Basic")
                throw UnauthorizedResponse();
            }
        }
    }

    // get roles from userRolesMap after extracting username/password from basic-auth header
    private val Context.userRoles: List<Role>
        get() = this.basicAuthCredentials()?.let { (username, password) ->
            userRolesMap[Pair(username, password)] ?: listOf()
        } ?: listOf()

    // we'll store passwords in clear text (and in memory) for this example,
    // but please don't do this if you have actual users !
    // typically you would store hashed passwords in a database and validate them using
    // something like bcrypt (http://www.mindrot.org/projects/jBCrypt/)
    private val userRolesMap = mapOf(
        Pair("alice", "weak-1234") to listOf(Role.USER_READ),
        Pair("bob", "weak-123456") to listOf(Role.USER_READ, Role.USER_WRITE)
    )

}
