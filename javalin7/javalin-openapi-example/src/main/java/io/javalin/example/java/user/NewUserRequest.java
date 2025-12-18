package io.javalin.example.java.user;

public class NewUserRequest {

    private String name;
    private String email;

    public NewUserRequest() {
    }

    public NewUserRequest(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

}
