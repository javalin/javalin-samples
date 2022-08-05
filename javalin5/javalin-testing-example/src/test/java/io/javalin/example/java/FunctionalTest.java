package io.javalin.example.java;

import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;
import io.javalin.testtools.JavalinTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FunctionalTest {

    Javalin app = new JavalinTestingExampleApp("someDependency").javalinApp(); // inject any dependencies you might have
    private final String usersJson = new JavalinJackson().toJsonString(UserController.users);

    @Test
    public void GET_to_fetch_users_returns_list_of_users() {
        JavalinTest.test(app, (server, client) -> {
            assertThat(client.get("/users").code()).isEqualTo(200);
            assertThat(client.get("/users").body().string()).isEqualTo(usersJson);
        });
    }

}
