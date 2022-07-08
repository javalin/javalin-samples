package app.user;

public class User {
    public final String username;
    public final String salt;
    public final String hashedPassword;

    public User(String username, String salt, String hashedPassword) {
        this.username = username;
        this.salt = salt;
        this.hashedPassword = hashedPassword;
    }
}

