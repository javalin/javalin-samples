package io.javalin.example.kotlin

import io.javalin.plugin.json.JavalinJackson
import io.javalin.testtools.JavalinTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class FunctionalTest {

    private val app = Application("someDependency").app // inject any dependencies you might have
    private val usersJson = JavalinJackson().toJsonString(UserController.users)

    @Test
    fun `GET to fetch users returns list of users`() = JavalinTest.test(app) { server, client ->
        assertThat(client.get("/users").code).isEqualTo(200)
        assertThat(client.get("/users").body?.string()).isEqualTo(usersJson)
    }

}
