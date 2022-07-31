import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.Javalin

fun main() {

    val app = Javalin.create{
        it.accessManager(Auth::accessManager)
    }.start(7000)

    app.routes {
        get("/", { ctx -> ctx.redirect("/users") }, Role.ANYONE)
        path("users") {
            get(UserController::getAllUserIds, Role.ANYONE)
            post(UserController::createUser, Role.USER_WRITE)
            path("{userId}") {
                get(UserController::getUser, Role.USER_READ)
                patch(UserController::updateUser, Role.USER_WRITE)
                delete(UserController::deleteUser, Role.USER_WRITE)
            }
        }
    }

}
