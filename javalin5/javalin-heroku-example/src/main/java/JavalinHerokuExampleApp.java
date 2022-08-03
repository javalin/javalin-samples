import io.javalin.Javalin;

public class JavalinHerokuExampleApp {

    public static void main(String[] args) {
        Javalin.create()
            .get("/", ctx -> ctx.result("Hello Heroku"))
            .start(getHerokuAssignedPort());
    }

    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 7070;
    }

}
