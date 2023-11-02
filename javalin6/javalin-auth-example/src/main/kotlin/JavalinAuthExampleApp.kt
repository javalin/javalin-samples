import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.Javalin

fun main() {

    Javalin.create {
        it.router.mount {
            it.beforeMatched(Auth::handleAccess)
        }.apiBuilder {
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
    }.start(7070)

}
