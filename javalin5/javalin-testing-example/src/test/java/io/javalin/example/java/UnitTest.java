package io.javalin.example.java;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class UnitTest {

    private final Context ctx = mock(Context.class);

    @Test
    public void POST_to_create_users_gives_201_for_valid_username() {
        when(ctx.queryParam("username")).thenReturn("Roland");
        UserController.create(ctx); // the handler we're testing
        verify(ctx).status(201);
    }

    @Test(expected = BadRequestResponse.class)
    public void POST_to_create_users_throws_for_invalid_username() {
        when(ctx.queryParam("username")).thenReturn(null);
        UserController.create(ctx); // the handler we're testing
    }

}
