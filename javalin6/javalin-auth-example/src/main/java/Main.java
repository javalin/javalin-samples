import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.router.mount(router -> {
                router.beforeMatched(AuthJ::handleAccess);
            }).apiBuilder(() -> {
                get("/", ctx -> ctx.redirect("/users"), RoleJ.ANYONE);
                path("users", () -> {
                    get(UserControllerJ::getAllUserIds, RoleJ.ANYONE);
                    post(UserControllerJ::createUser, RoleJ.USER_WRITE);
                    path("{userId}", () -> {
                        get(UserControllerJ::getUser, RoleJ.USER_READ);
                        patch(UserControllerJ::updateUser, RoleJ.USER_WRITE);
                        delete(UserControllerJ::deleteUser, RoleJ.USER_WRITE);
                    });
                });
            });
        }).start(7070);

    }
}
