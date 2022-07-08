package io.javalin.example.java;

import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;
import io.javalin.testtools.TestUtil;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FunctionalTest {

    Javalin app = new Application("someDependency").javalinApp(); // inject any dependencies you might have
    private String usersJson = new JavalinJackson().toJsonString(UserController.users);

    @Test
    public void GET_to_fetch_users_returns_list_of_users() {
        TestUtil.test(app, (server, client) -> {
            assertThat(client.get("/users").code()).isEqualTo(200);
            assertThat(client.get("/users").body().string()).isEqualTo(usersJson);
        });
    }

}
