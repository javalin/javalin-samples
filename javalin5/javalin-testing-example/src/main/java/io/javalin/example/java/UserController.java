package io.javalin.example.java;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserController {

    static List<String> users = new ArrayList<>(Arrays.asList("User1", "User2", "User3"));

    public static void create(Context ctx) {
        String username = ctx.queryParam("username");
        if (username == null || username.length() < 5) {
            throw new BadRequestResponse();
        } else {
            users.add(username);
            ctx.status(201);
        }
    }

    public static void getAll(Context ctx) {
        ctx.json(users);
    }

}

